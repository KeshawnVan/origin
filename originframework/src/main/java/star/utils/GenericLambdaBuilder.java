package star.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.BiConsumer;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

/**
 * @author WangB
 * Date: 2017/12/1
 */
public class GenericLambdaBuilder<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GenericLambdaBuilder.class);

    private T instance;
    private boolean ifCond = true;

    public GenericLambdaBuilder(Class<T> clazz) {
        try {
            instance = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public GenericLambdaBuilder(T instance) {
        this.instance = instance;
    }

    public static <T> GenericLambdaBuilder<T> build(Class<T> clazz) {
        return new GenericLambdaBuilder<>(clazz);
    }

    public static <T> GenericLambdaBuilder<T> of(T instance) {
        return new GenericLambdaBuilder<>(instance);
    }

    public GenericLambdaBuilder<T> with(Consumer<T> setter) {
        if (ifCond) {
            setter.accept(instance);
        }
        return this;
    }

    public <U> GenericLambdaBuilder<T> with(BiConsumer<T, U> consumer, U value) {
        if (ifCond) {
            consumer.accept(instance, value);
        }
        return this;
    }

    public GenericLambdaBuilder<T> startIf(BooleanSupplier condition) {
        this.ifCond = condition.getAsBoolean();
        return this;
    }

    public GenericLambdaBuilder<T> endIf() {
        this.ifCond = true;
        return this;
    }

    public T get() {
        Object newInstance = null;
        try {
            Class<?> clazz = instance.getClass();
            Field[] fields = clazz.getDeclaredFields();
            int length = fields.length;
            Class<?>[] args = new Class[length];
            for (int i = 0; i < length; i++) {
                args[i]  = fields[i].getType();
            }
            Constructor<?> constructor = clazz.getConstructor(args);
            Object[] argsValue = new Object[length];
            for (int i = 0; i <length; i++) {
                String getMethodName = StringUtil.getGetMethodName(fields[i].getName());
                Method method = clazz.getMethod(getMethodName);
                argsValue[i] = method.invoke(instance);
            }
            newInstance = constructor.newInstance(argsValue);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            LOGGER.error("GenericLambdaBuilder get {} error {}",instance.getClass().getName(),e);
            e.printStackTrace();
        }
        return (T) newInstance;
    }
}