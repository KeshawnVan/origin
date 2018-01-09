package star.service.impl;

import com.google.common.collect.Lists;
import star.annotation.bean.Inject;
import star.annotation.bean.Service;
import star.bean.User;
import star.dao.UserRepository;
import star.service.TestService;
import star.utils.JsonUtil;
import star.utils.PojoManufactureUtil;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
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
        System.out.println(JsonUtil.encodeJson(userRepository.findByNameInAndAge(Lists.newArrayList("a","na"),23)));
    }

    @Override
    public List<User> findByNamesAndAge() {
        Method findByNamesAndAge = null;
        try {
            findByNamesAndAge = TestServiceImpl.class.getMethod("findByNamesAndAge");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
//        Type type = findByNamesAndAge.getGenericReturnType();
        return userRepository.findByNameInAndAge(Lists.newArrayList("liuna","na"),23);
//        return (List<User>) PojoManufactureUtil.manufacture(type);
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }
}
