package star.factory;

import star.annotation.bean.Component;
import star.annotation.bean.Controller;
import star.annotation.bean.Service;
import star.bean.ClassInfo;
import star.utils.ClassUtil;
import star.utils.ReflectionUtil;
import star.utils.StringUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

/**
 * @author keshawn
 * @date 2017/11/9
 */
public final class ClassFactory {

    private static final Set<Class<?>> CLASS_SET;

    private static final ConcurrentHashMap<Class<?>, ClassInfo> CLASS_INFO_MAP = new ConcurrentHashMap<>();

    static {
        String basePackage = ConfigFactory.getAppBasePackage();
        CLASS_SET = ClassUtil.getClassSet(basePackage);
        CLASS_SET.forEach(ClassFactory::getClassInfo);
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

    public static ClassInfo getClassInfo(Class<?> cls) {
        return CLASS_INFO_MAP.containsKey(cls) ? CLASS_INFO_MAP.get(cls) : generateClassInfo(cls);
    }

    private static ClassInfo generateClassInfo(Class<?> cls) {
        ClassInfo classInfo = new ClassInfo();
        List<Field> fields = ReflectionUtil.getFields(cls);
        List<Method> methods = Arrays.asList(cls.getDeclaredMethods());
        List<String> getMethods = fields.stream().map(field -> StringUtil.getGetMethodName(field.getName())).collect(Collectors.toList());
        List<String> setMethods = fields.stream().map(field -> StringUtil.getSetMethodName(field.getName())).collect(Collectors.toList());
        classInfo.setFields(fields);
        classInfo.setMethods(methods);
        classInfo.setGetMethodNames(getMethods);
        classInfo.setSetMethodNames(setMethods);
        classInfo.setFieldMap(fields.stream().collect(toMap(Field::getName, Function.identity())));
        return classInfo;
    }
}
