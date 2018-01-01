package star.repository;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static star.constant.RepositoryConstant.*;
import static star.utils.CastUtil.castString;
import static star.utils.StringUtil.generateBracketPlaceHolder;

/**
 * Created by fankaixiang on 2018/1/1.
 */
public final class MethodNameParser {

    private MethodNameParser() {
    }


    public static String generateConditionSqlByMethodName(String sqlPrefix, Object[] params, Map<String, String> fieldMap, String methodName) {
        StringBuilder stringBuilder = new StringBuilder(sqlPrefix);
        String querySentence = StringUtils.substringAfter(methodName, BY);
        String[] queryParams = querySentence.split(SEPARATOR);
        List<String> queryFields = buildQueryFields(queryParams);
        for (int i = 0; i < queryFields.size(); i++) {
            String queryField = queryFields.get(i);
            if (i != 0) {
                stringBuilder.append(AND);
            }
            queryTypeMatch(params, fieldMap, stringBuilder, i, queryField);
        }
        return stringBuilder.toString();
    }

    private static void queryTypeMatch(Object[] params, Map<String, String> fieldMap, StringBuilder stringBuilder, int i, String queryField) {
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

    private static List<String> buildQueryFields(String[] queryParams) {
        return Lists.newArrayList(queryParams).stream()
                .map(queryParam -> queryParam.replaceFirst(castString(queryParam.charAt(0)), castString(queryParam.charAt(0)).toLowerCase()))
                .collect(Collectors.toList());
    }

    private static void append(Map<String, String> fieldMap, StringBuilder stringBuilder, String queryField, String suffix, String conjunction) {
        String fieldName = StringUtils.substringBeforeLast(queryField, suffix);
        String columnName = fieldMap.get(fieldName);
        stringBuilder.append(columnName).append(conjunction).append(PLACEHOLDER);
    }
}
