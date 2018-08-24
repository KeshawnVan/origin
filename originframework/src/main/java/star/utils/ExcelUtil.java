package star.utils;

import star.annotation.bean.ExcelColumn;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import static star.utils.ReflectionUtil.getFields;

public final class ExcelUtil {

    public static Map<String, Object> toMap(Object object) {
        return object == null
                ? Collections.emptyMap()
                : getFields(object.getClass()).stream().collect(Collectors.toMap(field -> field.isAnnotationPresent(ExcelColumn.class)
                ? field.getAnnotation(ExcelColumn.class).value() : field.getName(), field -> ReflectionUtil.getField(field, object)));
    }
}
