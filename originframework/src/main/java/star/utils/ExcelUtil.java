package star.utils;

import star.annotation.bean.ExcelColumn;

import java.util.Map;
import java.util.stream.Collectors;

public final class ExcelUtil {

    public static Map<Integer, String> buildColumnInfo(Class<?> clazz) {
        return ClassUtil.getClassInfo(clazz).getFields()
                .stream()
                .filter(field -> field.isAnnotationPresent(ExcelColumn.class))
                .map(field -> field.getAnnotation(ExcelColumn.class))
                .collect(Collectors.toMap(ExcelColumn::index, ExcelColumn::value));
    }
}
