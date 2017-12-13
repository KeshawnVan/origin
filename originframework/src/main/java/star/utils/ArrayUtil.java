package star.utils;

import org.apache.commons.lang3.ArrayUtils;

/**
 * @author keshawn
 * @date 2017/12/5
 */
public final class ArrayUtil {
    private ArrayUtil(){
        //do nothing
    }

    public static <T> boolean isEmpty(T[] array){
        return ArrayUtils.isEmpty(array);
    }

    public static <T> boolean isNotEmpty(T[] array){
        return !isEmpty(array);
    }
}
