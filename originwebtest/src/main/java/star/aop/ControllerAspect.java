package star.aop;

import star.annotation.aop.Aspect;
import star.annotation.bean.Controller;
import star.proxy.AspectProxy;
import star.proxy.ProxyChain;

import java.lang.reflect.Method;

/**
 * @author keshawn
 * @date 2017/12/22
 */
@Aspect(Controller.class)
public class ControllerAspect extends AspectProxy {
    @Override
    public void begin() {
        System.out.println("begin");
    }

    @Override
    public boolean intercept(Class<?> cls, Method method, Object[] params) throws Throwable {
        return super.intercept(cls, method, params);
    }

    @Override
    public void before(Class<?> cls, Method method, Object[] params) throws Throwable {
        System.out.println("before");
    }

    @Override
    public Object around(ProxyChain proxyChain) throws Throwable {
        System.out.println("around start");
        Object chain = proxyChain.doProxyChain();
        System.out.println("around end");
        return chain;
    }

    @Override
    public void after(Class<?> cls, Method method, Object[] params) throws Throwable {
        System.out.println("after");
    }

    @Override
    public void error(Class<?> cls, Method method, Object[] params) throws Throwable {
        System.out.println("error");
    }

    @Override
    public void end() {
        System.out.println("end");
    }
}
