/**
 * ClassName  BaseAction
 *
 * History
 * Create User: Shiy
 * Create Date: 2014-09-23
 * Update User:
 * Update Date:
 */
package cn.com.bbut.iy.itemmaster.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;

import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.dto.AjaxResultDto;
import cn.com.bbut.iy.itemmaster.dto.SessionACL;
import cn.com.bbut.iy.itemmaster.entity.User;

/**
 * 
 * @author Shiy
 */
@Slf4j
public class BaseAction {

    /** 操作类型 */
    public static enum Action {
        /** 查看 */
        VIEW(1),
        /** 转excel */
        EXCEL(2),
        /** 打印 */
        PRINT(3);
        private int flg;

        private Action(int flag) {
            this.flg = flag;
        }

        public int getFlag() {
            return this.flg;
        }
    }

    /**
     * 对应nginx转发的ip
     * 
     * @return
     */
    protected static String getRemoteIP(HttpServletRequest request) {
        String ipx = request.getHeader("X-Forwarded-For");
        if (ipx != null && ipx.length() > 0 && ipx.indexOf(",") > 0) {
            ipx = ipx.substring(0, ipx.indexOf(","));
        }
        // log.debug("X-Forwarded-For:{}", ipx);
        return StringUtils.isNotBlank(ipx) ? ipx : request.getRemoteAddr();
    }

    /**
     * 获取当前用户信息
     *
     * @param session
     * @return
     */
    public User getUser(HttpSession session) {
        SessionACL acl = ((SessionACL) session.getAttribute(Constants.SESSION_USER));
        return acl == null ? null : acl.getUser();
    }

    // /**
    // * 获取当前用户的权限信息
    // *
    // * @param session
    // * @param busiCode
    // * 业务code
    // * @return
    // */
    // public PermissionResource getPermissionResource(HttpSession session,
    // String busiCode) {
    // SessionACL acl = ((SessionACL)
    // session.getAttribute(Constants.SESSION_USER));
    // return acl == null ? null : acl.getPermissionResource(busiCode);
    // }

    /**
     * 获取当前用户id
     *
     * @param session
     * @return userid或""（未登录时）
     */
    public String getUserId(HttpSession session) {
        User user = getUser(session);
        return user == null ? null : user.getUserId();
    }

    // /**
    // * 验证是否重复提交并删除token
    // *
    // * @see ReSubmitChkInterceptor#isReSubmit(HttpServletRequest)
    // * @param request
    // * @return
    // */
    // protected boolean isReSubmit(HttpServletRequest request) {
    // return ReSubmitChkInterceptor.isReSubmit(request);
    // }

    /**
     * 验证标记是否有效
     * 
     * @author HanHaiyun
     * @param request
     * @return true:有效；false:无效
     */
    protected boolean isTokenValid(HttpServletRequest request) {
        return isTokenValid(request, true);
    }

    /**
     * 验证标记是否有效
     * 
     * @author HanHaiyun
     * @param request
     * @param reset
     * @return true:有效；false:无效
     */
    protected boolean isTokenValid(HttpServletRequest request, boolean reset) {
        HttpSession session = request.getSession();
        // 获取request中传递过来的标记
        String toKen = (String) request.getParameter("toKen");
        // 获取session中的标记
        String sessionToKen = (String) session.getAttribute(toKen);
        boolean isToken = false;
        // 如果session存在request中的标记，则表明当前标记有效
        if (sessionToKen != null && sessionToKen.equals("true")) {
            isToken = true;
        }
        if (reset == false && toKen != null && sessionToKen == null) {
            // 将生成的标记保存在session
            session.setAttribute(toKen, "true");
            isToken = true;
        }
        return isToken;
    }

    /**
     * 清除当前标记
     * 
     * @author HanHaiyun
     * @param request
     */
    protected boolean resetToken(HttpServletRequest request) {
        HttpSession session = request.getSession();
        // 获取request中传递过来的标记
        String toKen = (String) request.getParameter("toKen");
        // 获取session中的标记
        String sessionToKen = (String) session.getAttribute(toKen);
        // 如果session存在request中的标记，则将session中的标记清除
        if (sessionToKen != null && sessionToKen.equals("true")) {
            request.setAttribute("toKen", toKen);
            session.removeAttribute(toKen);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 保存当前请求标记
     * 
     * @author HanHaiyun
     * @param request
     */
    protected void saveToken(HttpServletRequest request) {
        saveToken(request, false);
    }

    /**
     * 保存当前请求标记
     * 
     * @author HanHaiyun
     * @param request
     * @param reset
     */
    protected void saveToken(HttpServletRequest request, boolean reset) {
        HttpSession session = request.getSession();
        // 如果不存在标记，需重新生成标记
        if (!isTokenValid(request, reset)) {
            StringBuffer buf = new StringBuffer(
                    "a,b,c,d,e,f,g,h,i,g,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z");
            buf.append(",A,B,C,D,E,F,G,H,I,G,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z");
            buf.append(",1,2,3,4,5,6,7,8,9,0");
            String[] arr = buf.toString().split(",");
            // 随机标记码
            StringBuffer b = new StringBuffer();
            java.util.Random r;
            int k;
            for (int i = 0; i < 40; i++) {
                r = new java.util.Random();
                k = r.nextInt();
                b.append(String.valueOf(arr[Math.abs(k % 61)]));
            }
            // 将生成的标记保存在session
            session.setAttribute(b.toString(), "true");
            // 将生成的标记保存在request中
            request.setAttribute("toKen", b.toString());
        }
    }

    /**
     * Ajax重复提交验证
     * 
     * @author HanHaiyun
     * @return 返回success为true表示不是重复提交 ,为false则为重复提交
     */
    protected AjaxResultDto ajaxRepeatSubmitCheck(HttpServletRequest request, HttpSession session) {
        AjaxResultDto ard = repeatSubmitCheck(request, session);
        this.saveToken(request, true);
        ard.setToKen((String) request.getAttribute("toKen"));
        return ard;
    }

    /**
     * 重复提交验证
     * 
     * @author HanHaiyun
     * @return 返回success为true表示不是重复提交 ,为false则为重复提交
     */
    protected AjaxResultDto repeatSubmitCheck(HttpServletRequest request, HttpSession session) {
        AjaxResultDto ard = new AjaxResultDto();
        // 重复提交验证
        if (this.isTokenValid(request, true) && (this.resetToken(request))) {
            ard.setSuccess(true);
            ard.setRepeat(false);
        } else {
            log.debug("user:{},不能重复操作", "");
            ard.setSuccess(false);
            ard.setRepeat(true);
            ard.setMessage("Operation duplicated!");
        }
        return ard;
    }

    // /**
    // * log输出到数据库
    // *
    // * @param action
    // * 操作类型
    // */
    // protected void oplog(HttpServletRequest request, String busiName, Action
    // action) {
    // User user = this.getUser(request.getSession());
    // if (user == null) {
    // log.warn("not login but trigger logdb");
    // return;
    // }
    // String ip = getRemoteIP(request);
    // // OPLogService logService = Container.getBean(OPLogService.class);
    // // logService.log(user, busiName, ip, action);
    // }

}
