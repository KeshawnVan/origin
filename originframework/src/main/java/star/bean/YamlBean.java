package star.bean;

import java.util.List;
import java.util.Map;

/**
 * @author keshawn
 * @date 2017/11/13
 */
public class YamlBean {
    private String jdbcDriver;
    private String jdbcUrl;
    private String jdbcUsername;
    private String jdbcPassword;

    private String basePackage;
    private String jspPath;
    private String assetPath;

    private String autoCast;

    private List<Map<String, String>> beanIdMapping;
    private List<Map<String, String>> implementMapping;

    public YamlBean() {
    }

    public YamlBean(String jdbcDriver, String jdbcUrl, String jdbcUsername, String jdbcPassword, String basePackage, String jspPath, String assetPath, String autoCast, List<Map<String, String>> beanIdMapping, List<Map<String, String>> implementMapping) {
        this.jdbcDriver = jdbcDriver;
        this.jdbcUrl = jdbcUrl;
        this.jdbcUsername = jdbcUsername;
        this.jdbcPassword = jdbcPassword;
        this.basePackage = basePackage;
        this.jspPath = jspPath;
        this.assetPath = assetPath;
        this.autoCast = autoCast;
        this.beanIdMapping = beanIdMapping;
        this.implementMapping = implementMapping;
    }

    public String getJdbcDriver() {
        return jdbcDriver;
    }

    public void setJdbcDriver(String jdbcDriver) {
        this.jdbcDriver = jdbcDriver;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public String getJdbcUsername() {
        return jdbcUsername;
    }

    public void setJdbcUsername(String jdbcUsername) {
        this.jdbcUsername = jdbcUsername;
    }

    public String getJdbcPassword() {
        return jdbcPassword;
    }

    public void setJdbcPassword(String jdbcPassword) {
        this.jdbcPassword = jdbcPassword;
    }

    public String getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public String getJspPath() {
        return jspPath;
    }

    public void setJspPath(String jspPath) {
        this.jspPath = jspPath;
    }

    public String getAssetPath() {
        return assetPath;
    }

    public void setAssetPath(String assetPath) {
        this.assetPath = assetPath;
    }

    public String getAutoCast() {
        return autoCast;
    }

    public void setAutoCast(String autoCast) {
        this.autoCast = autoCast;
    }

    public List<Map<String, String>> getBeanIdMapping() {
        return beanIdMapping;
    }

    public void setBeanIdMapping(List<Map<String, String>> beanIdMapping) {
        this.beanIdMapping = beanIdMapping;
    }

    public List<Map<String, String>> getImplementMapping() {
        return implementMapping;
    }

    public void setImplementMapping(List<Map<String, String>> implementMapping) {
        this.implementMapping = implementMapping;
    }
}
