package star.repository.factory;

import star.repository.executor.*;
import star.repository.interfaces.SqlExecutor;

import static star.constant.RepositoryConstant.*;

/**
 * @author keshawn
 * @date 2017/12/29
 */
public final class SqlExecutorFactory {

    private SqlExecutorFactory() {
    }

    public static SqlExecutor getExecutor(String methodName) {
        if (methodName.startsWith(FIND) || methodName.startsWith(SELECT) || methodName.startsWith(QUERY)) {
            return QueryExecutor.getInstance();
        }
        if (methodName.startsWith(SAVE)) {
            return SaveExecutor.getInstance();
        }
        if (methodName.startsWith(UPDATE)) {
            return UpdateExecutor.getInstance();
        }
        if (methodName.startsWith(DELETE)) {
            return DeleteExecutor.getInstance();
        }
        return CustomExecutor.getInstance();
    }
}
