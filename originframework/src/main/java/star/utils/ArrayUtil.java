package star.utils;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;

/**
 * @author keshawn
 * @date 2017/12/5
 */
public final class ArrayUtil {
    private ArrayUtil() {
        //do nothing
    }

    public static <T> boolean isEmpty(T[] array) {
        return ArrayUtils.isEmpty(array);
    }

    public static <T> boolean isNotEmpty(T[] array) {
        return !isEmpty(array);
    }

    public static <T> T[] concat(T[] first, T[] second) {
        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    public static <T> T[] concatAll(T[] first, T[]... rest) {
        int totalLength = first.length;
        for (T[] array : rest) {
            totalLength += array.length;
        }
        T[] result = Arrays.copyOf(first, totalLength);
        int offset = first.length;
        for (T[] array : rest) {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }
        return result;
    }
}
