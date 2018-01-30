package star.repository.generator;

import star.repository.interfaces.SqlGenerator;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author keshawn
 * @date 2017/12/29
 */
public class CustomSqlGenerator implements SqlGenerator {

    private static final CustomSqlGenerator instance = new CustomSqlGenerator();

    private CustomSqlGenerator() {
    }

    public static CustomSqlGenerator getInstance() {
        return instance;
    }

    @Override
    public String generate(Method method, String tableName, String selectAllColumns, Object[] params, Map<String, String> fieldMap) {
        return null;
    }
}
