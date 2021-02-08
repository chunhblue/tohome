package cn.com.bbut.iy.itemmaster.controller;

import java.util.Map;

import javax.servlet.http.HttpSession;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.com.bbut.iy.itemmaster.constant.Constants;

/**
 * @author songxz
 */
@Controller
@Slf4j
@RequestMapping(value = Constants.REQ_HEADER)
public class MsgController {
    @RequestMapping(value = "/tomsg")
    public ModelAndView view(HttpSession session, Map<String, ?> model, String targetUrl,
            String text) {
        ModelAndView mv = new ModelAndView("/common/msg");
        mv.addObject("targetUrl", targetUrl);
        mv.addObject("text", text);
        return mv;
    }
}
