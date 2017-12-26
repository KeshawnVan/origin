package star.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * @author keshawn
 * @date 2017/11/13
 */
public final class YamlUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(YamlUtil.class);

    private YamlUtil() {
    }

    public static <T> T getYamlBean(String configPath, Class<T> yamlBeanClass){
        Yaml yaml = new Yaml();
        T yamlBean = null;
        try {
            yamlBean = yaml.loadAs(Thread.currentThread().getContextClassLoader().getResourceAsStream(configPath),yamlBeanClass);
        } catch (Exception e) {
            LOGGER.error("yaml config file not found , "+e);
        }
        return yamlBean;
    }
}
