package star.repository.executor;

import org.apache.commons.lang3.StringUtils;
import star.repository.PreparedStatementLoader;
import star.repository.factory.ConnectionFactory;
import star.repository.interfaces.SqlExecutor;
import star.repository.parser.ResultSetParser;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static star.repository.IndexValueBuilder.buildIndexValueMap;

/**
 * @author keshawn
 * @date 2017/12/29
 */
public class CustomExecutor implements SqlExecutor {

    private static final CustomExecutor instance = new CustomExecutor();
    private static final String REGEX = "\\#\\{([^\\\\}]+)\\}";
    private static final String REPLACEMENT = "?";
    private static final String OPEN = "#{";
    private static final String CLOSE = "}";
    private static final String SELECT = "select";

    private Map<String, Integer> fieldIndexMap;
    private String executeSql;

    private CustomExecutor() {
    }

    public static CustomExecutor getInstance() {
        return instance;
    }

    @Override
    public Object execute(String sql, Method method, Object[] params, List<Field> fields, Class<?> beanClass, Field idField) throws SQLException {
        init(sql);

        Map<Integer, Object> indexValueMap = buildIndexValueMap(params, fieldIndexMap);

        Connection connection = ConnectionFactory.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(executeSql);

        PreparedStatementLoader.setPreparedStatement(indexValueMap, preparedStatement);

        return sql.toLowerCase().startsWith(SELECT)
                ? ResultSetParser.parseResultSet(method, fields, beanClass, preparedStatement.executeQuery())
                : preparedStatement.executeUpdate();
    }

    private void init(String sql) {

        if (executeSql != null) {
            return;
        }

        String[] fieldNames = StringUtils.substringsBetween(sql, OPEN, CLOSE);
        int length = fieldNames.length;
        fieldIndexMap = new HashMap<>(length);

        for (int i = 0; i < length; i++) {
            String fieldName = fieldNames[i];
            fieldIndexMap.put(fieldName, i + 1);
        }

        executeSql = StringUtils.replaceAll(sql, REGEX, REPLACEMENT);
    }
}
