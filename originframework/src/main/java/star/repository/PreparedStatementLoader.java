package star.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author keshawn
 * @date 2017/12/28
 */
public final class PreparedStatementLoader {

    private PreparedStatementLoader() {
    }

    public static void setPreparedStatement(PreparedStatement preparedStatement, Object[] args) throws SQLException {
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            preparedStatement.setObject(i + 1, arg);
        }
    }
}
