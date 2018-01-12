package star.repository;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import star.utils.DateUtil;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
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
     * 用户自定义SQL不应使用本方法
     * @param preparedStatement
     * @param params
     * @throws SQLException
     */
    public static void setPreparedStatement(PreparedStatement preparedStatement, Object[] params) throws SQLException {
        int num = 0;
        int paramLength = params.length;
        for (int i = 0; i < paramLength; i++) {
            Object param = params[i];
            if (param == null){
                num = setSingleObject(param,num,preparedStatement);
            }else {
                //param为方法参数，方法参数也有可能是集合
                if (Collection.class.isAssignableFrom(param.getClass())) {
                    for (Object value : (Collection) param) {
                        num = setSingleObject(value, num, preparedStatement);
                    }
                } else {
                    num = setSingleObject(param, num, preparedStatement);
                }
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
        if (paramClass == Instant.class){
            param = DateUtil.toTimestamp((Instant) param);
        }
        preparedStatement.setObject(num, param);
        return num;
    }
}
