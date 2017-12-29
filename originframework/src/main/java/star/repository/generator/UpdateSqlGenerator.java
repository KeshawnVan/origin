package star.repository.generator;

import star.repository.SqlGenerator;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author keshawn
 * @date 2017/12/29
 */
public class UpdateSqlGenerator implements SqlGenerator {

    private static final UpdateSqlGenerator instance = new UpdateSqlGenerator();

    private UpdateSqlGenerator() {
    }

    public static UpdateSqlGenerator getInstance() {
        return instance;
    }

    @Override
    public String generate(Method method, ConcurrentHashMap<String, String> sqlMap, String tableName, String selectAllColumns, Object[] params, Map<String, String> fieldMap) {
        return null;
    }
}
