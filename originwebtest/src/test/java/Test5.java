import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.junit.Test;
import star.bean.Company;
import star.bean.User;
import star.utils.*;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

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
    public static void main(String[] args) {
        get("/hello", (request, response) -> "Hello World!");
    }
}
