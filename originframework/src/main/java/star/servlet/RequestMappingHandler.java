package star.servlet;

import star.bean.Handler;
import star.factory.ControllerFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * @author keshawn
 * @date 2018/1/9
 */
public final class RequestMappingHandler {

    private RequestMappingHandler() {
    }

    public static Handler getHandler(HttpServletRequest request){
        String requestMethod = request.getMethod();
        String requestPath = request.getPathInfo();
        return ControllerFactory.getHandler(requestMethod, requestPath);
    }
}
