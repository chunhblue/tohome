package cn.com.bbut.iy.itemmaster.controller.od0010Referma1010;

import cn.com.bbut.iy.itemmaster.annotation.Secure;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.controller.BaseAction;
import cn.com.bbut.iy.itemmaster.dto.PermissionResource;
import cn.com.bbut.iy.itemmaster.entity.Ma1010aboutOd0000;
import cn.com.bbut.iy.itemmaster.entity.base.Ma1000;
import cn.com.bbut.iy.itemmaster.service.od0010refer.od0010RefService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collection;

/**
 * @ClassName od0010Referma1010
 * @Description TODO
 * @Author Administrator
 * @Date 2020/3/13 14:16
 * @Version 1.0
 */


@Controller
@Slf4j
@Secure
@RequestMapping(value = Constants.REQ_HEADER +"/od0010ReferMa1010" ,method = RequestMethod.GET)
public class Od0010Referma1010Controller extends BaseAction {


    @Autowired
    private od0010RefService  od0010refService;
    @RequestMapping(value = "/selectStore",method = RequestMethod.GET)
    @ResponseBody
    public Collection<Ma1000> selectAllStoreInfo(Ma1000  ma1000){
        Collection<Ma1000> allInfoStode = od0010refService.getAllInfoStode(ma1000);
       return  allInfoStode;
    }






}
