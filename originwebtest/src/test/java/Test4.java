import com.google.common.collect.Lists;
import org.junit.Test;
import org.springframework.util.StringUtils;
import star.bean.*;
import star.core.LoadCore;
import star.factory.BeanFactory;
import star.proxy.TransactionProxy;
import star.thread.RandomList;
import star.utils.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author keshawn
 * @date 2018/3/20
 */
public class Test4 {

    @Test
    public void printThread() {
        System.out.println(Thread.currentThread().hashCode());
        System.out.println(Thread.currentThread().getId());
        System.out.println(Thread.currentThread().getName());
    }

    @Test
    public void test() {
        int k = 2;
        Integer[] array = {2, 1, 4, 5, 9};
        for (int i = 0; i < array.length; i++) {
            for (int j = i + 1; j <= k; j++) {
                if (array[i] > array[j]) {
                    int temp = array[i];
                    array[i] = array[j];
                    array[j] = temp;
                }
            }
            if (i == k) {
                System.out.println(array[k - 1]);
            }
        }
        System.out.println(Arrays.toString(array));
    }

    @Test
    public void testDate() {
        LocalDate start = LocalDate.of(2018, 3, 16);
        LocalDate end = LocalDate.of(2018, 6, 30);
        Long unAccountDays = ChronoUnit.DAYS.between(start, end) + 1L;
        Double avgFee = 9600D / unAccountDays;
        System.out.println(avgFee);
        System.out.println(unAccountDays);
        LocalDate now = LocalDate.now();
        LocalDate releaseStartDay = LocalDate.now().plusDays(1);
        Long releaseDays = ChronoUnit.DAYS.between(releaseStartDay, end) + 1L;
        Double sum = 11111305.00D + 95;
        Double amount = avgFee * releaseDays + sum;
        System.out.println(new BigDecimal(NumberUtil.scale2(amount) + ""));
        System.out.println();
        System.out.println(amount);
        System.out.println(releaseDays);
    }

    @Test
    public void buildTree() {
        Node 河南 = new Node(1L, "河南", null);
        Node 河北 = new Node(2L, "河北", null);
        Node 郑州 = new Node(11L, "郑州", 1L);
        Node 周口 = new Node(12L, "周口", 1L);
        Node 金水区 = new Node(111L, "金水区", 11L);
        Node 二七区 = new Node(112L, "二七区", 11L);
        ArrayList<Node> nodes = Lists.newArrayList(河南, 河北, 郑州, 周口, 金水区, 二七区);
        HashMap<Long, List<Node>> parentIdNodeMap = new HashMap<>();
        for (Node node : nodes) {
            //省没有parent id
            Long parentId = node.getParentId() == null ? 0L : node.getParentId();
            if (parentIdNodeMap.containsKey(parentId)) {
                parentIdNodeMap.get(parentId).add(node);
            } else {
                parentIdNodeMap.put(parentId, Lists.newArrayList(node));
            }
        }
        System.out.println("xiao na na zhen ke ai");
        System.out.println("baobao e l ma?");
        for (Map.Entry<Long, List<Node>> entry : parentIdNodeMap.entrySet()) {
            System.out.println(JsonUtil.encodeJson(entry));
        }
        System.out.println(parentIdNodeMap);
    }

    @Test
    public void testGetMethod() {
        LoadCore.init();
        Object testController = BeanFactory.getBean("testController");
        Class<?> aClass = testController.getClass();
        System.out.println(aClass);
        Arrays.asList(aClass.getDeclaredMethods()).stream().map(Method::getName).forEach(System.out::println);
    }

    @Test
    public void testClassInfo() {
        LoadCore.init();
        ClassInfo classInfo = ClassUtil.getClassInfo(User.class);
        System.out.println(JsonUtil.encodeJson(classInfo));
    }

    @Test
    public void testGetFields() throws Exception {
        List<Field> fields = ReflectionUtil.getFields(TransactionProxy.class);
        System.out.println(fields);
        List<Field> fields1 = ReflectionUtil.getFields(UserDTO.class);
        System.out.println(fields1);
        Field createId = UserDTO.class.getDeclaredField("createId");
        System.out.println(createId);
    }

    @Test
    public void notNull() {
        List<Integer> collect = Lists.newArrayList(1, 2, 3, null).stream().filter(Objects::nonNull).collect(Collectors.toList());
        System.out.println(collect);
    }

    @Test
    public void asset() {
        String s = "";
        assert s != null;
        assert s == null;
    }

    @Test
    public void testType() throws Exception {
        Method gene = UserDTO.class.getDeclaredMethod("gene", List.class);
        Parameter[] parameters = gene.getParameters();
        Parameter parameter = parameters[0];
        Type parameterizedType = parameter.getParameterizedType();
        TypeWrapper typeWrapper1 = ReflectionUtil.typeParse(parameterizedType);
        System.out.println(JsonUtil.encodeJson(typeWrapper1));
    }

    @Test
    public void sum() {
        String content = " cda.url=http://localhost/CDAEngineUI-1.0/CDAEngineUI.html\n" +
                " \n" +
                " #Xds服务器地址\n" +
                "-xds.iti18=xds-iti18://localhost/democam/xds-iti18\n" +
                "-xds.iti41=xds-iti41://localhost/democam/xds-iti41\n" +
                "-xds.iti42=xds-iti42://localhost/democam/xds-iti42\n" +
                "-xds.iti43=xds-iti43://localhost/democam/xds-iti43\n" +
                "+xds.iti18=xds-iti18://10.4.54.13:8082/hip_ipf/xds-iti18\n" +
                "+xds.iti41=xds-iti41://10.4.54.13:8082/hip_ipf/xds-iti41\n" +
                "+xds.iti42=xds-iti42://10.4.54.13:8082/hip_ipf/xds-iti42\n" +
                "+xds.iti43=xds-iti43://10.4.54.13:8082/hip_ipf/xds-iti43";
        if (!StringUtils.isEmpty(content)) {
            String[] strings = content.split("\\n");
            System.out.println(Arrays.toString(strings));
            long sum = Arrays.asList(strings).stream().filter(s -> s.startsWith("+")).count();
            System.out.println(sum);
        }
    }

    @Test
    public void testFormat() {
        String s = "delay.%s.mins.queue";
        System.out.println(String.format(s, 1));
    }

    @Test
    public void testBetween() {
        Instant now = Instant.now();
        long between = ChronoUnit.MINUTES.between(now, now.plusSeconds(601));
        System.out.println(between);
        System.out.println(ChronoUnit.DAYS.between(LocalDate.of(2018, 5, 15), LocalDate.of(2018, 5, 20)));
    }

    @Test
    public void testData() {
        User user = (User) PojoManufactureUtil.manufacture(User.class);
        String userString = user.toString();
        System.out.println(userString);
        User jsonUser = JsonUtil.decodeJson(userString, User.class);
        System.out.println(jsonUser);
    }

    @Test
    public void testClassType() {
        List<Field> fields = ReflectionUtil.getFields(User.class);
        fields.forEach(field -> {
            Class<?> fieldType = field.getType();
            System.out.println(fieldType);
        });
    }

    @Test
    public void testCast() {
        String s = "1.0";
        Double integer = JsonUtil.decodeJson(s, Double.class);
        System.out.println(integer);
    }

    @Test
    public void testLocalDateToString() {
        LocalDate localDate = LocalDate.now();
        System.out.println(localDate.toString());
        System.out.println(LocalTime.now().toString());
        System.out.println(LocalTime.now().format(DateTimeFormatter.ofPattern("HH-mm-ss")));
        System.out.println(String.format("mkdir {%s} error", null));
    }

    @Test
    public void testGroup() {
        List<User> users = new ArrayList<>(10000);
        for (int i = 0; i < 10000; i++) {
            User user = (User) PojoManufactureUtil.manufacture(User.class);
            users.add(user);
        }

        users.parallelStream().forEach(user -> user.setId(1L));
        System.out.println(users.get(0));
        long start = System.currentTimeMillis();
        Map<Status, List<User>> statusListMap = users.stream().collect(Collectors.groupingBy(User::getStatus));
        System.out.println(System.currentTimeMillis() - start);

        long begin = System.currentTimeMillis();
        Map<Status, List<User>> listMap = users.parallelStream().collect(Collectors.groupingBy(User::getStatus));
        System.out.println(System.currentTimeMillis() - begin);
    }

    @Test
    public void testGetClassInfo() {
        System.out.println(JsonUtil.encodeJson(ClassUtil.getClassInfo(User.class)));
    }

    @Test
    public void testBeanIsNull() throws Exception {
        User user = new User();
        System.out.println(BeanUtil.checkAllFieldIsNull(user));
        long l = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            BeanUtil.checkAllFieldIsNull(user);
        }
        System.out.println(System.currentTimeMillis() - l);
    }

    @Test
    public void testCon() {
        List<User> users = new ArrayList<>(100);
        for (int i = 0; i < 100; i++) {
            User user = (User) PojoManufactureUtil.manufacture(User.class);
            users.add(user);
        }

        long start = System.currentTimeMillis();
        users.forEach(user -> JsonUtil.encodeJson(user));
        System.out.println(System.currentTimeMillis() - start);

        long begin = System.currentTimeMillis();
        users.parallelStream().forEach(user -> JsonUtil.encodeJson(user));
        System.out.println(System.currentTimeMillis() - begin);

    }

    public static boolean isAllFieldNull(Object obj) throws Exception {
        Class stuCla = (Class) obj.getClass();// 得到类对象
        Field[] fs = stuCla.getDeclaredFields();//得到属性集合
        boolean flag = true;
        for (Field f : fs) {//遍历属性
            f.setAccessible(true); // 设置属性是可以访问的(私有的也可以)
            Object val = f.get(obj);// 得到此属性的值
            if (val != null) {//只要有1个属性不为空,那么就不是所有的属性值都为空
                flag = false;
                break;
            }
        }
        return flag;
    }

    @Test
    public void testLock() {
        ArrayList<String> strings = Lists.newArrayList("a", "b", "c");
        RandomList<String> randomList = new RandomList<>(strings);
        Thread thread = new Thread(() -> get(randomList));
        Thread a = new Thread(() -> {
            randomList.remove("a");
        });
        thread.start();
        a.start();
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(randomList);
    }

    public static void get(RandomList<String> randomList) {
        System.out.println(randomList.randomGet().get());
    }

    @Test
    public void testPeek() {
        List<String> collect = Lists.newArrayList("a", "b", "c").stream().peek(System.out::println).collect(Collectors.toList());
    }

    @Test
    public void testLocalDateTime() {
        System.out.println(DateUtil.toUtilDate(LocalDateTime.now()));
        System.out.println(Instant.now().toEpochMilli());
        System.out.println(System.currentTimeMillis());

        System.out.println(Instant.now());

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
        ZonedDateTime zonedDateTime = ZonedDateTime.of(DateUtil.toLocalDateTime(new Date()), ZoneId.systemDefault());
        String format = zonedDateTime.format(dateTimeFormatter);
        System.out.println(format);

        LocalDateTime localDateTime = LocalDateTime.parse(format, dateTimeFormatter);
        Date date = DateUtil.toUtilDate(localDateTime);
        System.out.println(date);
    }

    @Test
    public void testStringFormat() {
        String s = "#%paycontrol%#*&" + "" + "01" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + "&*";
        System.out.println(s);
        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
    }

    @Test
    public void testRandom() {
        System.out.println(ThreadLocalRandom.current().nextInt(10));
    }

    @Test
    public void testOrElse() {
        String orElse = Optional.ofNullable("a").orElse(getS());
        System.out.println(orElse);
        String o = Optional.ofNullable("b").orElseThrow(Test4::getException);
        String s = Optional.ofNullable("b").orElseGet(() -> {
            System.out.println("get");
            return "asiuhd";
        });
    }

    public static String getS() {
        System.out.println("get String");
        return "ss";
    }

    public static RuntimeException getException() {
        System.out.println("get ex");
        return new RuntimeException();
    }

    @Test
    public void testDecode() {
        ArrayList<Long> arrayList = new ArrayList<>();
        for (int i = 0; i < 900; i++) {
             arrayList.add(CastUtil.castLong(NumberUtil.getRandomNum(100000)));
        }
        User manufacture = (User) PojoManufactureUtil.manufacture(User.class);
        manufacture.setIds(arrayList);
        String json = JsonUtil.encodeJson(manufacture);
        System.out.println(json);
    }

    @Test
    public void testdecode() {
        String json = "{\"age\":522745074,\"birthday\":\"2018-07-03T11:36:40.803Z\",\"createId\":1909614712620466,\"id\":1909614707944773,\"ids\":[1581,60684,69321,30935,88190,3314,78198,56328,49147,41640,55059,54343,77821,91949,10040,50529,69559,43792,58517,89535,66904,99707,58282,53823,33360,28399,27716,86604,60743,87580,60502,57075,65092,11816,15186,96922,74519,75572,22615,24759,78174,9535,58995,74931,46180,23786,71624,57488,36809,42949,18764,25982,30891,9110,62311,77716,65670,42092,43604,69189,50348,4897,25157,12462,32255,13525,9327,30303,42900,17211,74320,36911,62307,29126,66617,31032,75287,72807,29995,50379,81298,9902,27421,55247,9796,21022,52497,86424,51049,61400,22384,81595,75177,78394,81897,88075,13026,30972,47188,37784,20617,38847,96293,84205,83364,30048,38075,10528,9326,33206,14119,99212,81585,9847,39269,47600,71481,35772,95504,58431,58881,5649,98346,73386,84995,87827,9568,70342,65309,67070,55114,16147,37305,69634,80021,2090,44066,81651,17482,93827,82365,74509,46145,67364,30077,94341,30539,61674,756,64905,10405,49494,50726,91779,76684,51644,21980,25348,4484,19091,58521,45228,89190,64166,52198,31048,42777,67934,11104,6901,56164,79140,34637,18621,78563,96822,41748,64894,12789,25428,8292,25089,73213,6281,65233,7833,94061,71255,39803,67211,59251,42508,65914,26911,44728,13413,45395,14264,30191,79274,35070,36810,80925,82381,72882,81878,67081,86874,30141,32810,43781,16896,95604,57795,84700,25929,94348,5750,52815,66689,7964,25293,41824,21240,33697,74033,29160,71563,92148,20385,24996,49098,31985,40464,45417,86731,42866,19867,90161,83925,63606,88891,19213,46011,98147,97587,52050,37800,27811,76293,6541,84815,26385,42725,24425,46090,69442,59885,91641,11496,99939,33398,80731,3064,30259,16481,86539,51816,79510,65803,33284,34008,34508,67879,65806,98476,92666,50186,88877,62760,4237,96340,84034,82238,76803,23682,6424,51341,79101,13939,28146,7995,33943,46401,17176,64570,27667,6925,4912,67709,77513,63685,16137,80613,1603,72498,99796,40845,44230,17502,96800,17059,7002,62830,47620,96841,4636,10998,46935,41320,34166,8507,21717,27324,70986,24803,97868,88675,87769,87546,43704,71540,14594,24338,37937,99419,22051,40494,13745,84958,17177,13006,70748,70661,29844,78027,85609,42507,66245,48459,47161,59504,6740,35929,4097,84185,53483,8082,8005,8469,34074,9198,46154,619,14912,42104,610,64623,32366,61274,35012,62565,66045,55854,28112,40830,91103,63069,11855,99261,56149,17711,77512,30612,8152,72422,59733,14105,22820,19126,81,35291,1141,49761,20005,36502,82164,30479,32964,37979,87956,28027,32151,52153,37548,26528,16448,36124,93301,97195,73343,12407,38724,28103,45969,39739,44795,55542,62499,84447,38502,5100,41921,42073,91670,82646,16123,72565,63512,60228,86543,29122,27668,95848,72611,46918,6770,68045,91772,76120,61072,93980,53470,7671,33098,22602,47242,32730,27559,19670,61750,31950,35446,19169,4942,73189,23471,7247,15537,23251,90032,9662,15155,83469,57019,63490,47469,60609,55234,310,62500,26514,60179,77205,23517,5855,12492,38629,19039,17520,24876,33977,69987,48918,756,46196,22665,24949,14291,58626,37360,35841,77,21501,98972,72535,30169,45455,60588,89882,79470,59031,45909,61087,28544,24622,84690,65034,47669,59526,55268,31635,72403,68612,53014,14326,12613,70706,29699,94798,42434,55790,9248,7890,67214,71434,50855,68197,72173,9481,20147,27397,46618,11006,56789,39482,61819,99683,83501,30445,63231,37615,58007,89112,575,3182,6924,5250,94332,36132,22582,62162,34768,91271,52290,64220,69055,29645,6851,91145,62624,76719,49373,11724,67104,93130,3774,51860,54003,93285,24647,1363,68049,63193,38299,46606,47005,70665,81071,72443,43553,47405,79941,78041,63097,25582,60567,10911,50819,63914,40909,6597,22476,7764,85992,11188,99147,43723,17541,82173,53563,61456,89112,62687,61824,22871,13964,37973,43214,71579,47443,35061,34862,54571,99499,49880,65547,99140,16354,70182,9543,25589,74996,49678,44799,89949,96127,63224,90493,22992,6714,98328,17605,50787,12999,60277,12289,58831,41025,57986,51175,18098,37836,53643,23122,57462,67756,92212,14659,21377,91704,17626,74692,44046,71299,83075,83888,48475,54033,35016,20412,95744,36671,7149,77927,55359,10664,16484,15465,74739,70263,31525,38848,68931,12998,42513,21853,51640,21464,67403,31294,50015,36397,12513,89059,17755,69057,83072,36025,79310,90193,57989,5932,77966,45801,48020,11461,36989,65022,26483,23645,83920,96322,18594,81994,99748,15180,49421,26398,72077,4487,5438,1239,68898,7373,13973,37553,33001,55066,89416,77974,82557,66801,41055,18017,7762,47298,14503,67384,70325,19033,32266,95396,21511,15900,15968,23512,54736,23557,76312,69059,33143,41044,60313,42501,5487,23673,23496,96605,73440,47566,199,25344,66486,80902,12164,69595,16539,13014,98996,46082,53596,2519,13947,55844,54377,10942,14203,12012,60612,87045,7423,59595,70004,88275,74042,42704,17417,65031,9998,67278,84550,84791,98068,63522,25331,40225,78735,88042,56232,9617,23615,97007,95097,34547,26195,18576,84731,42126,75930,66350,27960,65249,60654,64530,38038,80187,78266,36204,28928,92916,81457,8581,34873,13229,59660,26162,12664,79190,5664,70736,46288,28567,68170,17915,71280,38096,16157,84249,44720,70958,25075,64295,52918,22094,36801,93391,9508,61259,12851,99104,94254,85571,34888,96936,57216,98162,12500,33742,49673,99574,67075,35162,36691,54354,22605,61558,58271,12606,56168,79342,71645,85010,94953,3465,19857,38278,27891,36712,33411,61667,34701,11008,13401,62500,25677,58836,24242,45422,23331,30407,95598,26610,29960,26285,81499,30853,65693,74907,7390,27843,18842,78107,72043,13006,61715,99355,29007,43668,57664,89704,24177,51443,98558,45416,58290,89050,42813,98596,53127,25815,23340,70034,7637,70045,85396,25558,20395,46679,2737,8668,32566,34574,4981,90875,74962,4897,382,19123,37723,10466,20244,11430,71276,60582,34456,66442,23797,53849,25967,65356,85861,68844,33466,8141,29695,14283,22802,24849,75423,2758,7919,58200,53191,47196,48225,37377,47585,97493,64141,92847,90325,40707,32972,84386,92745,82354,90209,21841,28959,58965,3832,27034,48908,55987,41005,34997,89896,12871,87740,60247,56383,6214,1715,91901,65068,69582,36857,32742,65649,32317,71563,75431,10695,84771,59498,76515,5944,50112,39564],\"name\":\"z5OfEbXPEW\",\"status\":\"INVALID\"}\n";
        long l = System.currentTimeMillis();
        User user = JsonUtil.decodeJson(json, User.class);
        System.out.println(System.currentTimeMillis() - l);

        String j = "{\"age\":566828042,\"birthday\":\"2018-07-03T11:40:12.547Z\",\"createId\":1909826456677275,\"id\":1909826451906642,\"ids\":[39770,50819,90505,98666,83802,78869,41054,27866,69013,98357,86285,77525,60150,38053,81536,48326,11927,9085,23375,10241,70403,80283,16647,92896,24428,73639,19964,84980,61487,29687,98442,12576,32234,80050,1487,91705,37787,79384,27290,20607,9975,32732,11578,14011,68009,74568,1953,49510,86830,98718,19441,16169,42515,15352,8750,15998,92084,77209,78311,79213,97821,21337,95991,31149,38271,6675,18670,75743,31250,57326,55671,28784,21166,10484,5520,78752,54161,45130,50819,66796,13362,90718,92696,41239,7725,1367,65299,68281,21262,87975,71015,72751,55171,77268,39991,25914,64438,9984,10896,24127,6242,80758,80708,23351,58046,41576,79405,40652,19396,85666,59412,70695,90114,78576,4164,77815,27329,21775,29254,19478,70556,36195,61090,54824,86599,31499,16159,70647,4356,87301,32804,81976,54778,11442,21038,38473,44353,87819,65442,83472,284,88600,52662,5244,57942,9291,36237,7065,26802,50843,87637,79567,53025,66112,85352,83257,76817,74606,38049,6133,91973,50685,94704,65134,92716,7679,94214,41320,40549,45117,43187,94332,20370,97888,5348,19571,22351,74032,2550,71398,87353,30200,26948,86029,19441,78522,52604,88987,84445,99435,67172,90351,93219,21439,98682,11297,35135,21831,85743,27263,63024,19973,12168,28799,94453,14974,59080,5013,97790,93588,34044,76726,45851,64708,33296,37783,80568,69209,18156,31453,30375,36274,34039,11931,6863,75930,75838,88958,81549,2641,69152,53622,19345,57869,54362,78249,94412,93447,59397,76184,87580,88265,35973,29031,7888,75238,95051,32403,82730,30379,57856,42528,72544,40620,21441,47480,91851,12514,33590,44157,92819,38262,41393,94628,30507,92963,37066,8457,48752,48759,5705,69680,63427,29968,55882,52872,14125,42950,54790,68321,64843,36228,63193,51130,58734,97383,81357,75385,63717,82480,42314,77164,58486,56920,401,53963,67913,21663,24478,61426,49135,71993,2245,28240,26371,91293,54931,22080,40954,92633,84529,77098,6057,12566,66537,30209,13257,54719,75317,56597,4874,10134,6079,56886,5937,93850,2895,74343,59555,98242,39091,25048,6223,30916,84445,48274,83042,99426,78202,21015,38584,77147,12900,77970,3212,68655,73099,68693,59909,47647,71987,89678,58714,87896,24220,91539,14552,15740,90411,51446,21200,42650,76367,47589,71749,24109,21546,74045,39792,39314,81303,7346,23341,54793,69645,5823,72390,48762,67148,61482,42108,29997,40480,12491,41094,83245,18359,30621,84045,90725,41170,7432,25751,78692,4721,93514,99326,33175,76217,90460,12524,92593,11881,52021,42356,96381,88173,79221,89207,37462,14391,11429,4467,62599,6917,84624,33368,69283,73399,81087,2793,77053,45958,42108,31938,53876,94489,40457,6648,52468,8582,38892,85406,54862,17974,85942,41942,87407,99794,58963,66774,56281,66445,3474,12819,71586,62516,99781,94755,14655,5042,62830,75391,66889,73782,13128,11115,85295,94265,45365,27135,75018,65709,83103,46710,37915,15715,95089,35004,14839,31848,24324,45762,32709,29736,86317,63594,40210,32805,25360,87831,20931,70441,17175,37967,30986,98419,98942,61222,75629,61151,16840,32915,72515,55308,57874,61302,14951,77395,95692,84358,11912,7954,77395,91421,15248,1337,75504,99451,52261,81051,43523,15527,27256,51013,20156,97520,83398,73508,29974,36518,37825,68621,74370,58070,76166,20749,98090,21729,99397,48498,4214,53987,13974,24203,24604,91783,3899,85438,22367,62235,12168,47935,60013,87025,75297,67151,65569,32555,86269,2037,44018,45517,11285,34300,39473,94844,24490,32804,43490,70729,32005,53737,11118,94462,53360,37880,43725,63754,65467,45278,516,65363,8667,96834,78170,21328,31570,46649,44067,62534,85217,11884,96964,30238,40496,79790,10033,22320,32097,79013,22741,65562,69555,53904,81473,62746,68537,7101,88474,9539,16147,52147,13215,28122,92681,38205,93843,77540,43082,15126,4874,46513,28688,9733,78333,81508,59274,46542,53673,15389,24714,36731,19566,84390,69951,1941,24625,12496,70173,51990,96515,69160,23935,76591,14483,5718,86788,62049,23261,33393,37254,64392,29945,8094,2555,85905,51061,70839,64428,82160,98713,19894,81469,10921,44197,47757,27244,28815,33140,43421,8890,6000,29724,26284,71546,61694,48644,15265,40328,18223,94776,21536,10303,48375,29339,60354,91393,26408,44418,71043,36623,55181,87406,30994,37889,64910,56163,12095,2543,39220,74800,73176,15715,66575,52054,34079,55279,75564,85036,52665,61769,98343,73510,24083,53536,8988,56241,80653,25748,75301,91320,32818,55869,34526,34067,34355,92655,61203,68159,39451,83861,78682,55825,55728,4658,939,3694,64143,81672,76594,67867,11729,58633,95791,19443,42632,26433,91496,79743,21329,18455,8902,40145,82557,12332,41230,59767,67489,67971,33111,90748,70747,5325,14736,17541,98970,42244,30045,1815,30104,94710,19986,32413,83742,90265,32977,91265,85820,77040,35387,24658,81731,6208,89402,35421,60789,73301,22954,77517,59908,59153,66269,83597,78843,38740,39152,72391,14179,29238,16990,59303,65483,67349,8114,24436,36514,67876,24518,3779,69042,40127,35373,85959,93287,20706,22970,69329,17611,35943,71642,74483,52418,14913,79293,72772,51807,18381,3459,82893,89321,47766,73952,29476,47004,30684,81228,24272,22127,16394,39651,73845,52612,34662,28217,91912,39870,40143,93259,57406,75043,97828,82073,54128,73336,43339,25307,75749,56201,94311,10586,50043,81919,96878,5908,14137,64266,78334,27312,89667,35901,30537,64300,57536,38619,73469,69582,27847,65777,2460,32333,51109,35848,34149,45634,78230,13321,34542,13653,47295,37678,61172,8158,10858,28727,28928,2204,19578,3597,66676,33099,70186,73598,35708,52094,50677,93662,4333,30893,86422],\"name\":\"JqTi1PkCmh\",\"status\":\"VALID\"}\n";
        long l2 = System.currentTimeMillis();
        User user2 = JsonUtil.decodeJson(j, User.class);
        System.out.println(System.currentTimeMillis() - l2);
    }

    @Test
    public void testIsAlive() {
        Thread thread = new Thread(() -> {
            System.out.println("begin sleep");

            System.out.println("end sleep");
            throw new RuntimeException();
        });

        thread.start();
        try {
            Thread.sleep(100L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(thread.isAlive());

        try {
            Thread.sleep(10000000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testComp() {
        ArrayList<String> arrayList = Lists.newArrayList("asd", "sss", "qwe", "");
        arrayList.removeIf(String::isEmpty);
        System.out.println(arrayList);
    }

    @Test
    public void testGe() {
        List<User> users = Lists.newArrayList();
        users.stream().collect(Collectors.toMap(user -> user.getId(), Function.identity()));
    }

}
