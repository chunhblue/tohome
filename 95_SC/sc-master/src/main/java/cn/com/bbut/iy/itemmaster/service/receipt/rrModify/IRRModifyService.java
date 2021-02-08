package cn.com.bbut.iy.itemmaster.service.receipt.rrModify;

import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.receipt.rrModify.RRModifyDTO;
import cn.com.bbut.iy.itemmaster.dto.receipt.rrModify.RRModifyDetailsDTO;
import cn.com.bbut.iy.itemmaster.dto.receipt.rrModify.RRModifyParamDTO;
import cn.com.bbut.iy.itemmaster.entity.od0010.OD0010;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 进退货传票修正Service
 *
 */
public interface IRRModifyService {

    /**
     * 条件查询记录
     *
     */
    GridDataDTO<RRModifyDTO> getList(RRModifyParamDTO param);

    /**
     * 主键查询头档
     *
     */
    RRModifyDTO getRecordByKey(RRModifyParamDTO param);

    /**
     * 查询明细详情
     *
     */
    GridDataDTO<RRModifyDetailsDTO> getDetail(RRModifyParamDTO param);

    RRModifyDTO getdirectItemInfo(RRModifyParamDTO dto);

    /**
     * 保存记录
     *
     */
    String insertRecord(RRModifyDTO dto, List<OD0010> list);

    /**
     * 获取原订单待选list
     *
     */
    List<AutoCompleteDTO> getOrgOrderId(String type, String storeCd, String v);

    int countOrderId(String receiveId);

    int updateLastCorr(String orderId);
}
