package star.proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author keshawn
 * @date 2017/12/21
 */
public class CGLibProxy implements MethodInterceptor {

    private static CGLibProxy instance = new CGLibProxy();

    private Supplier beforeSupplier;

    private Supplier afterSupplier;

    private Consumer beforeConsumer;

    private BiConsumer afterConsumer;

    private CGLibProxy() {
    }

    @Override
    public Object intercept(Object object, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        Object beforeResult = beforeSupplier == null ? object : beforeSupplier.get();
        if (beforeConsumer != null) {
            beforeConsumer.accept(beforeResult);
        }
        Object result = methodProxy.invokeSuper(object, args);
        Object afterResult = afterSupplier == null ? result : afterSupplier.get();
        if (afterConsumer != null) {
            afterConsumer.accept(beforeResult, afterResult);
        }
        return result;
    }

    public <T> T getProxy(Class<T> cls) {
        return (T) Enhancer.create(cls, this);
    }

    public static CGLibProxy getInstance() {
        return instance;
    }

    public Supplier getBeforeSupplier() {
        return beforeSupplier;
    }

    public void setBeforeSupplier(Supplier beforeSupplier) {
        this.beforeSupplier = beforeSupplier;
    }

    public Supplier getAfterSupplier() {
        return afterSupplier;
    }

    public void setAfterSupplier(Supplier afterSupplier) {
        this.afterSupplier = afterSupplier;
    }

    public Consumer getBeforeConsumer() {
        return beforeConsumer;
    }

    public void setBeforeConsumer(Consumer beforeConsumer) {
        this.beforeConsumer = beforeConsumer;
    }

    public BiConsumer getAfterConsumer() {
        return afterConsumer;
    }

    public void setAfterConsumer(BiConsumer afterConsumer) {
        this.afterConsumer = afterConsumer;
    }
}
