package cn.com.bbut.iy.itemmaster.constant;

import java.io.Serializable;

/**
 * DB常量类
 * 
 * @author lilw
 */
public class ConstantsDB implements Serializable {

    private static final long serialVersionUID = 1L;
    /** 全局-公司 编码 */
    public static final String COMPANY_CODE = "095";
    /** 常量 通用值 0 */
    public static final int COMMON_ZERO = 0;
    /** 常量 通用值 1 */
    public static final int COMMON_ONE = 1;
    /** 常量 通用值 2 */
    public static final int COMMON_TWO = 2;
    /** 常量 通用值 3 */
    public static final int COMMON_THREE = 3;
    /** 常量 通用值 4 */
    public static final int COMMON_FOUR = 4;

    /** 常量 通用值 9 */
    public static final int COMMON_NINE = 9;
    /** 全局-公司 编码 */
    public static final String ALL_DPT = "999";
    /** 自店铺的标识 ***** */
    public static final String SELF_STORE = "*****";
    public static final String SELF_STORE_NAME = "自店铺";
    /** 全店铺的标识 99999 */
    public static final String ALL_STORE = "99999";
    public static final String ALL_STORE_NAME = "全店铺";

    // ------order 类型
    /** 01捆绑类型 **/
    public static final String BM_TYPE_01 = "01";
    /** 02混合类型 **/
    public static final String BM_TYPE_02 = "02";
    /** 03固定组合类型 **/
    public static final String BM_TYPE_03 = "03";
    /** 04阶梯折扣类型 **/
    public static final String BM_TYPE_04 = "04";
    /** 05AB组类型 **/
    public static final String BM_TYPE_05 = "05";
    /** 序列 **/
    public static final String IY_ITEM_M_CK_SEQ = "IY_ITEM_M_CK_SEQ";
    /** 序列 **/
    public static final String NEW_NO_LENGTH = "0000000000";
}
