package star.core;

import org.apache.commons.lang3.ArrayUtils;
import star.annotation.Inject;
import star.exception.ImplementDuplicateException;
import star.factory.BeanFactory;
import star.factory.ConfigFactory;
import star.utils.CollectionUtil;
import star.utils.ReflectionUtil;
import star.utils.StringUtil;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * @author keshawn
 * @date 2017/11/9
 */
public final class IocCore {
    /**
     * 获取所有的Bean类和Bean实例之间的映射关系
     */
    private static final Map<Class<?>, Object> BEAN_MAP = BeanFactory.getBeanMap();
    /**
     * 获取yml配置文件中接口与实现类beanId的映射关系
     */
    private static final Map<Class<?>, String> IMPLEMENT_MAPPING = ConfigFactory.getImplementMapping();
    /**
     * 获取所有带有@Service注解的类的接口与自身的映射关系
     */
    private static final Map<Class<?>, Class<?>> SERVICE_MAPPING = BeanFactory.getServiceMappingMap();

    static {
        if (CollectionUtil.isNotEmpty(BEAN_MAP)) {
            for (Map.Entry<Class<?>, Object> beanEntry : BEAN_MAP.entrySet()) {
                Class<?> beanClass = beanEntry.getKey();
                Object beanInstance = beanEntry.getValue();
                dependencyInjection(beanClass, beanInstance);
            }
        }
    }

    public static void dependencyInjection(Class<?> beanClass, Object beanInstance) {
        Field[] beanFields = beanClass.getDeclaredFields();
        if (ArrayUtils.isNotEmpty(beanFields)) {
            //循环进行依赖注入
            for (Field beanField : beanFields) {
                fieldDependencyInjection(beanClass, beanInstance, beanField);
            }
        }
    }

    private static void fieldDependencyInjection(Class<?> beanClass, Object beanInstance, Field beanField) {
        if (beanField.isAnnotationPresent(Inject.class)) {
            Class<?> beanFieldClass = beanField.getType();
            //如果是接口，注入实现类，否则注入类自身
            if (beanFieldClass.isInterface()) {
                interfaceDependencyInjection(beanInstance, beanField, beanFieldClass);
            } else {
                Object beanFieldInstance = BeanFactory.getBean(beanFieldClass);
                if (beanFieldInstance != null) {
                    //通过反射初始化BeanField的值
                    ReflectionUtil.setField(beanInstance, beanField, beanFieldInstance);
                }
            }
        }
    }

    private static void interfaceDependencyInjection(Object beanInstance, Field beanField, Class<?> beanFieldClass) {

        //先查看配置文件中是否配置了映射
        if (IMPLEMENT_MAPPING.containsKey(beanFieldClass)) {
            Object beanFieldInstance = BeanFactory.getBean(IMPLEMENT_MAPPING.get(beanFieldClass));
            if (beanFieldInstance != null) {
                //通过反射初始化BeanField的值
                ReflectionUtil.setField(beanInstance, beanField, beanFieldInstance);
            }
            return;
        }

        Inject inject = beanField.getAnnotation(Inject.class);
        String injectValue = inject.value();
        //如果使用@Inject注解未指定实现类，则从serviceMappingMap中寻找实现类进行注入
        if (StringUtil.isEmpty(injectValue)) {
            Class<?> implementClass = SERVICE_MAPPING.get(beanFieldClass);
            if (implementClass == null) {
                throw new RuntimeException(beanFieldClass.getTypeName() + " have no implement error");
            }
            //如果实现类为ImplementDuplicateException，则代表一个接口有多个实现类，而使用者又未指明使用那个实现类
            if (ImplementDuplicateException.class.equals(implementClass)) {
                throw new ImplementDuplicateException("cannot inject bean , this interface has more than one implement");
            } else {
                Object beanFieldInstance = BeanFactory.getBean(implementClass);
                if (beanFieldInstance != null) {
                    //通过反射初始化BeanField的值
                    ReflectionUtil.setField(beanInstance, beanField, beanFieldInstance);
                }
            }
        } else {
            //@Inject注解未指定实现类 bean id
            Object beanFieldInstance = BeanFactory.getBean(injectValue);
            if (beanFieldInstance != null) {
                //通过反射初始化BeanField的值
                ReflectionUtil.setField(beanInstance, beanField, beanFieldInstance);
            }
        }
    }
}
