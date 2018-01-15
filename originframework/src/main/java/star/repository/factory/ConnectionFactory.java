package star.repository.factory;

import com.alibaba.druid.pool.DruidDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import star.factory.ConfigFactory;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author keshawn
 * @date 2017/12/25
 */
public class ConnectionFactory {

    private static final String JDBC_DRIVER = ConfigFactory.getJdbcDriver();
    private static final String JDBC_URL = ConfigFactory.getJdbcUrl();
    private static final String JDBC_USERNAME = ConfigFactory.getJdbcUsername();
    private static final String JDBC_PASSWORD = ConfigFactory.getJdbcPassword();
    private static final Integer MAX_ACTIVE = ConfigFactory.getMaxActive();
    private static final Integer MIN_IDLE = ConfigFactory.getMinIdle();
    private static final Integer INITIAL_SIZE = ConfigFactory.getInitialSize();

    private static final DruidDataSource druidDataSource = new DruidDataSource();

    private final static ThreadLocal<Connection> CONNECTION_THREAD_LOCAL = new ThreadLocal<>();

    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionFactory.class);

    static {
        try {
            Class.forName(JDBC_DRIVER);

            druidDataSource.setUrl(JDBC_URL);
            druidDataSource.setUsername(JDBC_USERNAME);
            druidDataSource.setPassword(JDBC_PASSWORD);
            druidDataSource.setMaxActive(MAX_ACTIVE);
            druidDataSource.setMinIdle(MIN_IDLE);
            druidDataSource.setInitialSize(INITIAL_SIZE);
            druidDataSource.setDefaultReadOnly(Boolean.FALSE);
        } catch (ClassNotFoundException e) {
            LOGGER.error("JDBC Driver error", e);
        }
    }

    private ConnectionFactory() {
    }

    public static Connection getConnection() {
        Connection connection = CONNECTION_THREAD_LOCAL.get();
        synchronized (ConnectionFactory.class) {
            if (connection == null) {
                try {
                    connection = druidDataSource.getConnection();
                } catch (SQLException e) {
                    LOGGER.error("get jdbc connection error", e);
                } finally {
                    CONNECTION_THREAD_LOCAL.set(connection);
                }
            }
            return connection;
        }
    }

    public static void closeConnection() {
        Connection connection = CONNECTION_THREAD_LOCAL.get();
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                LOGGER.error("jdbc connection close error", e);
            } finally {
                CONNECTION_THREAD_LOCAL.remove();
            }
        }
    }

    public static ThreadLocal<Connection> getConnectionThreadLocal() {
        return CONNECTION_THREAD_LOCAL;
    }
}
