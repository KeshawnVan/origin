import com.google.common.collect.Lists;
import org.junit.Test;
import star.utils.CastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author keshawn
 * @date 2018/1/9
 */
public class Test2 {

    @Test
    public void testLongAndInt(){
        int a = 2;
        long b = 2L;
        System.out.println(a == CastUtil.castInt(b));
    }

    @Test
    public void testList(){
//        ArrayList<String> l1 = Lists.newArrayList(1, "b", "c", "d");
//        ArrayList<String> l2 = Lists.newArrayList("a", "b", "c", "d", "e");
        ArrayList<Integer> integers = Lists.newArrayList(1, 2, 3, 4);
        ArrayList<Integer> integers2 = Lists.newArrayList(1, 2, 3, 4, 5);
//        integers2.removeAll(integers);
//        System.out.println(integers2);
        integers2.retainAll(integers);
        System.out.println(integers2);
    }
}
