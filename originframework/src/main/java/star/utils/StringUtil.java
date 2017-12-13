package star.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author keshawn
 * @date 2017/11/8
 */
public final class StringUtil {

    private static final Pattern PATTERN = Pattern.compile("[A-Z]([a-z\\d]+)?");

    private static final String URL_PREFIX = "/";

    private static final String BLANK = "";

    private static final Pattern TIME_PATTERN = Pattern.compile("^(((20[0-9][0-9]-(0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|(20[0-3][0-9]-(0[2469]|11)-(0[1-9]|[12][0-9]|30))) (20|21|22|23|[0-1][0-9]):[0-5][0-9]:[0-5][0-9])$");


    public static boolean isEmpty(String string) {
        if (string != null) {
            string = string.trim();
        }
        return StringUtils.isEmpty(string);
    }

    public static boolean isNotEmpty(String string) {
        return !isEmpty(string);
    }

    public static String getStackTrace(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        pw.flush();
        pw.close();
        return sw.toString();
    }

    public static String toHex(byte[] hash) {
        StringBuilder buf = new StringBuilder(hash.length * 2);
        int i;

        for (i = 0; i < hash.length; i++) {
            if (((int) hash[i] & 0xff) < 0x10) {
                buf.append("0");
            }
            buf.append(Long.toString((int) hash[i] & 0xff, 16));
        }
        return buf.toString();
    }

    public static boolean checkStringIsAllDigital(String str) {
        return StringUtils.isNumeric(str);
    }

    public static boolean isSame(Object first, Object second) {
        if (first == null && second == null) {
            return true;
        }
        if (first != null && second == null) {
            return false;
        }
        if (first == null && second != null) {
            return false;
        } else {
            return first.equals(second);
        }
    }

    public static String firstToLowerCase(String string) {
        if (isEmpty(string)) {
            return BLANK;
        }
        return (new StringBuilder()).append(Character.toLowerCase(string.charAt(0))).append(string.substring(1)).toString();
    }

    public static String castJsonString(Object object) {
        if (object != null) {
            return castJsonString(object.toString());
        } else {
            return BLANK;
        }
    }

    public static String castJsonString(String string) {
        if (isEmpty(string)) {
            return BLANK;
        }
        if (string.startsWith("{") || string.startsWith("[")) {
            return string;
        }
        return "\"" + string + "\"";
    }

    public static String camel2Underline(String line) {
        if (isEmpty(line)) {
            return BLANK;
        }
        line = String.valueOf(line.charAt(0)).toUpperCase().concat(line.substring(1));
        StringBuffer sb = new StringBuffer();
        Matcher matcher = PATTERN.matcher(line);
        while (matcher.find()) {
            String word = matcher.group();
            sb.append(word.toLowerCase());
            sb.append(matcher.end() == line.length() ? "" : "_");
        }
        return sb.toString();
    }

    public static String getSetMethodName(String fieldName) {
        String prefix = "set";
        String fieldFirst = fieldName.substring(0, 1).toUpperCase();
        return prefix + fieldFirst + fieldName.substring(1, fieldName.length());
    }

    public static String checkUrlPrefix(String requestMapping) {
        String classRequestMapping;
        if (requestMapping.startsWith(URL_PREFIX)) {
            classRequestMapping = requestMapping;
        } else {
            classRequestMapping = URL_PREFIX + requestMapping;
        }
        return classRequestMapping;
    }

    public static String removeLast(String string){
        if (isEmpty(string)){
            return BLANK;
        }
        return string.substring(0,string.length() - 1);
    }

    public static boolean checkTimeValid(String patternString) {
        Matcher matcher= TIME_PATTERN.matcher(patternString);
        return matcher.matches();
    }
}
