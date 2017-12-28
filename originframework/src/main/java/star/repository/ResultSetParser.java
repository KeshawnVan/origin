package star.repository;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * @author keshawn
 * @date 2017/12/28
 */
public final class ResultSetParser {

    public static Object getResult(ResultSet resultSet, Field field) throws SQLException {
        Class<?> fieldType = field.getType();
        String fieldName = field.getName();
        if (fieldType == String.class) {
            return resultSet.getString(fieldName);
        }
        if (fieldType == Integer.class || fieldType == int.class) {
            return resultSet.getInt(fieldName);
        }
        if (fieldType == Long.class || fieldType == long.class) {
            return resultSet.getLong(fieldName);
        }
        if (fieldType == Boolean.class || fieldType == boolean.class) {
            return resultSet.getBoolean(fieldName);
        }
        if (fieldType == Double.class || fieldType == double.class) {
            return resultSet.getDouble(fieldName);
        }
        if (fieldType == Float.class || fieldType == float.class) {
            return resultSet.getFloat(fieldName);
        }
        if (fieldType == Byte.class || fieldType == byte.class) {
            return resultSet.getByte(fieldName);
        }
        if (fieldType == Short.class || fieldType == short.class) {
            return resultSet.getShort(fieldName);
        }
        if (fieldType == Date.class) {
            return "";
        }

        return resultSet.getObject(fieldName);
    }
}
