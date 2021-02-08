package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dto.receipt.rrVoucher.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RRVoucherMapper {

    /**
     * 条件查询记录总数
     * @param dto
     */
    int selectCountByCondition(RRVoucherParamDTO dto);

    /**
     * 条件查询记录
     * @param dto
     */
    List<RRVoucherDTO> selectListByCondition(RRVoucherParamDTO dto);

    /**
     * 查询基本信息
     * @param dto
     */
    RRVoucherDTO selectVoucherByKey(RRVoucherParamDTO dto);

    /**
     * 查询明细条数
     * @param dto
     */
    int selectDetailCount(RRVoucherParamDTO dto);

    /**
     * 查询明细信息
     * @param dto
     */
    List<RRVoucherDetailsDTO> selectDetail(RRVoucherParamDTO dto);

    /**
     * 更新头档信息
     * @param dto
     */
    void updateVoucher(@Param("dto") RRVoucherDTO dto, @Param("flag") String flag);

    /**
     * 更新明细信息
     * @param dto
     */
    void updateDetails(@Param("dto") RRVoucherDetailsDTO dto, @Param("flag") String flag);

    // 打印功能
    List<RRVoucherDTO> getPrintdata(RRVoucherParamDTO dto);
}
