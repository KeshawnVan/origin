package star.repository.generator;

import star.annotation.Query;
import star.repository.RepositoryManager;
import star.repository.SqlGenerator;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static star.constant.RepositoryConstant.*;
import static star.repository.MethodNameParser.generateConditionSqlByMethodName;

/**
 * @author keshawn
 * @date 2017/12/29
 */
public final class QuerySqlGenerator implements SqlGenerator {

    private static final QuerySqlGenerator instance = new QuerySqlGenerator();

    private QuerySqlGenerator() {
    }

    public static QuerySqlGenerator getInstance() {
        return instance;
    }

    @Override
    public String generate(Method method, ConcurrentHashMap<String, String> sqlMap, String tableName, String selectAllColumns, Object[] params, Map<String, String> fieldMap) {

        //先判断是否是默认方法
        if (method.getName().equals(FIND_ALL)) {
            return SELECT + selectAllColumns + FROM + tableName;
        }
        //如果带有Query注解，使用自定义SQL生成器
        if (method.isAnnotationPresent(Query.class)) {
            return CustomSqlGenerator.getInstance().generate(method, sqlMap, tableName, selectAllColumns, params, fieldMap);
        } else {
            String sqlPrefix = SELECT + selectAllColumns + FROM + tableName + WHERE;
            return generateConditionSqlByMethodName(sqlPrefix, params, fieldMap, method.getName());
        }
    }
}
