package star.ioc;

import star.annotation.bean.Service;

@Service
public class BeanA implements TestBean {
    public void hello(){
        System.out.println("I am BeanA");
    }
}
