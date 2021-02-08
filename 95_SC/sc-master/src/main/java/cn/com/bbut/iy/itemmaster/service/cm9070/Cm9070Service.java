package cn.com.bbut.iy.itemmaster.service.cm9070;

import cn.com.bbut.iy.itemmaster.dto.cm9070.Cm9070ReturnDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.UnknownHostException;
import java.util.Date;

public interface Cm9070Service {
    /**
     * @Description:取得采番流水号
     * @param session
     * @param request
     * @param businessCd
     * @param asType 单号类型
     * @param adtNrDate 日结日期
     * @param asCustomerFix 用户定制字符串
     * @return Cm9070ReturnDTO 返回类型
     */
    public Cm9070ReturnDTO Get(HttpSession session, HttpServletRequest request, String businessCd,
                               String asType, Date adtNrDate, String asCustomerFix)throws UnknownHostException;;
}
