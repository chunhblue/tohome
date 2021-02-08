package cn.com.bbut.iy.itemmaster.dto.base;


/**
 * AJAX前台请求的返回对象
 *
 */
public class ReturnDTO{

    /**
     * 默认构造函数 success = false msg = "error" o = ""
     */
    public ReturnDTO() {
        this.success = false;
        this.msg = "error";
        this.o = "";
    }

    /**
     * 是否更新成功的构造方法
     *
     * @param success
     *            是否成功
     * @param msg
     *            消息
     */
    public ReturnDTO(boolean success, Object msg) {
        this.success = success;
        this.msg = msg;
        this.o = "";
    }

    /**
     * 是否更新成功的构造方法
     *
     * @param success
     *            是否成功
     * @param msg
     *            消息
     * @param other
     *            其他对象
     */
    public ReturnDTO(boolean success, Object msg, Object other) {
        this.success = success;
        this.msg = msg;
        this.o = other;
}

    /**
     * 异常时的构造方法
     *
     * @param errormsg
     *            异常消息
     */
    public ReturnDTO(Object errormsg) {
        // 异常情况
        this.success = false;
        this.msg = errormsg;
        this.o = "";
    }

    /**
     * 是否成功
     */
    protected boolean success;
    /**
     * 返回消息
     */
    protected Object msg;
    /**
     * 其他对象
     */
    protected Object o;

    protected String date;
    protected String hms;
    protected String createUserId;
    /**
     * @return 是否成功
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * @param success
     *            是否成功
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * @return 返回消息
     */
    public Object getMsg() {
        if ("error".equals(this.msg)) {
            this.msg = this.success ? "success" : "error";
        }
        return msg;
    }

    /**
     * @param msg
     *            返回消息
     */
    public void setMsg(Object msg) {
        this.msg = msg;
    }

    /**
     * @return 其他对象
     */
    public Object getO() {
        return o;
    }
    public String  getDate() {
        return date;
    }
    public String  getHms() {
        return hms;
    }
    public String  getCreateUserId() {
        return createUserId;
    }
    /**
     * @param o
     *            其他对象
     */
    public void setO(Object o) {
        this.o = o;
    }
    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }
    public  void setDate(String date){
        this.date = date;
    }
    public  void setHms(String hms){
        this.hms = hms;
    }
}
