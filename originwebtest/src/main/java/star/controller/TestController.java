package star.controller;

import star.annotation.Action;
import star.annotation.Controller;
import star.annotation.Fresh;
import star.annotation.Inject;
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
    public void hello() {
        System.out.println(testService.hashCode());
        testService.hello();
    }
}
