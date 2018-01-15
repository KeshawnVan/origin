package star.repository.executor;

import star.repository.factory.ConnectionFactory;
import star.repository.SqlExecutor;
import star.utils.ReflectionUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import static star.repository.PreparedStatementLoader.setPreparedStatement;

/**
 * @author keshawn
 * @date 2017/12/29
 */
public final class SaveExecutor implements SqlExecutor {

    private static final SaveExecutor instance = new SaveExecutor();

    private SaveExecutor() {
    }

    public static SaveExecutor getInstance() {
        return instance;
    }

    @Override
    public Object execute(String sql, Method method, Object[] params, List<Field> fields, Class<?> beanClass, Field idField) throws SQLException {
        Object param = params[0];
        Class<?> paramClass = param.getClass();
        if (!beanClass.equals(paramClass)) {
            throw new RuntimeException("param type invalid");
        }
        Connection connection = ConnectionFactory.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        List<Object> paramList = fields.stream().map(field -> ReflectionUtil.getField(field, param)).collect(Collectors.toList());
        setPreparedStatement(preparedStatement, paramList.toArray());
        return preparedStatement.executeUpdate();
    }
}
