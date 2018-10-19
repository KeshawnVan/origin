import com.google.common.collect.Lists;
import org.junit.Test;
import star.bean.Company;
import star.bean.StructureEntity;
import star.bean.User;
import star.utils.*;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static spark.Spark.get;

public class Test5 {

    @Test
    public void testComputeIIfAbsent() {
        Map<String, String> map = new HashMap<>();
        map.put("1", "a");
        String b = map.putIfAbsent("1", "b");
        System.out.println(map);
        System.out.println(b);
        String computeIfPresent = map.computeIfPresent("1", (oldValue, newValue) -> {
            System.out.println("---");
            System.out.println(oldValue);
            System.out.println(newValue);
            return oldValue + newValue;
        });

        String computeIfAbsent = map.computeIfAbsent("1", it -> {
            System.out.println(it);
            return "c";
        });
        System.out.println(computeIfAbsent);
        System.out.println(map);
        System.out.println(computeIfPresent);
    }

    @Test
    public void testMap() {
        Map<Integer, String> map = MapUtil.newHashMap(new Tuple<>(1, "a"), new Tuple<>(2, "b"));
        System.out.println(map);
    }

    @Test
    public void sleepTime() {
        System.out.println((10000 - 4014) * 10);
    }


    @Test
    public void testCount() {
        User user = new User();
        user.setId(1L);
        User user1 = new User();
        long count = Lists.newArrayList(user, user1).stream().map(User::getId).distinct().count();
        System.out.println(count);
    }

    @Test
    public void testJsonField() {
        Company company = new Company();
        company.setId(1L);
        company.setName("123");
        String json = JsonUtil.encodeJson(company);
        Company decodeJson = JsonUtil.decodeJson(json, Company.class);
        System.out.println(decodeJson);
    }

    @Test
    public void testDate() {
        LocalDate.now().minusDays(1L).atStartOfDay();
    }

    @Test
    public void testValidate() {
        User user = new User();
        ValidateUtil.validate(user);
    }

    @Test
    public void testDateUtil() {
        Instant now = Instant.now();
        LocalDateTime localDateTime = DateUtil.toLocalDateTime(now);
        String s = DateUtil.toString(localDateTime, "yyyy-MM-dd HH:mm:ss");
        System.out.println(s);
    }

    @Test
    public void date() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime localDateTime = LocalDateTime.of(2018, 9, 2, 0, 0);
        System.out.println(Duration.between(now, localDateTime).toDays());
        System.out.println(Duration.between(LocalDate.now().atStartOfDay(), localDateTime).toDays());
    }

    @Test
    public void gener() {
        String prefix = "  partition NATIONAL_B_D_LOG_P";
        String value = "  values (";
        String end = "),";
        for (int i = 1; i < 41; i++) {
            System.out.println(prefix + i + value + i + end);

        }
    }

    @Test
    public void testCop() {
        Map<String, String> map = new HashMap<>();
        map.computeIfPresent("1", (oldKey, oldValue) -> {
            System.out.println(oldKey);
            System.out.println(oldValue);
            return "2";
        });
        System.out.println(map);
    }

    @Test
    public void resourceSpecSql() {
        String prefix = "ALTER TABLE RESOURCE_SPEC ADD INFO";
        String suffix = "_CONSTRAINT VARCHAR2(100);";
        for (int i = 1; i < 10; i++) {
            System.out.println(prefix + i + suffix);
        }
    }

    static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    @Test
    public void testHash() {
        String key1 = "98127983719";
        String key2 = "98127983719";
        String key3 = "983719";
        System.out.println(hash(key1));
        System.out.println(hash(key2));
        System.out.println(hash(key3));
    }

    @Test
    public void test() {
        ArrayList<Integer> integers = Lists.newArrayList(1, 2, 3, 4, 5);
        List<List<Integer>> partition = Lists.partition(integers, 10);
        System.out.println(partition);
    }

    @Test
    public void testJsonDecode() {
        String json = "[{\"说明与描述\":\"消息ID，其中，@root=\\\"待定\\\"\",\"CDR字段\":\"id\",\"元素路径\":\"/id/@extension\",\"是否必填\":\"1.0\",\"CDR表名\":\"PRPA_IN201311UV02\",\"字段类型\":\"\",\"对应的数据元标识符\":\"\"},{\"说明与描述\":\"消息创建时间\",\"CDR字段\":\"creation_time\",\"元素路径\":\"/creationTime/@value\",\"是否必填\":\"1.0\",\"CDR表名\":\"PRPA_IN201311UV02\",\"字段类型\":\"\",\"对应的数据元标识符\":\"\"},{\"说明与描述\":\"状态代码，固定值”active”\",\"CDR字段\":\"status_code\",\"元素路径\":\"/controlActProcess/subject/registrationEvent/statusCode/@code\",\"是否必填\":\"1.0\",\"CDR表名\":\"PRPA_IN201311UV02\",\"字段类型\":\"\",\"对应的数据元标识符\":\"\"},{\"说明与描述\":\"要合并到的患者 ID,其中@root=”待分配的应用系统OID”\",\"CDR字段\":\"patient_id\",\"元素路径\":\"/controlActProcess/subject/registrationEvent/subject1/patient/id/@extension\",\"是否必填\":\"0.0\",\"CDR表名\":\"PRPA_IN201311UV02\",\"字段类型\":\"\",\"对应的数据元标识符\":\"\"},{\"说明与描述\":\"状态代码，固定值”active”\",\"CDR字段\":\"patient_status_code\",\"元素路径\":\"/controlActProcess/subject/registrationEvent/subject1/patient/statusCode/@code\",\"是否必填\":\"1.0\",\"CDR表名\":\"PRPA_IN201311UV02\",\"字段类型\":\"\",\"对应的数据元标识符\":\"\"},{\"说明与描述\":\"患者登记时间\",\"CDR字段\":\"patient_effective_time\",\"元素路径\":\"/controlActProcess/subject/registrationEvent/subject1/patient/effectiveTime/@value\",\"是否必填\":\"1.0\",\"CDR表名\":\"PRPA_IN201311UV02\",\"字段类型\":\"\",\"对应的数据元标识符\":\"DE06.00.218.00\"},{\"说明与描述\":\"身份证号，其中，@root=\\\"2.16.156.10011.2.2.1\\\"\",\"CDR字段\":\"patient_person_id\",\"元素路径\":\"/controlActProcess/subject/registrationEvent/subject1/patient/patientPerson/id/@extension\",\"是否必填\":\"0.0\",\"CDR表名\":\"PRPA_IN201311UV02\",\"字段类型\":\"\",\"对应的数据元标识符\":\"DE02.01.030.00\"},{\"说明与描述\":\"要合并到的姓名\",\"CDR字段\":\"patient_persion_name\",\"元素路径\":\"/controlActProcess/subject/registrationEvent/subject1/patient/patientPerson/name\",\"是否必填\":\"1.0\",\"CDR表名\":\"PRPA_IN201311UV02\",\"字段类型\":\"\",\"对应的数据元标识符\":\"DE02.01.039.00\"},{\"说明与描述\":\"操作人职工代码，其中@root=”待定”\",\"CDR字段\":\"assign_persion_id\",\"元素路径\":\"/controlActProcess/subject/registrationEvent/subject1/custodian/assignedEntity/id/@extension\",\"是否必填\":\"1.0\",\"CDR表名\":\"PRPA_IN201311UV02\",\"字段类型\":\"\",\"对应的数据元标识符\":\"\"},{\"说明与描述\":\"操作人姓名\",\"CDR字段\":\"assign_persion_name\",\"元素路径\":\"/controlActProcess/subject/registrationEvent/subject1/custodian/assignedEntity/assignedPerson/name\",\"是否必填\":\"0.0\",\"CDR表名\":\"PRPA_IN201311UV02\",\"字段类型\":\"\",\"对应的数据元标识符\":\"DE02.01.039.00\"},{\"说明与描述\":\"状态代码，固定值”obsolete”\",\"CDR字段\":\"\",\"元素路径\":\"/controlActProcess/subject/registrationEvent/subject1/replacementOf/priorRegistration/statusCode/@code\",\"是否必填\":\"1.0\",\"CDR表名\":\"\",\"字段类型\":\"\",\"对应的数据元标识符\":\"\"},{\"说明与描述\":\"失效的患者ID,其中@root=”待分配的应用系统OID”\",\"CDR字段\":\"patient_id\",\"元素路径\":\"/controlActProcess/subject/registrationEvent/subject1/replacementOf/priorRegistration/subject1/priorRegisteredRole/id/@extension\",\"是否必填\":\"1.0\",\"CDR表名\":\"PRPA_IN201311UV02\",\"字段类型\":\"\",\"对应的数据元标识符\":\"\"}]";
        List<StructureEntity> structureEntities = JsonUtil.decodeArrayJson(json, StructureEntity.class);
        System.out.println(structureEntities);
    }

    public static void main(String[] args) {
        get("/hello", (request, response) -> "Hello World!");
    }

    @Test
    public void testPrT() {
        String s = StreamUtil.getString(null);
//        String s = JsonUtil.encodeJson(null);
        printTrack();
        System.out.println(s);
    }

    void printTrack() {
        StackTraceElement[] st = Thread.currentThread().getStackTrace();
        StringBuffer sbf = new StringBuffer();
        for (StackTraceElement e : st) {
            if (sbf.length() > 0) {
                sbf.append(" <- ");
                sbf.append(System.getProperty("line.separator"));
            }
            sbf.append(java.text.MessageFormat.format("{0}.{1}() {2}"
                    , e.getClassName()
                    , e.getMethodName()
                    , e.getLineNumber()));
        }
        System.out.println(sbf.toString());
    }

    @Test
    public void testrex() {
        String rex = "^[A-Za-z]{5}\\w{8}";
        String s = "ASDF1G2345678";
        System.out.println(s.matches(rex));
        String rex2 = "\\d{17}";
        System.out.println("27082812305049294".matches(rex2));

    }

    @Test
    public void testAsList() {
        Long[] longs = {1L,2L};
        List<Long> longList = Arrays.asList(longs);
        System.out.println(longList);
    }

}
