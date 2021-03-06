package star.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import star.annotation.bean.Component;
import star.annotation.bean.Controller;
import star.annotation.bean.Fresh;
import star.annotation.bean.Service;
import star.constant.ConfigConstant;
import star.core.IocCore;
import star.exception.ImplementDuplicateException;
import star.proxy.Proxy;
import star.proxy.ProxyManager;
import star.repository.interfaces.CommonRepository;
import star.utils.CollectionUtil;
import star.utils.ReflectionUtil;
import star.utils.StringUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import static star.constant.RepositoryConstant.BLANK;

/**
 * @author keshawn
 * @date 2017/11/9
 */
public final class BeanFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(BeanFactory.class);

    private static final int INITIAL_CAPACITY = 32;

    private static final Map<Class<?>, Object> BEAN_MAP = new HashMap<>(INITIAL_CAPACITY);

    private static final Map<String, Class<?>> BEAN_CONTEXT = new HashMap<>(INITIAL_CAPACITY);

    private static final Map<String, String> YAML_BEAN_ID_MAPPING = ConfigFactory.getBeanIdMapping();

    private static final Map<Class<?>, List<Proxy>> TARGET_MAP = ProxyFactory.getTargetMap();

    private static final Map<Class<?>, Object> CLASS_PROXY_MAP = ProxyFactory.getClassProxyMap();

    private static final Map<Class<?>, CommonRepository> REPOSITORY_MAP = RepositoryFactory.getRepositoryMap();

    static {
        Set<Class<?>> beanClassSet = ClassFactory.getBeanClassSet();
        beanClassSet.forEach(beanClass -> {
            buildBeanContext(beanClass);
            //带有@Fresh注解为多例,每次依赖注入都进行类的初始化，不加入实例中
            if (!beanClass.isAnnotationPresent(Fresh.class)) {
                Object object = getSingletonInstance(beanClass);
                BEAN_MAP.put(beanClass, object);
            }
        });
        BEAN_MAP.putAll(REPOSITORY_MAP);
        ClassFactory.getClassSetBySuper(CommonRepository.class).forEach(cls -> addBean(BLANK, cls));
    }

    private BeanFactory() {
    }

    private static Object getSingletonInstance(Class<?> beanClass) {
        //如果该类存在代理，则使用代理对象
        return CLASS_PROXY_MAP.containsKey(beanClass) ? CLASS_PROXY_MAP.get(beanClass) : ReflectionUtil.newInstance(beanClass);
    }

    public static Map<Class<?>, Object> getBeanMap() {
        return BEAN_MAP;
    }

    public static Map<Class<?>, Class<?>> getServiceMappingMap() {
        Map<Class<?>, Class<?>> serviceMap = new HashMap<>(ConfigConstant.INITIAL_CAPACITY);
        if (CollectionUtil.isNotEmpty(BEAN_CONTEXT)) {
            //目前只有带Service注解的需要做接口与实现类的映射关系
            BEAN_CONTEXT.values().stream().filter(beanClass -> beanClass.isAnnotationPresent(Service.class)).forEach(buildServiceMap(serviceMap));
        }
        return serviceMap;
    }

    private static Consumer<Class<?>> buildServiceMap(Map<Class<?>, Class<?>> serviceMap) {
        return beanClass -> {
            Class<?>[] interfaces = beanClass.getInterfaces();
            for (Class<?> interFace : interfaces) {
                if (serviceMap.containsKey(interFace)) {
                    //说明一个接口有多个实现类,在Map中存放ImplementDuplicateException作为接口多实现的标记
                    serviceMap.put(interFace, ImplementDuplicateException.class);
                } else {
                    serviceMap.put(interFace, beanClass);
                }
            }
        };
    }

    public static <T> T getBean(Class<T> cls) {
        //带有@Fresh注解为多例，每次都进行类的初始化,并进行依赖注入
        if (cls.isAnnotationPresent(Fresh.class)) {
            //查看该类是否存在代理
            List<Proxy> proxyList = TARGET_MAP.get(cls);
            T beanInstance = CollectionUtil.isNotEmpty(proxyList) ? ProxyManager.createProxy(cls, proxyList) : ReflectionUtil.newInstance(cls);
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
        } else if (beanClass.isAnnotationPresent(Component.class)) {
            Component component = beanClass.getAnnotation(Component.class);
            String value = component.value();
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

    public static void setSingletonBean(Class<?> cls, Object object) {
        //BEAN_MAP中只存储单例的对象
        if (!cls.isAnnotationPresent(Fresh.class)) {
            BEAN_MAP.put(cls, object);
        }
    }
}
