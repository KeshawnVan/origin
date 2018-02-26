package star.dao;

import star.annotation.repository.Query;
import star.bean.User;
import star.repository.interfaces.CommonRepository;

import java.util.List;
import java.util.Map;

/**
 * @author keshawn
 * @date 2017/12/25
 */
public interface UserRepository extends CommonRepository<User, Long> {
    List<User> findByName(String name);

    List<User> findByNameInAndAge(List<String> names, Integer age);

    @Query("select * from user where name = #{name} and age = #{age}")
    List<User> findBySql(Map<String, Object> params);
}
