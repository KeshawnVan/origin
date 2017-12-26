package star.bean;

import java.util.List;
import java.util.Map;

/**
 * @author keshawn
 * @date 2017/11/13
 */
public final class YamlBean {
    private String jdbcDriver;
    private String jdbcUrl;
    private String jdbcUsername;
    private String jdbcPassword;

    private String basePackage;
    private String jspPath;
    private String assetPath;

    private Boolean autoCast = Boolean.TRUE;

    private Map<String, Object> datasource;

    private List<Map<String, String>> beanIdMapping;
    private List<Map<String, String>> implementMapping;

    public YamlBean() {
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

    public Boolean getAutoCast() {
        return autoCast;
    }

    public void setAutoCast(Boolean autoCast) {
        this.autoCast = autoCast;
    }

    public Map<String, Object> getDatasource() {
        return datasource;
    }

    public void setDatasource(Map<String, Object> datasource) {
        this.datasource = datasource;
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
