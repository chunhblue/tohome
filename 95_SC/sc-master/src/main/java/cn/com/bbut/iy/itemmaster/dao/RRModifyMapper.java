package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.receipt.rrModify.RRModifyDTO;
import cn.com.bbut.iy.itemmaster.dto.receipt.rrModify.RRModifyDetailsDTO;
import cn.com.bbut.iy.itemmaster.dto.receipt.rrModify.RRModifyParamDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RRModifyMapper {

    /**
     * 条件查询记录总数
     * @param dto
     */
    int selectCountByCondition(RRModifyParamDTO dto);

    /**
     * 条件查询记录
     * @param dto
     */
    List<RRModifyDTO> selectListByCondition(RRModifyParamDTO dto);

    /**
     * 主键查询记录
     * @param dto
     */
    RRModifyDTO selectByKey(RRModifyParamDTO dto);

    /**
     * 查询记录明细
     * @param dto
     */
    List<RRModifyDetailsDTO> selectDetailsByKey(RRModifyParamDTO dto);

    int selectCountDetailsByKey(RRModifyParamDTO dto);

    /**
     * 修正传票获取原数据<br/>
     * 取退货确认后的数据发起修正，即receive_qty
     * @param dto
     */
    List<RRModifyDetailsDTO> selectReturnModify(RRModifyParamDTO dto);

    int selectCountReturnModify(RRModifyParamDTO dto);

    /**
     * 保存修正传票
     * @param dto
     */
    int insertRecord(RRModifyDTO dto);

    /**
     * 查询待选
     * @param storeCd
     * @param v
     */
    List<AutoCompleteDTO> selectOrgOrderId(@Param("storeCd") String storeCd, @Param("v") String v);

    /**
     * 根据主键查询收货单头档信息
     * @param dto
     */
    RRModifyDTO selectReceiveByKey(RRModifyParamDTO dto);

    RRModifyDTO getdirectItemInfo(@Param("dto")RRModifyParamDTO dto,@Param("businessDate")String businessDate);

    /**
     * 根据主键查询退货单头档信息
     * @param dto
     */
    RRModifyDTO selectReturnByKey(RRModifyParamDTO dto);

    int updateRecord(RRModifyDTO dto);
}
