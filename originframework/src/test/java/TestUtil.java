import org.junit.Test;
import star.utils.ClassUtil;

import java.util.Set;

/**
 * @author keshawn
 * @date 2017/11/9
 */
public class TestUtil {
    @Test
    public void testClassUtil(){
        Set<Class<?>> set = ClassUtil.getClassSet("org.junit");
        System.out.println(set);
    }
}
