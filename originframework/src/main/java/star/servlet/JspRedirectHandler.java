package star.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static star.constant.ServletConstant.JSP_SUFFIX;

/**
 * @author keshawn
 * @date 2018/1/9
 */
public final class JspRedirectHandler {

    private JspRedirectHandler() {
    }

    public static void jspRedirectHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestPath = request.getPathInfo();
        String requestContextPath = request.getContextPath();
        if (requestPath.startsWith(requestContextPath) && requestPath.endsWith(JSP_SUFFIX)) {
            String forwardPath = requestPath.substring(requestContextPath.length() - 1, requestPath.length());
            request.getRequestDispatcher(forwardPath).forward(request, response);
        }
    }
}
