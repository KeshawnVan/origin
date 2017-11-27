package star.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import star.annotation.Controller;
import star.annotation.Fresh;
import star.annotation.Service;
import star.constant.ConfigConstant;
import star.core.IocCore;
import star.exception.ImplementDuplicateException;
import star.utils.CollectionUtil;
import star.utils.ReflectionUtil;
import star.utils.StringUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author keshawn
 * @date 2017/11/9
 */
public final class BeanFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(BeanFactory.class);

    private static final Map<Class<?>, Object> BEAN_MAP = new HashMap<>(32);

    private static final Map<String, Class<?>> BEAN_CONTEXT = new HashMap<>(32);

    private static final Map<String, String> YAML_BEAN_ID_MAPPING = ConfigFactory.getBeanIdMapping();

    static {
        Set<Class<?>> beanClassSet = ClassFactory.getBeanClassSet();
        beanClassSet.forEach(beanClass -> {
            buildBeanContext(beanClass);
            //带有@Fresh注解为多例,每次依赖注入都进行类的初始化，不加入实例中
            if (!beanClass.isAnnotationPresent(Fresh.class)) {
                Object object = ReflectionUtil.newInstance(beanClass);
                BEAN_MAP.put(beanClass, object);
            }
        });
    }

    public static Map<Class<?>, Object> getBeanMap() {
        return BEAN_MAP;
    }

    public static Map<Class<?>, Class<?>> getServiceMappingMap() {
        Map<Class<?>, Class<?>> serviceMap = new HashMap<>(ConfigConstant.INITIAL_CAPACITY);
        if (CollectionUtil.isNotEmpty(BEAN_CONTEXT)) {
            for (Class<?> beanClass : BEAN_CONTEXT.values()) {
                //目前只有带Service注解的需要做接口与实现类的映射关系
                if (beanClass.isAnnotationPresent(Service.class)) {
                    Class<?>[] interfaces = beanClass.getInterfaces();
                    for (Class<?> interFace : interfaces) {
                        if (serviceMap.containsKey(interFace)) {
                            //说明一个接口有多个实现类,在Map中存放ImplementDuplicateException作为接口多实现的标记
                            serviceMap.put(interFace, ImplementDuplicateException.class);
                        } else {
                            serviceMap.put(interFace, beanClass);
                        }
                    }
                }
            }
        }
        return serviceMap;
    }

    public static <T> T getBean(Class<T> cls) {
        //带有@Fresh注解为多例，每次都进行类的初始化,并进行依赖注入
        if (cls.isAnnotationPresent(Fresh.class)) {
            T beanInstance = (T) ReflectionUtil.newInstance(cls);
            IocCore.dependencyInjection(cls, beanInstance);
            return beanInstance;
        } else {
            if (!BEAN_MAP.containsKey(cls)) {
                throw new RuntimeException("can not get bean by class: " + cls);
            }
            return (T) BEAN_MAP.get(cls);
        }
    }

    public static Object getBean(String beanId) {
        Class beanClass = BEAN_CONTEXT.get(beanId);
        return getBean(beanClass);
    }

    private static void buildBeanContext(Class<?> beanClass) {
        //先从yaml文件中找映射关系
        if (YAML_BEAN_ID_MAPPING.containsKey(beanClass.getTypeName())) {
            String beanName = YAML_BEAN_ID_MAPPING.get(beanClass.getTypeName());
            checkBeanIdDuplicated(beanName);
            BEAN_CONTEXT.put(beanName, beanClass);
        } else if (beanClass.isAnnotationPresent(Service.class)) {
            Service service = beanClass.getAnnotation(Service.class);
            String value = service.value();
            addBean(value, beanClass);
        } else if (beanClass.isAnnotationPresent(Controller.class)) {
            Controller controller = beanClass.getAnnotation(Controller.class);
            String value = controller.value();
            addBean(value, beanClass);
        }
    }

    private static void checkBeanIdDuplicated(String beanName) {
        if (BEAN_CONTEXT.containsKey(beanName)) {
            LOGGER.error("cannot create bean :" + beanName, ", the beanId is duplicated :" + BEAN_CONTEXT.get(beanName).getTypeName() + " and " + beanName);
            throw new RuntimeException();
        }
    }

    private static void addBean(String beanName, Class<?> beanClass) {
        //如果未指定bean id，则取类名首字母小写
        if (StringUtil.isEmpty(beanName)) {
            String beanClassSimpleName = beanClass.getSimpleName();
            String beanId = StringUtil.firstToLowerCase(beanClassSimpleName);
            checkBeanIdDuplicated(beanName);
            BEAN_CONTEXT.put(beanId, beanClass);
        } else {
            String beanId = beanName;
            checkBeanIdDuplicated(beanName);
            BEAN_CONTEXT.put(beanId, beanClass);
        }
    }
}
