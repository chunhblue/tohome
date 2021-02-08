package cn.com.bbut.iy.itemmaster.serviceimpl;

import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.dao.LockServiceMapper;
import cn.com.bbut.iy.itemmaster.dto.LockServiceDTO;
import cn.com.bbut.iy.itemmaster.dto.SessionACL;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.service.ExclusiveService;
import cn.com.bbut.iy.itemmaster.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Service
@Slf4j
public class ExclusiveServiceImpl implements ExclusiveService {
    @Autowired
    LockServiceMapper lockServiceMapper;

    /**
     *
     * @Description:获取排他key
     * @param session
     * @param request
     * @param businessCd
     *            业务功能id
     * @param businessKey
     *            业务主键
     * @return
     */
//	private String GetExclusiveKey(HttpSession session,
//			HttpServletRequest request, String businessCd, String businessKey) {
//		return this.GetExclusiveKey(session, request, businessCd, businessKey,
//				null, null);
//	}

    /**
     * 获取排他key 前10位为业务ID，中间30位为表ID，后面18位为rowid
     *
     * @Description:获取排他key
     * @param session
     * @param request
     * @param businessCd
     *            业务功能id
     * @param businessKey
     *            业务主键
     * @param tableId
     *            表id
     * @param dataId
     *            数据id(rowid)
     * @return
     */
    private String GetExclusiveKey(HttpSession session,
                                   HttpServletRequest request, String businessCd, String businessKey,
                                   String tableId, String dataId) {
        Assert.isTrue(StringUtil.IsBlank(businessCd) == false, "业务ID不能为空");
        /*
         * 排他key，默认为空字符串
         */
        String _exclusiveKey = StringUtil.EMPTY;
        _exclusiveKey = StringUtil.Rpad(businessCd, 20, '*');
        if(StringUtil.IsBlank(businessKey) == false)
        {
            _exclusiveKey += StringUtil.Rpad(businessKey, 100, '*');
        }
        if (StringUtil.IsBlank(tableId) == false
                && StringUtil.IsBlank(dataId) == false) {
            _exclusiveKey += "_" + StringUtil.Rpad(tableId, 30, '*');
            _exclusiveKey += "_" + StringUtil.Rpad(dataId, 18, '*');
        }
        return _exclusiveKey;
    }

    @Override
    public boolean LockFunction(HttpSession session,
                                HttpServletRequest request, String businessCd, String businessKey)
            throws UnknownHostException {
        return this.LockData(session, request, businessCd, businessKey, null,
                null);
    }

    @Override
    public boolean UnloakFunction(HttpSession session,
                                  HttpServletRequest request, String businessCd, String businessKey) {
        return this.UnLockData(session, request, businessCd, businessKey, null,
                null);
    }

    @Override
    public boolean LockData(HttpSession session, HttpServletRequest request,
                            String businessCd, String businessKey, String tableId, String dataId)
            throws UnknownHostException {
        try {
            String _exclusiveKey = this.GetExclusiveKey(session, request,
                    businessCd, businessKey, tableId, dataId);
            String _ip = InetAddress.getByName(request.getRemoteAddr())
                    .getHostAddress();
            String _name = InetAddress.getByName(request.getRemoteAddr())
                    .getHostName();
            if (((SessionACL) session.getAttribute(Constants.SESSION_USER)) == null) {
                log.debug("未登陆或session已超时，无法获取排他锁");
                return false;
            }
            User _loginUser = ((SessionACL) session.getAttribute(Constants.SESSION_USER)).getUser();
            String _userId = _loginUser.getUserId();

            LockServiceDTO _lockDto = lockServiceMapper.Get(null,_exclusiveKey,null);

            if (_lockDto != null ) {
                boolean _isOwner = (_userId == _lockDto.getUserId() && session.getId().equals(_lockDto.getSessionId()));
                if(_isOwner){
                    log.debug("user:" + _userId + ", session[" + session.getId() + "]已拥有排它锁[" + _lockDto.getLockKey()+ "]");
                }else
                {
                    log.debug("user:" + _lockDto.getUserId() + ", session[" + _lockDto.getSessionId() + "]拥有排它锁[" + _lockDto.getLockKey()+ "]，[" +_userId + "/" + session.getId() + "]不能获取排他锁");
                }
                return _isOwner;// 有数据，直接返回排他失败
            }

            _lockDto = new LockServiceDTO();
            _lockDto.setLockKey(_exclusiveKey);
            _lockDto.setClientIp(_ip);
            _lockDto.setBusinessCd(businessCd);
            _lockDto.setSessionId(session.getId());
            _lockDto.setWebAplName(_name);
            _lockDto.setUserId(_userId);
            // 这里往DB写入数据
            // 首先检查是否存在锁，如存在直接返回false，否则写入锁，写入成功后返回true
            long _result = lockServiceMapper.Save(_lockDto);

            log.debug("功能/数据排他锁结果：" + _result);
            return _result > 0;
        } catch (UnknownHostException e) {
            log.error("功能排他加锁失败", e);
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public boolean UnLockData(HttpSession session, HttpServletRequest request,
                              String businessCd, String businessKey, String tableId, String dataId) {
        String _exclusiveKey = this.GetExclusiveKey(session, request,
                businessCd, businessKey, tableId, dataId);
        if ((SessionACL) session.getAttribute(Constants.SESSION_USER) == null) {
            // 清除session对应的排他数据
            //lockServiceMapper.Delete(null, null, session.getId());// 删除session所有的lock
            log.debug("未登陆或session已超时，无法删除排他锁");
            return false;
        }

        User _loginUser = ((SessionACL) session.getAttribute(Constants.SESSION_USER)).getUser();
        String _userId = _loginUser.getUserId();

        // 删除锁信息
        long _result = lockServiceMapper.Delete(_userId, _exclusiveKey,
                session.getId());
        log.debug("功能/数据解锁结果：" + _result);

        return _result >= 0;
    }

    @Override
    public boolean UnlockSession(String sessionId) {
        boolean _return = false;
        long _result = lockServiceMapper.Delete(null, null, sessionId);
        _return = _result > 0;
        log.debug("session解锁结果：" + _result);
        return _return;
    }
}
