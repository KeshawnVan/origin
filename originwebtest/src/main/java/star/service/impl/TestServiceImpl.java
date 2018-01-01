package star.service.impl;

import com.google.common.collect.Lists;
import star.annotation.Inject;
import star.annotation.Service;
import star.bean.User;
import star.dao.UserRepository;
import star.service.TestService;
import star.utils.JsonUtil;

import java.util.List;

/**
 * @author keshawn
 * @date 2017/11/10
 */
@Service
public class TestServiceImpl implements TestService {

    @Inject
    private UserRepository userRepository;

    private String s = "asda";

    public void hello() {
        System.out.println(JsonUtil.encodeJson(userRepository.findByNameInAndAge(Lists.newArrayList("liuna","na"),23)));
    }

    @Override
    public List<User> findByNamesAndAge() {
        return userRepository.findByNameInAndAge(Lists.newArrayList("liuna","na"),23);
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }
}
