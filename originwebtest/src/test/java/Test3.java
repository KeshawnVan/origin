import com.google.common.collect.Lists;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.BeanUtilsBean2;
import org.junit.Test;
import star.bean.User;
import star.bean.UserDTO;
import star.core.LoadCore;
import star.factory.BeanFactory;
import star.utils.JsonUtil;
import star.utils.PojoManufactureUtil;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author keshawn
 * @date 2018/1/11
 */
public class Test3 {
    @Test
    public void test()throws Exception{
//        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
//        User user = (User)PojoManufactureUtil.manufacture(User.class);
//        mapperFactory.classMap(User.class, UserDTO.class).byDefault().register();
//        MapperFacade mapperFacade = mapperFactory.getMapperFacade();
//        UserDTO userDTO = mapperFacade.map(user, UserDTO.class);
//        System.out.println(JsonUtil.encodeJson(userDTO));
//        UserDTO dto = new UserDTO();
//        BeanUtils.copyProperties(dto,user);
//        System.out.println(JsonUtil.encodeJson(dto));
    }

    @Test
    public void testComponent(){
        LoadCore.init();
        UserDTO userDTO = BeanFactory.getBean(UserDTO.class);
        System.out.println(JsonUtil.encodeJson(userDTO));
    }
}
