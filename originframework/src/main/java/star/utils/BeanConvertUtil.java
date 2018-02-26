package star.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author keshawn
 * @date 2017/11/8
 */

public final class BeanConvertUtil {

    private static final int INITIAL_CAPACITY = 12;

    private static final Logger LOGGER = LoggerFactory.getLogger(BeanConvertUtil.class);

    private BeanConvertUtil() {
    }

    public static Map<String, Object> toMap(Object obj) {
        Map<String, Object> result = new HashMap<>(INITIAL_CAPACITY);
        List<Field> fields = getFields(obj.getClass());
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                result.put(field.getName(), field.get(obj));
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
        return result;
    }

    private static List<Field> getFields(Class clazz) {
        List<Field> result = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            result.add(field);
        }
        if (clazz.getSuperclass() != null) {
            result.addAll(getFields(clazz.getSuperclass()));
        }
        return result;
    }
}
