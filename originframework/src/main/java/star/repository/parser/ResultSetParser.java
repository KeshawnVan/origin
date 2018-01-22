package star.repository.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import star.repository.interfaces.ResultSetGetFunction;
import star.utils.ReflectionUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static star.utils.DateUtil.*;

/**
 * @author keshawn
 * @date 2017/12/28
 */
public final class ResultSetParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResultSetParser.class);

    /**
     * 某些数据类型在resultSet.getXXX()，即使数据库中为null，取出来也会被初始化
     * 如：数据库中为null，getInt()拿到的为0
     * 解决方案：使用resultSet.wasNull()判断最后一次取出的值是否为null
     *
     * @param fieldName
     * @param resultSet
     * @param getResult
     * @param <R>
     * @return
     * @throws SQLException
     */

    private static <R> R checkAndGetResult(String fieldName, ResultSet resultSet, ResultSetGetFunction<String, R> getResult) throws SQLException {
        R result = getResult.getObject(fieldName);
        boolean wasNull = false;
        try {
            wasNull = resultSet.wasNull();
        } catch (SQLException e) {
            LOGGER.error("checkAndGetResult check result is null error", e);
        }
        return wasNull ? null : result;
    }

    /**
     * 方法返回值为集合时，通过ResultSet构建返回值
     * 目前只处理List和Set两种情况
     *
     * @param fields
     * @param resultSet
     * @param genericType
     * @param returnType
     * @return
     * @throws SQLException
     */
    public static Object buildCollectionResult(List<Field> fields, ResultSet resultSet, Type[] genericType, Class<?> returnType) throws SQLException {

        Class<?> originType = parseGenericType(genericType);

        if (List.class.isAssignableFrom(returnType)) {
            return buildResults(fields, resultSet, originType, new ArrayList<>());
        }

        if (Set.class.isAssignableFrom(returnType)) {
            return buildResults(fields, resultSet, originType, new HashSet());
        }

        LOGGER.warn("buildCollectionResult cannot match suitable type : {}", returnType);
        return buildResults(fields, resultSet, originType, new ArrayList<>());
    }

    private static Class<?> parseGenericType(Type[] genericType) {
        //目前只处理List和Set，genericType应为一位
        if (genericType != null && genericType.length == 1) {
            return (Class<?>) genericType[0];
        } else {
            throw new RuntimeException("genericType invalid :" + genericType);
        }
    }

    /**
     * 通过ResultSet构建Collection类型的对象
     *
     * @param fields
     * @param resultSet
     * @param originType
     * @param collection
     * @return
     * @throws SQLException
     */
    public static Collection buildResults(List<Field> fields, ResultSet resultSet, Class<?> originType, Collection collection) throws SQLException {
        while (resultSet.next()) {
            Object result = ReflectionUtil.newInstance(originType);
            fieldInject(fields, result, resultSet);
            collection.add(result);
        }
        return collection;
    }

    /**
     * 通过ResultSet构建Bean对象
     *
     * @param fields
     * @param resultSet
     * @param originType
     * @return
     * @throws SQLException
     */
    public static Object buildResult(List<Field> fields, ResultSet resultSet, Class<?> originType) throws SQLException {
        Object result = null;
        while (resultSet.next()) {
            result = ReflectionUtil.newInstance(originType);
            fieldInject(fields, result, resultSet);
        }
        return result;
    }

    /**
     * 从ResultSet中取值并反射设值给bean
     *
     * @param fields
     * @param result
     * @param resultSet
     * @throws SQLException
     */
    public static void fieldInject(List<Field> fields, Object result, ResultSet resultSet) throws SQLException {
        for (Field field : fields) {
            Object resultSetObject = getResult(resultSet, field);
            ReflectionUtil.setField(result, field, resultSetObject);
        }
    }

    /**
     * 从ResultSet中取单列的值，做类型匹配
     *
     * @param resultSet
     * @param field
     * @return
     * @throws SQLException
     */
    public static Object getResult(ResultSet resultSet, Field field) throws SQLException {
        Class<?> fieldType = field.getType();
        String fieldName = field.getName();
        if (fieldType == String.class) {
            return resultSet.getString(fieldName);
        }
        if (fieldType.isPrimitive()) {
            return primitiveTypeHandler(fieldType, fieldName, resultSet);
        }
        if (fieldType == Integer.class) {
            return checkAndGetResult(fieldName, resultSet, resultSet::getInt);
        }
        if (fieldType == Long.class) {
            return checkAndGetResult(fieldName, resultSet, resultSet::getLong);
        }
        if (fieldType == Boolean.class) {
            return checkAndGetResult(fieldName, resultSet, resultSet::getBoolean);
        }
        if (fieldType == Double.class) {
            return checkAndGetResult(fieldName, resultSet, resultSet::getDouble);
        }
        if (fieldType == Float.class) {
            return checkAndGetResult(fieldName, resultSet, resultSet::getFloat);
        }
        if (fieldType == Byte.class) {
            return checkAndGetResult(fieldName, resultSet, resultSet::getByte);
        }
        if (fieldType == Short.class) {
            return checkAndGetResult(fieldName, resultSet, resultSet::getShort);
        }
        if (fieldType.isEnum()) {
            Integer num = checkAndGetResult(fieldName, resultSet, resultSet::getInt);
            return num == null ? null : fieldType.getEnumConstants()[num];
        }
        if (fieldType == Date.class) {
            return toUtilDate(resultSet.getTimestamp(fieldName));
        }
        if (fieldType == LocalDate.class) {
            return checkAndGetResult(fieldName, resultSet, resultSet::getDate);
        }
        if (fieldType == LocalDateTime.class) {
            return toLocalDateTime(resultSet.getTimestamp(fieldName));
        }
        if (fieldType == Instant.class) {
            return toInstant(resultSet.getTimestamp(fieldName));
        }
        LOGGER.warn("ResultSetParser didn't match suitable type : {}", fieldType);
        return resultSet.getObject(fieldName);
    }

    /**
     * 从ResultSet中取单列的值，对基本类型做类型匹配
     *
     * @param fieldType
     * @param fieldName
     * @param resultSet
     * @return
     * @throws SQLException
     */
    private static Object primitiveTypeHandler(Class<?> fieldType, String fieldName, ResultSet resultSet) throws SQLException {
        if (fieldType == int.class) {
            return checkAndGetResult(fieldName, resultSet, resultSet::getInt);
        }
        if (fieldType == long.class) {
            return checkAndGetResult(fieldName, resultSet, resultSet::getLong);
        }
        if (fieldType == boolean.class) {
            return checkAndGetResult(fieldName, resultSet, resultSet::getBoolean);
        }
        if (fieldType == double.class) {
            return checkAndGetResult(fieldName, resultSet, resultSet::getDouble);
        }
        if (fieldType == float.class) {
            return checkAndGetResult(fieldName, resultSet, resultSet::getFloat);
        }
        if (fieldType == byte.class) {
            return checkAndGetResult(fieldName, resultSet, resultSet::getByte);
        }
        if (fieldType == short.class) {
            return checkAndGetResult(fieldName, resultSet, resultSet::getShort);
        }
        LOGGER.warn("ResultSetParser primitiveTypeHandler didn't match suitable type : {}", fieldType);
        return checkAndGetResult(fieldName, resultSet, resultSet::getObject);
    }
}
