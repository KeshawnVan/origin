package star.servlet;

import star.bean.Handler;
import star.core.LoadCore;
import star.factory.BeanFactory;
import star.factory.ConfigFactory;
import star.utils.ReflectionUtil;

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
import java.util.Map;

import static star.constant.ServletConstant.BACKLASH;
import static star.servlet.JspRedirectHandler.jspRedirectHandler;
import static star.servlet.ParameterValueBuilder.buildParameterValues;
import static star.servlet.RequestMappingHandler.getHandler;
import static star.servlet.RequestParamParser.buildParamMap;
import static star.servlet.ResultHandler.resultHandler;

/**
 * @author keshawn
 * @date 2017/11/17
 * 请求转发器
 */
@WebServlet(urlPatterns = "/*", loadOnStartup = 0)
public final class DispatcherServlet extends HttpServlet {

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
        Handler handler = getHandler(request);
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
        jspRedirectHandler(request, response);
    }
}
