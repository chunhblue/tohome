package cn.com.bbut.iy.itemmaster.constant;

import java.io.Serializable;

/**
 * 评审 xml父类节点名称
 * 
 * @author songxz
 * @date: 2019年9月19日 - 下午3:38:43
 */
public class ConstantsReview implements Serializable {
    private static final long serialVersionUID = 1L;
    /** xml 父节点 */
    public static final String PARENT_NODE = "review";
    /** xml 子节点 */
    public static final String CHILD_NODE = "module";
    /** xml 参数名：步骤 */
    public static final String PROPERTY_STEP = "step";
    /** xml 参数名：权限许可 */
    public static final String PROPERTY_PERMISSION = "permission";
    /** xml 参数名：备注 */
    public static final String PROPERTY_REMARKS = "remarks";

    // -----------业务节点名称------------

    /** xml id */
    public static final String P_TEST = "PS_TEST";
}
