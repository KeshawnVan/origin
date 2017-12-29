package star.repository.executor;

import star.repository.SqlExecutor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.List;

/**
 * @author keshawn
 * @date 2017/12/29
 */
public class CustomExecutor implements SqlExecutor {

    private static final CustomExecutor instance = new CustomExecutor();

    private CustomExecutor() {
    }

    public static CustomExecutor getInstance() {
        return instance;
    }

    @Override
    public Object execute(String sql, Method method, Object[] params, List<Field> fields, Class<?> beanClass, Field idField) throws SQLException {
        return null;
    }
}
