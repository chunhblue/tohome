package cn.com.bbut.iy.itemmaster.constant;

import java.io.Serializable;

/**
 * songxz
 * 
 * @author songxz
 * @date: 2019年9月19日 - 下午3:38:43
 */
public class Constants implements Serializable {

    private static final long serialVersionUID = 1L;
    /** 默认编码 */
    public static final String DEFAULT_ENCODING = "UTF-8";
    /** 未登录时设置httpheader的字段名 */
    public static final String SECURE_HEADER_NAME = "errStatus";
    /** 未登录时httpheader错误码：未登录 */
    public static final int ERR_CODE_NOTLOGIN = 2;

    public static final String REQ_HEADER = "/a";

    /** 没有权限时httpheader错误码：未登录 */
    public static final int ERR_CODE_NOPERMISSION = 3;

    /** 系统不在服务时间时httpheader错误码：未登录 */
    public static final int ERR_CODE_OUTOFSERVICE = 4;
    /** session中user对象名 */
    public static final String SESSION_USER = "IY_MASTER_USER";

    /** session中roleids对象名 */
    public static final String SESSION_ROLES = "IY_MASTER_ROLES";
    /** session key 菜单数据 */
    public static final String SESSION_MENUS = "IY_MASTER_MENUS";
    /** session key 资源数据 */
    public static final String SESSION_RESOURCES = "IY_MASTER_RESOURCES";
    /** session key 店铺数据 */
    public static final String SESSION_STORES = "IY_MASTER_STORES";
    /** session key 验证码 */
    public static final String S_CAPTCHA_CODE = "S_CAPTCHA_CODE";
    /**
     * 参数 errMsg
     */
    public static final String P_ERR_MSG = "errMsg";
    /**
     * 参数 code
     */
    public static final String P_CODE = "code";
    /**
     * 参数 userid
     */
    public static final String P_USER_ID = "userid";

    /** session key 显示验证码 */
    public static final String S_SHOW_CAPTCHA = "S_SHOWCAPTCHA";

    /** 附件下载 URL */
    public static final String ATTACHMENT_DOWNLOAD_URL = "/f/download/";

    /** PNG后缀 */
    public static final String L_PNG = "PNG";

    /** excel默认字体 */
    public static final String DEFAULT_FONT = "宋体";

    /** excel生成路径 */
    public static String FILE_DIR = "";

    /** excel下载 URL */
    public static final String EXPORT_DOWNLOAD_URL = "/f/down/";

    /** 生成excel文件默认的sheet名称 **/
    public static final String EXCEL_SHEET_DEF_NAME = "Data";

    /** 全系统禁用开始时间 HHmm */
    public static final String TIME_HALT_FROM = "1800";

    /** 全系统禁用截止时间 HHmm */
    public static final String TIME_HALT_TO = "2000";

    /** BM管理禁用开始时间 HHmm */
    public static final String TIME_BM_HALT_FROM = "1700";

    /** BM管理禁用截止时间 HHmm */
    public static final String TIME_BM_HALT_TO = "2359";

    /** 订货管理禁用开始时间 HHmm */
    public static final String TIME_ORDER_HALT_FROM = "1900";

    /** 订货管理禁用截止时间 HHmm */
    public static final String TIME_ORDER_HALT_TO = "0600";

    /** 订货管理禁用时间 是否跨天 true(跨天) false(当天) */
    public static final Boolean IS_NEXT_DAY = true;

    /** batch处理禁用开始时间 HHmm */
    public static final String TIME_BATCH_HALT_FROM = "2359";

    /** batch处理禁用截止时间 HHmm */
    public static final String TIME_BATCH_HALT_TO = "0600";

    /** batch管理禁用时间 是否跨天 true(跨天) false(当天) */
    public static final boolean BATCH_IS_NEXT_DAY = true;

}
