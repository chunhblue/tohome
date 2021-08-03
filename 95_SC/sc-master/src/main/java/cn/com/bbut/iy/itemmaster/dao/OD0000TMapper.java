package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.CommonDTO;
import cn.com.bbut.iy.itemmaster.dto.od0000_t.OD0000TDTO;
import cn.com.bbut.iy.itemmaster.dto.od0000_t.OD0000TParamDTO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface OD0000TMapper {

    /**
     * 根据订单号查询收货单数量
     * @param dto
     */
    int selectCountByOrder(OD0000TParamDTO dto);

    /**
     * 根据订单号查询收货单
     * @param dto
     */
    List<OD0000TDTO> selectListByOrder(OD0000TParamDTO dto);

    /**
     * 根据主键查询头档信息
     * @param dto
     */
    OD0000TDTO selectByKey(OD0000TParamDTO dto);

    int deleteCopy(OD0000TDTO dto);

    /**
     * 复制订单头档信息
     * 收货金额 = 订货金额
     * @param dto
     */
    int insertByCopy(OD0000TDTO dto);

    /**
     * 添加数据
     * @param dto
     */
    int insertRecord(OD0000TDTO dto);

    /**
     * 更新数据
     * @param dto
     */
    int updateRecord(OD0000TDTO dto);

    /**
     * 根据主键删除记录
     * @param receiveId
     */
    int deleteByReceiveId(String receiveId);

    /**
     * 更新原订单头档信息
     * @param dto
     */
    int updateOldRecord(OD0000TDTO dto);

    int updateNewRecord(String receiveId, @Param("commonDTO") CommonDTO commonDTO,@Param("checkMouthFlg")Integer checkMouthFlg);
    /**
     * 查询待选
     * @param storeCd
     * @param v
     */
    List<AutoCompleteDTO> selectOrgOrderId(@Param("storeCd") String storeCd, @Param("v") String v);

    // 更新审核状态
    int updateReviewStatus(String receiveId);

    int orgOrderIdCount(@Param("receiveId")String receiveId);

    OD0000TDTO selectByOrder(String receiveId);

    OD0000TDTO getHHTreceiveDateTime(String orderId,String storeCd);


    OD0000TDTO receiveIdIsExist(String orderId);

}
