package star.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * @author keshawn
 * @date 2017/11/13
 */
public final class YamlUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(YamlUtil.class);

    private YamlUtil() {
    }

    public static <T> T getYamlBeanWithRelativePath(String path, Class<T> yamlBeanClass) {
        return getYamlBean(Thread.currentThread().getContextClassLoader().getResourceAsStream(path), yamlBeanClass);
    }

    public static <T> T getYamlBeanWithAbsolutePath(String path, Class<T> yamlBeanClass) {
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            LOGGER.error("cannot find file", e);
        }
        return getYamlBean(inputStream, yamlBeanClass);
    }

    private static <T> T getYamlBean(InputStream inputStream, Class<T> yamlBeanClass) {
        Yaml yaml = new Yaml();
        T yamlBean = null;
        try {
            yamlBean = yaml.loadAs(inputStream, yamlBeanClass);
        } catch (Exception e) {
            LOGGER.error("yaml config file not found , " + e);
        }
        return yamlBean;
    }
}
