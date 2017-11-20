package star.controller;

import star.annotation.*;
import star.constant.RequestMethod;
import star.service.TestService;

/**
 * @author keshawn
 * @date 2017/11/10
 */
@Controller
@Fresh
@Action("test")
public class TestController {
    @Inject("testServiceImpl2")
    private TestService testService;

    @Action(value = "/hello", method = RequestMethod.GET)
    public void hello(@QueryParam("n") String name, int age) {
        System.out.println(testService.hashCode());
        testService.hello();
        System.out.println(name);
        System.out.println(age);
    }
}
