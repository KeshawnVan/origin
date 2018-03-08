import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.junit.Test;
import star.bean.Source;
import star.bean.Target;
import star.bean.User;
import star.bean.UserDTO;
import star.core.LoadCore;
import star.dao.UserRepository;
import star.factory.BeanFactory;
import star.repository.factory.ConnectionFactory;
import star.utils.BeanUtil;
import star.utils.JsonUtil;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.*;
import java.util.function.Supplier;

/**
 * @author keshawn
 * @date 2018/1/11
 */
public class Test3 {
    @Test
    public void test() throws Exception {
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
    public void testComponent() {
        LoadCore.init();
        UserDTO userDTO = BeanFactory.getBean(UserDTO.class);
        System.out.println(JsonUtil.encodeJson(userDTO));
    }

    @Test
    public void testSave() throws Exception {
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
    public void testUpdate() throws Exception {
        LoadCore.init();
        UserRepository userRepository = BeanFactory.getBean(UserRepository.class);
        User u = userRepository.findById(14L);
        u.setName("fffffff");
        Integer update = userRepository.update(u);
        System.out.println(update);
    }

    @Test
    public void testLambda() {
        Supplier supplier = new Supplier() {
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
    public void testRetain() {
        ArrayList<Long> con = Lists.newArrayList(2L);
        ArrayList<Long> longs = Lists.newArrayList(1L, 3L);
        con.retainAll(longs);
        System.out.println(con);
    }

    @Test
    public void testList() {
        List<String> strings = new ArrayList<>();
        int size = strings.size();
        System.out.println(size);
    }

    @Test
    public void testStream() {
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

    @Test
    public void testFun() {

    }

    @Test
    public void testReturnMap() throws Exception {
        String sql = "select * from user";
        Connection connection = ConnectionFactory.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        int rowCount = 0;
        List<Map<String, Object>> results = new LinkedList<>();
        while (resultSet.next()) {
            rowCount++;
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            Map<String, Object> result = new HashMap<>(columnCount);
            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnName(i);
                String columnClassName = metaData.getColumnClassName(i);
                Object columnValue = resultSet.getObject(i);
                System.out.println(columnClassName);
                result.put(columnName, columnValue);
            }
            results.add(result);
            if (rowCount > 5000) break;
        }
        ConnectionFactory.closeConnection();
        results.forEach(System.out::println);
    }

    @Test
    public void testTry() throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new StringReader("sss"))) {
            String line = bufferedReader.readLine();
            System.out.println(line);
        }
    }

    @Test
    public void debugParams(){
        LoadCore.init();
        UserRepository userRepository = BeanFactory.getBean(UserRepository.class);
        Map<String,Object> params = new HashMap<>();
        params.put("name","fkx");
        params.put("age",22);
        List<User> users = userRepository.findBySql(params);
        System.out.println(JsonUtil.encodeJson(users));
    }

    @Test
    public void testCl(){
        Source source = new Source();
        source.setId(1L);
        source.setName("nana");
        source.setLists("[1,2,3]");
        source.setSs(2);
        Target target = BeanUtil.copyProperties(source, Target.class);
        System.out.println(JsonUtil.encodeJson(target));
    }
}
