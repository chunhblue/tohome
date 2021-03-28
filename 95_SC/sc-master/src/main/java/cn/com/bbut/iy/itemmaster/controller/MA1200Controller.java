package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.dto.AjaxResultDto;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.ma1200.MA1200DTO;
import cn.com.bbut.iy.itemmaster.service.CM9060Service;
import cn.com.bbut.iy.itemmaster.service.Ma1200Service;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Ma1200
 *
 */
@Controller
@Slf4j
@RequestMapping(value = Constants.REQ_HEADER + "/ma1200")
public class MA1200Controller extends BaseAction {

    @Autowired
    private CM9060Service cm9060Service;
    @Autowired
    private Ma1200Service ma1200Service;


    /**
     * 判断母货号是否允许操作
     *
     * @param request
     * @param session
     * @param itemId
     * @return
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/check")
    public AjaxResultDto check(HttpServletRequest request, HttpSession session,
                             String itemId) {
        AjaxResultDto _return = new AjaxResultDto();
        if(StringUtils.isBlank(itemId)){
            _return.setMessage("Parameter cannot be empty!");
            return _return;
        }
        boolean flag = true;
        MA1200DTO _dto = ma1200Service.getByParentId(itemId);
        if(_dto != null){
//            String _val = cm9060Service.getValByKey("0634");
//            if(!"0".equals(_val)){
//                flag = false;
//                _return.setMessage("You cannot choose a combination of goods!");
//            }
        }
        _return.setSuccess(flag);
        return _return;
    }

    /**
     * 获取母货号待选
     *
     */
    @ResponseBody
    @RequestMapping(value = "/getList")
    public List<AutoCompleteDTO> getList(HttpServletRequest request, HttpSession session, String v){
        if(StringUtils.isBlank(v)){
            return null;
        }
        List<AutoCompleteDTO> _list = ma1200Service.getList(v);
        return _list;
    }

}