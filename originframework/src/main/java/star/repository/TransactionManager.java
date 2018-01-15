package star.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import star.repository.factory.ConnectionFactory;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author keshawn
 * @date 2018/1/15
 */
public final class TransactionManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionManager.class);

    private static final ThreadLocal<Connection> CONNECTION_THREAD_LOCAL = ConnectionFactory.getConnectionThreadLocal();

    private TransactionManager() {
    }

    public static void begin() {
        Connection connection = ConnectionFactory.getConnection();
        if (connection != null) {
            try {
                connection.setAutoCommit(false);
                connection.setReadOnly(false);
            } catch (SQLException e) {
                LOGGER.error("begin transaction failure", e);
                throw new RuntimeException(e);
            } finally {
                CONNECTION_THREAD_LOCAL.set(connection);
            }
        }
    }

    public static void commit() {
        Connection connection = ConnectionFactory.getConnection();
        if (connection != null) {
            try {
                connection.commit();
                connection.close();
            } catch (SQLException e) {
                LOGGER.error("commit transaction failure", e);
                throw new RuntimeException(e);
            } finally {
                CONNECTION_THREAD_LOCAL.remove();
            }
        }
    }

    public static void rollback() {
        Connection connection = ConnectionFactory.getConnection();
        try {
            connection.rollback();
            connection.close();
        } catch (SQLException e) {
            LOGGER.error("rollback transaction failure", e);
            throw new RuntimeException(e);
        } finally {
            CONNECTION_THREAD_LOCAL.remove();
        }
    }

    public static void readOnly() {
        Connection connection = ConnectionFactory.getConnection();
        if (connection != null) {
            try {
                connection.setReadOnly(true);
            } catch (SQLException e) {
                LOGGER.error("set read only failure", e);
                throw new RuntimeException(e);
            } finally {
                CONNECTION_THREAD_LOCAL.set(connection);
            }
        }
    }
}
