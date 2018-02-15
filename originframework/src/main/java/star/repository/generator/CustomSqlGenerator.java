package star.repository.generator;

import org.apache.commons.lang3.StringUtils;
import star.annotation.repository.Query;
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
        checkQuery(method);
        Query query = method.getAnnotation(Query.class);
        return query.value();
    }

    private void checkQuery(Method method) {
        if (!method.isAnnotationPresent(Query.class)) {
            throw new RuntimeException("cannot get query sql");
        }
    }
}
