package cn.com.bbut.iy.itemmaster.dao;


import cn.com.bbut.iy.itemmaster.dto.auditTask.AuditTaskGridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.auditTask.AuditTaskParamDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AuditTaskMapper {

    /**
     * 查询角色待审核信息
     */
    List<AuditTaskGridDataDTO> getShortcutList(AuditTaskParamDTO auditTaskParamDTO);
    /**
     * 查询总条数
     */
    int selectCount(AuditTaskParamDTO auditTaskParamDTO);

    /**
     *  根据id查询待审核通知信息
     */
    AuditTaskGridDataDTO selectAuditById(Integer nRecordid);
}
