package star.service.impl;

import star.annotation.Service;
import star.service.TestService;

/**
 * @author keshawn
 * @date 2017/11/10
 */
@Service
public class TestServiceImpl implements TestService {

    public void hello() {
        System.out.println("hello origin ioc");
    }
}
