package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.annotation.Permission;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.PermissionCode;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.base.MenuParamDTO;
import cn.com.bbut.iy.itemmaster.dto.base.Node;
import cn.com.bbut.iy.itemmaster.dto.base.role.MenuPermDTO;
import cn.com.bbut.iy.itemmaster.dto.base.role.ParentMenuDTO;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.entity.base.Menu;
import cn.com.bbut.iy.itemmaster.service.base.MenuService;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 菜单管理
 *
 * @author zcz
 */
@Controller
@Slf4j
//@Secure
@RequestMapping(value = Constants.REQ_HEADER + "/menu")
public class MenuController extends BaseAction {

    /**
     * 菜单核心
     */
    @Autowired
    MenuService menuService;

    /**
     * 菜单管理画面
     * @param request
     * @param session
     * @param model
     * @return
     */
    @Permission(codes = { PermissionCode.CODE_P_MENU_LIST_VIEW})
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView tolistView(HttpServletRequest request, HttpSession session,
                                   Map<String, ?> model) {
        User u = this.getUser(session);
        log.debug("User:{} 进入 菜单管理", u.getUserId());
        Collection<Integer> roleIds = (Collection<Integer>) request.getSession().getAttribute(
                Constants.SESSION_ROLES);
        ModelAndView mv = new ModelAndView("base/menu/menu");
        mv.addObject("useMsg", "菜单管理画面");
        return mv;
    }

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getMenulist(HttpServletRequest request, HttpSession session,
                                           Map<String, ?> model) {
        Collection<Menu> menus = menuService.getAllMenus("parent_id asc, sort asc");
        List<Node> nodes = new ArrayList<Node>();//把所有资源转换成树模型的节点集合，此容器用于保存所有节点
        nodes.add(new Node("0", "-1","Store Controller System","glyphicon glyphicon-stop",""));
        menus.forEach(menu -> {
//            if (menu.getParentId() == null) {
                Node node = new Node();
                node.setNodeId(String.valueOf(menu.getId()));
                if(menu.getParentId()==null){
                    node.setPid("0");
                }else{
                    node.setPid(String.valueOf(menu.getParentId()));
                }
                node.setText(menu.getName());
                nodes.add(node);
//            }
        });
        List<Node> nodes1 = nodes.stream().sorted(Comparator.comparing(Node::getPid)).collect(Collectors.toList());//pid升序排序
        Node tree = new Node();//重要插件，创建一个树模型
        Node mt = tree.createTree(nodes1);//Node类里面包含了一个创建树的方法。这个方法就是通过节点的信息（nodes）来构建一颗多叉树manytree->mt

        Map<String, Object> map = new HashMap<>();
        map.put("list",mt.getNodes());
        return map;
    }

    /**
     * 得到zggrid数据
     *
     * @param session
     * @param model
     * @param v
     * @return
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/getdata")
    public GridDataDTO<Menu> getData(String searchJson, int page, int rows,HttpServletRequest request, HttpSession session) {
        MenuParamDTO param = null;

        if (!StringUtils.isEmpty(searchJson)) {
            try {
                searchJson= URLDecoder.decode(searchJson,"UTF-8");
                Gson gson = new Gson();
                param = gson.fromJson(searchJson, MenuParamDTO.class);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                param = new MenuParamDTO();
            }
        } else {
            param = new MenuParamDTO();
        }

        param.setPage(page);
        param.setRows(rows);
        param.setLimitStart((page - 1)*rows);
        return menuService.getData(param);
    }
}
