package star.servlet;

import com.alibaba.druid.sql.visitor.functions.Char;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import star.annotation.controller.Internal;
import star.annotation.controller.QueryParam;
import star.annotation.controller.Stream;
import star.bean.Handler;
import star.constant.ConfigConstant;
import star.core.LoadCore;
import star.factory.BeanFactory;
import star.factory.ConfigFactory;
import star.factory.ControllerFactory;
import star.utils.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author keshawn
 * @date 2017/11/17
 * 请求转发器
 */
@WebServlet(urlPatterns = "/*", loadOnStartup = 0)
public final class DispatcherServlet extends HttpServlet {

    private static final String APPLICATION_JSON = "application/json";

    private static final String UTF_8 = "UTF-8";

    private static final String BACKLASH = "/";

    private static final String JSP_SUFFIX = ".jsp";

    private static final String HTTP_SERVLET_REQUEST = "httpServletRequest";

    private static final String HTTP_SERVLET_RESPONSE = "httpServletResponse";

    private static final String HTTP_SESSION = "httpSession";

    private static final String SERVLET_CONTEXT = "servletContext";

    @Override
    public void init(ServletConfig config) throws ServletException {
        LoadCore.init();
        //获取ServletContext对象（用于注册Servlet）
        ServletContext servletContext = config.getServletContext();
        //注册处理JSP的Servlet
        ServletRegistration jspServlet = servletContext.getServletRegistration("jsp");
        jspServlet.addMapping(ConfigFactory.getAppJspPath() + BACKLASH + "*");
        //注册处理静态资源的默认Servlet
        ServletRegistration defaultServlet = servletContext.getServletRegistration("default");
        defaultServlet.addMapping(ConfigFactory.getAppAssetPath() + BACKLASH + "*");
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestMethod = request.getMethod();
        String requestPath = request.getPathInfo();
        Handler handler = ControllerFactory.getHandler(requestMethod, requestPath);
        if (handler != null) {
            //获取Controller类模板及实例
            Class<?> controllerClass = handler.getControllerClass();
            Object controllerBean = BeanFactory.getBean(controllerClass);
            //创建请求参数对象
            Map<String, Object> paramMap = buildParamMap(request, response);
            Method method = handler.getActionMethod();
            //方法参数注入
            Object[] parameterValues = buildParameterValues(paramMap, method);
            Object result = ReflectionUtil.invokeMethod(controllerBean, method, parameterValues);
            //处理结果
            resultHandler(request, response, method, result);
        }
        //Jsp重定向请求处理
        jspRedirectHandler(request, response, requestPath);
    }

    private void jspRedirectHandler(HttpServletRequest request, HttpServletResponse response, String requestPath) throws ServletException, IOException {
        String requestContextPath = request.getContextPath();
        if (requestPath.startsWith(requestContextPath) && requestPath.endsWith(JSP_SUFFIX)) {
            String forwardPath = requestPath.substring(requestContextPath.length() - 1, requestPath.length());
            request.getRequestDispatcher(forwardPath).forward(request, response);
        }
    }

    private void resultHandler(HttpServletRequest request, HttpServletResponse response, Method method, Object result) throws IOException, ServletException {
        String requestContextPath = request.getContextPath();
        if (result != null) {
            //如果带有@Stream注解则代表以流的方式打回数据，内容默认使用JSON
            if (method.isAnnotationPresent(Stream.class)) {
                writeResult(response, result);
            } else if (method.isAnnotationPresent(Internal.class)) {
                //跳转到到其他Action,如果以/开头认为是重定向，否则为转发
                String path = result.toString();
                if (path.startsWith(BACKLASH)) {
                    response.sendRedirect(requestContextPath + path);
                } else {
                    request.getRequestDispatcher(path).forward(request, response);
                }
            } else {
                //跳转到到Jsp,如果以/开头认为是重定向，否则为转发
                String path = result.toString();
                if (path.startsWith(BACKLASH)) {
                    response.sendRedirect(requestContextPath + ConfigFactory.getAppJspPath() + path + JSP_SUFFIX);
                } else {
                    request.getRequestDispatcher(ConfigFactory.getAppJspPath() + BACKLASH + path + JSP_SUFFIX).forward(request, response);
                }
            }
        }
    }


    private void writeResult(HttpServletResponse response, Object result) throws IOException {
        response.setContentType(APPLICATION_JSON);
        response.setCharacterEncoding(UTF_8);
        PrintWriter printWriter = response.getWriter();
        String jsonResult = JsonUtil.encodeJson(result);
        printWriter.write(jsonResult);
        printWriter.flush();
        printWriter.close();
    }

    private Object[] buildParameterValues(Map<String, Object> paramMap, Method method) {
        Object[] parameterValues = null;
        Parameter[] parameters = method.getParameters();
        if (ArrayUtils.isNotEmpty(parameters)) {
            int parametersLength = parameters.length;
            parameterValues = new Object[parametersLength];
            for (int i = 0; i < parametersLength; i++) {
                Parameter parameter = parameters[i];
                String parameterName = parameter.getName();
                Class<?> parameterType = parameter.getType();
                Object parameterValue;
                QueryParam queryParam = parameter.getAnnotation(QueryParam.class);
                //如果使用QueryParam，则使用注解上的值去解析参数
                if (queryParam == null) {
                    parameterValue = getByParamMap(paramMap, parameterName);
                } else {
                    parameterValue = getByParamMap(paramMap, queryParam.value());
                }
                //如果请求体是JSON
                Object jsonString = paramMap.get(APPLICATION_JSON);
                if (jsonString != null) {
                    parameterValue = jsonString;
                }
                //如果parameterValue没找到匹配的参数，则可能是一个接收对象,也可能是容器内置对象
                if (parameterValue == null) {
                    parameterValue = beanParameterInject(paramMap, parameterType, parameterValue);
                }
                //如果parameterValue不为空且为String类型，则认为需要进行转型
                if (parameterValue != null && parameterValue instanceof String) {
                    String jsonParam = StringUtil.castJsonString(parameterValue);
                    parameterValues[i] = JsonUtil.decodeJson(jsonParam, parameterType);
                } else {
                    parameterValues[i] = parameterValue;
                }
            }
        }
        return parameterValues;
    }

    private Object getByParamMap(Map<String, Object> paramMap, String parameterName) {
        Object parameterValue = paramMap.get(parameterName);
        if (parameterValue instanceof List) {
            parameterValue = JsonUtil.encodeJson(parameterValue);
        }
        return parameterValue;
    }

    private Object beanParameterInject(Map<String, Object> paramMap, Class<?> parameterType, Object parameterValue) {
        //如果parameterType是基本类型，基本类型的包装类，或者Request等，不对该对象注入值
        if (!(parameterType.isPrimitive()
                || parameterType.equals(Integer.class)
                || parameterType.equals(String.class)
                || parameterType.equals(Long.class)
                || parameterType.equals(Double.class)
                || parameterType.equals(Byte.class)
                || parameterType.equals(Short.class)
                || parameterType.equals(Float.class)
                || parameterType.equals(Char.class)
                || parameterType.equals(HttpServletRequest.class)
                || parameterType.equals(HttpServletResponse.class)
                || parameterType.equals(HttpSession.class)
                || parameterType.equals(ServletContext.class)
                || parameterType.equals(Map.class)
                || parameterType.equals(List.class)
        )) {
            parameterValue = MapToBeanUtil.buildBean(paramMap, parameterType);
        } else if (parameterType.equals(HttpServletRequest.class)) {
            parameterValue = paramMap.get(HTTP_SERVLET_REQUEST);
        } else if (parameterType.equals(HttpServletResponse.class)) {
            parameterValue = paramMap.get(HTTP_SERVLET_RESPONSE);
        } else if (parameterType.equals(HttpSession.class)) {
            parameterValue = paramMap.get(HTTP_SESSION);
        } else if (parameterType.equals(ServletContext.class)) {
            parameterValue = paramMap.get(SERVLET_CONTEXT);
        }
        return parameterValue;
    }

    private Map<String, Object> buildParamMap(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding(UTF_8);
        Map<String, Object> paramMap = new HashMap<>(ConfigConstant.INITIAL_CAPACITY);

        paramMap.put(HTTP_SERVLET_REQUEST, request);
        paramMap.put(HTTP_SERVLET_RESPONSE, response);
        paramMap.put(HTTP_SESSION, request.getSession());
        paramMap.put(SERVLET_CONTEXT, request.getSession().getServletContext());

        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            String paramValue = request.getParameter(paramName);
            putParamMap(paramMap, paramName, paramValue);
        }
        //获得请求体
        String body = CodeUtil.decodeURL(StreamUtil.getString(request.getInputStream()));
        if (StringUtil.isNotEmpty(body)) {
            String contentType = request.getContentType();
            //JSON格式的
            if (APPLICATION_JSON.equals(contentType)) {
                paramMap.put(APPLICATION_JSON, body);
            } else {
                //其他的都按文本处理
                String[] params = StringUtils.split(body, "&");
                if (ArrayUtils.isNotEmpty(params)) {
                    for (String param : params) {
                        String[] paramArray = StringUtils.split(param, "=");
                        if (ArrayUtils.isNotEmpty(paramArray) && paramArray.length == 2) {
                            String paramName = paramArray[0];
                            String paramValue = paramArray[1];
                            putParamMap(paramMap, paramName, paramValue);
                        }
                    }
                }
            }

        }
        return paramMap;
    }

    private void putParamMap(Map<String, Object> paramMap, String paramName, String paramValue) {
        //如果key相同，使用List去存储
        if (paramMap.containsKey(paramName)) {
            Object duplicateValue = paramMap.get(paramName);
            if (duplicateValue instanceof List) {
                ((List) duplicateValue).add(paramValue);
            } else {
                paramMap.put(paramName, Lists.newArrayList(duplicateValue, paramValue));
            }
        } else {
            paramMap.put(paramName, paramValue);
        }
    }
}
