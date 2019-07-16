package star.bean;

import java.time.Instant;

public class CollectionRecordDTO {

    private Long id;

    private int version;

    private Long createId;

    private Instant createInstant;

    private Long modifyId;

    private Instant modifyInstant;

    private String transactionId;

    private Long requestFileId;

    private Long resultFileId;

    private Long accountId;

    private String bankAccountCode;

    private String bankAccountName;

    private String certificateNo;

    private Long bankId;

    private Long bankBranchId;

    private String  branchCode;


    private Integer deductionDate;

    private Double amount;

    private Double actualAmount;

    private Long companyId;

    private CollectionStatus collectionStatus;

    private Long payProjectId;

    private CollectionObjectType objectType;

    private String desc;

    private String responseCode;

    private String responseMsg;

    private Long partnerId;

    private Status status;

    public Long getRequestFileId() {
        return requestFileId;
    }

    public void setRequestFileId(Long requestFileId) {
        this.requestFileId = requestFileId;
    }

    public Long getResultFileId() {
        return resultFileId;
    }

    public void setResultFileId(Long resultFileId) {
        this.resultFileId = resultFileId;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getBankAccountCode() {
        return bankAccountCode;
    }

    public void setBankAccountCode(String bankAccountCode) {
        this.bankAccountCode = bankAccountCode;
    }

    public String getBankAccountName() {
        return bankAccountName;
    }

    public void setBankAccountName(String bankAccountName) {
        this.bankAccountName = bankAccountName;
    }

    public String getCertificateNo() {
        return certificateNo;
    }

    public void setCertificateNo(String certificateNo) {
        this.certificateNo = certificateNo;
    }

    public Long getBankId() {
        return bankId;
    }

    public void setBankId(Long bankId) {
        this.bankId = bankId;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public Integer getDeductionDate() {
        return deductionDate;
    }

    public void setDeductionDate(Integer deductionDate) {
        this.deductionDate = deductionDate;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(Double actualAmount) {
        this.actualAmount = actualAmount;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public CollectionStatus getCollectionStatus() {
        return collectionStatus;
    }

    public void setCollectionStatus(CollectionStatus collectionStatus) {
        this.collectionStatus = collectionStatus;
    }

    public Long getBankBranchId() {
        return bankBranchId;
    }

    public void setBankBranchId(Long bankBranchId) {
        this.bankBranchId = bankBranchId;
    }

    public Long getPayProjectId() {
        return payProjectId;
    }

    public void setPayProjectId(Long payProjectId) {
        this.payProjectId = payProjectId;
    }

    public CollectionObjectType getObjectType() {
        return objectType;
    }

    public void setObjectType(CollectionObjectType objectType) {
        this.objectType = objectType;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMsg() {
        return responseMsg;
    }

    public void setResponseMsg(String responseMsg) {
        this.responseMsg = responseMsg;
    }

    public Long getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Long partnerId) {
        this.partnerId = partnerId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
