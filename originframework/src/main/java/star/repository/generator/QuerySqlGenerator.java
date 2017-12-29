package star.repository.generator;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import star.annotation.Query;
import star.repository.SqlGenerator;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static star.constant.RepositoryConstant.*;
import static star.utils.CastUtil.castString;
import static star.utils.StringUtil.generateBracketPlaceHolder;

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
        String methodName = method.getName();

        //先判断是否是默认方法
        if (methodName.equals(FIND_ALL)) {
            return getFindAllSql(sqlMap, tableName, selectAllColumns);
        }
        if (method.isAnnotationPresent(Query.class)) {
            return CustomSqlGenerator.getInstance().generate(method, sqlMap, tableName, selectAllColumns, params, fieldMap);
        } else {
            String sql;
            if (sqlMap.containsKey(methodName)) {
                sql = sqlMap.get(methodName);
            } else {
                sql = generateSqlByMethodName(sqlMap, tableName, selectAllColumns, params, fieldMap, methodName);
            }
            return sql;
        }
    }

    private String generateSqlByMethodName(ConcurrentHashMap<String, String> sqlMap, String tableName, String selectAllColumns, Object[] params, Map<String, String> fieldMap, String methodName) {
        String sql;
        String querySentence = StringUtils.substringAfter(methodName, BY);
        String[] queryParams = querySentence.split(SEPARATOR);
        System.out.println(queryParams.length);
        List<String> queryFields = Lists.newArrayList(queryParams).stream()
                .map(queryParam -> queryParam.replaceFirst(castString(queryParam.charAt(0)), castString(queryParam.charAt(0)).toLowerCase()))
                .collect(Collectors.toList());
        StringBuilder stringBuilder = new StringBuilder(SELECT + selectAllColumns + FROM + tableName + WHERE);
        for (int i = 0; i < queryFields.size(); i++) {
            String queryField = queryFields.get(i);
            //改为判断where条件是否为空能否支持参数为空？
            if (i != 0) {
                stringBuilder.append(AND);
            }
            if (queryField.endsWith(BATCH_QUERY)) {
                String fieldName = StringUtils.substringBeforeLast(queryField, BATCH_QUERY);
                String columnName = fieldMap.get(fieldName);
                Collection param = (Collection) params[i];
                stringBuilder.append(columnName).append(IN).append(generateBracketPlaceHolder(param.size()));
            } else if (queryField.endsWith(ABSENT)) {
                String fieldName = StringUtils.substringBeforeLast(queryField, ABSENT);
                String columnName = fieldMap.get(fieldName);
                Collection param = (Collection) params[i];
                stringBuilder.append(columnName).append(NOT_IN).append(generateBracketPlaceHolder(param.size()));
            } else if (queryField.endsWith(FUZZY_QUERY)) {
                String fieldName = StringUtils.substringBeforeLast(queryField, FUZZY_QUERY);
                String columnName = fieldMap.get(fieldName);
                stringBuilder.append(columnName).append(LIKE).append(PERCENT).append(PLACEHOLDER).append(PERCENT);
            } else if (queryField.endsWith(LESS_THAN)) {
                append(fieldMap, stringBuilder, queryField, LESS_THAN, LESS);
            } else if (queryField.endsWith(LESS_THAN_EQUAL)) {
                append(fieldMap, stringBuilder, queryField, LESS_THAN_EQUAL, LESS_EQUAL);
            } else if (queryField.endsWith(GREATER_THAN)) {
                append(fieldMap, stringBuilder, queryField, GREATER_THAN, GREATER);
            } else if (queryField.endsWith(GREATER_THAN_EQUAL)) {
                append(fieldMap, stringBuilder, queryField, GREATER_THAN_EQUAL, GREATER_EQUAL);
            } else if (queryField.endsWith(NOT)) {
                append(fieldMap, stringBuilder, queryField, NOT, UNEQUAL);
            } else {
                String columnName = fieldMap.get(queryField);
                stringBuilder.append(columnName).append(EQUALS).append(PLACEHOLDER);
            }
        }
        sql = stringBuilder.toString();
        sqlMap.put(methodName, sql);
        return sql;
    }

    private void append(Map<String, String> fieldMap, StringBuilder stringBuilder, String queryField, String suffix, String conjunction) {
        String fieldName = StringUtils.substringBeforeLast(queryField, suffix);
        String columnName = fieldMap.get(fieldName);
        stringBuilder.append(columnName).append(conjunction).append(PLACEHOLDER);
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
}
