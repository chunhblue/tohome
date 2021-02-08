package cn.com.bbut.iy.itemmaster.serviceimpl;

import cn.com.bbut.iy.itemmaster.dao.AuditTaskMapper;
import cn.com.bbut.iy.itemmaster.dto.auditTask.AuditTaskGridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.auditTask.AuditTaskParamDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.service.AuditTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("AuditTaskServiceImpl")
public class AuditTaskServiceImpl implements AuditTaskService {

    @Autowired
    AuditTaskMapper auditTaskMapper;

    @Override
    public GridDataDTO<AuditTaskGridDataDTO> selectAuditList(AuditTaskParamDTO auditTaskParamDTO) {
        int count = auditTaskMapper.selectCount(auditTaskParamDTO);
        if(count < 1){
            return new GridDataDTO<AuditTaskGridDataDTO>();
        }

        List<AuditTaskGridDataDTO> _list = auditTaskMapper.getShortcutList(auditTaskParamDTO);
        GridDataDTO<AuditTaskGridDataDTO> data = new GridDataDTO<>(_list,
                auditTaskParamDTO.getPage(), count, auditTaskParamDTO.getRows());


        return data;
    }

    /**
     * 根据id取得记录
     * @param nRecordid
     * @return
     */
    @Override
    public AuditTaskGridDataDTO selectAuditById(Integer nRecordid) {

        return auditTaskMapper.selectAuditById(nRecordid);
    }


}
