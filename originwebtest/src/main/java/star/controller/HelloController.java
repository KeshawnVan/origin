package star.controller;

import star.annotation.bean.Controller;
import star.annotation.controller.Action;
import star.annotation.controller.Stream;

@Controller
public class HelloController {

    @Stream
    @Action("hello")
    public String hello() {
        return "welcome use origin !";
    }
}
