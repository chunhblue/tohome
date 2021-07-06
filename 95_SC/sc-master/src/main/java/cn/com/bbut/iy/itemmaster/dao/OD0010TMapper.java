package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dto.od0000_t.OD0000TDTO;
import cn.com.bbut.iy.itemmaster.dto.od0000_t.OD0000TParamDTO;
import cn.com.bbut.iy.itemmaster.dto.od0010_t.OD0010TDTO;
import cn.com.bbut.iy.itemmaster.dto.receipt.rrModify.RRModifyDetailsDTO;
import cn.com.bbut.iy.itemmaster.dto.receipt.rrModify.RRModifyParamDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface OD0010TMapper {

    /**
     * 查询DC商品详情
     * @param dto
     */
    List<OD0010TDTO> selectDcDetail(OD0000TParamDTO dto);
    /**
     * 查询供应商商品详情
     * @param dto
     */
    List<OD0010TDTO> selectVendorDetail(OD0000TParamDTO dto);

    /**
     * 修正传票获取原数据
     * @param dto
     */
    List<RRModifyDetailsDTO> selectReceiveModify(RRModifyParamDTO dto);

    int selectCountReceiveModify(RRModifyParamDTO dto);


    int deleteCopy(OD0000TDTO dto);
    /**
     * 复制供应商订单商品信息
     * 收货数量、金额 = 订货数量、金额
     * @param dto
     */
    int insertByCopy(OD0000TDTO dto);

    /**
     * 复制DC订单商品信息
     * 收货数量、金额 = 订货数量、金额
     * @param dto
     */
    int insertDCByCopy(OD0000TDTO dto);

    /**
     * 根据主键添加收货数据
     * 赋值收货数量、金额
     * @param dto
     */
    int insertByKey(OD0010TDTO dto);

    /**
     * 更新原订单明细信息
     * @param dto
     */
    int updateOldDetails(OD0000TDTO dto);

    /**
     * 根据主键删除记录
     * @param receiveId
     */
    int deleteByReceiveId(String receiveId);

}
