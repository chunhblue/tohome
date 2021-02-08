package cn.com.bbut.iy.itemmaster.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import cn.com.bbut.iy.itemmaster.constant.Constants;

/**
 * @author songxz
 */
@Controller
@RequestMapping(value = "/")
@Slf4j
public class CommonController extends BaseAction {

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView view(HttpServletRequest req, HttpSession session, Map<String, ?> model) {
        ModelAndView mv = new ModelAndView("redirect:" + Constants.REQ_HEADER + "/index");
        return mv;
    }
}
