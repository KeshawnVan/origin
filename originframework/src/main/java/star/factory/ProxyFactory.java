package star.factory;

import com.google.common.collect.Lists;
import star.annotation.Aspect;
import star.annotation.Pure;
import star.constant.ConfigConstant;
import star.proxy.AspectProxy;
import star.proxy.Proxy;
import star.proxy.ProxyManager;
import star.utils.ReflectionUtil;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;

/**
 * @author keshawn
 * @date 2017/12/22
 */
public class ProxyFactory {

    private static final int INITIAL_CAPACITY = 1;

    private static final Map<Class<?>, Set<Class<?>>> PROXY_MAP = createProxyMap();

    private static final Map<Class<?>, List<Proxy>> TARGET_MAP = createTargetMap(PROXY_MAP);

    private static final Map<Class<?>, Object> CLASS_PROXY_MAP = createClassProxyMap(TARGET_MAP);


    private static Set<Class<?>> createTargetClassSet(Aspect aspect){
        Class<? extends Annotation> annotation = aspect.value();
        return annotation != null && !annotation.equals(Aspect.class)
                ? ClassFactory.getAnnotationClassSet(annotation).stream().filter(cls -> !cls.isAnnotationPresent(Pure.class)).collect(toSet())
                : new HashSet<>(INITIAL_CAPACITY);
    }

    private static Map<Class<?>, Set<Class<?>>> createProxyMap(){
        return ClassFactory.getClassSetBySuper(AspectProxy.class).stream()
                .filter(proxyClass -> proxyClass.isAnnotationPresent(Aspect.class))
                .collect(Collectors.toMap(cls -> cls,cls -> createTargetClassSet(cls.getAnnotation(Aspect.class))));
    }

    private static Map<Class<?>, List<Proxy>> createTargetMap(Map<Class<?>, Set<Class<?>>> proxyMap){
        Map<Class<?>, List<Proxy>> targetMap = new HashMap<>(ConfigConstant.INITIAL_CAPACITY);
        for (Map.Entry<Class<?>, Set<Class<?>>> proxyEntry : proxyMap.entrySet()) {
            Class<?> proxyClass = proxyEntry.getKey();
            Set<Class<?>> targetClassSet = proxyEntry.getValue();
            for (Class<?> targetClass : targetClassSet) {
                Proxy proxy = (Proxy) ReflectionUtil.newInstance(proxyClass);
                if (targetMap.containsKey(targetClass)){
                    targetMap.get(targetClass).add(proxy);
                }else {
                    targetMap.put(targetClass, Lists.newArrayList(proxy));
                }
            }
        }
        return targetMap;
    }

    private static Map<Class<?>, Object> createClassProxyMap(Map<Class<?>, List<Proxy>> targetMap){
        Map<Class<?>, Object> classProxyMap = new HashMap<>(ConfigConstant.INITIAL_CAPACITY);
        targetMap.forEach((targetClass,proxyList) -> classProxyMap.put(targetClass, ProxyManager.createProxy(targetClass,proxyList)));
        return classProxyMap;
    }

    public static Map<Class<?>, Object> getClassProxyMap(){
        return CLASS_PROXY_MAP;
    }

    public static Map<Class<?>, List<Proxy>> getTargetMap(){
        return TARGET_MAP;
    }
}
