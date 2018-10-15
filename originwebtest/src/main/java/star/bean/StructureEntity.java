package star.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

public class StructureEntity {
    /**
     * 版本号
     */
    private static final long serialVersionUID = 7156421479382699779L;

    /**
     * 主键
     */
    private String id;

    /**
     * 节点路径
     */
    @JSONField(name = "元素路径")
    private String xpath;

    /**
     * 基数:0..*,1..1,..
     */
    private String cardinal;

    /**
     * 可选项,0(R),1(O)
     */
    @JSONField(name = "是否必填")
    private String required;

    /**
     * 节点描述
     */
    @JSONField(name = "说明与描述")
    private String description;

    /**
     * 对应数据元标识符
     */
    @JSONField(name = "对应的数据元标识符")
    private String identifier;

    /**
     * CDR表名
     */
    @JSONField(name = "CDR表名")
    private String cdrTableName;

    /**
     * CDR列名
     */
    @JSONField(name = "CDR字段")
    private String cdrColumnName;

    /**
     * 模板ID
     */
    private String templateId;

    /**
     * 节点为List时使用
     */
    private String pId;

    /**
     * 父组织
     */
    private StructureEntity parent;

    /**
     * 子组织
     */
    private List<StructureEntity> children;

    /**
     * 获取主键
     *
     * @return 主键
     */
    public String getId() {
        return this.id;
    }

    /**
     * 设置主键
     *
     * @param id 主键
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取节点路径
     *
     * @return 节点路径
     */
    public String getXpath() {
        return this.xpath;
    }

    /**
     * 设置节点路径
     *
     * @param xpath 节点路径
     */
    public void setXpath(String xpath) {
        this.xpath = xpath;
    }

    /**
     * 获取基数:0..*,1..1,..
     *
     * @return 基数:0..*
     */
    public String getCardinal() {
        return this.cardinal;
    }

    /**
     * 设置基数:0..*,1..1,..
     *
     * @param cardinal 基数:0..*
     */
    public void setCardinal(String cardinal) {
        this.cardinal = cardinal;
    }

    /**
     * 获取可选项,0(R),1(O)
     *
     * @return 可选项
     */
    public String getRequired() {
        return this.required;
    }

    /**
     * 设置可选项,0(R),1(O)
     *
     * @param required 可选项
     */
    public void setRequired(String required) {
        this.required = required;
    }

    /**
     * 获取节点描述
     *
     * @return 节点描述
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * 设置节点描述
     *
     * @param description 节点描述
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 获取对应数据元标识符
     *
     * @return 对应数据元标识符
     */
    public String getIdentifier() {
        return this.identifier;
    }

    /**
     * 设置对应数据元标识符
     *
     * @param identifier 对应数据元标识符
     */
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getCdrTableName() {
        return cdrTableName;
    }

    public void setCdrTableName(String cdrTableName) {
        this.cdrTableName = cdrTableName;
    }

    public String getCdrColumnName() {
        return cdrColumnName;
    }

    public void setCdrColumnName(String cdrColumnName) {
        this.cdrColumnName = cdrColumnName;
    }

    /**
     * 获取模板ID
     *
     * @return 模板ID
     */
    public String getTemplateId() {
        return this.templateId;
    }

    /**
     * 设置模板ID
     *
     * @param templateId 模板ID
     */
    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public List<StructureEntity> getChildren() {
        return children;
    }

    public void setChildren(List<StructureEntity> children) {
        this.children = children;
    }
}
