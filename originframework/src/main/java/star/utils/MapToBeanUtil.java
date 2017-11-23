package star.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Map;

import static star.utils.StringUtil.camel2Underline;

/**
 * @author keshawn
 * @date 2017/11/23
 */
public class MapToBeanUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(MapToBeanUtil.class);

    private MapToBeanUtil() {

    }

    public static <T> T buildBeanWithUnderLine(Map<String, Object> map, Class<T> beanClass) {
        T bean = null;
        try {
            bean = beanClass.newInstance();
            Field[] fields = beanClass.getDeclaredFields();
            for (Field field : fields) {
                String fieldName = field.getName();
                Object mapValue = map.get(camel2Underline(fieldName));
                String jsonValue = StringUtil.castJsonString(mapValue);
                Class<?> fieldClass = field.getType();
                ReflectionUtil.setField(bean, field, JsonUtil.decodeJson(jsonValue, fieldClass));
            }
        } catch (Exception e) {
            LOGGER.error("buildBeanWithUnderLine error ,", e);
        }
        return bean;
    }

    public static <T> T buildBean(Map<String, Object> map, Class<T> beanClass) {
        T bean = null;
        try {
            bean = beanClass.newInstance();
            Field[] fields = beanClass.getDeclaredFields();
            for (Field field : fields) {
                String fieldName = field.getName();
                Object mapValue = map.get(fieldName);
                String jsonValue = StringUtil.castJsonString(mapValue);
                Class<?> fieldClass = field.getType();
                ReflectionUtil.setField(bean, field, JsonUtil.decodeJson(jsonValue, fieldClass));
            }
        } catch (Exception e) {
            LOGGER.error("buildBean error ,", e);
        }
        return bean;
    }
}
