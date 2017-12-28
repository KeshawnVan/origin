package star.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import star.utils.DateUtil;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;

/**
 * @author keshawn
 * @date 2017/12/28
 */
public final class PreparedStatementLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(PreparedStatementLoader.class);

    private PreparedStatementLoader() {
    }

    /**
     * 有序设置PreparedStatement，一般仅用于自动生成的查询方法
     *
     * @param preparedStatement
     * @param params
     * @throws SQLException
     */
    public static void setPreparedStatement(PreparedStatement preparedStatement, Object[] params) throws SQLException {
        int num = 0;
        int paramLength = params.length;
        for (int i = 0; i < paramLength; i++) {
            Object param = params[i];
            //param为方法参数，方法参数也有可能是集合
            if (Collection.class.isAssignableFrom(param.getClass())) {
                for (Object value : (Collection) param) {
                    num = setSingleObject(value, num, preparedStatement);
                }
            } else {
                setSingleObject(param, num, preparedStatement);
            }
        }
    }

    public static int setSingleObject(Object param, int num, PreparedStatement preparedStatement) throws SQLException {
        num++;
        if (param == null) {
            preparedStatement.setObject(num, null);
            return num;
        }
        Class<?> paramClass = param.getClass();
        if (paramClass.isEnum()) {
            param = ((Enum) param).ordinal();
        }
        if (paramClass == Date.class) {
            param = DateUtil.toSqlDate((Date) param);
        }
        preparedStatement.setObject(num, param);
        return num;
    }
}
