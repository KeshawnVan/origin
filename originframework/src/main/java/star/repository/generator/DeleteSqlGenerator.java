package star.repository.generator;

import star.repository.SqlGenerator;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author keshawn
 * @date 2017/12/29
 */
public class DeleteSqlGenerator implements SqlGenerator {

    private static final DeleteSqlGenerator instance = new DeleteSqlGenerator();

    private DeleteSqlGenerator() {
    }

    public static DeleteSqlGenerator getInstance() {
        return instance;
    }

    @Override
    public String generate(Method method, ConcurrentHashMap<String, String> sqlMap, String tableName, String selectAllColumns, Object[] params) {
        return null;
    }
}
