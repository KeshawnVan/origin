package star.servlet;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import star.annotation.QueryParam;
import star.bean.Handler;
import star.bean.Param;
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
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author keshawn
 * @date 2017/11/17
 * 请求转发器
 */
@WebServlet(urlPatterns = "/*", loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet {


    @Override
    public void init(ServletConfig config) throws ServletException {
        LoadCore.init();
        //获取ServletContext对象（用于注册Servlet）
        ServletContext servletContext = config.getServletContext();
        //注册处理JSP的Servlet
        ServletRegistration jspServlet = servletContext.getServletRegistration("jsp");
        jspServlet.addMapping(ConfigFactory.getAppJspPath() + "*");
        //注册处理静态资源的默认Servlet
        ServletRegistration defaultServlet = servletContext.getServletRegistration("default");
        defaultServlet.addMapping(ConfigFactory.getAppAssetPath() + "*");
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
            Map<String, Object> paramMap = buildParamMap(request);
            Param param = new Param(paramMap);
            Method method = handler.getActionMethod();
            //方法参数注入
            Object[] parameterValues = buildParameterValues(paramMap, method);
            Object result = ReflectionUtil.invokeMethod(controllerBean, method, parameterValues);
        }
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
                Object parameterValue;
                QueryParam queryParam = parameter.getAnnotation(QueryParam.class);
                //如果使用QueryParam，则使用注解上的值去解析参数
                if (queryParam == null) {
                    parameterValue = paramMap.get(parameterName);
                } else {
                    parameterValue = paramMap.get(queryParam.value());
                }
                Class<?> parameterType = parameter.getType();
                String jsonParam = "\"" + parameterValue.toString() + "\"";
                parameterValues[i] = JsonUtil.decodeJson(jsonParam, parameterType);
            }
        }
        return parameterValues;
    }

    private Map<String, Object> buildParamMap(HttpServletRequest request) throws IOException {
        Map<String, Object> paramMap = new HashMap<>(ConfigConstant.INITIAL_CAPACITY);
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            String paramValue = request.getParameter(paramName);
            paramMap.put(paramName, paramValue);
        }
        //获得请求体
        String body = CodeUtil.decodeURL(StreamUtil.getString(request.getInputStream()));
        if (StringUtil.isNotEmpty(body)) {
            //处理表单提交
            String[] params = StringUtils.split(body, "&");
            if (ArrayUtils.isNotEmpty(params)) {
                for (String param : params) {
                    String[] paramArray = StringUtils.split(param, "=");
                    if (ArrayUtils.isNotEmpty(paramArray) && paramArray.length == 2) {
                        String paramName = paramArray[0];
                        String paramValue = paramArray[1];
                        paramMap.put(paramName, paramValue);
                    }
                }
            }
            //TODO 需考虑接收JSON
        }
        return paramMap;
    }
}
