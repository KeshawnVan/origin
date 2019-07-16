package star.bean;

/**
 * 托收状态
 */
public enum CollectionStatus {
    /**
     * 未托收
     */
    UNDEBITED,
    /**
     * 托收中
     */
    DEBITTING,
    /**
     * 托收成功
     */
    DEBITSUCCESS,
    /**
     * 托收失败
     */
    DEBITFAILED,
    /**
     * 缴费回退（处理unpaid的文件）
     */
    PAYMENTRETUEN;

    public static CollectionStatus valueOf(int ordinal) {
        CollectionStatus[] values = CollectionStatus.values();
        if (ordinal >= values.length) {
            return null;
        } else {
            return values[ordinal];
        }
    }

}
