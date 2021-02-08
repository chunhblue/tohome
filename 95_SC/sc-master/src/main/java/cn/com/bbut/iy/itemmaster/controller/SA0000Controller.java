package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.annotation.Secure;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.entity.SA0000;
import cn.com.bbut.iy.itemmaster.service.sa0000.SA0000Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * SA0000查询
 * @author
 */
@RestController
@Slf4j
@Secure
@RequestMapping(value = Constants.REQ_HEADER + "/sa0000")
public class SA0000Controller extends BaseAction {

    @Autowired
    private SA0000Service service;

    @RequestMapping(value = "/getList")
    @ResponseBody
    public List<SA0000> getDpt(HttpSession session, HttpServletRequest req) {
        return service.getList();
    }

}
