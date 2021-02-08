package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.annotation.Secure;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.dto.AjaxResultDto;
import cn.com.bbut.iy.itemmaster.dto.approval.ReviewGridDto;
import cn.com.bbut.iy.itemmaster.dto.approval.ReviewParamDto;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.service.ReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 审核流程
 *
 * @author zcz
 */
@Controller
@Slf4j
@Secure
@RequestMapping(value = Constants.REQ_HEADER + "/review")
public class ReviewController extends BaseAction {
    @Autowired
    private ReviewService reviewService;
    /**
     * 审核流程设定画面
     * @param request
     * @param session
     * @param model
     * @return
     */
    //@Permission(codes = { PermissionCode.SC_CODE_ORDER_VIEW})
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView tolistView(HttpServletRequest request, HttpSession session,
                                   Map<String, ?> model) {
        User u = this.getUser(session);
        log.debug("User:{} 进入 审核流程设定", u.getUserId());
        Collection<Integer> roleIds = (Collection<Integer>) request.getSession().getAttribute(
                Constants.SESSION_ROLES);
        ModelAndView mv = new ModelAndView("review/review");
        mv.addObject("useMsg", "审核流程设定画面");
        this.saveToken(request);
        return mv;
    }

    /**
     * 获取区域信息
     * @param session
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getApprovalType")
    public List<AutoCompleteDTO> getApprovalType(HttpSession session) {
        return reviewService.getApprovalType();
    }

    /**
     * 审核流程设定头档一览
     * @param request
     * @param session
     * @return
     */
    @RequestMapping(value = "/getReviewList")
    @ResponseBody
    public GridDataDTO<ReviewGridDto> getList(HttpServletRequest request, HttpSession session,
                                              ReviewParamDto param) {
        return reviewService.getReviewList(param);
    }

    /**
     * 删除审核流程
     * @param request
     * @param session
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/deleteById", method = RequestMethod.GET)
    public AjaxResultDto deleteById(HttpServletRequest request, HttpSession session,
                                    Map<String, ?> model, Integer nReviewid) {
        AjaxResultDto ard = ajaxRepeatSubmitCheck(request, session);
        if (!ard.isSuccess()) {
            ard.setToKen(ard.getToKen());
            return ard;
        }
        if( nReviewid !=null && nReviewid>0){
            int flg = reviewService.deleteReviewById(nReviewid);
            if(flg>0){
                ard.setSuccess(true);
            }else{
                ard.setSuccess(false);
                ard.setMessage("Data failed to delete!");
            }
        }else{
            ard.setSuccess(false);
            ard.setMessage("Approval ID cannot be empty!");
        }
        ard.setToKen(ard.getToKen());
        return ard;
    }

}
