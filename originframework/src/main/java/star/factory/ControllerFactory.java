package star.factory;

import org.apache.commons.lang3.ArrayUtils;
import star.annotation.Action;
import star.bean.Handler;
import star.bean.Request;
import star.constant.ConfigConstant;
import star.constant.RequestMethod;
import star.utils.CollectionUtil;
import star.utils.StringUtil;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static star.utils.StringUtil.checkUrlPrefix;

/**
 * @author keshawn
 * @date 2017/11/17
 */
public final class ControllerFactory {
    /**
     * 存放请求与处理器的映射关系
     */
    private static final Map<Request, Handler> ACTION_MAP = new HashMap<>(ConfigConstant.INITIAL_CAPACITY);

    static {
        //获取所有的Controller类
        Set<Class<?>> controllerClassSet = ClassFactory.getControllerClassSet();
        if (CollectionUtil.isNotEmpty(controllerClassSet)) {
            controllerClassSet.forEach(controllerClass -> {
                Method[] methods = controllerClass.getDeclaredMethods();
                if (ArrayUtils.isNotEmpty(methods)) {
                    //获取类上的URL映射规则
                    String classRequestMapping = getClassRequestMapping(controllerClass);
                    StringBuilder requestMappingBuilder = new StringBuilder();
                    for (Method method : methods) {
                        buildMethodHandler(controllerClass, classRequestMapping, requestMappingBuilder, method);
                    }
                }
            });
        }
    }

    private ControllerFactory() {
    }

    private static void buildMethodHandler(Class<?> controllerClass, String classRequestMapping, StringBuilder requestMappingBuilder, Method method) {
        if (method.isAnnotationPresent(Action.class)) {
            Action methodAction = method.getAnnotation(Action.class);
            //获取requestPath
            String requestPath = getRequestPath(classRequestMapping, requestMappingBuilder, methodAction);
            //获取请求方法
            String requestMethod = methodAction.method();
            //如果指定了请求方法，就按指定值保存
            if (StringUtil.isNotEmpty(requestMethod)) {
                Request request = new Request(requestMethod, requestPath);
                Handler handler = new Handler(controllerClass, method);
                ACTION_MAP.put(request, handler);
            } else {
                //否则就匹配所有的请求方法
                Request getRequest = new Request(RequestMethod.GET, requestPath);
                Request postRequest = new Request(RequestMethod.POST, requestPath);
                Request putRequest = new Request(RequestMethod.PUT, requestPath);
                Request deleteRequest = new Request(RequestMethod.DELETE, requestPath);
                Handler handler = new Handler(controllerClass, method);
                ACTION_MAP.put(getRequest, handler);
                ACTION_MAP.put(postRequest, handler);
                ACTION_MAP.put(putRequest, handler);
                ACTION_MAP.put(deleteRequest, handler);
            }
        }
    }

    private static String getRequestPath(String classRequestMapping, StringBuilder requestMappingBuilder, Action methodAction) {
        String requestPath;
        String methodRequestMapping = checkUrlPrefix(methodAction.value());
        //如果类上没有URL映射，就直接取方法上指定的映射
        if (classRequestMapping == null) {
            requestPath = methodRequestMapping;
        } else {
            //否则将类上的路径和方法上的路径拼接起来
            requestPath = requestMappingBuilder.append(classRequestMapping).append(methodRequestMapping).toString();
            requestMappingBuilder.setLength(0);
        }
        return requestPath;
    }

    private static String getClassRequestMapping(Class<?> controllerClass) {
        String classRequestMapping = null;
        if (controllerClass.isAnnotationPresent(Action.class)) {
            Action classAction = controllerClass.getAnnotation(Action.class);
            String requestMapping = classAction.value();
            classRequestMapping = checkUrlPrefix(requestMapping);
        }
        return classRequestMapping;
    }

    public static Handler getHandler(String requestMethod, String requestPath) {
        Request request = new Request(requestMethod, requestPath);
        return ACTION_MAP.get(request);
    }
}
