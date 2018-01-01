package star.repository.generator;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import star.annotation.Query;
import star.repository.RepositoryManager;
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
        String argsMethodName = RepositoryManager.generateArgsMethodName(methodName, params);

        if (sqlMap.containsKey(argsMethodName)) {
            return argsMethodName;
        } else {
            String sql = generateSql(method, sqlMap, tableName, selectAllColumns, params, fieldMap);
            sqlMap.put(argsMethodName, sql);
            return sql;
        }
    }

    private String generateSql(Method method, ConcurrentHashMap<String, String> sqlMap, String tableName, String selectAllColumns, Object[] params, Map<String, String> fieldMap) {
        //先判断是否是默认方法
        if (method.getName().equals(FIND_ALL)) {
            return SELECT + selectAllColumns + FROM + tableName;
        }
        if (method.isAnnotationPresent(Query.class)) {
            return CustomSqlGenerator.getInstance().generate(method, sqlMap, tableName, selectAllColumns, params, fieldMap);
        } else {
            return generateSqlByMethodName(tableName, selectAllColumns, params, fieldMap, method.getName());
        }
    }

    private String generateSqlByMethodName(String tableName, String selectAllColumns, Object[] params, Map<String, String> fieldMap, String methodName) {
        String querySentence = StringUtils.substringAfter(methodName, BY);
        String[] queryParams = querySentence.split(SEPARATOR);
        List<String> queryFields = buildQueryFields(queryParams);
        StringBuilder stringBuilder = new StringBuilder(SELECT + selectAllColumns + FROM + tableName + WHERE);
        for (int i = 0; i < queryFields.size(); i++) {
            String queryField = queryFields.get(i);
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
        return stringBuilder.toString();
    }

    private List<String> buildQueryFields(String[] queryParams) {
        return Lists.newArrayList(queryParams).stream()
                    .map(queryParam -> queryParam.replaceFirst(castString(queryParam.charAt(0)), castString(queryParam.charAt(0)).toLowerCase()))
                    .collect(Collectors.toList());
    }

    private void append(Map<String, String> fieldMap, StringBuilder stringBuilder, String queryField, String suffix, String conjunction) {
        String fieldName = StringUtils.substringBeforeLast(queryField, suffix);
        String columnName = fieldMap.get(fieldName);
        stringBuilder.append(columnName).append(conjunction).append(PLACEHOLDER);
    }
}
