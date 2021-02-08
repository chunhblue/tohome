package cn.com.bbut.iy.itemmaster.serviceimpl;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.dao.ConmmonMapper;
import cn.com.bbut.iy.itemmaster.service.CommonService;

/**
 * @author songxz
 */
@Slf4j
@Service
public class CommonServiceImpl implements CommonService {
    @Autowired
    private ConmmonMapper mapper;
    @Autowired
    private HttpServletRequest request;

    @Override
    public String getSequenceNext(String sequenceName, String pattern) {
        Map<String, String> map = new HashMap<>();
        map.put("sequenceName", sequenceName);
        long nextNo = mapper.getSequenceNext(map);
        if (pattern != null) {
            DecimalFormat BM_NEW_NO = new DecimalFormat(pattern);
            return BM_NEW_NO.format(nextNo);
        } else {
            return String.valueOf(nextNo);
        }
    }

    @Override
    public Collection<Integer> getSessionUserRoleIds() {
        HttpSession session = request.getSession();
        Collection<Integer> roleIds = (Collection<Integer>) session
                .getAttribute(Constants.SESSION_ROLES);
        return roleIds;
    }
}
