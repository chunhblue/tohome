package cn.com.bbut.iy.itemmaster.constant;

import java.io.Serializable;

/**
 * songxz
 * 
 * @author songxz
 * @date: 2019年9月19日 - 下午3:38:43
 */
public class ConstantsCache implements Serializable {

    private static final long serialVersionUID = 1L;
    /** 用户名对应的SessionID，Key:userid,value:SessionId */
    public static final String CAHCE_SESSION_ID = "cacheSessionId";

	public static final String CACHE_MENU = "menuCache";

    public static final String CACHE_REVIEW = "reviewCache";

    public static final String CACHE_DEP = "depCache";

    public static final String CACHE_PMA = "pmaCache";

    public static final String CACHE_CATEGORY = "categoryCache";

    public static final String CACHE_SUB_CATEGORY = "subCategoryCache";
}
