package star.service;

import star.bean.User;

import java.util.List;

/**
 * @author keshawn
 * @date 2017/11/10
 */
public interface TestService {
    void hello();
    List<User> findByNamesAndAge();
    Integer updateUser(User user);
}
