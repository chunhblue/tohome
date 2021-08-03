package cn.com.bbut.iy.itemmaster.service;

import cn.com.bbut.iy.itemmaster.dto.base.CommonDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.od0000_t.OD0000TDTO;
import cn.com.bbut.iy.itemmaster.dto.od0000_t.OD0000TParamDTO;
import cn.com.bbut.iy.itemmaster.dto.od0010_t.OD0010TDTO;

import java.util.List;
import java.util.Map;

/**
 * @author mxy
 */
public interface ReceiveService {
 
    /**
     * 根据订单号查询收货单
     * 
     * @param dto
     * @return
     */
    GridDataDTO<OD0000TDTO> getList(OD0000TParamDTO dto);

    /**
     * 查询头档信息
     *
     * @param dto
     * @return
     */
    OD0000TDTO getOD0000(OD0000TParamDTO dto);

    /**
     * 查询商品明细信息
     *
     * @param dto
     * @return
     */
    GridDataDTO<OD0010TDTO> getOD0010(OD0000TParamDTO dto);

    /**
     * 快速收货
     *
     * @param dto
     * @return
     */
    Map<String,String> insertByQuick(List<OD0000TDTO> list, CommonDTO dto);

    /**
     * 订单收货
     *
     * @param dto
     * @return
     */
    String insertByReceive(OD0000TDTO dto, List<OD0010TDTO> list);

    /**
     * 收货单修改
     *
     * @param dto
     * @return
     */
    String updateReceive(OD0000TDTO dto, List<OD0010TDTO> list);
    Integer insertDocumentUrl(OD0000TDTO dto);

    int updateStatus(String receiveId,CommonDTO dto);

    boolean receiveIdIsExist(String orderId);
}