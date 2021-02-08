package cn.com.bbut.iy.itemmaster.service.audit;


import cn.com.bbut.iy.itemmaster.dto.audit.ApprovalRecordsGridDTO;
import cn.com.bbut.iy.itemmaster.dto.audit.ApprovalRecordsParamDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;

import java.util.List;

public interface ApprovalRecordsService {
    /**
     * 根据数据id查询审核记录
     */
    GridDataDTO<ApprovalRecordsGridDTO> selectApprovalRecords(ApprovalRecordsParamDTO paramDTO);
}
