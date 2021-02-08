package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.annotation.Secure;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.entity.MA1060;
import cn.com.bbut.iy.itemmaster.service.ma1060.MA1060Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * MA1060查询
 * @author
 */
@RestController
@Slf4j
@Secure
@RequestMapping(value = Constants.REQ_HEADER + "/ma1060")
public class MA1060Controller extends BaseAction {

    @Autowired
    private MA1060Service service;

    @RequestMapping(value = "/getList")
    @ResponseBody
    public List<MA1060> getDpt(HttpSession session, HttpServletRequest req) {
        return service.getList();
    }

}
