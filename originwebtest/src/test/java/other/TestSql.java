package other;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Pattern;

public class TestSql {

    @Test
    public void testIf(){
        String sql = "select * from user |if (name != null) where name = #{name} if|";
        String regex = "\\if\\(([^\\\\}]+)\\}";
        String[] substringsBetween = StringUtils.substringsBetween(sql, "|if", "if|");
        Arrays.asList(substringsBetween).stream().forEach(seg -> {
            System.out.println(seg);
        });
        System.out.println(substringsBetween);
    }

}
