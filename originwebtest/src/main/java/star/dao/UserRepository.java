package star.dao;

import star.bean.User;
import star.repository.CommonRepository;

import java.util.List;

/**
 * @author keshawn
 * @date 2017/12/25
 */
public interface UserRepository extends CommonRepository<User,Long> {
    List<User> findByName(String name);
}
