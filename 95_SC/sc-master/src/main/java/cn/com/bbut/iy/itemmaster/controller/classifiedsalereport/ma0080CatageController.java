package cn.com.bbut.iy.itemmaster.controller.classifiedsalereport;

import cn.com.bbut.iy.itemmaster.annotation.Secure;
import cn.com.bbut.iy.itemmaster.controller.BaseAction;
import cn.com.bbut.iy.itemmaster.entity.ma0080.MA0080;
import cn.com.bbut.iy.itemmaster.service.ma0080.MA0080Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @ClassName ClassifiedSaleReportController
 * @Description TODO
 * @Author Administrator
 * @Date 2020/3/26 16:52
 * @Version 1.0
 */
@Controller
@Slf4j
@Secure
@RequestMapping(value = "a/classifiedSaleReport" ,method = RequestMethod.GET)
public class ma0080CatageController  extends BaseAction {
    @Autowired
   private MA0080Service ma0080Service;

    @RequestMapping(value = "/getBigCatage",method = RequestMethod.GET)
    @ResponseBody
    public List<MA0080> getBigCatage(MA0080 ma0080){

        return   ma0080Service.getBigCatage(ma0080);
}









}
