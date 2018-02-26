package star.repository.factory;

import star.annotation.repository.Query;
import star.repository.generator.*;
import star.repository.interfaces.SqlGenerator;

import java.lang.reflect.Method;

import static star.constant.RepositoryConstant.*;

/**
 * @author keshawn
 * @date 2017/12/29
 */
public final class SqlGeneratorFactory {

    private SqlGeneratorFactory() {
    }

    public static SqlGenerator getGenerator(Method method) {
        if (method.isAnnotationPresent(Query.class)) {
            return CustomSqlGenerator.getInstance();
        }
        String methodName = method.getName();
        if (methodName.startsWith(FIND) || methodName.startsWith(SELECT) || methodName.startsWith(QUERY)) {
            return QuerySqlGenerator.getInstance();
        }
        if (methodName.startsWith(SAVE)) {
            return SaveSqlGenerator.getInstance();
        }
        if (methodName.startsWith(UPDATE)) {
            return UpdateSqlGenerator.getInstance();
        }
        if (methodName.startsWith(DELETE)) {
            return DeleteSqlGenerator.getInstance();
        }
        return CustomSqlGenerator.getInstance();
    }
}
