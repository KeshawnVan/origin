package star.helper;

import star.utils.CollectionUtil;

import java.util.Map;

/**
 * @author keshawn
 * @date 2017/11/9
 */
public final class IocHelper {
    static {
        Map<Class<?>, Object> beanMap = BeanHelper.getBeanMap();
        if (CollectionUtil.isNotEmpty(beanMap)) {
            for (Map.Entry<Class<?>, Object> beanEntry : beanMap.entrySet()) {
                Class<?> beanClass = beanEntry.getKey();
                //TODO
            }
        }
    }
}
