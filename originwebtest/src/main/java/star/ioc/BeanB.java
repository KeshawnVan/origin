package star.ioc;

import star.annotation.bean.Component;
import star.annotation.bean.Inject;
import star.core.LoadCore;
import star.factory.BeanFactory;

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
