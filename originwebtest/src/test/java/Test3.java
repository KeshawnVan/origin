import org.junit.Test;
import star.bean.User;
import star.bean.UserDTO;
import star.core.LoadCore;
import star.dao.UserRepository;
import star.factory.BeanFactory;
import star.factory.ConnectionFactory;
import star.utils.JsonUtil;
import star.utils.PojoManufactureUtil;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.sql.Connection;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

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

    @Test
    public void testSave()throws Exception{
        PodamFactory podamFactory = new PodamFactoryImpl();
        LoadCore.init();
        UserRepository userRepository = BeanFactory.getBean(UserRepository.class);
        List<User> userList = JsonUtil.decodeArrayJson(JsonUtil.encodeJson(podamFactory.manufacturePojo(List.class, User.class)), User.class);
        System.out.println(userList.size());
        long s = System.currentTimeMillis();
        userList.forEach(user -> {
            user.setId(null);
            userRepository.save(user);
        });
        long e = System.currentTimeMillis();
        System.out.println(e - s);
        Connection connection = ConnectionFactory.getConnection();
        connection.commit();
        ConnectionFactory.closeConnection();
    }

    @Test
    public  void testUpdate()throws Exception{
        LoadCore.init();
        UserRepository userRepository = BeanFactory.getBean(UserRepository.class);
        User u = userRepository.findById(14L);
        u.setName("fffffff");
        Integer update = userRepository.update(u);
        System.out.println(update);
        Connection connection = ConnectionFactory.getConnection();
        connection.commit();
        ConnectionFactory.closeConnection();
    }
}
