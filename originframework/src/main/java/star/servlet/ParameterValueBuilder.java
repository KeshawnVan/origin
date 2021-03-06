package star.servlet;

import org.apache.commons.lang3.ArrayUtils;
import star.annotation.controller.QueryParam;
import star.utils.JsonUtil;
import star.utils.MapToBeanUtil;
import star.utils.StringUtil;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Map;

import static star.constant.ServletConstant.*;
import static star.utils.ReflectionUtil.isPrimitive;

/**
 * @author keshawn
 * @date 2018/1/9
 */
public final class ParameterValueBuilder {

    private ParameterValueBuilder() {
    }

    public static Object[] buildParameterValues(Map<String, Object> paramMap, Method method) {
        Object[] parameterValues = null;
        Parameter[] parameters = method.getParameters();
        if (ArrayUtils.isNotEmpty(parameters)) {
            int parametersLength = parameters.length;
            parameterValues = new Object[parametersLength];
            for (int i = 0; i < parametersLength; i++) {
                buildValueByParameter(paramMap, parameterValues, parameters[i], i);
            }
        }
        return parameterValues;
    }

    private static void buildValueByParameter(Map<String, Object> paramMap, Object[] parameterValues, Parameter parameter, int i) {
        String parameterName = parameter.getName();
        Class<?> parameterType = parameter.getType();
        Object parameterValue;
        QueryParam queryParam = parameter.getAnnotation(QueryParam.class);
        //如果使用QueryParam，则使用注解上的值去解析参数
        parameterValue = getByParamMap(paramMap, queryParam == null ? parameterName : queryParam.value());
        //如果请求体是JSON
        Object jsonString = paramMap.get(APPLICATION_JSON);
        if (jsonString != null) {
            parameterValue = jsonString;
        }
        //如果parameterValue没找到匹配的参数，则可能是一个接收对象,也可能是容器内置对象
        if (parameterValue == null) {
            parameterValue = beanParameterInject(paramMap, parameterType);
        }
        //如果parameterValue不为空且为String类型，则认为需要进行转型
        parameterValues[i] = parameterValue != null && parameterValue instanceof String
                ? JsonUtil.decodeJson(StringUtil.castJsonString(parameterValue), parameterType)
                : parameterValue;
    }

    private static Object beanParameterInject(Map<String, Object> paramMap, Class<?> parameterType) {
        //如果parameterType是基本类型，基本类型的包装类，或者Request等，不对该对象注入值
        if (!(isPrimitive(parameterType)
                || parameterType.equals(HttpServletRequest.class)
                || parameterType.equals(HttpServletResponse.class)
                || parameterType.equals(HttpSession.class)
                || parameterType.equals(ServletContext.class)
                || parameterType.equals(Map.class)
                || parameterType.equals(List.class)
        )) {
            return MapToBeanUtil.buildBean(paramMap, parameterType);
        } else if (parameterType.equals(HttpServletRequest.class)) {
            return paramMap.get(HTTP_SERVLET_REQUEST);
        } else if (parameterType.equals(HttpServletResponse.class)) {
            return paramMap.get(HTTP_SERVLET_RESPONSE);
        } else if (parameterType.equals(HttpSession.class)) {
            return paramMap.get(HTTP_SESSION);
        } else if (parameterType.equals(ServletContext.class)) {
            return paramMap.get(SERVLET_CONTEXT);
        }
        return null;
    }


    private static Object getByParamMap(Map<String, Object> paramMap, String parameterName) {
        Object parameterValue = paramMap.get(parameterName);
        if (parameterValue instanceof List) {
            parameterValue = JsonUtil.encodeJson(parameterValue);
        }
        return parameterValue;
    }
}
