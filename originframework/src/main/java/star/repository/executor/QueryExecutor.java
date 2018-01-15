package star.repository.executor;

import star.bean.TypeWrapper;
import star.factory.ConnectionFactory;
import star.repository.SqlExecutor;
import star.utils.ReflectionUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    private TypeWrapper beanClassWrapper;

    private QueryExecutor() {
    }

    public static QueryExecutor getInstance() {
        return instance;
    }

    @Override
    public Object execute(String sql, Method method, Object[] params, List<Field> fields, Class<?> beanClass, Field idField) throws SQLException {
        Connection connection = ConnectionFactory.getConnection();
        //修改为按照返回值泛型处理
        Type genericReturnType = method.getGenericReturnType();
        TypeWrapper typeWrapper = genericReturnType instanceof TypeVariable
                ? buildTypeVariableWrapper(beanClass)
                : ReflectionUtil.typeParse(genericReturnType);
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        setPreparedStatement(preparedStatement, params);
        ResultSet resultSet = preparedStatement.executeQuery();
        return typeWrapper.isCollection()
                ? buildCollectionResult(fields, resultSet, typeWrapper.getGenericType(), typeWrapper.getCls())
                : buildResult(fields, resultSet, typeWrapper.getCls());
    }

    private TypeWrapper buildTypeVariableWrapper(Class<?> beanClass) {
        if (beanClassWrapper == null){
            beanClassWrapper = new TypeWrapper(beanClass,null,false);
        }
        return beanClassWrapper;
    }

}
