import com.google.common.reflect.Reflection;
import org.junit.Test;
import star.bean.User;
import star.dao.UserRepository;
import star.factory.ConfigFactory;
import star.factory.ConnectionFactory;
import star.proxy.CGLibProxy;
import star.proxy.DynamicProxy;
import star.proxy.RepositoryProxy;
import star.service.TestService;
import star.service.impl.TestServiceImpl;
import star.service.impl.TestServiceImpl2;
import star.utils.JsonUtil;
import star.utils.ReflectionUtil;
import star.utils.StringUtil;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;

/**
 * @author keshawn
 * @date 2017/12/21
 */
public class Test1 {
    @Test
    public void testProxy() {
        TestService testService = new TestServiceImpl();
        DynamicProxy proxy = new DynamicProxy(testService);
        proxy.setBeforeSupplier(() -> System.currentTimeMillis());
        proxy.setBeforeConsumer(it -> System.out.println(it));
        proxy.setAfterSupplier(() -> System.currentTimeMillis());
        proxy.setAfterConsumer((before, after) -> System.out.println((long) after - (long) before));
        TestService service = proxy.getProxy();
        try {
            Method method = TestService.class.getMethod("hello");
            ReflectionUtil.invokeMethod(service, method);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCGlib() {
        CGLibProxy proxy = CGLibProxy.getInstance();
        proxy.setBeforeSupplier(() -> System.currentTimeMillis());
        proxy.setBeforeConsumer(it -> System.out.println(it));
        proxy.setAfterSupplier(() -> System.currentTimeMillis());
        proxy.setAfterConsumer((before, after) -> System.out.println((long) after - (long) before));
        TestServiceImpl service = proxy.getProxy(TestServiceImpl.class);
        service.hello();
        TestServiceImpl2 testServiceImpl2 = CGLibProxy.getInstance().getProxy(TestServiceImpl2.class);
        testServiceImpl2.hello();
    }

    @Test
    public void testGuavaProxy() {
        TestService testService = new TestServiceImpl();
        TestService service = Reflection.newProxy(TestService.class, new DynamicProxy(testService));
        service.hello();
    }

    @Test
    public void testInterfaceProxy() {
        DynamicProxy proxy = new DynamicProxy(TestService.class);
        proxy.setBeforeSupplier(() -> System.currentTimeMillis());
        proxy.setBeforeConsumer(it -> System.out.println(it));
        proxy.setAfterSupplier(() -> System.currentTimeMillis());
        TestService testService = (TestService) java.lang.reflect.Proxy.newProxyInstance(TestService.class.getClassLoader(), new Class[]{TestService.class}, proxy);
        testService.hello();
    }

    @Test
    public void testJDBC() throws Exception {
        Connection connection = ConnectionFactory.getConnection();
        String sql = "insert into user values(?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setLong(1, 6L);
        preparedStatement.setString(2, "a");
        preparedStatement.setInt(3, 22);
        boolean execute = preparedStatement.execute();
        System.out.println(execute);
        preparedStatement.close();
        ConnectionFactory.closeConnection();
    }

    @Test
    public void testSelect() throws Exception {
        Connection connection = ConnectionFactory.getConnection();
        String sql = "select * from user where id = 5";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        boolean execute = preparedStatement.execute();
        ResultSet resultSet = preparedStatement.getResultSet();
        resultSet.next();
        String string = resultSet.getString(2);
        System.out.println(string);
        System.out.println(resultSet);
    }

    @Test
    public void testJson() {
        User user = User.newBuilder().name("fkx").age(22).build();
        String s = JsonUtil.encodeJson(user);
        Map map = JsonUtil.decodeJson(s, Map.class);
        map.put("test", "test");
        String ss = JsonUtil.encodeJson(map);
        System.out.println(ss);
        User user1 = JsonUtil.decodeJson(ss, User.class);
        System.out.println(JsonUtil.encodeJson(user1));
    }

    @Test
    public void testCast() {
        String ss = "user_id";
        System.out.println(StringUtil.underLineToCamel(ss));
        String s = StringUtil.underLineToCamel(ss);
        System.out.println(StringUtil.camelToUnderlineLowerCase(s));
    }

    @Test
    public void testInterfacesProxy(){
        RepositoryProxy proxy = new RepositoryProxy(UserRepository.class);
        UserRepository userRepository = proxy.getProxy();
        userRepository.findById(1L);
    }

    @Test
    public void testDefault(){
        String autoCast = ConfigFactory.getAutoCast();
        System.out.println(autoCast);
    }
}
