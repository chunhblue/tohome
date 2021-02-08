package cn.com.bbut.iy.itemmaster.service;

import java.util.Collection;

/**
 * 共通服务
 * 
 * @author songxz
 */
public interface CommonService {

    /**
     * 取得指定 sequence的下一个值，并补位
     * 
     * @param sequenceName
     *            sequence 名 慎用！有sql恶意注入的危险，
     * @param pattern
     *            补多少位0 有几位写几个0
     * @return
     */
    String getSequenceNext(String sequenceName, String pattern);

    /**
     * 取得当前登录人的session中的角色id集合
     * 
     * @return
     */
    Collection<Integer> getSessionUserRoleIds();
}
