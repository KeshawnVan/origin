package star.repository.generator;

import star.repository.SqlGenerator;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author keshawn
 * @date 2017/12/29
 */
public class SaveSqlGenerator implements SqlGenerator {

    private static final SaveSqlGenerator instance = new SaveSqlGenerator();

    private SaveSqlGenerator() {
    }

    public static SaveSqlGenerator getInstance() {
        return instance;
    }

    @Override
    public String generate(Method method, ConcurrentHashMap<String, String> sqlMap, String tableName, String selectAllColumns, Object[] params) {
        return null;
    }
}
