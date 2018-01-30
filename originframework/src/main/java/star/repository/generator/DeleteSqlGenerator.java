package star.repository.generator;

import star.repository.interfaces.SqlGenerator;

import java.lang.reflect.Method;
import java.util.Map;

import static star.constant.RepositoryConstant.*;
import static star.repository.parser.MethodNameParser.generateConditionSqlByMethodName;

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
    public String generate(Method method, String tableName, String selectAllColumns, Object[] params, Map<String, String> fieldMap) {
        String deletePrefix = DELETE + FROM + tableName + WHERE;
        return generateConditionSqlByMethodName(deletePrefix, params, fieldMap, method.getName());
    }
}
