package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.annotation.Permission;
import cn.com.bbut.iy.itemmaster.annotation.Secure;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.PermissionCode;
import cn.com.bbut.iy.itemmaster.dto.base.ReturnDTO;
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
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 紧急变价
 *
 * @author zcz
 */
@Controller
@Slf4j
@Secure
@RequestMapping(value = Constants.REQ_HEADER + "/priceChange")
public class PriceChangeController extends BaseAction {
    @Autowired
    private CM9060Service cm9060Service;
    @Autowired
    private SA0070Service sa0070Service;
    @Autowired
    private SuspendSaleService suspendSaleService;

    /**
     * 紧急变价登录画面
     * @param request
     * @param session
     * @param model
     * @return
     */
    @Permission(codes = { PermissionCode.CODE_SC_PRI_CAG_EDIT})
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView toPriceChangeEntey(HttpServletRequest request, HttpSession session,
                                   Map<String, ?> model) {
        User u = this.getUser(session);
        log.debug("User:{} 进入 紧急变价登录", u.getUserId());
        Collection<Integer> roleIds = (Collection<Integer>) request.getSession().getAttribute(
                Constants.SESSION_ROLES);
        //获取业务时间
        String date = cm9060Service.getValByKey("0000");
        ModelAndView mv = new ModelAndView("priceChange/priceChangeEntry");
        mv.addObject("accDate", date);//业务日期
        mv.addObject("useMsg", "紧急变价登录画面");
        return mv;
    }

    /**
     * 获取货号
     * @param session
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getNewPrice")
    public ResultDto getCashier(HttpServletRequest request, HttpSession session,
                                Map<String, ?> model, String barcode,String effectiveDate,String storeCd) {
        ResultDto resultDto = new ResultDto();
        resultDto.setSuccess(false);
        if(StringUtils.isNotBlank(barcode)){
            String articleId = sa0070Service.getArticleId(barcode);
            if(StringUtil.isNotBlank(articleId)){
                PriceSts priceSts = sa0070Service.getPriceSts(articleId);
                if(priceSts!=null){
                    if("10".equals(priceSts.getLifecycleStatus())){
                        resultDto.setMessage("The goods are not in normal condition and cannot be changed in an emergency！"); // 该商品非正常上架状态，不能紧急变价！
                        return resultDto;
                    }
                    if("1".equals(priceSts.getArticleType())){
                        resultDto.setMessage("This commodity is raw material and cannot be changed urgently！");/*该商品为原料，不能紧急变价！*/
                        return resultDto;
                    }
                    if("1".equals(priceSts.getStorePriceChgFlg())){
                        resultDto.setMessage("Emergency price changes are not allowed in this article！");/*该商品不允许紧急变价*/
                        return resultDto;
                    }
                    Price price = sa0070Service.getPrice(articleId,effectiveDate,storeCd);
                    if(price!=null){
                        resultDto.setSuccess(true);
                        resultDto.setData(price);
                    }else{
                        resultDto.setMessage("The bar code/article number does not exist, please re-enter it！");/*该条码/货号不存在，请重新输入！*/
                    }
                }else{
                    resultDto.setMessage("The bar code/article number does not exist, please re-enter it！");/*该条码/货号不存在，请重新输入！*/
                }
            }else{
                resultDto.setMessage("The bar code/article number does not exist, please re-enter it！");/*该条码/货号不存在，请重新输入！*/
            }
        }else{
            resultDto.setMessage("Barcode cannot be empty！");/*条码不能为空*/
        }
        return resultDto;
    }

    /**
     * 获取变价单号信息
     * @param session
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getChangeId")
    public ResultDto getChangeId(HttpServletRequest request, HttpSession session,
                                Map<String, ?> model, String changeId) {
        ResultDto resultDto = new ResultDto();
        resultDto.setSuccess(false);
        if(StringUtils.isNotBlank(changeId)){
            String resultId = sa0070Service.getChangeId(changeId);
            if(StringUtil.isBlank(resultId)){
                resultDto.setSuccess(true);
            }else{
                resultDto.setMessage("The order number already exists, please re-enter it！");/*该单号已存在，请重新输入！*/
            }
        }else{
            resultDto.setMessage("Please input the emergency price change number first！");/*单号不能为空！*/
        }
        return resultDto;
    }

    /**
     * 紧急变价信息保存
     * @param request
     * @param session
     * @return
     */
    @RequestMapping(value = "/save")
    @ResponseBody
    @Permission(codes = { PermissionCode.CODE_SC_PRI_CAG_EDIT})
    public ResultDto save(HttpServletRequest request, HttpSession session,
                                     String itemDetailJson, SA0070 param) {
        User user = this.getUser(session);
        log.debug("保存紧急变价信息，user:{}", user.getUserId());
        ResultDto res = new ResultDto();
        SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMdd");
        SimpleDateFormat sdf1 = new SimpleDateFormat("HHmmss");
        Date date = new Date();
        String ymd = sdf.format(date);
        String hms = sdf1.format(date);
        if(StringUtils.isBlank(param.getArticleId())||
                StringUtils.isBlank(param.getAccDate())){
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
    public String getArticleIdAndName(String v,String storeCd,String effectiveDate,HttpServletRequest request, HttpSession session){
        List<Price> list = sa0070Service.getArticleAndName(v,storeCd,effectiveDate);
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

    @RequestMapping(value = "/getBarcode",method = RequestMethod.POST)
    @ResponseBody
    public String getBarcodeByInfo(String articleId,String storeCd,String businessDate){

        String barcode = suspendSaleService.getBarcodeByInfo(articleId,storeCd,businessDate);
        if (barcode==null) {
            return null;
        }
        return barcode;
    }
}
