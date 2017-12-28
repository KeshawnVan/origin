package star.proxy;

import star.factory.ConnectionFactory;
import star.utils.ArrayUtil;
import star.utils.ClassUtil;
import star.utils.ReflectionUtil;

import java.lang.reflect.*;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;

import static star.constant.RepositoryConstant.*;
import static star.repository.PreparedStatementLoader.setPreparedStatement;
import static star.repository.RepositoryManager.*;
import static star.repository.ResultSetParser.getResult;

/**
 * @author keshawn
 * @date 2017/12/25
 */
public class RepositoryProxy implements InvocationHandler {

    private static final int TYPE_LENGTH = 2;

    private Class<?> repositoryInterface;

    private Class<?> beanClass;

    private Class<?> idClass;

    private String tableName;

    private Type[] actualTypeArguments;

    private Field[] fields;

    private Map<String, String> fieldMap;

    private String findByIdSQL;

    public RepositoryProxy(Class<?> repositoryInterface) {
        this.repositoryInterface = repositoryInterface;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        //一个Proxy对应一个接口，对一些常用数据做初始化
        init();

        Object result = null;

        Connection connection = ConnectionFactory.getConnection();

        String methodName = method.getName();

        if (methodName.equals(FIND_BY_ID)) {
            result = ReflectionUtil.newInstance(beanClass);
            PreparedStatement preparedStatement = connection.prepareStatement(findByIdSQL);
            setPreparedStatement(preparedStatement, args);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                for (Field field : fields) {
                    Object resultSetObject = getResult(resultSet, field);
                    ReflectionUtil.setField(result, field, resultSetObject);
                }
            }
        }
        return result;
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
        fields = beanClass.getDeclaredFields();
        fieldMap = buildFieldMap(fields);
        String selectColumns = buildSelectColumns(fieldMap);
        findByIdSQL = SELECT + selectColumns + FROM + tableName + WHERE + ID + EQUALS + PLACEHOLDER;
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
