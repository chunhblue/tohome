package cn.com.bbut.iy.itemmaster.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import cn.com.bbut.iy.itemmaster.entity.base.Ma1000;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.service.StoreService;

/**
 * @author songxz
 */
@Controller
@Slf4j
@RequestMapping(value = Constants.REQ_HEADER + "/demo")
public class DemoController {
    @Autowired
    private StoreService storeService;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public ModelAndView view(HttpSession session, Map<String, ?> model) {

        /**
         * 如果是像画面返回json数据的话
         * 
         * ModelAndView mav =new ModelAndView(new MappingJackson2JsonView());
         * 
         * mav.addObject("loginUrl","www.baidu.com");
         */

        ModelAndView mv = new ModelAndView("demo");
        List<Ma1000> list = storeService.getAllStore();
        mv.addObject("stores", list);
        return mv;
    }
}
