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
        if (null != str) {
            int len = str.length();
            Pattern p = Pattern.compile("[0-9]{" + len + "}");
            Matcher m = p.matcher(str);
            if (!m.matches()) {
                return false;
            }
        }
        return true;
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
}
