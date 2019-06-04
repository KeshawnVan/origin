package star.service.impl;

import star.annotation.bean.Fresh;
import star.annotation.bean.Service;
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
        findByNamesAndAge();
    }

    @Override
    public List<User> findByNamesAndAge() {
        System.out.println("findByNamesAndAge");
        in();
        return null;
    }

    public void in() {
        System.out.println("in");
    }

    @Override
    public Integer updateUser(User user) {
        return null;
    }
}
