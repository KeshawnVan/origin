package star.proxy;

import com.google.common.collect.Lists;
import star.annotation.Column;
import star.annotation.Table;
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
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static star.constant.RepositoryConstant.*;
import static star.utils.StringUtil.camelToUnderlineUpperCase;

/**
 * @author keshawn
 * @date 2017/12/25
 */
public class RepositoryProxy implements InvocationHandler {

    private static final int INITIAL_CAPACITY = 2;

    private static final int TYPE_LENGTH = 2;

    private static final String BLANK = " ";

    private static final int CLEAR = 0;

    private static final ConcurrentHashMap<String, Class<?>> CLASS_MAP = new ConcurrentHashMap<>(INITIAL_CAPACITY);

    private Class<?> repositoryInterface;

    private Class<?> beanClass;

    private Class<?> idClass;

    private String tableName;

    private Type[] actualTypeArguments;

    private Field[] fields;

    private Map<String, String> fieldMap;

    private String FIND_BY_ID_SQL;

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
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL);
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

    private void setPreparedStatement(PreparedStatement preparedStatement, Object[] args) throws SQLException {
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            preparedStatement.setObject(i + 1, arg);
        }
    }

    private Object getResult(ResultSet resultSet, Field field) throws SQLException {
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

    private String getTableName(Class<?> beanClass) {
        if (beanClass.isAnnotationPresent(Table.class)) {
            Table table = beanClass.getAnnotation(Table.class);
            return table.value();
        } else {
            return camelToUnderlineUpperCase(beanClass.getSimpleName());
        }
    }

    private Class<?> loadClass(String className) {
        Class<?> beanClass;
        if (CLASS_MAP.containsKey(className)) {
            beanClass = CLASS_MAP.get(className);
        } else {
            beanClass = ClassUtil.loadClass(className, true);
            CLASS_MAP.put(className, beanClass);
        }
        return beanClass;
    }

    public void init() {
        //如果actualTypeArguments不为null说明已经初始化过一次
        if (actualTypeArguments != null) {
            return;
        }

        actualTypeArguments = getActualTypeArguments();
        String beanClassName = actualTypeArguments[0].getTypeName();
        String idClassName = actualTypeArguments[1].getTypeName();
        beanClass = loadClass(beanClassName);
        idClass = loadClass(idClassName);
        tableName = getTableName(beanClass);
        fields = beanClass.getDeclaredFields();
        fieldMap = buildFieldMap();
        String selectColumns = buildSelectColumns();
        FIND_BY_ID_SQL = SELECT + selectColumns + FROM + tableName + WHERE + ID + EQUALS + PLACEHOLDER;
        System.out.println(FIND_BY_ID_SQL);
    }

    private Map<String, String> buildFieldMap() {
        return Lists.newArrayList(fields).stream()
                .collect(Collectors.toMap(field -> field.getName(),
                        field -> field.isAnnotationPresent(Column.class)
                                ? field.getAnnotation(Column.class).value()
                                : camelToUnderlineUpperCase(field.getName())));
    }

    private String buildSelectColumns() {
        StringBuilder stringBuilder = new StringBuilder();
        return fieldMap.keySet().stream().map(fieldName -> {
            stringBuilder.setLength(CLEAR);
            String databaseName = fieldMap.get(fieldName);
            stringBuilder.append(databaseName).append(BLANK).append(fieldName);
            return stringBuilder.toString();
        }).collect(Collectors.joining(DELIMITER));
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
