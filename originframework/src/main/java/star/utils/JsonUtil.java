package star.utils;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author keshawn
 * @date 2017/11/17
 */
public final class JsonUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtil.class);

    public static <T> String encodeJson(T object) {
        String json;
        try {
            json = JSON.toJSONString(object);
        } catch (Exception e) {
            LOGGER.error("convert POJO to JSON failure", e);
            throw new RuntimeException(e);
        }
        return json;
    }

    public static <T> T decodeJson(String json, Class<T> type) {
        T pojo;
        try {
            pojo = JSON.parseObject(json, type);
        } catch (Exception e) {
            LOGGER.error("convert JSON to POJO failure", e);
            throw new RuntimeException(e);
        }
        return pojo;
    }
}
