package star.repository.generator;

import star.repository.SqlGenerator;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static star.constant.RepositoryConstant.DELETE;
import static star.constant.RepositoryConstant.FROM;
import static star.constant.RepositoryConstant.WHERE;
import static star.repository.MethodNameParser.generateConditionSqlByMethodName;

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
    public String generate(Method method, ConcurrentHashMap<String, String> sqlMap, String tableName, String selectAllColumns, Object[] params, Map<String, String> fieldMap) {
        String deletePrefix = DELETE + FROM + tableName + WHERE;
        return generateConditionSqlByMethodName(deletePrefix, params, fieldMap, method.getName());
    }
}
