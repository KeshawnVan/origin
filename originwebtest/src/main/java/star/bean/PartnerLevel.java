package star.bean;

/**
 * @author keshawn
 * @date 2017/12/12
 */
public enum PartnerLevel {
    /**
     * 国家级代理商（原总经销商）
     */
    SUP_DEALER(1),
    /**
     * 城市代理商
     */
    DEALER(2),
    /**
     * 区县代理商
     */
    SUB_DEALER(3),
    /**
     * 乡镇代理商
     */
    VILLAGE_DEALER(4);

    private int code;

    private PartnerLevel(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
