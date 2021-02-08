package cn.com.bbut.iy.itemmaster.service;

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.UnknownHostException;

@Service
public interface ExclusiveService {
    /**
     * 不排他
     */
    public static int LOCK_NONE = 0;

    /**
     * 功能排他
     */
    public static int LOCK_FUNCTION = 1;

    /**
     * 业务排他
     */
    public static int LOCK_BUSSINESS = 2;

    /**
     * 独立排他
     */
    public static int LOCK_ONLYONE = 3;

    /**
     * 数据排他
     */
    public static int LOCK_DATA = 4;


    /**
     * @Description: 功能排他加锁
     * @param session
     * @param request 页面请求
     * @param businessCd 业务ID(界面ID)
     * @param businessKey 业务主键(根据业务字段拼接而成)
     * @return boolean true→加锁成功；false→加锁失败(被其他锁住)
     * @throws UnknownHostException
     */
    public boolean LockFunction(HttpSession session,
                                HttpServletRequest request, String businessCd,String businessKey) throws UnknownHostException;

    /**
     * @Description: 功能排他解锁
     * @param session
     * @param request 页面请求
     * @param businessCd 业务ID(界面ID)
     * @param businessKey 业务主键(根据业务字段拼接而成)
     * @return boolean true→解锁成功；false→解锁失败(异常)
     */
    public boolean UnloakFunction(HttpSession session,
                                  HttpServletRequest request, String businessCd,String businessKey);

    /**
     * @Description:数据排他加锁
     * @param session
     * @param request 页面请求
     * @param businessCd 业务ID(界面ID)
     * @param businessKey 业务主键(根据业务字段拼接而成)
     * @param tableId 表ID
     * @param dataId 数据ID
     * @return boolean true→加锁成功；false→加锁失败(被其他锁住)
     * @throws UnknownHostException
     */
    public boolean LockData(HttpSession session, HttpServletRequest request,
                            String businessCd,String businessKey, String tableId, String dataId) throws UnknownHostException;

    /**
     * @Description:数据排他解锁
     * @param session
     * @param request 页面请求
     * @param businessCd 业务ID(界面ID)
     * @param businessKey 业务主键(根据业务字段拼接而成)
     * @param tableId 表ID
     * @param dataId 数据ID
     * @return boolean true→解锁成功；false→解锁失败(异常)
     */
    public boolean UnLockData(HttpSession session, HttpServletRequest request,
                              String businessCd, String businessKey, String tableId, String dataId);

    /**
     *
     * @Description:session超时自动解锁
     * @param sessionId
     * @return boolean true→解锁成功；false→解锁失败(异常)
     */
    public boolean UnlockSession(String sessionId);
}
