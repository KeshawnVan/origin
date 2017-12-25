package star.proxy;

import org.apache.commons.lang3.ArrayUtils;
import star.factory.ConnectionFactory;
import star.utils.ArrayUtil;

import java.lang.reflect.*;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.util.Arrays;

/**
 * @author keshawn
 * @date 2017/12/25
 */
public class RepositoryProxy implements InvocationHandler {

    private static final String FIND_BY_ID = "findById";

    private static final String SELECT = "select";

    private Class<?> repositoryInterface;

    public RepositoryProxy(Class<?> repositoryInterface) {
        this.repositoryInterface = repositoryInterface;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        Connection connection = ConnectionFactory.getConnection();

        String methodName = method.getName();
        System.out.println(methodName);
        if (methodName.equals(FIND_BY_ID)){
            String sql;
        }
        return null;
    }

    public <T> T getProxy() {
        return (T) Proxy.newProxyInstance(repositoryInterface.getClassLoader(), ArrayUtil.concat(repositoryInterface.getInterfaces(), new Class<?>[]{repositoryInterface}), this);
    }
}
