package star.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import star.annotation.aop.Aspect;
import star.annotation.bean.Service;
import star.annotation.repository.Transaction;
import star.repository.TransactionManager;

import java.lang.reflect.Method;

/**
 * @author keshawn
 * @date 2018/1/15
 */
@Aspect(Service.class)
public class TransactionProxy extends AspectProxy {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionProxy.class);

    private static final ThreadLocal<Boolean> FLAG_HOLDER = ThreadLocal.withInitial(() -> Boolean.FALSE);

    @Override
    public Object around(ProxyChain proxyChain) throws Throwable {
        Boolean flag = FLAG_HOLDER.get();
        Method method = proxyChain.getTargetMethod();
        Class<?> targetClass = proxyChain.getTargetClass();
        return check(flag, method, targetClass) ? transactionHandler(proxyChain, method, targetClass) : proxyChain.doProxyChain();
    }

    private Object transactionHandler(ProxyChain proxyChain, Method method, Class<?> targetClass) throws Throwable {
        Transaction transaction = method.isAnnotationPresent(Transaction.class) ? method.getAnnotation(Transaction.class) : targetClass.getAnnotation(Transaction.class);
        return transaction.readOnly() ? readOnlyHandler(proxyChain) : commonTransactionHandler(proxyChain);
    }

    private Object commonTransactionHandler(ProxyChain proxyChain) throws Throwable {
        FLAG_HOLDER.set(true);
        Object result;
        try {
            TransactionManager.begin();
            LOGGER.info("begin transaction");
            result = proxyChain.doProxyChain();
            TransactionManager.commit();
            LOGGER.info("commit transaction");
        } catch (Exception e) {
            TransactionManager.rollback();
            LOGGER.error("rollback transaction");
            throw e;
        } finally {
            FLAG_HOLDER.remove();
        }
        return result;
    }

    private Object readOnlyHandler(ProxyChain proxyChain) throws Throwable {
        TransactionManager.readOnly();
        LOGGER.info("set read only");
        return proxyChain.doProxyChain();
    }

    private boolean check(Boolean flag, Method method, Class<?> targetClass) {
        return !flag && (method.isAnnotationPresent(Transaction.class) || targetClass.isAnnotationPresent(Transaction.class));
    }
}
