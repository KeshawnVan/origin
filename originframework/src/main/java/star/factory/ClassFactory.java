package star.factory;

import star.annotation.Controller;
import star.annotation.Service;
import star.utils.ClassUtil;

import java.util.HashSet;
import java.util.Set;

/**
 * @author keshawn
 * @date 2017/11/9
 */
public final class ClassFactory {

    private static final Set<Class<?>> CLASS_SET;

    static {
        String basePackage = ConfigFactory.getAppBasePackage();
        CLASS_SET = ClassUtil.getClassSet(basePackage);
    }

    public static Set<Class<?>> getClassSet() {
        return CLASS_SET;
    }

    public static Set<Class<?>> getServiceClassSet() {
        return getAnnotationClassSet(Service.class);
    }

    public static Set<Class<?>> getControllerClassSet() {
        return getAnnotationClassSet(Controller.class);
    }

    public static Set<Class<?>> getBeanClassSet() {
        Set<Class<?>> classSet = new HashSet<>();
        CLASS_SET.forEach(cls -> {
            if (cls.isAnnotationPresent(Controller.class) || cls.isAnnotationPresent(Service.class)) {
                classSet.add(cls);
            }
        });
        return classSet;
    }

    public static Set<Class<?>> getAnnotationClassSet(Class annotation) {
        Set<Class<?>> classSet = new HashSet<>();
        CLASS_SET.forEach(cls -> {
            if (cls.isAnnotationPresent(annotation)) {
                classSet.add(cls);
            }
        });
        return classSet;
    }

}
