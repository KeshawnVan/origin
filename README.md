# origin

## 简介
这是一个参考Spring和Smart Framework的Java框架，实现了一些常用功能

## 主要特性
* IOC
* AOP
* Repository
* Transaction
* DispatcherServlet

## 快速开始
下面将通过简单的代码展示如何创建一个简单的Web项目
* 首先需要引入origin的依赖
* 接着在resource目录下创建名为origin.yml的配置文件, 指定项目的基本路径
~~~
basePackage:项目的基本路径
~~~
* 在基本路径下创建HelloController

~~~
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
~~~
* 通过tomcat发布该项目，接着访问：http://localhost:8080/${application name}/hello
* 页面会返回"welcome use origin !"

## 控制反转

