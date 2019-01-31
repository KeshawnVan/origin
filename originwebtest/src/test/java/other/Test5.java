package other;

import com.google.common.collect.Lists;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import star.bean.BaseDTO;
import star.bean.Company;
import star.bean.StructureEntity;
import star.bean.User;
import star.utils.*;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


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
        Long[] longs = {1L, 2L};
        List<Long> longList = Arrays.asList(longs);
        System.out.println(longList);
    }

    @Test
    public void testStream() throws Exception {
        byte[] buffer = new byte[1024];
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(new byte[0]);
        ServletInputStream servletInputStream = new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            @Override
            public int read() throws IOException {
                return byteArrayInputStream.read();
            }
        };
        int read = servletInputStream.read();
    }

    @Test
    public void testSpec() {
        // 1.智能卡：0开头，共11位数字
        String smartCardRex = "^0\\d{10}";
        String smartCardNum = "01234567890";
        String errorSmartCardNum = "001123456789";
        System.out.println(smartCardNum.matches(smartCardRex));
        System.out.println(errorSmartCardNum.matches(smartCardRex));

        // 2.机顶盒：17位数字
        String stbRex = "\\d{17}";
        String stbCode = "27082812305049294";
        String errorStbCode = "2708281230504929";
        System.out.println(stbCode.matches(stbRex));
        System.out.println(errorStbCode.matches(stbRex));

        // 3.机顶盒验证码：00开头，共11位数字
        String stbVerificationRex = "^00\\d{9}";
        String stbVerification = "00123456789";
        String errorStbVerification = "01123456789";
        System.out.println(stbVerification.matches(stbVerificationRex));
        System.out.println(errorStbVerification.matches(stbVerificationRex));

        // 4.CAM卡：W大写字母开头，共14位，大写字母和数字组成
        String camRex = "^W[A-Z0-9]{13}";
        String camCard = "W1234567890ABC";
        String errorCamCard = "W1234567890abc";
        System.out.println(camCard.matches(camRex));
        System.out.println(errorCamCard.matches(camRex));
    }

    @Test
    public void testClients() {
        String cardErrorRex = "Client \\w+ does not exist.";
        String cardErrorInfo = "Client 2139112357 does not exist.";
        System.out.println(cardErrorInfo.matches(cardErrorRex));
        String substring = cardErrorInfo.substring(6, 18);
        String trim = substring.trim();
        System.out.println(trim);
        String cardNum = cardErrorInfo.replace("Client ", "").replace(" does not exist.", "");
        System.out.println(cardNum);
    }

    @Test
    public void buildTaskSql() {
        String originSql = "CREATE TABLE `task` (\n" +
                "\t`id` BIGINT (20) NOT NULL AUTO_INCREMENT COMMENT '标识|fankx|2018-11-05',\n" +
                "\t`name` VARCHAR (100) NOT NULL COMMENT '名称|wanggx|2018-03-19',\n" +
                "\t`code` VARCHAR (50) NOT NULL COMMENT '优惠券唯一编码|wanggx|2018-03-19',\n" +
                "\t`brief_desc` VARCHAR (500) COMMENT '优惠券简述|wanggx|2018-03-19',\n" +
                "  `coupon_type` TINYINT (2) COMMENT '优惠类型@0:满减@1:满赠@2:折扣|wanggx|2018-03-19',\n" +
                "  `country` VARCHAR (2) NOT NULL COMMENT '国家|wanggx|2018-03-19',\n" +
                "\t`valid_start_time` DATETIME NOT NULL COMMENT '有效期开始时间|wanggx|2018-03-19',\n" +
                "\t`valid_end_time` DATETIME NOT NULL COMMENT '有效期结束时间|wanggx|2018-03-19',\n" +
                "\t`publish_start_time` DATETIME NOT NULL COMMENT '发布开始时间|wanggx|2018-03-19',\n" +
                "\t`publish_end_time` DATETIME NOT NULL COMMENT '发布结束时间|wanggx|2018-03-19',\n" +
                "  `valid_days_after_get` INT COMMENT '领券后N天内有效|wanggx|2018-03-19',\n" +
                "  `valid_days_after_activation` INT COMMENT '激活后N天有效|wanggx|2018-03-19',\n" +
                "  `coupon_effective_type` TINYINT (2) NOT NULL COMMENT '优惠券生效类型@0:普通券，@1:激活券|wanggx|2018-03-19',\n" +
                "  `publish_channel` VARCHAR(50) COMMENT '发布渠道|wanggx|2018-03-19',\n" +
                "\t`user_type` VARCHAR (50) COMMENT '用户类型|wanggx|2018-03-19',\n" +
                "\t`issue_count` INT COMMENT '总发行量|wanggx|2018-03-19',\n" +
                "\t`one_time_get_limit` INT NOT NULL DEFAULT 1 COMMENT '单次限领数量|wanggx|2018-03-19',\n" +
                "\t`one_time_use_limit` INT NOT NULL DEFAULT 1 COMMENT '单次限使数量|wanggx|2018-03-19',\n" +
                "\t`amount_threshhold` DECIMAL(10,2) COMMENT '可使用优惠的最低金额|wanggx|2018-03-19',\n" +
                "  `currency` VARCHAR (50) COMMENT '货币代码，货币编码 采用active codes表中的3位编码|wanggx|2018-03-19',\n" +
                "  `preferential_amount` DECIMAL(10,2) COMMENT '满减金额，支持两位小数|wanggx|2018-03-19',\n" +
                "  `discount` DECIMAL(5,2) COMMENT '折扣劵时减掉的折扣，支持两位小数，如88折时，该值为：12，单位为：%|wanggx|2018-03-19',\n" +
                "  `use_guide` VARCHAR (1024) COMMENT '使用说明|wanggx|2018-03-19',\n" +
                "  `logo_url` VARCHAR (1024) COMMENT 'LOGO|wanggx|2018-03-19',\n" +
                "  `gift_type` TINYINT (2) COMMENT '赠送券赠送类型@0:商品，@1:优惠券|wanggx|2018-03-19',\n" +
                "  `use_scope` TINYINT (2) COMMENT '使用范围@0:ALL通用，@1:SINGLE_PRODUCTION单品|wanggx|2018-03-19',\n" +
                "  `delete_flag` TINYINT (2) NOT NULL DEFAULT 1 COMMENT '数据有效状态@1:有效@0:无效|wanggx|2018-03-19',\n" +
                "\t`version` INT (10) DEFAULT 1 COMMENT '版本号|wanggx|2018-03-19',\n" +
                "\t`create_id` INT (10) COMMENT '创建者标识|wanggx|2018-03-19',\n" +
                "\t`create_instant` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间|wanggx|2018-03-19',\n" +
                "\t`modify_id` INT (10) COMMENT '修改者标识|wanggx|2018-03-19',\n" +
                "\t`modify_instant` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间|wanggx|2018-03-19',\n" +
                "\t`transaction_id` VARCHAR (100) COMMENT '事务标识|wanggx|2018-03-19',\n" +
                "\t`server_name` VARCHAR (100) COMMENT '调用服务器信息|wanggx|2018-03-19',\n" +
                "\tPRIMARY KEY (`id`)\n" +
                ") ENGINE = INNODB COMMENT = 'promotion_coupon|优惠券表|wanggx|2018-03-19' CHARACTER\n" +
                "SET utf8 COLLATE utf8_general_ci;";
        System.out.println(StringUtils.replaceAll(originSql, "wanggx\\|2018-03-19", "fankx|2018-11-05"));
    }

    @Test
    public void read() throws Exception {
        String fileName = "cs302363.emm";
        File file = FileUtil.createFile("C:\\Users\\10007675\\Documents\\WeChat Files\\fkx0703\\Files\\2018-11-05");
        for (File dir : file.listFiles()) {
            File[] files = dir.listFiles();
            if (files == null) continue;
            for (File f : files) {
                if (f.getName().equals(fileName) || FileUtils.readFileToString(f).contains(fileName)) {
                    System.out.println(dir.getName());
                }
            }
        }
    }

    @Test
    public void formatSql() {
        String sql = "CREATE TABLE `action` (\n" +
                "\t`id` INT (10) NOT NULL AUTO_INCREMENT COMMENT '标识|fankx|2018-11-05',\n" +
                "\t`name` VARCHAR (100) NOT NULL COMMENT '名称|fankx|2018-11-05',\n" +
                "\t`description` VARCHAR (1024) COMMENT '描述|fankx|2018-11-05',\n" +
                "\t`code` VARCHAR (100) NOT NULL COMMENT '编码|fankx|2018-11-05',\n" +
                "  `is_data_valid` TINYINT (1) NOT NULL DEFAULT 1 COMMENT '数据是否有效 布尔值：0无效、1有效|fankx|2018-11-05',\n" +
                "  `data_mem` VARCHAR (1024) COMMENT '数据备注 数据修复使用，一般界面不需要展示该字段|fankx|2018-11-05',\n" +
                "\t`version` INT (10) DEFAULT 1 COMMENT '版本号|fankx|2018-11-05',\n" +
                "\t`create_id` INT (10) COMMENT '创建者标识|fankx|2018-11-05',\n" +
                "\t`create_instant` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间|fankx|2018-11-05',\n" +
                "\t`modify_id` INT (10) COMMENT '修改者标识|fankx|2018-11-05',\n" +
                "\t`modify_instant` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间|fankx|2018-11-05',\n" +
                "\t`transaction_id` VARCHAR (100) COMMENT '事务标识|fankx|2018-11-05',\n" +
                "\t`server_name` VARCHAR (100) COMMENT '调用服务器信息|fankx|2018-11-05',\n" +
                "\tPRIMARY KEY (`id`)\n" +
                ") ENGINE = INNODB COMMENT = 'action|任务表|fankx|2018-11-05' CHARACTER\n" +
                "SET utf8 COLLATE utf8_general_ci;";
        System.out.println(StringUtils.replaceAll(sql, "2018-11-05", "2018-11-06"));
    }

    @Test
    public void sorted() {
        String time = "2018-11-08 12:12:12";
        LocalDateTime parse1 = LocalDateTime.parse(time, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.println(parse1);
    }

    @Test
    public void equ() {
        int size = 2;
        long count = 2L;
        System.out.println(size == count);
    }

    @Test
    public void test1() {
        ArrayList<Integer> integers = Lists.newArrayList(1, 2);
        Integer remove = integers.remove(1);
        System.out.println(integers);
    }

    @Test
    public void test2() {
        System.out.println(String.valueOf(1111417780L));
    }

    @Test
    public void sdm() throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = "2018-11-08 12:12:12";
        System.out.println(simpleDateFormat.parse(time));
    }

    @Test
    public void clear() {
        ArrayList<Integer> integers = Lists.newArrayList(1, 2, 3);
        integers.clear();
        System.out.println(integers);
        integers.add(1);
        System.out.println(integers);
    }

    @Test
    public void testFormat() {
        DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.of("+0"));
        System.out.println(DATE_TIME_FORMATTER.format(Instant.now()));
    }

    @Test
    public void testDownload() throws Exception {
        String url = "https://startimestv-service-data.s3.eu-west-1.amazonaws.com/dev/12323.txt?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20181127T052040Z&X-Amz-SignedHeaders=host&X-Amz-Expires=899&X-Amz-Credential=AKIAIV4HHHAXIVKVVE3A%2F20181127%2Feu-west-1%2Fs3%2Faws4_request&X-Amz-Signature=69aaabe56334759f51bd46b128e54e120239f311e431b838ac6d975844ce7793";
        HttpResponse<InputStream> inputStreamHttpResponse = Unirest.get(url).asBinary();
        String content = StreamUtil.getString(inputStreamHttpResponse.getBody());
        System.out.println(content);
    }

    @Test
    public void testZip() throws Exception {
        String path = "C:\\Users\\10007675\\Downloads\\users.zip";
        getZipContent(path);
    }

    private void getZipContent(String path) throws IOException {
        ZipFile zipFile = new ZipFile(path);
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            ZipEntry zipEntry = entries.nextElement();
            InputStream inputStream = zipFile.getInputStream(zipEntry);
            String string = StreamUtil.getString(inputStream);
            System.out.println(string);
        }
    }

    @Test
    public void testFindFirst() {
        Optional<Integer> first = Lists.newArrayList(null, 1).stream().findFirst();
        System.out.println(first);
    }

    @Test
    public void testG() {
        List<Company> companies = new ArrayList<>();
        t(companies);
        companies.add(new Company());
        Company baseDTO = new Company();
        List<Company> arrayList = Lists.newArrayList(baseDTO);
    }

    @Test
    public void testRight() {
        System.out.println(1 >> 1);
        System.out.println(NumberUtil.scale2(Math.pow(8D, 8D)));
    }

    public static void t(List<? extends BaseDTO> baseDTOS) {
        BaseDTO baseDTO = baseDTOS.get(0);
    }

    @Test
    public void testPlusDay() {
        LocalDate now = LocalDate.now();
        System.out.println(now);
        System.out.println(now.plusDays(3));
    }

    @Test
    public void castS() {
        String config109 = "-DGIT_PASSWORD=root\n" +
                "-DGIT_USERNAME=1q2w3e4R\n" +
                "-Dpartner_service=10.0.251.109\n" +
                "-Dproblem_service=10.0.251.109\n" +
                "-Dnote_service=10.0.251.109\n" +
                "-Dcollection_center_service=10.0.251.109\n" +
                "-Dresource_service=10.0.251.109\n" +
                "-Daccount_center_service=10.0.251.109\n" +
                "-Dsystem_query_service=10.0.251.109\n" +
                "-Dresource_center_service=10.0.251.109\n" +
                "-Dcard_center_service=10.0.251.109\n" +
                "-Diom_service=10.0.251.109\n" +
                "-Dchannel_service=10.0.251.109\n" +
                "-Darea_service=10.0.251.109\n" +
                "-Dproblem_center_service=10.0.251.109\n" +
                "-Drequest_graduate_service=10.0.251.109\n" +
                "-Dcustomer_center_service=10.0.251.109\n" +
                "-Dorder_center_service=10.0.251.109\n" +
                "-Dcard_service=10.0.251.109\n" +
                "-Dcheck_service=10.0.251.109\n" +
                "-Daccount_service=10.0.251.109\n" +
                "-Diom_center_service=10.0.251.109\n" +
                "-Dpms_center_service=10.0.251.109\n" +
                "-Dsystem_service=10.0.251.109\n" +
                "-Dcustomer_service=10.0.251.109\n" +
                "-Dknowledge_service=10.0.251.109\n" +
                "-Dproduct_service=10.0.251.109\n" +
                "-Djob_service=10.0.251.109\n" +
                "-Dpms_frontend_conax_service=10.0.251.109\n" +
                "-Dpms_partition_service=10.0.251.109\n" +
                "-Dapi_gateway_service=10.0.251.109\n" +
                "-Dcollection_service=10.0.251.109\n" +
                "-Dorder_service=10.0.251.109\n" +
                "-Dnote_center_service=10.0.251.109\n" +
                "-Dmessage_center_service=10.0.251.109\n" +
                "-Drest_port=8080\n" +
                "-Dstariboss_version=109";
        System.out.println(config109.replaceAll("109", "187"));
    }

    @Test
    public void testFor() {
        DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.of("+0"));
        String format = DATE_TIME_FORMATTER.format(LocalDate.now().atStartOfDay());
        System.out.println(format);

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.of("+0"));
        System.out.println(dateFormatter.format(LocalDate.now()));

        System.out.println(LocalDate.now().toString());
    }

    @Test
    public void testFile() {
        File file = FileUtil.createFile("C:\\Users\\Administrator\\IdeaProjects\\stariboss\\employee-web\\finance_other\\partner-ui\\src\\main\\webapp\\messages");
        File[] files = file.listFiles();
        System.out.println(false);
    }

    @Test
    public void n() {
        int[] items = {4, 1, 2, 1, 2};
        Map<Integer, Integer> map = new HashMap<>();
        for (int item : items) {
            Integer count = map.get(item);
            if (count == null) {
                map.put(item, 1);
            } else {
                map.remove(item);
            }
        }
        System.out.println(map.values());
    }

    @Test
    public void poll() throws Exception{
        BlockingQueue<String> queue = new LinkedBlockingQueue<>();
        System.out.println("start poll");
        String poll = queue.poll(10, TimeUnit.SECONDS);
        System.out.println("poll end");
    }

    @Test
    public void testDateMin() {
        Date date = new Date();
        Date min = new Date(System.currentTimeMillis() - 1000 * 60);
        System.out.println(date);
        System.out.println(min);
    }

    @Test
    public void compare() {
        Integer[] array = {6,1,3,4,5};
        System.out.println(findMax(1, array));
    }

    private static <T> T findMax(T x, Object[] array) {
        T max = (T) array[0];
        for (Object o : array) {
            Comparable<? super T> comparable = (Comparable<? super T>) o;
            if (comparable.compareTo(max) > 0) max = (T) o;
        }
        return max;
    }
}
