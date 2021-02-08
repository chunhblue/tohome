package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.annotation.Secure;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.base.ReturnDTO;
import cn.com.bbut.iy.itemmaster.dto.fsInventory.PI0100DTOD;
import cn.com.bbut.iy.itemmaster.dto.fsInventory.PI0100ParamDTOD;
import cn.com.bbut.iy.itemmaster.dto.fsInventory.PI0110DTOD;
import cn.com.bbut.iy.itemmaster.dto.pi0100c.PI0100DTOC;
import cn.com.bbut.iy.itemmaster.dto.pi0100c.PI0100ParamDTOC;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.service.fsInvenoryentry.fsInventoryPlanService;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @ClassName fsInventoryPlanController
 * @Description TODO
 * @Author Administrator
 * @Date 2020/3/23 10:05
 * @Version 1.0
 */
@Controller
@Slf4j
@Secure
@RequestMapping(value = Constants.REQ_HEADER + "/fsInventoryPlanEntry",produces ={"application/json;charset=UTF-8"})
public class fsInventoryPlanController extends BaseAction{
    @Autowired
    private fsInventoryPlanService fsInventorplanService;
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView fsIncentoryPlanList(HttpServletRequest request, HttpSession session,
                                          Map<String, ?> model) {
        User u = this.getUser(session);
        log.debug("User:{} 进入FS库存一览画面", u.getUserId());
        Collection<Integer> roleIds = (Collection<Integer>) request.getSession().getAttribute(
                Constants.SESSION_ROLES);
        ModelAndView mv = new ModelAndView("fsInventoryplanLd/fsInventoryPlan");
        mv.addObject("use", 0);
        mv.addObject("identity", 1);
        mv.addObject("useMsg", "FS库存一览画面");
        return mv;
    }
    @GetMapping("/getComboxData")
    @ResponseBody
    public ReturnDTO getComboxData(HttpServletRequest request, HttpSession session) {
        Map<String, List<Map<String,String>>> result =fsInventorplanService.getComboxData();
        return new ReturnDTO(true,"ok",result);
    }

    @GetMapping("/getPmaList")
    @ResponseBody
    public ReturnDTO getPmaList(HttpServletRequest request, HttpSession session) {
        List<Map<String, String>> pmaList =fsInventorplanService.getPmaList(request, session);
        return new ReturnDTO(true,"ok",pmaList);
    }

}