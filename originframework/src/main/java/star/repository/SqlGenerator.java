package star.repository;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static star.constant.RepositoryConstant.*;

/**
 * @author keshawn
 * @date 2017/12/28
 */
public final class SqlGenerator {
    private SqlGenerator() {
    }

    public static String getFindByIdSql(ConcurrentHashMap<String, String> sqlMap, String methodName, String tableName, String selectAllColumns) {
        String findByIdSQL;
        if (sqlMap.containsKey(methodName)) {
            findByIdSQL = sqlMap.get(methodName);
        } else {
            findByIdSQL = SELECT + selectAllColumns + FROM + tableName + WHERE + ID + EQUALS + PLACEHOLDER;
            sqlMap.put(methodName,findByIdSQL);
        }
        return findByIdSQL;
    }

    public static String getFindByIdsSQL(ConcurrentHashMap<String, String> sqlMap, String methodName, String tableName, String selectAllColumns,Object[] params) {
        String findByIdsSQL;
        if (sqlMap.containsKey(methodName)) {
            findByIdsSQL = sqlMap.get(methodName);
        } else {
            Collection param = (Collection) params[0];
            String bracketPlaceHolder = LEFT_BRACKET + Stream.generate(() -> PLACEHOLDER).limit(param.size()).collect(Collectors.joining(DELIMITER)) + RIGHT_BRACKET;
            findByIdsSQL = SELECT + selectAllColumns + FROM + tableName + WHERE + ID + IN + bracketPlaceHolder;
            sqlMap.put(methodName, findByIdsSQL);
        }
        return findByIdsSQL;
    }
}
