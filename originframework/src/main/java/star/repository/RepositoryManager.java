package star.repository;

import star.annotation.Column;
import star.annotation.Table;
import star.factory.ConfigFactory;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static star.constant.RepositoryConstant.*;
import static star.utils.StringUtil.camelToUnderlineUpperCase;

/**
 * @author keshawn
 * @date 2017/12/28
 */
public final class RepositoryManager {

    private static final boolean AUTO_CAST = ConfigFactory.getAutoCast();

    private RepositoryManager() {
    }

    public static String getTableName(Class<?> beanClass) {
        if (beanClass.isAnnotationPresent(Table.class)) {
            Table table = beanClass.getAnnotation(Table.class);
            return table.value();
        } else {
            return camelToUnderlineUpperCase(beanClass.getSimpleName());
        }
    }

    public static Map<String, String> buildFieldMap(List<Field> fields) {
        return fields.stream()
                .collect(Collectors.toMap(field -> field.getName(),
                        field -> field.isAnnotationPresent(Column.class)
                                ? field.getAnnotation(Column.class).value()
                                : AUTO_CAST ? camelToUnderlineUpperCase(field.getName()) : field.getName()));
    }


    public static String buildSelectColumns(Map<String, String> fieldMap) {
        StringBuilder stringBuilder = new StringBuilder();
        return fieldMap.keySet().stream().map(fieldName -> {
            stringBuilder.setLength(CLEAR);
            String databaseName = fieldMap.get(fieldName);
            stringBuilder.append(databaseName).append(BLANK).append(fieldName);
            return stringBuilder.toString();
        }).collect(Collectors.joining(DELIMITER));
    }
}
