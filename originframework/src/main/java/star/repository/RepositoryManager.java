package star.repository;

import star.annotation.Column;
import star.annotation.Id;
import star.annotation.Table;
import star.factory.ConfigFactory;
import star.utils.CollectionUtil;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
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

    /**
     * 通过domain类模板获取数据库中的表名
     *
     * @param beanClass
     * @return
     */
    public static String getTableName(Class<?> beanClass) {
        if (beanClass.isAnnotationPresent(Table.class)) {
            Table table = beanClass.getAnnotation(Table.class);
            return table.value();
        } else {
            return camelToUnderlineUpperCase(beanClass.getSimpleName());
        }
    }

    /**
     * 通过domain中的字段生成实体和数据库中名称的映射
     *
     * @param fields
     * @return
     */
    public static Map<String, String> buildFieldMap(List<Field> fields) {
        return fields.stream()
                .collect(Collectors.toMap(field -> field.getName(),
                        field -> field.isAnnotationPresent(Column.class)
                                ? field.getAnnotation(Column.class).value()
                                : AUTO_CAST ? camelToUnderlineUpperCase(field.getName()) : field.getName()));
    }

    /**
     * 通过传入的实体与数据库表名映射的map生成查询时使用的列名和别名拼接字符串
     *
     * @param fieldMap
     * @return
     */
    public static String buildSelectColumns(Map<String, String> fieldMap) {
        StringBuilder stringBuilder = new StringBuilder();
        return fieldMap.keySet().stream().map(fieldName -> {
            stringBuilder.setLength(CLEAR);
            String databaseName = fieldMap.get(fieldName);
            stringBuilder.append(databaseName).append(BLANK).append(fieldName);
            return stringBuilder.toString();
        }).collect(Collectors.joining(DELIMITER));
    }

    public static Field getId(String beanClassName, List<Field> fields) {
        List<Field> idList = fields.stream().filter(field -> field.isAnnotationPresent(Id.class)).collect(toList());
        if (CollectionUtil.isEmpty(idList)) {
            idList = fields.stream().filter(field -> field.getName().equals(ID)).collect(toList());
            if (CollectionUtil.isEmpty(idList)) {
                throw new RuntimeException("cannot find id in " + beanClassName);
            }
        }
        return idList.get(0);
    }
}
