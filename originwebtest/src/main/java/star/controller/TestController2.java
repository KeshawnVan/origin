package star.controller;

import star.annotation.Action;
import star.annotation.Controller;

/**
 * @author keshawn
 * @date 2017/11/17
 */
@Controller
public class TestController2 {
    @Action("2")
    public void test(){
        System.out.println("world");
    }
}
