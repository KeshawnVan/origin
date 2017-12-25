package star.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import star.bean.YamlBean;
import star.constant.ConfigConstant;
import star.utils.ClassUtil;
import star.utils.CollectionUtil;
import star.utils.StringUtil;
import star.utils.YamlUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author keshawn
 * @date 2017/11/8
 */
public final class ConfigFactory {

    private static final YamlBean YAML_BEAN = YamlUtil.getYamlBean(ConfigConstant.CONFIG_FILE, YamlBean.class);

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigFactory.class);

    private static final String ID = "id";

    private static final String BEAN = "bean";

    private static final String INTERFACE = "interface";

    private static final String BEAN_ID = "beanId";

    private static final String BACKLASH = "/";

    public static String getJdbcDriver() {
        return YAML_BEAN.getJdbcDriver();
    }

    public static String getJdbcUrl() {
        return YAML_BEAN.getJdbcUrl();
    }

    public static String getJdbcUsername() {
        return YAML_BEAN.getJdbcUsername();
    }

    public static String getJdbcPassword() {
        return YAML_BEAN.getJdbcPassword();
    }

    public static String getAppBasePackage() {
        return YAML_BEAN.getBasePackage();
    }

    public static String getAutoCast() {
        return YAML_BEAN.getAutoCast();
    }

    public static String getAppJspPath() {
        String jspPath = YAML_BEAN.getJspPath();
        return jspPath.endsWith(BACKLASH) ? StringUtil.removeLast(jspPath) : jspPath;
    }

    public static String getAppAssetPath() {
        String assetPath = YAML_BEAN.getAssetPath();
        return assetPath.endsWith(BACKLASH) ? StringUtil.removeLast(assetPath) : assetPath;
    }

    public static Map<String, String> getBeanIdMapping() {
        Map<String, String> yamlBeanIdMapping = new HashMap<>(ConfigConstant.INITIAL_CAPACITY);
        List<Map<String, String>> beanIdMappingList = YAML_BEAN.getBeanIdMapping();
        if (CollectionUtil.isNotEmpty(beanIdMappingList)) {
            beanIdMappingList.forEach(convertBeanIdMapping(yamlBeanIdMapping));
        }
        return yamlBeanIdMapping;
    }

    private static Consumer<Map<String, String>> convertBeanIdMapping(Map<String, String> yamlBeanIdMapping) {
        return map -> {
            String beanId = map.get(ID);
            String beanName = map.get(BEAN);
            if (yamlBeanIdMapping.containsKey(beanName)) {
                LOGGER.error("beanName : " + beanName + " is repeat define beanId");
                throw new RuntimeException("beanName : " + beanName + " is repeat define beanId");
            } else {
                yamlBeanIdMapping.put(beanName, beanId);
            }
        };
    }

    public static Map<Class<?>, String> getImplementMapping() {
        Map<Class<?>, String> yamlImplementMapping = new HashMap<>(ConfigConstant.INITIAL_CAPACITY);
        List<Map<String, String>> implementMappingList = YAML_BEAN.getImplementMapping();
        if (CollectionUtil.isNotEmpty(implementMappingList)) {
            implementMappingList.forEach(convertImplementMapping(yamlImplementMapping));
        }
        return yamlImplementMapping;
    }

    private static Consumer<Map<String, String>> convertImplementMapping(Map<Class<?>, String> yamlImplementMapping) {
        return map -> {
            String interfaceName = map.get(INTERFACE);
            String beanId = map.get(BEAN_ID);
            yamlImplementMapping.put(ClassUtil.loadClass(interfaceName, false), beanId);
        };
    }
}
