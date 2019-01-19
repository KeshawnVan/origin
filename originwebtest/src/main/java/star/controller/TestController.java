package star.controller;

import com.google.common.collect.Lists;
import star.annotation.aop.Pure;
import star.annotation.bean.Controller;
import star.annotation.bean.Inject;
import star.annotation.controller.Action;
import star.annotation.controller.Internal;
import star.annotation.controller.QueryParam;
import star.annotation.controller.Stream;
import star.bean.User;
import star.constant.RequestMethod;
import star.service.TestService;
import star.utils.JsonUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.Instant;
import java.util.List;

/**
 * @author keshawn
 * @date 2017/11/10
 */
@Controller
public class TestController {
    @Inject
    private TestService testService;

    @Action(value = "/hello1", method = RequestMethod.GET)
    public String hello(@QueryParam("n") String name, Integer age, HttpServletRequest httpServletRequest, HttpSession session) {
        System.out.println(testService.hashCode());
        testService.hello();
        System.out.println(name);
        System.out.println(age);
        httpServletRequest.setAttribute("name", name);
        session.setAttribute("name", name);
        List<String> list = Lists.newArrayList("sasd","sadsa","aa");

        return "test";
    }

    @Action(value = "json")
    @Stream
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
        System.out.println(name);
        request.setAttribute("b", name);
        session.setAttribute("b", name);
        return "/internal";
    }

    @Action("blank")
    @Pure
    public String blank(){
        System.out.println("test");
        return "test";
    }

    @Action("h")
    @Stream
    public List<User> h(){
        return testService.findByNamesAndAge();
    }

    @Action("updateUser")
    @Stream
    public Integer updateUser(){
        List<User> byNamesAndAge = testService.findByNamesAndAge();
        User user = byNamesAndAge.get(0);
        user.setBirthday(Instant.now());
        return testService.updateUser(user);
    }
}
