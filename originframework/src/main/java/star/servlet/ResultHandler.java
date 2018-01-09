package star.servlet;

import star.annotation.controller.Internal;
import star.annotation.controller.Stream;
import star.factory.ConfigFactory;
import star.utils.JsonUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;

import static star.constant.ServletConstant.*;

/**
 * @author keshawn
 * @date 2018/1/9
 */
public final class ResultHandler {

    private ResultHandler() {
    }

    public static void resultHandler(HttpServletRequest request, HttpServletResponse response, Method method, Object result) throws IOException, ServletException {
        String requestContextPath = request.getContextPath();
        if (result != null) {
            //如果带有@Stream注解则代表以流的方式打回数据，内容默认使用JSON
            if (method.isAnnotationPresent(Stream.class)) {
                writeResult(response, result);
            } else if (method.isAnnotationPresent(Internal.class)) {
                InternalJump(request, response, result, requestContextPath);
            } else {
                JspJump(request, response, result, requestContextPath);
            }
        }
    }

    private static void JspJump(HttpServletRequest request, HttpServletResponse response, Object result, String requestContextPath) throws IOException, ServletException {
        //跳转到到Jsp,如果以/开头认为是重定向，否则为转发
        String path = result.toString();
        if (path.startsWith(BACKLASH)) {
            response.sendRedirect(requestContextPath + ConfigFactory.getAppJspPath() + path + JSP_SUFFIX);
        } else {
            request.getRequestDispatcher(ConfigFactory.getAppJspPath() + BACKLASH + path + JSP_SUFFIX).forward(request, response);
        }
    }

    private static void InternalJump(HttpServletRequest request, HttpServletResponse response, Object result, String requestContextPath) throws IOException, ServletException {
        //跳转到到其他Action,如果以/开头认为是重定向，否则为转发
        String path = result.toString();
        if (path.startsWith(BACKLASH)) {
            response.sendRedirect(requestContextPath + path);
        } else {
            request.getRequestDispatcher(path).forward(request, response);
        }
    }

    private static void writeResult(HttpServletResponse response, Object result) throws IOException {
        response.setContentType(APPLICATION_JSON);
        response.setCharacterEncoding(UTF_8);
        PrintWriter printWriter = response.getWriter();
        String jsonResult = JsonUtil.encodeJson(result);
        printWriter.write(jsonResult);
        printWriter.flush();
        printWriter.close();
    }
}
