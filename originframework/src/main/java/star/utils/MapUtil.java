package star.utils;

import java.util.HashMap;
import java.util.Map;

public final class MapUtil {

    private MapUtil() {
    }

    public static <K, V> Map<K, V> newHashMap(Tuple<K, V>... tuples) {
        HashMap<K, V> map = new HashMap<>();
        if (ArrayUtil.isNotEmpty(tuples)) {
            for (Tuple<K, V> tuple : tuples) {
                if (tuple != null) {
                    map.put(tuple._1, tuple._2);
                }
            }
        }
        return map;
    }
}
