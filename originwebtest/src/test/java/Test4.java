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
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
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
        List<User> users = new ArrayList<>(10000);
        for (int i = 0; i < 10000; i++) {
            User user = (User) PojoManufactureUtil.manufacture(User.class);
            users.add(user);
        }

        long start = System.currentTimeMillis();
        users.stream().map(JsonUtil::encodeJson).collect(Collectors.toList());
        System.out.println(System.currentTimeMillis() - start);

        long begin = System.currentTimeMillis();
        users.parallelStream().map(JsonUtil::encodeJson).collect(Collectors.toList());
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
}
