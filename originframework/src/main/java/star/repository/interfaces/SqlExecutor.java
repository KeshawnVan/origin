package star.repository.interfaces;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.List;

/**
 * @author keshawn
 * @date 2017/12/29
 */
public interface SqlExecutor {
    /**
     * 执行SQL
     * @param sql
     * @param method
     * @param params
     * @param fields
     * @param beanClass
     * @return
     * @throws SQLException
     */
    Object execute(String sql, Method method, Object[] params, List<Field> fields, Class<?> beanClass, Field idField) throws SQLException;
}
