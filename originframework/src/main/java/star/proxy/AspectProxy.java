package star.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import star.annotation.Pure;

import java.lang.reflect.Method;

/**
 * @author keshawn
 * @date 2017/12/22
 */
public class AspectProxy implements Proxy {

    private static final Logger LOGGER = LoggerFactory.getLogger(AspectProxy.class);

    @Override
    public final Object doProxy(ProxyChain proxyChain) throws Throwable {
        Object result = null;

        Class<?> cls = proxyChain.getTargetClass();
        Method method = proxyChain.getTargetMethod();
        Object[] params = proxyChain.getMethodParams();

        if (method.isAnnotationPresent(Pure.class)){
            return proxyChain.doProxyChain();
        }

        begin();
        try {
            if (intercept(cls, method, params)){
                before(cls, method, params);
                result = around(proxyChain);
                after(cls, method, params);
            }else {
                result = proxyChain.doProxyChain();
            }
        }catch (Exception e){
            LOGGER.error("proxy failure", e);
            error(cls, method, params);
            throw e;
        }finally {
            end();
        }

        return result;
    }

    public void begin(){
    }

    public boolean intercept(Class<?> cls, Method method, Object[] params)throws Throwable{
        return true;
    }

    public void before(Class<?> cls, Method method, Object[] params) throws Throwable{
    }

    public Object around(ProxyChain proxyChain) throws Throwable{
        return proxyChain.doProxyChain();
    }

    public void after(Class<?> cls, Method method, Object[] params) throws Throwable{
    }

    public void error(Class<?> cls, Method method, Object[] params) throws Throwable{
    }

    public void end(){
    }
}
