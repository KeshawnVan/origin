import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;
import star.annotation.Inject;
import star.factory.BeanFactory;
import star.utils.ClassUtil;
import star.utils.CollectionUtil;
import star.bean.YamlBean;
import star.utils.StringUtil;

import java.lang.reflect.Field;
import java.util.*;

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

    @Test
    public void testIoc(){
//        Map<Class<?>, Object> beanMap = BeanFactory.getBeanMap();
//        if (CollectionUtil.isNotEmpty(beanMap)) {
//            for (Map.Entry<Class<?>, Object> beanEntry : beanMap.entrySet()) {
//                Class<?> beanClass = beanEntry.getKey();
//                Object beanInstance = beanEntry.getValue();
//                Field[] beanFields = beanClass.getDeclaredFields();
//                if (ArrayUtils.isNotEmpty(beanFields)){
//                    for (Field beanField : beanFields){
//                        if (beanField.isAnnotationPresent(Inject.class)){
//                            System.out.println(beanField.getName());
//                        }
//                    }
//                }
//            }
//        }
    }
    @Test
    public void testClassType(){
        YamlBean yamlBean = new YamlBean();
        System.out.println(yamlBean.getClass().getTypeName());
    }

    @Test
    public void testMethodDI(){
        String s = "DASSD";
        System.out.println(StringUtil.firstToLowerCase(s));
    }
}
