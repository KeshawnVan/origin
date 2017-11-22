package star.controller;

import star.annotation.*;
import star.bean.User;
import star.constant.RequestMethod;
import star.service.TestService;

/**
 * @author keshawn
 * @date 2017/11/10
 */
@Controller
@Fresh
public class TestController {
    @Inject("testServiceImpl2")
    private TestService testService;

    @Action(value = "/hello")
    public void hello(@QueryParam("n") String name, int age) {
        System.out.println(testService.hashCode());
        testService.hello();
        System.out.println(name);
        System.out.println(age);
    }
    @Action(value = "json")
    public void json(User user){
        System.out.println(user.toString());
    }
}
