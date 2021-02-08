package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.annotation.Permission;
import cn.com.bbut.iy.itemmaster.annotation.Secure;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.PermissionCode;
import cn.com.bbut.iy.itemmaster.dto.AjaxResultDto;
import cn.com.bbut.iy.itemmaster.dto.ExcelParam;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.CommonDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.base.ReturnDTO;
import cn.com.bbut.iy.itemmaster.dto.inventory.*;
import cn.com.bbut.iy.itemmaster.dto.mRoleStore.MRoleStoreParam;
import cn.com.bbut.iy.itemmaster.dto.ma8350.MA8350dto;
import cn.com.bbut.iy.itemmaster.dto.rtInventory.RTInventoryQueryDTO;
import cn.com.bbut.iy.itemmaster.dto.rtInventory.RtInvContent;
import cn.com.bbut.iy.itemmaster.entity.SK0010;
import cn.com.bbut.iy.itemmaster.entity.SK0010Key;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.entity.sa0160.SA0160;
import cn.com.bbut.iy.itemmaster.excel.ExService;
import cn.com.bbut.iy.itemmaster.service.*;
import cn.com.bbut.iy.itemmaster.service.inventory.InventoryVouchersService;
import cn.com.bbut.iy.itemmaster.util.ExportUtil;
import cn.shiy.common.baseutil.Container;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 库存传票一览
 *
 * @author mxy
 */
@Controller
@Slf4j
@Secure
@RequestMapping(value = Constants.REQ_HEADER + "/inventoryVoucher")
public class InventoryVouchersController extends BaseAction {

    @Autowired
    private CM9060Service cm9060Service;
    @Autowired
    private Ma1200Service ma1200Service;
    @Autowired
    private InventoryVouchersService service;
    @Autowired
    private UserService userService;
    @Autowired
    private MRoleStoreService mRoleStoreService;
    @Autowired
    private RealTimeInventoryQueryService rtInventoryService;
    @Value("${esUrl.inventoryUrl}")
    private String inventoryUrl;

    private final String EXCEL_EXPORT_KEY = "EXCEL_Inventory_Voucher";
    private final String EXCEL_EXPORT_NAME = "Inventory Voucher Query.xlsx";

    /**
     * 库存传票一览管理画面
     *
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    @Permission(codes = { PermissionCode.CODE_SC_ST_KCCPYL_LIST_VIEW})
    public ModelAndView tolistView(HttpServletRequest request, HttpSession session,
                                   Map<String, ?> model) {
        User u = this.getUser(session);
        log.debug("User:{} 进入 库存传票一览画面", u.getUserId());
        Collection<Integer> roleIds = (Collection<Integer>) request.getSession().getAttribute(
                Constants.SESSION_ROLES);
        ModelAndView mv = new ModelAndView("inventory_vouchers/vouchersList");
        mv.addObject("use", 0);
        mv.addObject("identity", 1);
        mv.addObject("useMsg", "库存传票一览画面");
        return mv;
    }

    /**
     * 库存传票一览查询
     *
     * @param request
     * @param session
     * @return
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/getdata")
    public GridDataDTO<InventoryVouchersGridDTO> getData(HttpServletRequest request, HttpSession session,
                                                         int page, int rows, String searchJson) {
        Gson gson = new Gson();
        InventoryVouchersParamDTO param = gson.fromJson(searchJson, InventoryVouchersParamDTO.class);
        if(param == null){
            param = new InventoryVouchersParamDTO();
        }
        param.setPage(page);
        param.setRows(rows);
        param.setLimitStart((page - 1)*rows);
        // 获取当前角色店铺权限
        Collection<String> stores = getStores(session, param);
        if(stores.size() == 0){
            log.info(">>>>>>>>>>>>>>>>>>>>> get stores is null");
            return new GridDataDTO<InventoryVouchersGridDTO>();
        }
        param.setStores(stores);

        return service.getListByCondition(param);
    }

    /**
     * 类型&条件查询传票
     *
     * @param request
     * @param session
     * @return
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/getDataByType")
    public GridDataDTO<InventoryVouchersGridDTO> getDataByType(HttpServletRequest request, HttpSession session,
                                                         int page, int rows, String searchJson) {
        Gson gson = new Gson();
        InventoryVouchersParamDTO param = gson.fromJson(searchJson, InventoryVouchersParamDTO.class);
        // 为空返回
        if(param == null){
            return new GridDataDTO<InventoryVouchersGridDTO>();
        }
        param.setPage(page);
        param.setRows(rows);
        param.setLimitStart((page - 1)*rows);
        // 获取当前角色店铺权限
        Collection<String> stores = getStores(session, param);
        if(stores.size() == 0){
            log.info(">>>>>>>>>>>>>>>>>>>>> get stores is null");
            return new GridDataDTO<InventoryVouchersGridDTO>();
        }
        param.setStores(stores);

        return service.getByTypeCondition(param);
    }

    /**
     * 类型&条件查询传票
     *
     * @param request
     * @param session
     * @return
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/getInDataByType")
    public GridDataDTO<InventoryVouchersGridDTO> getInDataByType(HttpServletRequest request, HttpSession session,int page, int rows,
                                                                 String searchJson) {
        Gson gson = new Gson();
        InventoryVouchersParamDTO param = gson.fromJson(searchJson, InventoryVouchersParamDTO.class);
        // 为空返回
        if(param == null){
            return new GridDataDTO<InventoryVouchersGridDTO>();
        }
        param.setPage(page);
        param.setRows(rows);
        param.setLimitStart((page - 1)*rows);
        // 获取当前角色店铺权限
        Collection<String> stores = getStores(session, param);
        if(stores.size() == 0){
            log.info(">>>>>>>>>>>>>>>>>>>>> get stores is null");
            return new GridDataDTO<InventoryVouchersGridDTO>();
        }
        param.setStores(stores);

        return service.getByTypeInCondition(param);
    }
    /**
     * 查询商品信息
     *
     * @param request
     * @param session
     * @return
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/getItem")
    public ReturnDTO getItemInfoByCode(HttpServletRequest request, HttpSession session,
                                       String storeCd, String itemCode) {
        ReturnDTO _return = new ReturnDTO();
        if(StringUtils.isBlank(itemCode) || StringUtils.isBlank(storeCd)){
            _return.setMsg("Parameter cannot be empty!");
            return _return;
        }
        ItemInfoDTO _dto = service.getItemInfoByCode(storeCd, itemCode);
        if(_dto == null){
            _return.setMsg("No data found!");
        }else{
            _return.setSuccess(true);
            _return.setO(_dto);
        }
        return _return;
    }

    /**
     * 查询店铺信息
     *
     * @param request
     * @param session
     * @return
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/getstore")
    public ReturnDTO getStoreByCode(HttpServletRequest request, HttpSession session,
                                       String storeCd) {
        ReturnDTO _return = new ReturnDTO();
        if(StringUtils.isBlank(storeCd)){
            _return.setMsg("Parameter cannot be empty!");
            return _return;
        }
        StoreInfoDTO _dto = service.getStoreByCode(storeCd);
        if(_dto == null){
            _return.setMsg("No data found!");
        }else{
            _return.setSuccess(true);
            _return.setO(_dto);
        }
        return _return;
    }

    /**
     * 查询用户信息
     *
     * @param request
     * @param session
     * @return
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/getuser")
    public ReturnDTO getUserName(HttpServletRequest request, HttpSession session,
                                       String userId) {
        ReturnDTO _return = new ReturnDTO();
        if(StringUtils.isBlank(userId)){
            _return.setMsg("User number cannot be empty!");
            return _return;
        }
        User user = userService.getFullUserById(userId);
        if(user == null){
            _return.setMsg("Query failed!");
        }else{
            _return.setSuccess(true);
            _return.setO(user.getUserName());
        }
        return _return;
    }

    /**
     * 查询实时库存数量
     *
     * @param request
     * @param session
     * @return
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/getStock")
    public ReturnDTO getStock(HttpServletRequest request, HttpSession session,
                                       String storeCd, String itemId) {
        String inEsTime = cm9060Service.getValByKey("1206");

        ReturnDTO _return = new ReturnDTO();
        if(StringUtils.isBlank(storeCd) || StringUtils.isBlank(itemId)){
            _return.setMsg("Param cannot be empty!");
            return _return;
        }
        //拼接url，转义参数
        String connUrl = inventoryUrl + "GetRelTimeInventory/" + storeCd + "/"
                + itemId + "/*/*/*/*/" + inEsTime+"/*/*";
        RTInventoryQueryDTO rTInventoryQueryDTO = rtInventoryService.getRtInventory(connUrl);
        if(rTInventoryQueryDTO.getRealtimeQty() == null){
            rTInventoryQueryDTO.setRealtimeQty(BigDecimal.ZERO);
        }
//        String stock = service.getStock(storeCd, itemId);
        _return.setSuccess(true);
        _return.setO(rTInventoryQueryDTO);

        return _return;
    }

    /**
     * 获得 store 列表
     */
    @ResponseBody
    @RequestMapping(value = "/getStoreList")
    public List<AutoCompleteDTO> getStoreList(HttpServletRequest request, HttpSession session,
                                              String v) {
        List<AutoCompleteDTO> _list = service.getStoreList(v);
        return _list;
    }
    @ResponseBody
    @RequestMapping(value = "/getOutStoreList")
    public List<AutoCompleteDTO> getOutStoreList(HttpServletRequest request, HttpSession session,
                                              String v,String zoCd) {
        List<AutoCompleteDTO> _list = service.getOutStoreList(v,zoCd);
        return _list;
    }
    /**
     * 判断商品是否已经转出
     * @param storeCd
     * @param storeCd1
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getExistsDoc")
    public AjaxResultDto getExistsDoc(@RequestParam("tstore")String storeCd,
                                      @RequestParam("vstore")String storeCd1) {
        AjaxResultDto res = new AjaxResultDto();
        res.setSuccess(false);
        if (StringUtils.isNotBlank(storeCd) || StringUtils.isNotBlank(storeCd1)) {
            List<InventoryVouchersGridDTO> doc = service.existsDOC(storeCd, storeCd1);
            if (doc!=null&&doc.size()>0){
                res.setData(doc);
                res.setSuccess(true);
            }else{
                res.setMessage("Get Doc information is empty！");
            }
        }else{
            res.setMessage("Store No. is empty！");
        }
        return res;
    }
    /**
     * 获得 转出门店单号 列表
     */
    @ResponseBody
    @RequestMapping(value = "/getDOCList")
    public List<AutoCompleteDTO> getDOCList(@RequestParam("tstore")String storeCd,
                                            @RequestParam("vstore")String storeCd1,String v) {
        if(StringUtils.isBlank(storeCd) && StringUtils.isBlank(storeCd1)){
            return null;
        }
        List<AutoCompleteDTO> _list = service.getDOCList(storeCd, storeCd1, v);
        return _list;
    }

    /**
     * 判断转出商品是否已经通过审核
     * @param storeCd
     * @param storeCd1
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getApprovedList")
    public AjaxResultDto getApprovedList(@RequestParam("tstore")String storeCd,
                                      @RequestParam("vstore")String storeCd1) {
        AjaxResultDto res = new AjaxResultDto();
        res.setSuccess(false);
        if (StringUtils.isNotBlank(storeCd) || StringUtils.isNotBlank(storeCd1)) {
            List<InventoryVouchersGridDTO> doc = service.getApprovedList(storeCd, storeCd1);
            if (doc!=null&&doc.size()>0){
                res.setData(doc);
                res.setSuccess(true);
            }else{
                res.setMessage("Get Out Doc information is empty！");
            }
        }else{
            res.setMessage("Store No. is empty！");
        }
        return res;
    }

    /**
     * 获得 item 列表
     */
    @ResponseBody
    @RequestMapping(value = "/getItemList")
    public List<AutoCompleteDTO> getItemList(HttpServletRequest request, HttpSession session,
                                             String storeCd, String v) {
        if(StringUtils.isBlank(storeCd)){
            return null;
        }
        List<AutoCompleteDTO> _list = service.getItemList(storeCd, v);
        return _list;
    }

    /**
     * 保存传票
     *
     * @param request
     * @param session
     * @return
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/save")
    public  ReturnDTO saveInventoryVouchers(HttpServletRequest request, HttpSession session,
                                       String searchJson, String listJson,String fileDetailJson) {
        
        ReturnDTO _return = new ReturnDTO();

        if(StringUtils.isBlank(searchJson) || StringUtils.isBlank(listJson)){
            _return.setMsg("Parameter cannot be empty!");
            return _return;
        }
        // 转换参数对象
        Gson gson = new Gson();
        Sk0010DTO sk0010 = gson.fromJson(searchJson, Sk0010DTO.class);
        if(sk0010 == null){
            _return.setMsg("Failed to get subpoena information");/*获取传票信息失败*/
            return _return;
        }
        List<Sk0020DTO> sk0020List = gson.fromJson(listJson, new TypeToken<List<Sk0020DTO>>(){}.getType());
        if(sk0020List == null || sk0020List.size() == 0){
            _return.setMsg("Failed to get the details of the subpoena!");/*获取传票详情失败*/
            return _return;
        }
        // 获取当前用户、时间
        CommonDTO dto = getCommonDTO(session);
        if(dto == null){
            _return.setMsg("Failed to get user information!");
            return _return;
        }
        sk0010.setCommonDTO(dto);
        sk0010.setFileDetailJson(fileDetailJson);
        String _id = service.insert(sk0010, sk0020List);
        if(StringUtils.isBlank(_id)){
            _return.setMsg("Data saved failed！");
        }else {
            _return.setMsg("Data saved successfully！");
            _return.setSuccess(true);
            _return.setO(_id);
            _return.setDate(dto.getCreateYmd());
            _return.setHms(dto.getCreateHms());
            _return.setCreateUserId(dto.getCreateUserId());
        }

        return _return;
    }

    /**
     * 修改传票
     *
     * @param request
     * @param session
     * @return
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/modify")
    public ReturnDTO modifyInventoryVouchers(HttpServletRequest request, HttpSession session,
                                       String searchJson, String listJson ,String fileDetailJson , String pageType) {
        ReturnDTO _return = new ReturnDTO();
        if(StringUtils.isBlank(searchJson) || StringUtils.isBlank(listJson)){
            _return.setMsg("Parameter cannot be empty!");
            return _return;
        }
        // 转换参数对象
        Gson gson = new Gson();
        SK0010 sk0010 = gson.fromJson(searchJson, SK0010.class);
        if(sk0010 == null){
            _return.setMsg("Failed to get subpoena information");/*获取传票信息失败*/
            return _return;
        }
        List<Sk0020DTO> sk0020List = gson.fromJson(listJson, new TypeToken<List<Sk0020DTO>>(){}.getType());
        if(sk0020List == null || sk0020List.size() == 0){
            _return.setMsg("Failed to get the details of the subpoena!");/*获取传票详情失败*/
            return _return;
        }
        // 获取当前用户、时间
        CommonDTO dto = getCommonDTO(session);
        if(dto == null){
            _return.setMsg("Failed to get user information!");
            return _return;
        }
        // 判断母货号是否允许操作
        String _val = cm9060Service.getValByKey("0634");
        boolean parentFlg = "0".equals(_val) ? true : false;
        List<String> parentList = new ArrayList<>();
        // 店间调拨和报废需要判断数量是否超出库存数量
        boolean isCheck = "9".equals(pageType) ? true : false;
        boolean checkFlg = false;
        for(Sk0020DTO bean : sk0020List){
            bean.setCommonDTO(dto);
            if(!parentFlg){
                // 后续判断是否为母货号
                parentList.add(bean.getArticleId());
            }
            if(isCheck){
                // 调整单不执行后面判断
                continue;
            }
            String inEsTime = cm9060Service.getValByKey("1206");
            //拼接url，转义参数
            String connUrl = inventoryUrl + "GetRelTimeInventory/" + bean.getStoreCd() + "/"
                    + bean.getArticleId() + "/*/*/*/*/" + inEsTime+"/*/*";
            RTInventoryQueryDTO rTInventoryQueryDTO = rtInventoryService.getRtInventory(connUrl);
            if(rTInventoryQueryDTO.getRealtimeQty() == null){
                rTInventoryQueryDTO.setRealtimeQty(BigDecimal.ZERO);
            }
            String stock = rTInventoryQueryDTO.getRealtimeQty().toString();
            if(StringUtils.isBlank(stock)){
                checkFlg = true;
                break;
            }
            BigDecimal temp = new BigDecimal(stock);
            BigDecimal qty = bean.getQty1();
            if(qty.compareTo(temp) == 1){
                checkFlg = true;
                break;
            }
        }
        if(checkFlg){
            _return.setMsg("Transfer Quantity cannot be more than actual stock quantity, please check the data!");
            return _return;
        }
        // 判断是否存在母货号
        List<String> result = new ArrayList<>();
        if(!parentFlg && parentList.size()>0){
            result = ma1200Service.checkList(parentList);
        }
        if(result.size()>0){
            String msg = "";
            for(String temp : result){
                msg = "Item " + temp + ",";
            }
            msg = msg + " cannot operation a combination of goods!";
            _return.setMsg(msg);
            return _return;
        }
        // 执行保存
        sk0010.setFileDetailJson(fileDetailJson);
        sk0010.setUpdateUserId(dto.getUpdateUserId());
        sk0010.setUpdateYmd(dto.getUpdateYmd());
        sk0010.setUpdateHms(dto.getUpdateHms());
        int i = service.update(sk0010, sk0020List);
        if(i < 1){
            _return.setMsg("Data saved failed！");
        }else {
            _return.setMsg("Data saved successfully！");
            _return.setDate(dto.getCreateYmd());
            _return.setHms(dto.getCreateHms());
            _return.setCreateUserId(dto.getCreateUserId());
            _return.setSuccess(true);
        }
        return _return;
    }
    @ResponseBody
    @RequestMapping(value = "/getTotal")
    public ReturnDTO getTotal(String searchJson){
        Gson gson = new Gson();
        Sk0020ParamDTO sk0020 = gson.fromJson(searchJson, Sk0020ParamDTO.class);
        Map<String, Object> result=service.Total(sk0020);
        return new ReturnDTO(true,"ok",result);
    }

    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/updateQty1")
    public ReturnDTO modifyQty1(@RequestParam("qty1")BigDecimal qty1,
                                @RequestParam("voucherNo")String voucherNo1,@RequestParam("articleId")String articleId) {
        ReturnDTO _return = new ReturnDTO();
        Sk0020DTO sk0020DTO = new Sk0020DTO();
        sk0020DTO.setQty1(qty1);
        sk0020DTO.setVoucherNo(voucherNo1);
        sk0020DTO.setArticleId(articleId);
        int i = service.updateQty1(sk0020DTO);
        if(i < 1){
            _return.setMsg("Data modify failed！");
        }else {
            _return.setSuccess(true);
        }
        return _return;
    }

    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/updateDiffReasons")
    public ReturnDTO updateDiffReasons(@RequestParam("differenceReason")String differenceReason,
                                @RequestParam("voucherNo")String voucherNo1,@RequestParam("articleId")String articleId) {
        ReturnDTO _return = new ReturnDTO();
        Sk0020DTO sk0020DTO = new Sk0020DTO();
        sk0020DTO.setDifferenceReason(differenceReason);
        sk0020DTO.setVoucherNo(voucherNo1);
        sk0020DTO.setArticleId(articleId);
        int i = service.updateDiffReasons(sk0020DTO);
        if(i < 1){
            _return.setMsg("Data modify failed！");
        }else {
            _return.setSuccess(true);
        }
        return _return;
    }

    /**
     * 查询头档
     *
     * @param request
     * @param session
     * @return
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/get")
    public ReturnDTO getInventoryVouchers(HttpServletRequest request, HttpSession session,
                                       String searchJson) {
        ReturnDTO _return = new ReturnDTO();
        if(StringUtils.isBlank(searchJson)){
            _return.setMsg("Parameter cannot be empty!");
            return _return;
        }
        // 转换参数对象
        Gson gson = new Gson();
        SK0010Key sk0010 = gson.fromJson(searchJson, SK0010Key.class);
        if(sk0010 == null){
            _return.setMsg("Parameter cannot be empty!");
            return _return;
        }

        // 执行查询
        SK0010 _dto = service.getSk0010(sk0010);

        if(_dto == null){
            _return.setMsg("Query failed!");
        }else {
            _return.setMsg("Query succeeded!");
            _return.setO(_dto);
            _return.setSuccess(true);
        }
        return _return;
    }

    /**
     * 查询详情
     *
     * @param request
     * @param session
     * @return
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/getDetails")
    public GridDataDTO<Sk0020DTO> getDetails(HttpServletRequest request, HttpSession session,
                                             int page, int rows, String searchJson) {
        // 转换参数对象
        Gson gson = new Gson();
        Sk0020ParamDTO sk0020 = gson.fromJson(searchJson, Sk0020ParamDTO.class);
        if(sk0020 == null){
            return new GridDataDTO<Sk0020DTO>();
        }
        // 执行查询
        return service.getSk0020(sk0020);
    }
    @ResponseBody
    @RequestMapping(value = "/getDetailsOut")
    public GridDataDTO<Sk0020DTO> getDetailsout(HttpServletRequest request, HttpSession session,
                                             int page, int rows, String searchJson) {
        // 转换参数对象
        Gson gson = new Gson();
        Sk0020ParamDTO sk0020 = gson.fromJson(searchJson, Sk0020ParamDTO.class);
        if(sk0020 == null){
            return new GridDataDTO<Sk0020DTO>();
        }
        // 执行查询
        return service.getSk0020Out(sk0020);
    }
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/selectDetails")
    public GridDataDTO<Sk0020DTO> selectDetails(HttpServletRequest request, HttpSession session,
                                             int page, int rows, String searchJson) {
        // 转换参数对象
        Gson gson = new Gson();
        Sk0020ParamDTO sk0020 = gson.fromJson(searchJson, Sk0020ParamDTO.class);
        if(sk0020 == null){
            return new GridDataDTO<Sk0020DTO>();
        }
        // 执行查询
        return service.selectDetailSk0020(sk0020);
    }
    /**
     * 查询传票编号是否存在
     *
     * @param request
     * @param session
     * @return
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/checkNo")
    public ReturnDTO getRecordByNo(HttpServletRequest request, HttpSession session,
                                          String voucherNo) {
        ReturnDTO _return = new ReturnDTO();
        if(StringUtils.isBlank(voucherNo)){
            _return.setMsg("The Document No cannot be empty!");
            return _return;
        }

        // 执行查询
        int count = service.getSk0010ByVoucherNo(voucherNo);
        if(count > 0){
            _return.setMsg("The Document No already exists!");
        }else {
            _return.setMsg("传票编号不存在");
            _return.setSuccess(true);
        }
        return _return;
    }

    /**
     * 导出查询结果
     *
     * @param request
     * @param session
     * @param searchJson
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/export")
    @Permission(codes = { PermissionCode.CODE_SC_ST_KCCPYL_EXPORT })
    public String export(HttpServletRequest request, HttpSession session, String searchJson) {
        if(StringUtils.isBlank(searchJson)){
            return null;
        }
        Gson gson = new Gson();
        InventoryVouchersParamDTO param = gson.fromJson(searchJson, InventoryVouchersParamDTO.class);
        // 获取当前角色店铺权限
        Collection<String> stores = getStores(session, param);
        if(stores.size() == 0){
            log.info(">>>>>>>>>>>>>>>>>>>>> get stores is null");
            return null;
        }
        // 设置参数对象
        ExcelParam exParam = new ExcelParam();
        exParam.setStores(stores);
        exParam.setParam(searchJson);
        exParam.setPCode(PermissionCode.CODE_SC_ZD_VMQ_EXPORT);
        exParam.setExFileName(EXCEL_EXPORT_NAME);
        ExService service = Container.getBean("inVoucherExService", ExService.class);
        return ExportUtil.export(EXCEL_EXPORT_KEY, request, service, exParam);
    }

    /**
     * 生成传票编号
     */
    private String getVoucherId(Sk0010DTO sk0010){
        String _id = null;
        // 时间转换
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmssSSS");
        // 当前时间年月日
        String date = dateFormat.format(now);
        String _type = sk0010.getVoucherType();
        if(StringUtils.isNotBlank(_type)){
            _id = "D" + _type + date;
        }
        return _id;
    }

    /**
     * 获取当前操作用户、时间
     */
    private CommonDTO getCommonDTO(HttpSession session){
        User u = this.getUser(session);
        if(u == null){
            return null;
        }
        CommonDTO dto = new CommonDTO();
        // 当前用户ID
        dto.setUpdateUserId(u.getUserId());
        dto.setCreateUserId(u.getUserId());

        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HHmmss");
        // 当前时间年月日
        String date = dateFormat.format(now);
        dto.setCreateYmd(date);
        dto.setUpdateYmd(date);
        // 当前时间时分秒
        String time = timeFormat.format(now);
        dto.setCreateHms(time);
        dto.setUpdateHms(time);
        return dto;
    }

    /**
     * 根据画面选择，获取Store权限
     */
    private Collection<String> getStores(HttpSession session,InventoryVouchersParamDTO param){
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
        // 只选择了一部分参数，生成查询参数，后台查询
        MRoleStoreParam dto = new MRoleStoreParam();
        dto.setRegionCd(param.getRegionCd());
        dto.setCityCd(param.getCityCd());
        dto.setDistrictCd(param.getDistrictCd());
                stores = (Collection<String>) session.getAttribute(Constants.SESSION_STORES);
        dto.setStoreCds(stores);
        return mRoleStoreService.getStoreByChoose(dto);
    }

    @ResponseBody
    @RequestMapping(value = "/generalReason")
    public List<AutoCompleteDTO> getMa8350(HttpServletRequest request, HttpSession session,String v) {
        List<AutoCompleteDTO> list = service.generalReason(v);
        return list;
    }
    @ResponseBody
    @RequestMapping(value = "/getGeneralLevel")
    public MA8350dto getAboutMa8350(HttpServletRequest request, HttpSession session,String generalLevelCd) {
        return  service.getGeneralReason(generalLevelCd);
    }
    @ResponseBody
    @RequestMapping(value = "/getGeneralLevelReason")
    public MA8350dto getGeneralReason(HttpServletRequest request, HttpSession session,String detailedLevelCd) {

        return    service.getGeneralReasonDetail(detailedLevelCd);
    }
    @ResponseBody
    @RequestMapping(value = "/detailReason")
    public List<AutoCompleteDTO> getMa8360(HttpServletRequest request, HttpSession session,String generalLevelCd,String v) {
        List<AutoCompleteDTO> list;
        if(StringUtils.isBlank(generalLevelCd)){
            list =  service.Reasondetail(v);
        }else {
            list = service.detailReason(generalLevelCd,v);
        }
        return list;
    }

}
