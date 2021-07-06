package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.annotation.Secure;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.dto.ma0020.MA0020DTO;
import cn.com.bbut.iy.itemmaster.dto.store.MA4160DTO;
import cn.com.bbut.iy.itemmaster.entity.ma0020.MA0020C;
import cn.com.bbut.iy.itemmaster.entity.ma0080.MA0080;
import cn.com.bbut.iy.itemmaster.service.ma4160refer.Ma4160referService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@Slf4j
@Secure
@RequestMapping(value = "a/ma0060ReferMa4200" ,method = RequestMethod.GET)
public class Ma4160referMa4200Controller extends  BaseAction{
    @Autowired
    private Ma4160referService ma4160referService;
   @RequestMapping(value = "/getJobcatagoryName")
   @ResponseBody
    public List<MA4160DTO>  getJobcatagoryName(HttpSession session, HttpServletRequest req ,String selectId){
       //System.out.println("1111");
       boolean flg = false;
       if(selectId.equals("job_type_cd")){
           flg = true;
       }
       List<MA4160DTO> list =  ma4160referService.getLJobTye(flg);
       return list;
   }
    @RequestMapping(value = "/getStructureName")
    @ResponseBody
    public List<MA0020DTO> getStructureName(HttpSession session, HttpServletRequest req ,MA0020DTO ma0020DTO){
        return ma4160referService.getStructName();
    }
}
