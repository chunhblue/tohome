package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.annotation.Permission;
import cn.com.bbut.iy.itemmaster.annotation.Secure;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.PermissionCode;
import cn.com.bbut.iy.itemmaster.dto.AjaxResultDto;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.base.Node;
import cn.com.bbut.iy.itemmaster.dto.remoteUpdate.RemoteUpdateCheckStoresDto;
import cn.com.bbut.iy.itemmaster.dto.remoteUpdate.RemoteUpdateDto;
import cn.com.bbut.iy.itemmaster.dto.remoteUpdate.RemoteUpdateSaveDto;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.service.remoteupdate.RemoteUpdateService;
import cn.com.bbut.iy.itemmaster.util.StringUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * MD上传文件到pos service
 *
 * @author zcz
 */
@Controller
@Slf4j
@Secure
@RequestMapping(value = Constants.REQ_HEADER + "/remoteUpdate")
public class RemoteUpdateController extends BaseAction{
    @Autowired
    private RemoteUpdateService remoteUpdateService;

    /**
     * 上传文件到pos service查询画面
     * @param request
     * @param session
     * @param model
     * @return
     */
    @Permission(codes = {
            PermissionCode.CODE_SC_RU_VIEW_LIST,
            PermissionCode.CODE_SC_RU_EDIT
    })
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView tolistView(HttpServletRequest request, HttpSession session,
                                   Map<String, ?> model, String flag) {
        User u = this.getUser(session);
        log.debug("User:{} 进入 上传文件到pos service查询画面", u.getUserId());
        ModelAndView mv = new ModelAndView("remoteUpdate/remoteUpdate");
        mv.addObject("viewSts", flag);
        mv.addObject("useMsg", "上传文件到pos service查询画面");
        this.saveToken(request);
        return mv;
    }

    /**
     * 上传文件到pos service 编辑、新增画面
     * @param request
     * @param session
     * @return
     */
    @Permission(codes = {
            PermissionCode.CODE_SC_RU_ADD,
            PermissionCode.CODE_SC_RU_EDIT
    })
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public ModelAndView toupdateEdit(HttpServletRequest request, HttpSession session,
                                     RemoteUpdateDto remoteUpdateDto, String flag) {
        User u = this.getUser(session);
        log.debug("User:{} 上传文件到pos service 编辑、新增画面", u.getUserId());
        Collection<Integer> roleIds = (Collection<Integer>) request.getSession().getAttribute(
                Constants.SESSION_ROLES);
        ModelAndView mv = new ModelAndView("remoteUpdate/remoteUpdateEdit");
        mv.addObject("use", 0);
        mv.addObject("identity", 1);
        mv.addObject("data", remoteUpdateDto);
        mv.addObject("viewSts", flag);
        mv.addObject("useMsg", "上传文件到pos service 编辑、新增画面");
        this.saveToken(request);
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "/getList")
    public GridDataDTO<RemoteUpdateDto> getList(HttpServletRequest request, HttpSession session,
                                                int page,int rows, String searchJson) {
        GridDataDTO<RemoteUpdateDto> grid = null;
        RemoteUpdateDto remoteUpdateEntry = new Gson().fromJson(searchJson, RemoteUpdateDto.class);
        remoteUpdateEntry.setPage(page);
        remoteUpdateEntry.setRows(rows);
        grid = remoteUpdateService.getList(remoteUpdateEntry);
        return grid;
    }

    @RequestMapping(value = "/getStoreList",method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getStoreList(HttpServletRequest request, HttpSession session,
                                           Integer id) {
        List<Node> nodes1 = remoteUpdateService.getStoreNodeList(id);
        Node tree = new Node();//重要插件，创建一个树模型
        Node mt = tree.createTree(nodes1);//Node类里面包含了一个创建树的方法。这个方法就是通过节点的信息（nodes）来构建一颗多叉树manytree->mt

        Map<String, Object> map = new HashMap<>();
        map.put("list",mt.getNodes());
        return map;
    }

    @RequestMapping(value = "/checkStoresCanAdd",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResultDto checkStoresCanAdd(HttpServletRequest request, HttpSession session,
                                            String jsonStr) {
        AjaxResultDto rest = new AjaxResultDto();
        RemoteUpdateCheckStoresDto updateCheckStoresDto = new Gson().fromJson(jsonStr, RemoteUpdateCheckStoresDto.class);
        if(updateCheckStoresDto.getStoreCds().length == 0){
            rest.setSuccess(false);
            rest.setMessage("Pelese Choose the Remote Update Store at least one!");
            return rest;
        }
        List<RemoteUpdateDto> remoteUpdateDtos = remoteUpdateService.checkStoresCanAdd(updateCheckStoresDto);
        if(remoteUpdateDtos.size()>0){
            rest.setSuccess(false);
            StringBuffer msg = new StringBuffer("Upload failed!Store StartDate UpdateType for below data already exists:<br>");
            for(int i=0;i<remoteUpdateDtos.size();i++){
                RemoteUpdateDto remoteUpdateDto = remoteUpdateDtos.get(i);
                /*if(i==0){
                    msg.append("<table border='1' cellspacing='0' style='text-align: center;' ><tr><td>Update Type</td><td>Start Date</td><td>Store Code</td></tr>");
                }
                msg.append("<tr><td>"+remoteUpdateDto.getUpdateTypeName()+"</td><td>"+ StringUtil.getFormatDate(remoteUpdateDto.getStartDate())
                        +"</td><td>"+remoteUpdateDto.getStoreCd()+"</td></tr>");*/
                msg.append(remoteUpdateDto.getStoreCd()+"     "+StringUtil.getFormatDate(remoteUpdateDto.getStartDate())+"     "+remoteUpdateDto.getUpdateTypeName() +"<br>");
            }
            msg.append("</table>");
            rest.setMessage(msg.toString());
        }else{
            rest.setSuccess(true);
        }
        this.saveToken(request, true);
        rest.setToKen((String) request.getAttribute("toKen"));
        return rest;
    }

    @RequestMapping(value = "/saveRemodeUpdate",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResultDto saveRemodeUpdate(HttpServletRequest request, HttpSession session,
                                            String jsonStr) {
        AjaxResultDto rest = null;
        User u = this.getUser(session);
        RemoteUpdateSaveDto remoteUpdateSaveDto = new Gson().fromJson(jsonStr, RemoteUpdateSaveDto.class);
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMdd");
        SimpleDateFormat sdf1 = new SimpleDateFormat("HHmmss");
        remoteUpdateSaveDto.setCreateUserId(u.getUserId());
        remoteUpdateSaveDto.setCreateYmd(sdf.format(date));
        remoteUpdateSaveDto.setCreateHms(sdf1.format(date));
        remoteUpdateSaveDto.setUpdateUserId(u.getUserId());
        remoteUpdateSaveDto.setUpdateYmd(sdf.format(date));
        remoteUpdateSaveDto.setUpdateHms(sdf1.format(date));
        if(remoteUpdateSaveDto.getId() != null && remoteUpdateSaveDto.getId() != 0){
            rest = remoteUpdateService.updateRemodeUpdate(remoteUpdateSaveDto);
        }else{
            rest = remoteUpdateService.insertRemodeUpdate(remoteUpdateSaveDto);
        }

        return rest;
    }

    @RequestMapping(value = "/getRemodeUpdate",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResultDto getRemodeUpdate(HttpServletRequest request, HttpSession session,
                                         Integer id) {
        AjaxResultDto rest = null;
        rest = remoteUpdateService.getRemodeUpdate(id);
        return rest;
    }
}
