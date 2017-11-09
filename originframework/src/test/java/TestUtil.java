import org.junit.Test;
import star.utils.ClassUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * @author keshawn
 * @date 2017/11/9
 */
public class TestUtil {
    @Test
    public void testClassUtil() {
        Set<Class<?>> set = ClassUtil.getClassSet("org.junit");
        System.out.println(set);
    }

    @Test
    public void testArrayList() {
        int i = 0;
        List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6));
        while (true) {
            Integer n = null;
            if (i < list.size()) {
                n = list.get(i);
                i++;
                System.out.println(n);
                if (n < 3) {
                    list.add(10);
                }
            } else {
                break;
            }
        }
    }
}
