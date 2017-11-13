package star.controller;

import star.annotation.Controller;
import star.annotation.Fresh;
import star.annotation.Inject;
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

    public void hello() {
        System.out.println(testService.hashCode());
        testService.hello();
    }
}
