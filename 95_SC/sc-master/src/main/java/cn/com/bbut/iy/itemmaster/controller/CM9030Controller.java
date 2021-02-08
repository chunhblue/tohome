package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.annotation.Secure;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.entity.CM9030;
import cn.com.bbut.iy.itemmaster.service.cm9030.CM9030Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Cm9030共通查询
 * @author
 */
@RestController
@Slf4j
@Secure
@RequestMapping(value = Constants.REQ_HEADER + "/cm9030")
public class CM9030Controller extends BaseAction {

    @Autowired
    private CM9030Service cm9030Service;

    @RequestMapping(value = "/getList")
    @ResponseBody
    public List<CM9030> getList(HttpSession session, HttpServletRequest req) {
        return cm9030Service.getList();
    }

}
