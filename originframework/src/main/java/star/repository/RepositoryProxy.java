package star.repository;

import com.google.common.collect.Lists;
import star.repository.factory.SqlExecutorFactory;
import star.repository.factory.SqlGeneratorFactory;
import star.repository.interfaces.SqlExecutor;
import star.repository.interfaces.SqlGenerator;
import star.utils.ArrayUtil;
import star.utils.ClassUtil;
import star.utils.ReflectionUtil;

import java.lang.reflect.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static star.repository.RepositoryManager.*;

/**
 * @author keshawn
 * @date 2017/12/25
 */
public class RepositoryProxy implements InvocationHandler {

    private static final int TYPE_LENGTH = 2;

    private static final ConcurrentHashMap<String, String> SQL_MAP = new ConcurrentHashMap<>();

    private Class<?> repositoryInterface;

    private Class<?> beanClass;

    private Field idField;

    private String tableName;

    private Type[] actualTypeArguments;

    private List<Field> fields;

    private Map<String, String> fieldMap;

    String selectAllColumns;

    public RepositoryProxy(Class<?> repositoryInterface) {
        this.repositoryInterface = repositoryInterface;
        init();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] params) throws Throwable {
        String methodName = method.getName();
        String argsMethodName = RepositoryManager.generateArgsMethodName(methodName, params);
        String sql = buildSql(method, params, argsMethodName);
        SqlExecutor sqlExecutor = SqlExecutorFactory.getExecutor(method);
        return sqlExecutor.execute(sql, method, params, fields, beanClass, idField);
    }

    private String buildSql(Method method, Object[] params, String argsMethodName) {
        if (SQL_MAP.containsKey(argsMethodName)) {
            return SQL_MAP.get(argsMethodName);
        } else {
            SqlGenerator sqlGenerator = SqlGeneratorFactory.getGenerator(method);
            String sql = sqlGenerator.generate(method, tableName, selectAllColumns, params, fieldMap);
            SQL_MAP.put(argsMethodName, sql);
            return sql;
        }
    }

    public void init() {
        //如果actualTypeArguments不为null说明已经初始化过一次
        if (actualTypeArguments != null) {
            return;
        }
        actualTypeArguments = getActualTypeArguments(repositoryInterface);
        String beanClassName = actualTypeArguments[0].getTypeName();
        beanClass = ClassUtil.loadClass(beanClassName, true);
        tableName = getTableName(beanClass);
        fields = Lists.newArrayList(beanClass.getDeclaredFields());
        fieldMap = buildFieldMap(fields);
        selectAllColumns = buildSelectColumns(fieldMap);
        idField = getId(beanClassName, fields);
    }

    /**
     * 获取接口上定义的泛型，第一位为domain的类型，第二位为主键的类型
     *
     * @return
     */
    private Type[] getActualTypeArguments(Class<?> cls) {
        Type[] genericInterfaces = cls.getGenericInterfaces();
        Type[] actualTypeArguments = ReflectionUtil.getActualTypeArguments(genericInterfaces[0]);
        if (actualTypeArguments.length != TYPE_LENGTH) {
            throw new RuntimeException("actualTypeArguments length is invalid : " + actualTypeArguments.length);
        }
        return actualTypeArguments;
    }

    public <T> T getProxy() {
        return (T) Proxy.newProxyInstance(repositoryInterface.getClassLoader(), ArrayUtil.concat(repositoryInterface.getInterfaces(), new Class<?>[]{repositoryInterface}), this);
    }
}
