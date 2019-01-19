package other;

import java.io.Serializable;
import java.time.Instant;


public class ReceiveMessageDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long companyId;

    private String content;

    private String jsonContent;

    private Long messageEquipmentId;

    private Instant receiveInstant;

    private String recipient;

    private String sender;


    private String title;


    private String shortCode;

    private String transId;

    /**
     * 短信接收时获得的时间格式为String，生成Json时还不能获取公司id，无法确定时区，先存成String再手动转成Instant保存到receiveInstant字段
     */
    private String receiveInstantDate;

    public ReceiveMessageDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReceiveInstantDate() {
        return receiveInstantDate;
    }

    public void setReceiveInstantDate(String receiveInstantDate) {
        this.receiveInstantDate = receiveInstantDate;
    }

    public String getJsonContent() {
        return jsonContent;
    }

    public void setJsonContent(String jsonContent) {
        this.jsonContent = jsonContent;
    }

    public Long getMessageEquipmentId() {
        return messageEquipmentId;
    }

    public void setMessageEquipmentId(Long messageEquipmentId) {
        this.messageEquipmentId = messageEquipmentId;
    }

    public Instant getReceiveInstant() {
        return receiveInstant;
    }

    public void setReceiveInstant(Instant receiveInstant) {
        this.receiveInstant = receiveInstant;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }
}