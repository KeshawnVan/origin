# origin

## 简介
这是一个参考Spring和Smart Framework的Java框架，架构如下图所示：

![avatar](https://github.com/KeshawnVan/origin/blob/master/originframework/src/main/resources/image/origin.png?raw=true)

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
* 通过tomcat发布该项目，接着访问：http://localhost:8080/${applicationName}/hello
* 页面会返回"welcome use origin !"

## 控制反转
控制反转（Inversion of Control，缩写为IoC），是面向对象编程中的一种设计原则，可以用来减低计算机代码之间的耦合度。其中最常见的方式叫做依赖注入（Dependency Injection，简称DI），还有一种方式叫“依赖查找”（Dependency Lookup）。通过控制反转，对象在被创建的时候，由一个调控系统内所有对象的外界实体，将其所依赖的对象的引用传递给它。也可以说，依赖被注入到对象中。

origin使用依赖注入来实现控制反转，用户在使用origin时需要指定基本包路径来描述需要被托管的有效范围

在Class定义上使用@Controller，@Service，@Component注解标明将该类托管给origin容器，容器内的对象默认为单例，如果需要每次注入都使用新对象可以使用@Fresh注解

使用@Inject可以实现依赖注入，下面是一个简单的例子
~~~
package star.ioc;

import star.annotation.bean.Component;

@Component
public class BeanA {
    public void hello(){
        System.out.println("I am BeanA");
    }
}

package star.ioc;

import star.annotation.bean.Component;
import star.annotation.bean.Inject;
import star.core.LoadCore;
import star.factory.BeanFactory;

@Component
public class BeanB {

    @Inject
    private BeanA beanA;

    public void test() {
        beanA.hello();
    }

    public static void main(String[] args) {
        LoadCore.init();
        BeanB beanB = BeanFactory.getBean(BeanB.class);
        beanB.test();
    }
}
~~~
使用LoadCore.init()可以初始化容器，这里可以看到BeanB使用IOC拿到了BeanA的对象，最后输出：I am BeanA

在实际开发中，很多时候是基于接口编程的，那么origin是否支持基于接口的依赖注入？

下面改造一下之前的用例来演示一下origin基于接口的注入

首先创建一个接口TestBean
~~~
public interface TestBean {
    void hello();
}
~~~
让刚才的BeanA实现接口TestBean，注解修改为@Service，origin这里只支持service使用接口依赖注入
~~~
@Service
public class BeanA implements TestBean {
    public void hello(){
        System.out.println("I am BeanA");
    }
}
~~~
BeanB中使用接口TestBean注入
~~~
@Component
public class BeanB {

    @Inject
    private TestBean beanA;

    public void test() {
        beanA.hello();
    }

    public static void main(String[] args) {
        LoadCore.init();
        BeanB beanB = BeanFactory.getBean(BeanB.class);
        beanB.test();
    }
}
~~~
如果同一个接口存在多个实现类并且注入时未指定beanId时会抛出异常

使用@Inject(${beanId})即可指定对应的实现类进行注入

bean的默认beanId为Class名首字母变小写，如果需要手动指定beanId，可以在声明bean时在注解内填写beanId，如@Service("beanId")

如果不想修改之后重新编译或者习惯使用配置文件可以按照以下规则进行配置，配置文件内的声明优先于注解
~~~
beanIdMapping:
  - id: test123
    bean: star.service.impl.TestServiceImpl
  - id: test321
    bean: star.service.impl.TestServiceImpl2

implementMapping:
  - interface: star.service.TestService
    beanId: test123
~~~

