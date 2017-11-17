package star.servlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

/**
 * @author keshawn
 * @date 2017/11/17
 * 请求转发器
 */
@WebServlet(urlPatterns = "/*", loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet{


}
