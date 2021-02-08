package cn.com.bbut.iy.itemmaster.controller.audit;

import cn.com.bbut.iy.itemmaster.annotation.Secure;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.controller.BaseAction;
import cn.com.bbut.iy.itemmaster.dto.audit.ApprovalRecordsGridDTO;
import cn.com.bbut.iy.itemmaster.dto.audit.ApprovalRecordsParamDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.service.audit.ApprovalRecordsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 审核记录
 *
 * @author zcz
 */
@Controller
@Slf4j
@Secure
@RequestMapping(value = Constants.REQ_HEADER + "/approvalRecords")
public class ApprovalRecordsController extends BaseAction {
    @Autowired
    private ApprovalRecordsService approvalRecordsService;

    /**
     * 获取审核记录一览
     * @param request
     * @param session
     * @return
     */
    @RequestMapping(value = "/getApprovalRecords")
    @ResponseBody
    public GridDataDTO<ApprovalRecordsGridDTO> getList(HttpServletRequest request, HttpSession session,
                                                       ApprovalRecordsParamDTO param) {
        GridDataDTO<ApprovalRecordsGridDTO> grid = new GridDataDTO<>();
        if(param==null|| StringUtils.isBlank(param.getId())){
            return grid;
        }
        GridDataDTO<ApprovalRecordsGridDTO> approvalRecordsGridDTOGridDataDTO = approvalRecordsService.selectApprovalRecords(param);
        return approvalRecordsService.selectApprovalRecords(param);
    }
}
