package star.repository.interfaces;

import java.sql.SQLException;

/**
 * @author keshawn
 * @date 2017/12/28
 */
@FunctionalInterface
public interface ResultSetGetFunction<T, R> {
    R getObject(T t) throws SQLException;
}
