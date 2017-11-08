package star.helper;

import star.constant.ConfigConstant;
import star.utils.ProPertiesUtil;

import java.util.Properties;

/**
 * @author keshawn
 * @date 2017/11/8
 */
public final class ConfigHelper {

    private static final Properties CONFIG_PROPERTIES = ProPertiesUtil.loadProperties(ConfigConstant.CONFIG_FILE);

    public static String getJdbcDriver() {
        return ProPertiesUtil.getString(CONFIG_PROPERTIES, ConfigConstant.JDBC_DRIVER);
    }

    public static String getJdbcUrl() {
        return ProPertiesUtil.getString(CONFIG_PROPERTIES, ConfigConstant.JDBC_URL);
    }

    public static String getJdbcUsername() {
        return ProPertiesUtil.getString(CONFIG_PROPERTIES, ConfigConstant.JDBC_USERNAME);
    }

    public static String getJdbcPassword() {
        return ProPertiesUtil.getString(CONFIG_PROPERTIES, ConfigConstant.JDBC_PASSWORD);
    }

    public static String getAppBasePackage() {
        return ProPertiesUtil.getString(CONFIG_PROPERTIES, ConfigConstant.APP_BASE_PACKAGE);
    }

    public static String getAppJspPath() {
        return ProPertiesUtil.getString(CONFIG_PROPERTIES, ConfigConstant.APP_JSP_PATH);
    }

    public static String getAppAssetPath() {
        return ProPertiesUtil.getString(CONFIG_PROPERTIES, ConfigConstant.APP_ASSET_PATH);
    }
}
