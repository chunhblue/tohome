package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.annotation.Permission;
import cn.com.bbut.iy.itemmaster.annotation.Secure;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.PermissionCode;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.ReturnDTO;
import cn.com.bbut.iy.itemmaster.dto.mRoleStore.MRoleStoreParam;
import cn.com.bbut.iy.itemmaster.dto.order.ResultDto;
import cn.com.bbut.iy.itemmaster.dto.priceChange.Price;
import cn.com.bbut.iy.itemmaster.dto.priceChange.PriceSts;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.entity.sa0070.SA0070;
import cn.com.bbut.iy.itemmaster.service.CM9060Service;
import cn.com.bbut.iy.itemmaster.service.SA0070Service;
import cn.com.bbut.iy.itemmaster.service.suspendSale.SuspendSaleService;
import cn.com.bbut.iy.itemmaster.util.StringUtil;
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
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 暂停销售紧急录入
 *
 * @author ty
 */
@Controller
@Slf4j
@Secure
@RequestMapping(value = Constants.REQ_HEADER + "/suspendSaleEntry")
public class SuspendSaleEntryController extends BaseAction {
    @Autowired
    private CM9060Service cm9060Service;
    @Autowired
    private SA0070Service sa0070Service;

    @Autowired
    private SuspendSaleService suspendSaleService;

    /**
     * 获取店铺List
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/getStoreListByDistrictCd")
    public List<AutoCompleteDTO> getStoreListByDistrictCd(String cityCd,String districtCd,String articleId,String accDate) {
        return suspendSaleService.getStoreListByDistrictCd(cityCd,districtCd,articleId,accDate);
    }

    /**
     * 获取店铺组织架构数据数据
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/selectListByLevel")
    public List<AutoCompleteDTO> selectListByLevel(String level,String adminId,String articleId,String accDate,String v) {
        return suspendSaleService.selectListByLevel(level,adminId,articleId,accDate,v);
    }

    /**
     * 暂停销售紧急录入画面
     *
     * @param request
     * @param session
     * @param model
     * @return
     */
    @Permission(codes = { PermissionCode.CODE_SC_SUS_ENTRY_EDIT})
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView toSuspendSaleEntry(HttpServletRequest request, HttpSession session,
                                           Map<String, ?> model) {
        User u = this.getUser(session);
        log.debug("User:{} 进入 暂停销售紧急录入", u.getUserId());
        Collection<Integer> roleIds = (Collection<Integer>) request.getSession().getAttribute(
                Constants.SESSION_ROLES);
        //获取业务时间
        String date = cm9060Service.getValByKey("0000");
        ModelAndView mv = new ModelAndView("suspendSale/suspendSaleEntry");
        mv.addObject("accDate", date);//业务日期
        mv.addObject("useMsg", "暂停销售紧急录入");
        return mv;
    }

    /**
     * 获取货号
     *
     * @param session
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getArticleId")
    public ResultDto getCashier(HttpServletRequest request, HttpSession session,
                                Map<String, ?> model, String barcode, String effectiveDate, String storeCd) {
        ResultDto resultDto = new ResultDto();
        resultDto.setSuccess(false);

        if (StringUtils.isEmpty(barcode) || StringUtils.isEmpty(effectiveDate) || StringUtils.isEmpty(storeCd)) {
            resultDto.setMessage("Parameter cannot be empty！");/*参数不能为空*/
            return resultDto;
        }

        // 获取商品id
        String articleId = sa0070Service.getArticleId(barcode);
        if (StringUtils.isEmpty(articleId)) {
            resultDto.setMessage("The bar code/article number does not exist, please re-enter it！");/*该条码/货号不存在，请重新输入！*/
            return resultDto;
        }

        // 获取商品状态
        PriceSts priceSts = sa0070Service.getPriceSts(articleId);
        if (priceSts == null) {
            resultDto.setMessage("The bar code/article number does not exist, please re-enter it！");/*该条码/货号不存在，请重新输入！*/
            return resultDto;
        }
        if ("10".equals(priceSts.getLifecycleStatus())) {
            resultDto.setMessage("This goods is not in the normal status of being put on the shelves, so sales cannot be suspended!");/*该商品非正常上架状态，不能暂停销售！*/
            return resultDto;
        }
        if ("1".equals(priceSts.getArticleType())) {
            resultDto.setMessage("This goods is a raw material and cannot be suspended sales!");/*该商品为原料，不能暂停销售！*/
            return resultDto;
        }

        // 获取商品信息
        // 紧急变价和暂停销售字段相同, 所以用的同一个DTO
        Price price = suspendSaleService.getPrice(articleId, effectiveDate);
        if (price != null) {
            resultDto.setSuccess(true);
            resultDto.setData(price);
        } else {
            resultDto.setMessage("The bar code/article number does not exist, please re-enter it！");/*该条码/货号不存在，请重新输入！*/
        }

        return resultDto;
    }

    /**
     * 获取变价单号信息
     *
     * @param session
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getDocId")
    public ResultDto getChangeId(HttpServletRequest request, HttpSession session,
                                 Map<String, ?> model, String docId) {
        ResultDto resultDto = new ResultDto();
        resultDto.setSuccess(false);
        if (StringUtils.isNotBlank(docId)) {
            String resultId = suspendSaleService.getDocId(docId);
            if (StringUtil.isBlank(resultId)) {
                resultDto.setSuccess(true);
            } else {
                resultDto.setMessage("The 'Doc.No' already exists, please re-enter!");
            }
        } else {
            resultDto.setMessage("'Doc.No' cannot be empty！");
        }
        return resultDto;
    }

    /**
     * 暂停销售信息保存
     * 字段和sa0070紧急变价相同, 用了同一个DTO
     * @param request
     * @param session
     * @return
     */
    @RequestMapping(value = "/save")
    @ResponseBody
    @Permission(codes = { PermissionCode.CODE_SC_SUS_ENTRY_EDIT})
    public ResultDto save(HttpServletRequest request, HttpSession session,
                          String itemDetailJson, SA0070 param) {
        User user = this.getUser(session);
        log.debug("保存暂停销售信息，user:{}", user.getUserId());
        ResultDto res = new ResultDto();
        SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMdd");
        SimpleDateFormat sdf1 = new SimpleDateFormat("HHmmss");
        Date date = new Date();
        String ymd = sdf.format(date);
        String hms = sdf1.format(date);
        if (StringUtils.isBlank(param.getArticleId()) ||
                StringUtils.isBlank(param.getAccDate())) {
            res.setSuccess(false);
            res.setMessage("Save failed! Parameter cannot be empty!");
            return res;
        }
        param.setCreateUserId(user.getUserId());
        param.setCreateYmd(ymd);
        param.setCreateHms(hms);
        ReturnDTO result = sa0070Service.insertChangePrice(session, request, param, itemDetailJson);
        if (result.isSuccess()) {
            param.setChangeId(String.valueOf(result.getO()));
        }
        res.setSuccess(result.isSuccess());
        res.setData(param);
        return res;
    }
    //获取所有ArticleId
    @RequestMapping(value = "/getAllArticleIdAndName")
    @ResponseBody
    public String getArticleIdAndName(String v,String storeCd, String effectiveDate,HttpServletRequest request, HttpSession session,Map<String, ?> model){
        List<Price> list = sa0070Service.getArticleAndName(v, storeCd, effectiveDate);
        return  new Gson().toJson(list);
    }

    @RequestMapping(value = "/getItemInfo")
    @ResponseBody
    public ReturnDTO getItemInfo(String articleId, String effectiveDate, HttpServletRequest request, HttpSession session){
        Price price = suspendSaleService.getPrice(articleId,effectiveDate);
        if (price==null) {
            return new ReturnDTO(false,"Query result is empty!");
        }
        return new ReturnDTO(true,"Query succeeded!",price);
    }
}
