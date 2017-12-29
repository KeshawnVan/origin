package star.repository.generator;

import star.repository.SqlGenerator;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static star.constant.RepositoryConstant.*;

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
    public String generate(Method method, ConcurrentHashMap<String, String> sqlMap, String tableName, String selectAllColumns, Object[] params) {
        String methodName = method.getName();

        //先判断是否是默认方法
        if (methodName.equals(FIND_BY_ID)) {
            return getFindByIdSql(sqlMap, tableName, selectAllColumns);
        }
        if (methodName.equals(FIND_ALL)) {
            return getFindAllSql(sqlMap, tableName, selectAllColumns);
        }
        if (methodName.equals(FIND_BY_IDS)) {
            return getFindByIdsSQL(sqlMap, tableName, selectAllColumns, params);
        }
        return null;
    }

    /**
     * 生成通用方法findById的SQL，并在SQL_MAP中做缓存
     *
     * @param sqlMap
     * @param tableName
     * @param selectAllColumns
     * @return
     */
    private String getFindByIdSql(ConcurrentHashMap<String, String> sqlMap, String tableName, String selectAllColumns) {
        String findByIdSQL;
        if (sqlMap.containsKey(FIND_BY_ID)) {
            findByIdSQL = sqlMap.get(FIND_BY_ID);
        } else {
            findByIdSQL = SELECT + selectAllColumns + FROM + tableName + WHERE + ID + EQUALS + PLACEHOLDER;
            sqlMap.put(FIND_BY_ID, findByIdSQL);
        }
        return findByIdSQL;
    }

    /**
     * 生成通用方法findAll的SQL，并在SQL_MAP中做缓存
     *
     * @param sqlMap
     * @param tableName
     * @param selectAllColumns
     * @return
     */
    private String getFindAllSql(ConcurrentHashMap<String, String> sqlMap, String tableName, String selectAllColumns) {
        String findAllSQL;
        if (sqlMap.containsKey(FIND_ALL)) {
            findAllSQL = sqlMap.get(FIND_ALL);
        } else {
            findAllSQL = SELECT + selectAllColumns + FROM + tableName;
            sqlMap.put(FIND_ALL, findAllSQL);
        }
        return findAllSQL;
    }

    /**
     * 生成通用方法findByIds的SQL，并在SQL_MAP中做缓存
     *
     * @param sqlMap
     * @param tableName
     * @param selectAllColumns
     * @param params
     * @return
     */
    private String getFindByIdsSQL(ConcurrentHashMap<String, String> sqlMap, String tableName, String selectAllColumns, Object[] params) {
        String findByIdsSQL;
        if (sqlMap.containsKey(FIND_BY_IDS)) {
            findByIdsSQL = sqlMap.get(FIND_BY_IDS);
        } else {
            Collection param = (Collection) params[0];
            String joinPlaceHolder = Stream.generate(() -> PLACEHOLDER).limit(param.size()).collect(Collectors.joining(DELIMITER));
            String bracketPlaceHolder = LEFT_BRACKET + joinPlaceHolder + RIGHT_BRACKET;
            findByIdsSQL = SELECT + selectAllColumns + FROM + tableName + WHERE + ID + IN + bracketPlaceHolder;
            sqlMap.put(FIND_BY_IDS, findByIdsSQL);
        }
        return findByIdsSQL;
    }
}
