package cn.com.bbut.iy.itemmaster.dao.audit;

import cn.com.bbut.iy.itemmaster.dto.audit.ApprovalRecordsGridDTO;
import cn.com.bbut.iy.itemmaster.dto.audit.ApprovalRecordsParamDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ApprovalRecordsMapper {
    /**
     * 根据数据id查询审核记录
     */
    List<ApprovalRecordsGridDTO> selectApprovalRecords(ApprovalRecordsParamDTO paramDTO);

    long selectApprovalRecordsCount(ApprovalRecordsParamDTO paramDTO);
}
