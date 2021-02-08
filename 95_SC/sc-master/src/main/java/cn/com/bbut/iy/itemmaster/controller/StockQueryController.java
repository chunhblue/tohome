package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.annotation.Permission;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.PermissionCode;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.base.ReturnDTO;
import cn.com.bbut.iy.itemmaster.dto.mRoleStore.MRoleStoreParam;
import cn.com.bbut.iy.itemmaster.dto.stocktakeQuery.StocktakeQueryItemsDTO;
import cn.com.bbut.iy.itemmaster.dto.stocktakeQuery.StocktakeQueryListDTO;
import cn.com.bbut.iy.itemmaster.dto.stocktakeQuery.StocktakeQueryParam;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.entity.ma0080.MA0080;
import cn.com.bbut.iy.itemmaster.service.CM9060Service;
import cn.com.bbut.iy.itemmaster.service.MRoleStoreService;
import cn.com.bbut.iy.itemmaster.service.StocktakeQueryService;
import cn.com.bbut.iy.itemmaster.service.ma0070.Ma0070Service;
import com.google.gson.Gson;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
/**
 * 盘点一览
 *
 * @author lz
 */
@Controller
@Slf4j
//@Secure
@RequestMapping(value = Constants.REQ_HEADER + "/stocktakeQuery")
public class StockQueryController extends BaseAction {

    @Autowired
    private StocktakeQueryService stocktakeQueryService;

    @Autowired
    private Ma0070Service ma0070Service;

    @Autowired
    private CM9060Service cm9060Service;
    @Autowired
    private MRoleStoreService mRoleStoreService;

    /**
     * 盘点一览画面
     *
     * @param request
     * @param session
     * @param model
     * @return
     */
    @Permission(codes = { PermissionCode.CODE_SC_PD_QUERY_LIST_VIEW})
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView stocktakeQuery(HttpServletRequest request, HttpSession session,
                                       Map<String, ?> model) {
        User u = this.getUser(session);
        log.debug("User:{} 进入盘点一览画面", u.getUserId());
        ModelAndView mv = new ModelAndView("stocktake_query/stocktakeQuery");
        mv.addObject("use", 0);
        mv.addObject("identity", 1);
        mv.addObject("useMsg", "盘点一览画面");
        return mv;
    }

    //      一览页面条件查询
    /*@ResponseBody
    @RequestMapping(value = "/query",method={RequestMethod.POST,RequestMethod.GET})
    public String query(HttpServletRequest request, HttpSession session,
                        Map<String, ?> model, StocktakeQueryParamDTO param) {
        return new Gson().toJson(stocktakeQueryService.getStocktakeQueryList(param));
    }*/

    // 一览页面条件查询
    @ResponseBody
    @RequestMapping("/search")
    @Permission(codes = { PermissionCode.CODE_SC_PD_QUERY_LIST_VIEW})
    public GridDataDTO query(String searchJson,int page, int rows, HttpServletRequest request, HttpSession session) {
        if (searchJson==null||StringUtils.isEmpty(searchJson)) {
            return null;
        }
        Gson gson = new Gson();
        StocktakeQueryParam stockParam = gson.fromJson(searchJson, StocktakeQueryParam.class);
        // 获取当前角色店铺权限
        Collection<String> stores = getStores(session, stockParam);
        if(stores.size() == 0){
            log.info(">>>>>>>>>>>>>>>>>>>>> get stores is null");
            return new GridDataDTO<StocktakeQueryListDTO>();
        }
        stockParam.setStores(stores);
        stockParam.setPage(page);
        stockParam.setRows(rows);
        stockParam.setLimitStart((page - 1)*rows);
        GridDataDTO<StocktakeQueryListDTO> result = stocktakeQueryService.search(stockParam);
        // new Gson().toJson(stocktakeQueryService.getStocktakeQueryList(param));
        return result;
    }


    /**
     * 盘点表一览详情画面
     * @param request
     * @param session
     * @param model
     * @return
     */
    /*@RequestMapping(value = "/toView",method={RequestMethod.POST,RequestMethod.GET})
    public ModelAndView toView(HttpServletRequest request, HttpSession session,
                                     Map<String, ?> model,String params) {
        ModelAndView mv = new ModelAndView("stocktake_query/stocktakeQueryEdit");
        mv.addObject("use", 0);
        mv.addObject("identity", 1);
        mv.addObject("useMsg", "盘点一览详细管理画面");
        mv.addObject("flag",0);
        // 从选择的店铺缓存信息中拿取store_cd
//        String storeCd = session.getAttribute(Constants.SESSION_STORE).toString();
        User u = this.getUser(session);
        mv.addObject("userId",u.getUserId());
        List<StocktakeQueryItemParam> list = new Gson().fromJson(params,new TypeToken<List<StocktakeQueryItemParam>>() {}.getType());
        for (StocktakeQueryItemParam item: list) {
            if(StringUtils.isBlank(item.getAreaCd())){
                item.setAreaCd(null);
            }
        }
        //freemarker页面接收时不能有中括号
        String searchList = list.toString().replace("[","{").replace("]","}");
        mv.addObject("searchList",searchList);
        return mv;
    }*/

    /**
     * 盘点表一览详情画面
     */
    @RequestMapping(value = "/toView",method={RequestMethod.POST,RequestMethod.GET})
    @Permission(codes = { PermissionCode.CODE_SC_PD_QUERY_VIEW})
    public ModelAndView toView(HttpServletRequest request, HttpSession session,String identity,String piCd, String piDate,String flag) {
        ModelAndView mv = new ModelAndView("stocktake_query/stocktakeQueryEdit");
        mv.addObject("use", 0);
        mv.addObject("identity", identity);
        mv.addObject("piCd", piCd);
        mv.addObject("piDate", piDate);
        mv.addObject("useMsg", "盘点一览详细管理画面");
        mv.addObject("flag",flag);
        return mv;
    }


    @RequestMapping(value = "/toModify",method={RequestMethod.POST,RequestMethod.GET})
    @Permission(codes = {
            PermissionCode.CODE_SC_PD_QUERY_FIRST_STOCK,
            PermissionCode.CODE_SC_PD_QUERY_LAST_STOCK,
    })
    public ModelAndView toAdd(String identity,String piCd, String piDate,String flag) {
        ModelAndView mv = new ModelAndView("stocktake_query/stocktakeQueryEdit");
        mv.addObject("use", 0);
        mv.addObject("identity", identity);
        mv.addObject("piCd", piCd);
        mv.addObject("piDate", piDate);
        mv.addObject("useMsg", "盘点一览详细管理画面");
        mv.addObject("flag",flag);
        return mv;
    }


    // 修改商品的盘点数
    @ResponseBody
    @RequestMapping("/save")
    @Permission(codes = {
            PermissionCode.CODE_SC_PD_QUERY_FIRST_STOCK,
            PermissionCode.CODE_SC_PD_QUERY_LAST_STOCK,
    })
    public ReturnDTO save(String record, HttpServletRequest request, HttpSession session) {
        int count = stocktakeQueryService.update(record);
        if (count==-1) {
            return new ReturnDTO(false,"Modify failed!");
        }
        return new ReturnDTO(true,"Modify succeeded!");
    }


    // 初盘输入完成
    @ResponseBody
    @RequestMapping("/piFirstFinish")
    @Permission(codes = {
            PermissionCode.CODE_SC_PD_QUERY_STOCK_TAKE_COMPLETE,
    })
    public ReturnDTO piFirstFinish(String jsonStr, HttpServletRequest request, HttpSession session) {
        int count = stocktakeQueryService.updatePiFirstFinish(jsonStr);
        if (count==-1) {
            return new ReturnDTO(false,"初盘失败!");
        }
        return new ReturnDTO(true,"初盘成功!");
    }

    //盘点认列
    @ResponseBody
    @RequestMapping("/piCommit")
    @Permission(codes = {
            PermissionCode.CODE_SC_PD_QUERY_DIFFERENCE_CONFIRM,
    })
    public ReturnDTO piCommit(String jsonStr, HttpServletRequest request, HttpSession session) {
        int count = stocktakeQueryService.updatePiCommit(jsonStr);
        if (count==-1) {
            return new ReturnDTO(false,"初盘失败!");
        }
        return new ReturnDTO(true,"初盘成功!");
    }


    /**
     * 盘点表一览详情画面
     * @param request
     * @param session
     * @return
     */
    /*@RequestMapping(value = "/toModify",method={RequestMethod.POST,RequestMethod.GET})
    public ModelAndView toAdd(HttpServletRequest request, HttpSession session,
                               Map<String, ?> model,String params,String flag) {
        ModelAndView mv = new ModelAndView("stocktake_query/stocktakeQueryEdit");
        mv.addObject("use", 0);
        mv.addObject("identity", 1);
        mv.addObject("useMsg", "盘点一览详细管理画面");
        mv.addObject("flag",flag);
        // 从选择的店铺缓存信息中拿取store_cd
//        String storeCd = session.getAttribute(Constants.SESSION_STORE).toString();
        User u = this.getUser(session);
        mv.addObject("userId",u.getUserId());
        List<StocktakeQueryItemParam> list = new Gson().fromJson(params,new TypeToken<List<StocktakeQueryItemParam>>() {}.getType());
        for (StocktakeQueryItemParam item: list) {
            if(StringUtils.isBlank(item.getAreaCd())){
                item.setAreaCd(null);
            }
        }
        //freemarker页面接收时不能有中括号
        String searchList = list.toString().replace("[","{").replace("]","}");
        mv.addObject("searchList",searchList);
        return mv;
    }*/

    // 商品盘点表
    @ResponseBody
    @RequestMapping("/queryItems")
    public GridDataDTO<StocktakeQueryItemsDTO> queryItems(HttpServletRequest request, HttpSession session,String searchJson,int page, int rows) {
        GridDataDTO<StocktakeQueryItemsDTO> result = stocktakeQueryService.queryItems(searchJson,page,rows);
//        return new Gson().toJson(stocktakeQueryService.getDetailsList(param));
        return result;
    }

    /*// 商品盘点表
    @ResponseBody
    @RequestMapping(value = "/queryItems",method={RequestMethod.POST,RequestMethod.GET})
    public String queryItems(HttpServletRequest request, HttpSession session,
                             Map<String, ?> model, StocktakeQueryParamDTO param) {
        return new Gson().toJson(stocktakeQueryService.getDetailsList(param));
    }*/

    /**
     * 请求大分类
     * @param request
     * @param session
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getDep", method = RequestMethod.GET)
    public String getTDep(HttpServletRequest request, HttpSession session,
                          Map<String, ?> model) {
        return new Gson().toJson(ma0070Service.getList());
    }

    /**
     * 请求中分类
     * @param request
     * @param session
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getPma", method = RequestMethod.GET)
    public String getPma(HttpServletRequest request, HttpSession session,
                         Map<String, ?> model,String depCd) {
        MA0080 ma0080 = new MA0080();
        ma0080.setDepCd(depCd);
        return new Gson().toJson(ma0070Service.getList(ma0080));
    }

    /**
     * 根据画面选择，获取Store权限
     */
    private Collection<String> getStores(HttpSession session,StocktakeQueryParam param){
        Collection<String> stores = new ArrayList<>();
        // 画面未选择，直接返回所有权限店铺
        if(StringUtils.isEmpty(param.getRegionCd()) && StringUtils.isEmpty(param.getCityCd())
                && StringUtils.isEmpty(param.getDistrictCd()) && StringUtils.isEmpty(param.getStoreCd())){
            stores = (Collection<String>) session.getAttribute(Constants.SESSION_STORES);
            return stores;
        }
        // 画面选择完成，返回已选择店铺
        if(StringUtils.isNotBlank(param.getStoreCd())){
            stores.add(param.getStoreCd());
            return stores;
        }
        // 只选择了一部分参数，生成查询参数，后台查询判断
        MRoleStoreParam dto = new MRoleStoreParam();
        dto.setRegionCd(param.getRegionCd());
        dto.setCityCd(param.getCityCd());
        dto.setDistrictCd(param.getDistrictCd());
                stores = (Collection<String>) session.getAttribute(Constants.SESSION_STORES);
        dto.setStoreCds(stores);
        return mRoleStoreService.getStoreByChoose(dto);
    }
}
