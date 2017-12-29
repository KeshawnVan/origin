package star.core;

import star.factory.BeanFactory;
import star.factory.ClassFactory;
import star.factory.ControllerFactory;
import star.factory.ProxyFactory;
import star.utils.ClassUtil;

/**
 * @author keshawn
 * @date 2017/11/17
 */
public final class LoadCore {

    public static void init() {
        Class<?>[] classes = {
                ClassFactory.class,
                BeanFactory.class,
                ProxyFactory.class,
                IocCore.class,
                ControllerFactory.class
        };
        for (Class<?> cls : classes) {
            ClassUtil.loadClass(cls.getName(),true);
        }
    }
}
