package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.annotation.Secure;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.service.CM9060Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Cm9060共通查询
 * @author
 */
@RestController
@Slf4j
@Secure
@RequestMapping(value = Constants.REQ_HEADER + "/cm9060")
public class CM9060Controller extends BaseAction {

    @Autowired
    private CM9060Service cm9060Service;

    /**
     * 根据key 获取参数值
     * @param key
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getValByKey")
    public String getValByKey(String key, HttpSession session, HttpServletRequest req) {
        return cm9060Service.getValByKey(key);
    }

    /*
     *
     * 获取当前业务日期
     *
     */
    @ResponseBody
    @RequestMapping(value = "/getDate")
    public String getBsinessDate(HttpSession session, HttpServletRequest req) {
        return cm9060Service.getValByKey("0000");
    }

}
