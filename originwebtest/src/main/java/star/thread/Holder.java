package star.thread;

import star.utils.PropertiesUtil;

import java.util.Properties;

public class Holder {
    public static final String TEST_PROPERTIES = "test.properties";
    private static Properties properties = PropertiesUtil.loadProperties(TEST_PROPERTIES);

    public static String getString() {
        System.out.println(properties);
        return "";
    }
}
