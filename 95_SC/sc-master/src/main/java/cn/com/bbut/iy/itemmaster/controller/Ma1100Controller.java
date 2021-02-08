package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.annotation.Secure;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.entity.ma1100.MA1100;
import cn.com.bbut.iy.itemmaster.service.MA1100Service;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 商品信息
 *
 * @author zcz
 */
@Controller
@Slf4j
@Secure
@RequestMapping(value = Constants.REQ_HEADER + "/ma1100")
public class Ma1100Controller extends BaseAction {
    @Autowired
    private MA1100Service ma1100Service;

    /**
     * 获取商品下拉
     * @param session
     * @param req
     * @param v
     * @return
     */
    @RequestMapping(value = "/getArticleAll")
    @ResponseBody
    public List<AutoCompleteDTO> getStoreAll(HttpSession session, HttpServletRequest req,String storeCd,
                                              String v) {
        User user = this.getUser(session);
        log.debug("抓取商品信息，user:{}", user.getUserId());
        List<AutoCompleteDTO> dtos = ma1100Service.getItemAll(storeCd,v);
        return dtos;
    }
    @RequestMapping(value = "/getAllItems")
    @ResponseBody
    public String getStoreAll(HttpSession session, HttpServletRequest req) {
        User user = this.getUser(session);
        log.debug("抓取商品信息，user:{}", user.getUserId());
        List<MA1100> dtos = ma1100Service.getItemNo();
         return new Gson().toJson(dtos);

    }
}
