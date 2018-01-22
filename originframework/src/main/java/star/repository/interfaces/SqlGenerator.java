package star.repository.interfaces;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author keshawn
 * @date 2017/12/29
 */
public interface SqlGenerator {
    /**
     * 通过给定的参数生成SQL
     *
     * @return 生成的带占位符的SQL
     */
    String generate(Method method, ConcurrentHashMap<String, String> sqlMap, String tableName, String selectAllColumns, Object[] params, Map<String, String> fieldMap);
}
