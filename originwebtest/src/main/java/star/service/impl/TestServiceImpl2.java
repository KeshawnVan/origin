package star.service.impl;

import star.annotation.Fresh;
import star.annotation.Service;
import star.bean.User;
import star.service.TestService;

import java.util.List;

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

    @Override
    public List<User> findByNamesAndAge() {
        return null;
    }
}
