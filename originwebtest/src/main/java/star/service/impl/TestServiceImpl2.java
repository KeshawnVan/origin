package star.service.impl;

import star.annotation.Fresh;
import star.annotation.Service;
import star.service.TestService;

/**
 * @author keshawn
 * @date 2017/11/10
 */
@Service
@Fresh
public class TestServiceImpl2 implements TestService {
    public void hello() {
        System.out.println("good");
    }
}
