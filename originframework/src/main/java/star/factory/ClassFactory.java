package star.factory;

import star.annotation.bean.Component;
import star.annotation.bean.Controller;
import star.annotation.bean.Service;
import star.utils.ClassUtil;

import java.lang.annotation.Annotation;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

/**
 * @author keshawn
 * @date 2017/11/9
 */
public final class ClassFactory {

    private static final Set<Class<?>> CLASS_SET;

    static {
        String basePackage = ConfigFactory.getAppBasePackage();
        CLASS_SET = ClassUtil.getClassSet(basePackage);
        CLASS_SET.forEach(ClassUtil::getClassInfo);
    }

    private ClassFactory() {
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
        return CLASS_SET.stream()
                .filter(cls -> cls.isAnnotationPresent(Controller.class) || cls.isAnnotationPresent(Service.class) || cls.isAnnotationPresent(Component.class))
                .collect(toSet());
    }

    public static Set<Class<?>> getAnnotationClassSet(Class<? extends Annotation> annotation) {
        return CLASS_SET.stream().filter(cls -> cls.isAnnotationPresent(annotation)).collect(toSet());
    }

    public static Set<Class<?>> getClassSetBySuper(Class<?> superClass) {
        return CLASS_SET.stream().filter(cls -> superClass.isAssignableFrom(cls) && !superClass.equals(cls)).collect(toSet());
    }

}
