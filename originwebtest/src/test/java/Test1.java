import com.google.common.reflect.Reflection;
import org.junit.Test;
import star.proxy.CGLibProxy;
import star.proxy.DynamicProxy;
import star.service.TestService;
import star.service.impl.TestServiceImpl;
import star.service.impl.TestServiceImpl2;
import star.utils.ReflectionUtil;

import java.lang.reflect.Method;

/**
 * @author keshawn
 * @date 2017/12/21
 */
public class Test1 {
    @Test
    public void testProxy(){
        TestService testService = new TestServiceImpl();
        DynamicProxy proxy = new DynamicProxy(testService);
        proxy.setBeforeSupplier(()->System.currentTimeMillis());
        proxy.setBeforeConsumer(it -> System.out.println(it));
        proxy.setAfterSupplier(()->System.currentTimeMillis());
        proxy.setAfterConsumer((before,after)-> System.out.println((long)after-(long)before));
        TestService service = proxy.getProxy();
        try {
            Method method = TestService.class.getMethod("hello");
            ReflectionUtil.invokeMethod(service,method);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testCGlib(){
        CGLibProxy proxy = CGLibProxy.getInstance();
        proxy.setBeforeSupplier(()->System.currentTimeMillis());
        proxy.setBeforeConsumer(it -> System.out.println(it));
        proxy.setAfterSupplier(()->System.currentTimeMillis());
        proxy.setAfterConsumer((before,after)-> System.out.println((long)after-(long)before));
        TestServiceImpl service = proxy.getProxy(TestServiceImpl.class);
        service.hello();
        TestServiceImpl2 testServiceImpl2 = CGLibProxy.getInstance().getProxy(TestServiceImpl2.class);
        testServiceImpl2.hello();
    }
    @Test
    public void testGuavaProxy(){
        TestService testService = new TestServiceImpl();
        TestService service = Reflection.newProxy(TestService.class, new DynamicProxy(testService));
        service.hello();
    }
}
