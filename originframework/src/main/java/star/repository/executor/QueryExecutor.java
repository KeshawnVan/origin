package star.repository.executor;

import star.factory.ConnectionFactory;
import star.repository.SqlExecutor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import static star.repository.PreparedStatementLoader.setPreparedStatement;
import static star.repository.ResultSetParser.buildCollectionResult;
import static star.repository.ResultSetParser.buildResult;

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
        //修改为按照返回值泛型处理
        Class<?> returnType = method.getReturnType();
        boolean isCollection = Collection.class.isAssignableFrom(returnType);
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        setPreparedStatement(preparedStatement, params);
        ResultSet resultSet = preparedStatement.executeQuery();
        return isCollection
                ? buildCollectionResult(fields, resultSet, beanClass, returnType)
                : buildResult(fields, resultSet, beanClass);
    }

}
