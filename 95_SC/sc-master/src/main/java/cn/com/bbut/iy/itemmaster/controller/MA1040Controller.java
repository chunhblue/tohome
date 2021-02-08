package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.annotation.Secure;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.entity.MA1040;
import cn.com.bbut.iy.itemmaster.service.ma1040.MA1040Service;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * MA1040查询
 * @author
 */
@RestController
@Slf4j
@Secure
@RequestMapping(value = Constants.REQ_HEADER + "/ma1040")
public class MA1040Controller extends BaseAction {

    @Autowired
    private MA1040Service service;

    @RequestMapping(value = "/getList")
    @ResponseBody
    public List<MA1040> getDpt(HttpSession session, HttpServletRequest req, String storeCd) {
        if(StringUtils.isBlank(storeCd)){
            return null;
        }
        return service.getList(storeCd);
    }

}
