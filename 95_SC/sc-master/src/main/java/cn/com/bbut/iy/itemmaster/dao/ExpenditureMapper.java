package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.expenditure.ExpenditureDTO;
import cn.com.bbut.iy.itemmaster.dto.expenditure.ExpenditureParamDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface ExpenditureMapper {

    /**
     * 条件查询记录总数
     * @param dto
     */
    int selectCountByCondition(ExpenditureParamDTO dto);

    /**
     * 条件查询记录
     * @param dto
     */
    List<ExpenditureDTO> selectListByCondition(ExpenditureParamDTO dto);

    /**
     * 主键查询记录
     * @param dto
     */
    ExpenditureDTO selectByKey(ExpenditureParamDTO dto);

    /**
     * 判断编号是否存在
     * @param voucherNo
     */
    int selectCountByVoucherNo(String voucherNo);

    /**
     * 新增记录
     * @param dto
     */
    void insertRecord(ExpenditureDTO dto);

    /**
     * 更新记录
     * @param dto
     */
    void updateRecord(ExpenditureDTO dto);

    /**
     * 删除记录
     * @param dto
     */
    void deleteRecord(ExpenditureParamDTO dto);

    /**
     * 自动下拉voucher_no
     * @param v
     * @param accDate
     * @param storeCd
     * @return
     */
    List<AutoCompleteDTO> getExpenditureList(@Param("v") String v,@Param("accDate")String accDate, @Param("storeCd") String storeCd);

    ExpenditureDTO getFundEntryData(String voucherNo,String storeCd,String accDate);

    ExpenditureDTO getDescription(String voucherNo,String storeCd,String accDate);

    List<AutoCompleteDTO> getOperator(@Param("v") String v,@Param("storeCd") String storeCd);
}