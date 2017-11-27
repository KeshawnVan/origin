package star.controller;

import star.annotation.*;
import star.bean.User;
import star.constant.RequestMethod;
import star.service.TestService;
import star.utils.JsonUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author keshawn
 * @date 2017/11/10
 */
@Controller
@Fresh
public class TestController {
    @Inject("testServiceImpl2")
    private TestService testService;

    @Action(value = "/hello", method = RequestMethod.GET)
    public String hello(@QueryParam("n") String name, int age, HttpServletRequest httpServletRequest, HttpSession session) {
        System.out.println(testService.hashCode());
        testService.hello();
        System.out.println(name);
        System.out.println(age);
        httpServletRequest.setAttribute("name", name);
        session.setAttribute("name", name);
        return "test";
    }

    @Action(value = "json")
    public String json(User user, String name, Long[] list, HttpServletRequest httpServletRequest, HttpSession session) {
        System.out.println(user.toString());
        System.out.println(name);
        System.out.println(JsonUtil.encodeJson(list));
        System.out.println(httpServletRequest);
        System.out.println(session);
        return "test";
    }

    @Action("a")
    @Internal
    public String a(String name, HttpServletRequest request) {
        System.out.println("a");
        request.setAttribute("a", name);
        return "b";
    }

    @Action("b")
    public String b(String name, HttpServletRequest request, HttpSession session) {
        System.out.println("b");
        request.setAttribute("b", name);
        session.setAttribute("b", name);
        return "/internal";
    }

    @Action("blank")
    public String blank(){
        return "test";
    }
}
