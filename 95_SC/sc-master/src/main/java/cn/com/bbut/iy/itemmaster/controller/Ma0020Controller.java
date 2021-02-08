package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.annotation.Permission;
import cn.com.bbut.iy.itemmaster.annotation.Secure;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.PermissionCode;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.base.Node;
import cn.com.bbut.iy.itemmaster.dto.ma0020.MA0020DTO;
import cn.com.bbut.iy.itemmaster.dto.ma0020.Ma0020ParamDTO;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.service.ma0020.Ma0020Service;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName Ma0020Controller
 * @Description TODO
 * @Author Administrator
 * @Date 2020/3/7 16:41
 * @Version 1.0
 */
@Controller
@Slf4j
@Secure
@RequestMapping(value = Constants.REQ_HEADER + "/organizationalStructure")
public class Ma0020Controller extends BaseAction{

    @Autowired
    private Ma0020Service ma0020Service;

    @RequestMapping(method = RequestMethod.GET)
    @Permission(codes = {
            PermissionCode.CODE_SC_OR_STRUCT_LIST_VIEW
    })
    public ModelAndView tolistView(HttpServletRequest request, HttpSession session,
                                   Map<String, ?> model){
        User user = this.getUser(session);
        log.debug("User:{} 进入 组织架构画面", user.getUserId());
        ModelAndView mv = new ModelAndView("storepositiondataLd/OrganizationalStructure");
        mv.addObject("use", 0);
        mv.addObject("identity", 1);
        mv.addObject("useMsg", "组织架构画面");
        return mv;
    }

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getMenulist(HttpServletRequest request, HttpSession session,
                                           Map<String, ?> model) {
        Collection<MA0020DTO> ma0020Menus = ma0020Service.getAllMenus();
        List<Node> nodes = new ArrayList<Node>();
        ma0020Menus.forEach(ma0020 -> {
            Node node1 = new Node();
            if (!ma0020.getAdminStructureCd().equals("000000")) {
                node1.setPid(ma0020.getAdminStructureCd());
                node1.setIcon("glyphicon glyphicon-stop");
                node1.setHref(ma0020.getStructureCd());
                node1.setText(ma0020.getStructureName());
                nodes.add(node1);
                node1.setNodeId(ma0020.getStructureCd());
            } else {
                nodes.add(new Node(ma0020.getStructureCd(), "-1", ma0020.getStructureName(),
                        "glyphicon glyphicon-stop", ma0020.getStructureCd()));
            }
        });
        // 按pid排序会让部分子节点排到前面，创建树时父节点还未添加，导致无法添加进tree，在SQL中已经排序，此处省略
        // List<Node> nodes1 = nodes.stream().sorted(Comparator.comparing(Node::getPid)).collect(Collectors.toList());//pid升序排序
        Node tree = new Node();//重要插件，创建一个树模型
        Node mt = tree.createTree(nodes);//Node类里面包含了一个创建树的方法。这个方法就是通过节点的信息（nodes）来构建一颗多叉树manytree->mt
        Map<String, Object> map = new HashMap<>();
        map.put("list", mt.getNodes());
        return map;
    }

    @RequestMapping(value = "/lista")
    @ResponseBody
    public GridDataDTO<MA0020DTO> selectInformation(HttpServletRequest request, HttpSession session,String searchJson){
        if(StringUtils.isBlank(searchJson) || this.getUser(session) == null){
            return new GridDataDTO<MA0020DTO>();
        }
        MA0020DTO OneMa0020DTO = ma0020Service.selectDetailMa0020(searchJson);
        if(OneMa0020DTO==null){
            return new GridDataDTO<MA0020DTO>();
        }
        Ma0020ParamDTO dto = new Ma0020ParamDTO();
        dto.setStructureCd(searchJson);
        GridDataDTO<MA0020DTO> ma0020DTOGridDataDTO = ma0020Service.selectAMa0020C(dto);
        return  ma0020DTOGridDataDTO;
    }

    @RequestMapping(value = "/getStructureName")
    @ResponseBody
    public List<AutoCompleteDTO> getStructName(HttpServletRequest request, HttpSession session,String v){
        User user = this.getUser(session);
        log.debug("抓取组织结构信息，user:{}", user.getUserId());
        List<AutoCompleteDTO> dtos = null;
        dtos= ma0020Service.getListStructName(v);
        return dtos;
    }

    /**
     * 角色权限设置时，list获取
     *
     */
    @ResponseBody
    @RequestMapping(value = "/getStructureByLevel")
    public List<AutoCompleteDTO> getByLevel(HttpServletRequest request, HttpSession session,
                                               String level, String parentId, String v){
        if(StringUtils.isBlank(level)){
            return null;
        }
        if(!"0".equals(level) && StringUtils.isBlank(parentId)){
            return null;
        }
        List<AutoCompleteDTO> _list = ma0020Service.getListByLevel(level, parentId, v);
        if("0".equals(level)){
            AutoCompleteDTO dto = new AutoCompleteDTO("999999","All Region","");
            _list.add(0,dto);
        }else if("1".equals(level)){
            AutoCompleteDTO dto = new AutoCompleteDTO("999999","All City","");
            _list.add(0,dto);
        }else if("2".equals(level)){
            AutoCompleteDTO dto = new AutoCompleteDTO("999999","All District","");
            _list.add(0,dto);
        }else if("3".equals(level)){
            AutoCompleteDTO dto = new AutoCompleteDTO("999999","All Store","");
            _list.add(0,dto);
        }
        return _list;
    }

    /**
     * 直接获取角色全部店铺
     *
     */
    @ResponseBody
    @RequestMapping(value = "/getAll")
    public List<AutoCompleteDTO> getAllStore(HttpServletRequest request, HttpSession session, String v){
        Collection<String> stores = (Collection<String>) session.getAttribute(Constants.SESSION_STORES);
        if(stores == null || stores.size()==0){
            return null;
        }
        List<AutoCompleteDTO> _list = ma0020Service.getAllStore(stores, v);
        return _list;
    }

    @ResponseBody
    @RequestMapping(value = "/getdata")
    public GridDataDTO<MA0020DTO> getData( Ma0020ParamDTO  param, int page, int rows,
                                           HttpServletRequest request, HttpSession session) {
        param.setPage(page);
        param.setRows(rows);
        param.setLimitStart((page - 1)*rows);
        return ma0020Service.getData(param);
    }

}
