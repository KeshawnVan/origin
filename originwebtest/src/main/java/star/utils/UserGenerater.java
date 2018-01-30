package star.utils;

import star.bean.User;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class UserGenerater {

    private static final PodamFactory PODAM_FACTORY = new PodamFactoryImpl();

    public static List<User> generateUserList(){
        return Stream.generate(() -> JsonUtil.decodeJson(JsonUtil.encodeJson(PODAM_FACTORY.manufacturePojo(User.class)),User.class))
                .limit(20).collect(Collectors.toList());
    }
}
