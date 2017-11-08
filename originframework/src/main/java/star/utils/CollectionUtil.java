package star.utils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author keshawn
 * @date 2017/11/8
 */
public final class CollectionUtil {

    public static boolean isEmpty(Collection<?> collection){
        return CollectionUtils.isEmpty(collection);
    }

    public static boolean isNotEmpty(Collection<?> collection){
        return !isEmpty(collection);
    }

    public static boolean isEmpty(Map<?,?> map){
        return MapUtils.isEmpty(map);
    }

    public static boolean isNotEmpty(Map<?,?> map){
        return !isEmpty(map);
    }

    public static <T> List<List<T>> splitList(List<T> dataList, int size) {
        if (size <= 0) {
            throw new RuntimeException("Size Must More Than And Equal To 1");
        }
        List<List<T>> result = new ArrayList<>();
        List<T> unit = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            unit.add(dataList.get(i));
            if (unit.size() == size || i == dataList.size() - 1) {
                result.add(unit);
                unit = new ArrayList<>();
            }
        }
        return result;
    }
}
