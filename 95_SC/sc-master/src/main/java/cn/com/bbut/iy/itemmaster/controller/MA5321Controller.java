package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.service.MA5321Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 仓库查询
 *
 */
@Controller
@Slf4j
@RequestMapping(value = Constants.REQ_HEADER + "/ma5321")
public class MA5321Controller extends BaseAction {

    @Autowired
    private MA5321Service service;

    /**
     * 查询仓库list
     *
     * @param v
     * @return
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/getList")
    public List<AutoCompleteDTO> getList(String v){
        return service.getWarehouse(v);
    }

}