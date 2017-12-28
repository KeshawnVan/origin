package star.proxy;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import star.factory.ConnectionFactory;
import star.utils.ArrayUtil;
import star.utils.ClassUtil;
import star.utils.ReflectionUtil;

import java.lang.reflect.*;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static star.constant.RepositoryConstant.FIND_BY_ID;
import static star.constant.RepositoryConstant.FIND_BY_IDS;
import static star.repository.PreparedStatementLoader.setPreparedStatement;
import static star.repository.RepositoryManager.*;
import static star.repository.ResultSetParser.getResult;
import static star.repository.SqlGenerator.getFindByIdSql;
import static star.repository.SqlGenerator.getFindByIdsSQL;

/**
 * @author keshawn
 * @date 2017/12/25
 */
public class RepositoryProxy implements InvocationHandler {

    private static final int TYPE_LENGTH = 2;

    private static final Logger LOGGER = LoggerFactory.getLogger(RepositoryProxy.class);

    private static final ConcurrentHashMap<String, String> SQL_MAP = new ConcurrentHashMap<>();

    private Class<?> repositoryInterface;

    private Class<?> beanClass;

    private Class<?> idClass;

    private String tableName;

    private Type[] actualTypeArguments;

    private List<Field> fields;

    private Map<String, String> fieldMap;

    String selectAllColumns;

    public RepositoryProxy(Class<?> repositoryInterface) {
        this.repositoryInterface = repositoryInterface;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] params) throws Throwable {

        //一个Proxy对应一个接口，对一些常用数据做初始化
        init();

        Object result = null;

        Connection connection = ConnectionFactory.getConnection();

        String methodName = method.getName();
        Class<?> returnType = method.getReturnType();

        if (methodName.equals(FIND_BY_ID)) {
            String findByIdSQL = getFindByIdSql(SQL_MAP, methodName, tableName, selectAllColumns);
            return executeQuery(connection, findByIdSQL, params, fields, returnType);
        }
        if (methodName.equals(FIND_BY_IDS)) {
            String findByIdsSQL = getFindByIdsSQL(SQL_MAP, methodName, tableName, selectAllColumns, params);
            return executeQuery(connection, findByIdsSQL, params, fields, returnType);
        }
        return result;
    }

    private Object executeQuery(Connection connection, String sql, Object[] params, List<Field> fields, Class<?> returnType) throws SQLException {
        boolean isCollection = Collection.class.isAssignableFrom(returnType);
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        setPreparedStatement(preparedStatement, params);
        ResultSet resultSet = preparedStatement.executeQuery();
        return isCollection
                ? buildCollectionResult(fields, resultSet, beanClass, returnType)
                : buildResult(fields, resultSet, beanClass);
    }

    private Object buildCollectionResult(List<Field> fields, ResultSet resultSet, Class<?> originType, Class<?> returnType) throws SQLException {

        if (List.class.isAssignableFrom(returnType)) {
            return buildResults(fields, resultSet, originType, new ArrayList<>());
        }
        if (Set.class.isAssignableFrom(returnType)) {
            return buildResults(fields, resultSet, originType, new HashSet());
        }
        LOGGER.warn("buildCollectionResult cannot match suitable type : {}", returnType);
        return buildResults(fields, resultSet, originType, new ArrayList<>());
    }

    private Collection buildResults(List<Field> fields, ResultSet resultSet, Class<?> originType, Collection collection) throws SQLException {
        while (resultSet.next()) {
            Object result = ReflectionUtil.newInstance(originType);
            fieldInject(fields, result, resultSet);
            collection.add(result);
        }
        return collection;
    }

    private Object buildResult(List<Field> fields, ResultSet resultSet, Class<?> originType) throws SQLException {
        Object result = null;
        while (resultSet.next()) {
            result = ReflectionUtil.newInstance(originType);
            fieldInject(fields, result, resultSet);
        }
        return result;
    }

    private void fieldInject(List<Field> fields, Object result, ResultSet resultSet) throws SQLException {
        for (Field field : fields) {
            Object resultSetObject = getResult(resultSet, field);
            ReflectionUtil.setField(result, field, resultSetObject);
        }
    }

    public void init() {
        //如果actualTypeArguments不为null说明已经初始化过一次
        if (actualTypeArguments != null) {
            return;
        }

        actualTypeArguments = getActualTypeArguments();
        String beanClassName = actualTypeArguments[0].getTypeName();
        String idClassName = actualTypeArguments[1].getTypeName();
        beanClass = ClassUtil.loadClass(beanClassName, true);
        idClass = ClassUtil.loadClass(idClassName, true);
        tableName = getTableName(beanClass);
        fields = Lists.newArrayList(beanClass.getDeclaredFields());
        fieldMap = buildFieldMap(fields);
        selectAllColumns = buildSelectColumns(fieldMap);
    }


    private Type[] getActualTypeArguments() {
        Type[] genericInterfaces = repositoryInterface.getGenericInterfaces();
        actualTypeArguments = ((ParameterizedType) genericInterfaces[0]).getActualTypeArguments();
        if (actualTypeArguments.length != TYPE_LENGTH) {
            throw new RuntimeException("actualTypeArguments length is invalid : " + actualTypeArguments.length);
        }
        return actualTypeArguments;
    }

    public <T> T getProxy() {
        return (T) Proxy.newProxyInstance(repositoryInterface.getClassLoader(), ArrayUtil.concat(repositoryInterface.getInterfaces(), new Class<?>[]{repositoryInterface}), this);
    }
}
