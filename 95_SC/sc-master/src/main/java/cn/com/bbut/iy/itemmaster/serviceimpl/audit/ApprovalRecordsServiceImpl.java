package cn.com.bbut.iy.itemmaster.serviceimpl.audit;

import cn.com.bbut.iy.itemmaster.constant.ConstantsAudit;
import cn.com.bbut.iy.itemmaster.dao.audit.ApprovalRecordsMapper;
import cn.com.bbut.iy.itemmaster.dto.audit.ApprovalRecordsGridDTO;
import cn.com.bbut.iy.itemmaster.dto.audit.ApprovalRecordsParamDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.service.audit.ApprovalRecordsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
public class ApprovalRecordsServiceImpl implements ApprovalRecordsService {
    @Autowired
    private ApprovalRecordsMapper approvalRecordsMapper;

    @Override
    public GridDataDTO<ApprovalRecordsGridDTO> selectApprovalRecords(ApprovalRecordsParamDTO paramDTO) {
        if(paramDTO.getReviewId()>0){
           int typeId =  ConstantsAudit.getTypeIdByReviewId(paramDTO.getReviewId());
            paramDTO.setTypeIdArray(new int[]{typeId});
        }
        List<ApprovalRecordsGridDTO> list = approvalRecordsMapper.selectApprovalRecords(paramDTO);
        long count = approvalRecordsMapper.selectApprovalRecordsCount(paramDTO);
        return new GridDataDTO<>(list, paramDTO.getPage(), count,
                paramDTO.getRows());
    }

}
