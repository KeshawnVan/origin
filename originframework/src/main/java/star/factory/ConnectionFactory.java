package star.factory;

import com.alibaba.druid.pool.DruidDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static final DruidDataSource druidDataSource = new DruidDataSource();

    private static ThreadLocal<Connection> connectionThreadLocal = new ThreadLocal<>();

    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionFactory.class);

    static {
        try {
            Class.forName(JDBC_DRIVER);

            druidDataSource.setUrl(JDBC_URL);
            druidDataSource.setUsername(JDBC_USERNAME);
            druidDataSource.setPassword(JDBC_PASSWORD);

        } catch (ClassNotFoundException e) {
            LOGGER.error("JDBC Driver error", e);
        }
    }

    public static Connection getConnection() {
        Connection connection = connectionThreadLocal.get();
        synchronized (ConnectionFactory.class) {
            if (connection == null) {
                try {
                    connection = druidDataSource.getConnection();
                } catch (SQLException e) {
                    LOGGER.error("get jdbc connection error", e);
                } finally {
                    connectionThreadLocal.set(connection);
                }
            }
            return connection;
        }
    }

    public static void closeConnection() {
        Connection connection = connectionThreadLocal.get();
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                LOGGER.error("jdbc connection close error", e);
            } finally {
                connectionThreadLocal.remove();
            }
        }
    }
}
