import com.google.common.collect.Maps;
import org.junit.Test;
import star.utils.ArrayUtil;
import star.utils.MapUtil;
import star.utils.Tuple;

import java.util.HashMap;
import java.util.Map;

public class Test5 {

    @Test
    public void testComputeIIfAbsent() {
        Map<String, String> map = new HashMap<>();
        map.put("1", "a");
        String b = map.putIfAbsent("1", "b");
        System.out.println(map);
        System.out.println(b);
        String computeIfPresent = map.computeIfPresent("1", (oldValue, newValue) -> oldValue + newValue);
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
}
