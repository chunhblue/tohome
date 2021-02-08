package cn.com.bbut.iy.itemmaster.service;

import cn.com.bbut.iy.itemmaster.dto.auditTask.AuditTaskGridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.auditTask.AuditTaskParamDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;

import java.util.List;

public interface AuditTaskService {
    /**
     * 查询角色待审核信息
     */
    GridDataDTO<AuditTaskGridDataDTO> selectAuditList(AuditTaskParamDTO auditTaskParamDTO);

    /**
     *  根据id查询待审核通知信息
     */
    AuditTaskGridDataDTO selectAuditById(Integer nRecordid);

}
