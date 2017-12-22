package star.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author keshawn
 * @date 2017/12/21
 */
public class DynamicProxy implements InvocationHandler {

    private Object target;

    private Supplier beforeSupplier;

    private Supplier afterSupplier;

    private Consumer beforeConsumer;

    private BiConsumer afterConsumer;

    public DynamicProxy(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object beforeResult = beforeSupplier == null ? target : beforeSupplier.get();
        if (beforeConsumer != null) {
            beforeConsumer.accept(beforeResult);
        }
        Object result = method.invoke(target, args);
        Object afterResult = afterSupplier == null ? result : afterSupplier.get();
        if (afterConsumer != null) {
            afterConsumer.accept(beforeResult, afterResult);
        }
        return result;
    }

    public <T> T getProxy() {
        return (T) Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), this);
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
