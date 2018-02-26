package star.repository.executor;

import star.repository.factory.ConnectionFactory;
import star.repository.interfaces.SqlExecutor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static star.repository.PreparedStatementLoader.setPreparedStatement;
import static star.repository.parser.ResultSetParser.parseResultSet;

/**
 * @author keshawn
 * @date 2017/12/29
 */
public class QueryExecutor implements SqlExecutor {

    private static final QueryExecutor instance = new QueryExecutor();

    private QueryExecutor() {
    }

    public static QueryExecutor getInstance() {
        return instance;
    }

    @Override
    public Object execute(String sql, Method method, Object[] params, List<Field> fields, Class<?> beanClass, Field idField) throws SQLException {
        Connection connection = ConnectionFactory.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        setPreparedStatement(preparedStatement, params);
        ResultSet resultSet = preparedStatement.executeQuery();
        return parseResultSet(method, fields, beanClass, resultSet);
    }
}
