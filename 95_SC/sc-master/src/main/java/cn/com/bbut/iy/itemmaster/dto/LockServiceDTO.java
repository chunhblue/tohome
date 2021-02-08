package cn.com.bbut.iy.itemmaster.dto;

public class LockServiceDTO {
    private String lockKey;

    private String businessCd = "";

    private String sessionId = "";

    private String userId = "";

    private String apServerName = "";

    private String lockDate = "";

    private String webAplName = "";

    private String clientIp = "";

    private String attributes = "";

    /**
     * @return 获取参数 lockKey 的值
     */
    public String getLockKey() {
        return lockKey;
    }

    /**
     * @param 将参数 lockKey 的值赋给属性 lockKey
     */
    public void setLockKey(String lockKey) {
        this.lockKey = lockKey;
    }

    /**
     * @return 获取参数 businessCd 的值
     */
    public String getBusinessCd() {
        return businessCd;
    }

    /**
     * @param 将参数 businessCd 的值赋给属性 businessCd
     */
    public void setBusinessCd(String businessCd) {
        this.businessCd = businessCd;
    }

    /**
     * @return 获取参数 sessionId 的值
     */
    public String getSessionId() {
        return sessionId;
    }

    /**
     * @param 将参数 sessionId 的值赋给属性 sessionId
     */
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    /**
     * @return 获取参数 userId 的值
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param 将参数 userId 的值赋给属性 userId
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return 获取参数 apServerName 的值
     */
    public String getApServerName() {
        return apServerName;
    }

    /**
     * @param 将参数 apServerName 的值赋给属性 apServerName
     */
    public void setApServerName(String apServerName) {
        this.apServerName = apServerName;
    }

    /**
     * @return 获取参数 lockDate 的值
     */
    public String getLockDate() {
        return lockDate;
    }

    /**
     * @param 将参数 lockDate 的值赋给属性 lockDate
     */
    public void setLockDate(String lockDate) {
        this.lockDate = lockDate;
    }

    /**
     * @return 获取参数 webAplName 的值
     */
    public String getWebAplName() {
        return webAplName;
    }

    /**
     * @param 将参数 webAplName 的值赋给属性 webAplName
     */
    public void setWebAplName(String webAplName) {
        this.webAplName = webAplName;
    }

    /**
     * @return 获取参数 clientIp 的值
     */
    public String getClientIp() {
        return clientIp;
    }

    /**
     * @param 将参数 clientIp 的值赋给属性 clientIp
     */
    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    /**
     * @return 获取参数 attributes 的值
     */
    public String getAttributes() {
        return attributes;
    }

    /**
     * @param 将参数 attributes 的值赋给属性 attributes
     */
    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }
}
