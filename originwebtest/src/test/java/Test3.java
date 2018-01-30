import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.junit.Test;
import star.bean.User;
import star.bean.UserDTO;
import star.core.LoadCore;
import star.dao.UserRepository;
import star.factory.BeanFactory;
import star.repository.factory.ConnectionFactory;
import star.utils.JsonUtil;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import javax.annotation.Nullable;
import java.sql.Connection;
import java.util.*;
import java.util.function.Supplier;

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
    }

    @Test
    public void testUpdate()throws Exception{
        LoadCore.init();
        UserRepository userRepository = BeanFactory.getBean(UserRepository.class);
        User u = userRepository.findById(14L);
        u.setName("fffffff");
        Integer update = userRepository.update(u);
        System.out.println(update);
    }

    @Test
    public void testLambda(){
        Supplier supplier = new Supplier(){
            @Override
            public Object get() {
                return new User();
            }
        };
        Supplier supplierLambda = () -> {
            return new User();
        };
        Supplier supplierFinally = () -> new User();
    }

    @Test
    public void testRetain(){
        ArrayList<Long> con = Lists.newArrayList(2L);
        ArrayList<Long> longs = Lists.newArrayList(1L, 3L);
        con.retainAll(longs);
        System.out.println(con);
    }

    @Test
    public void testList(){
        List<String> strings = new ArrayList<>();
        int size = strings.size();
        System.out.println(size);
    }

    @Test
    public void testStream(){
        OptionalInt min = Lists.newArrayList(1, 2, 3, 4, 5, 1).stream().mapToInt(Integer::intValue).min();
        min.ifPresent(m -> System.out.println(m));
        Optional<Integer> reduce = Lists.newArrayList(1, 2, 3, 4, 5, 1).stream().reduce(Integer::min);
        reduce.ifPresent(System.out::println);
        User user1 = new User();
        User user2 = new User();
        User user3 = new User();
        User user4 = new User();
        user1.setAge(1);
        user2.setAge(1);
        user3.setAge(2);
        user4.setAge(3);
        ArrayList<User> users = Lists.newArrayList(user1, user2, user3, user4);
        Optional<User> max = users.stream().min(Comparator.comparing(User::getAge));
        max.ifPresent(System.out::println);
        System.out.println("=====================================");
        Lists.newArrayList(1, 2, 3, 4, 5, 1).stream();
    }
}
