package cn.com.bbut.iy.itemmaster.service.expenditure;

import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.expenditure.ExpenditureDTO;
import cn.com.bbut.iy.itemmaster.dto.expenditure.ExpenditureParamDTO;

import java.util.List;

public interface ExpenditureService {

    /**
     * 条件查询记录
     */
    GridDataDTO<ExpenditureDTO> getList(ExpenditureParamDTO dto);

    /**
     * 主键查询记录
     */
    ExpenditureDTO getByKey(ExpenditureParamDTO dto);

    /**
     * 判断编号是否存在
     */
    int getByVoucherNo(String voucherNo);

    /**
     * 新增记录
     */
    int insert(ExpenditureDTO dto);

    /**
     * 更新记录
     */
    int update(ExpenditureDTO dto);

    /**
     * 删除记录
     */
    int delete(List<ExpenditureParamDTO> list);

    /**
     * voucherNo 自动下拉
     * @param v
     * @param accDate
     * @param storeCd
     * @return
     */
    List<AutoCompleteDTO> getExpenditureList(String v,String accDate, String storeCd);

    ExpenditureDTO getFundEntryData(String voucherNo, String storeCd, String accDate);

    ExpenditureDTO getDescription(String voucherNo,String storeCd,String accDate);

    List<AutoCompleteDTO> getOperator(String v,String storeCd);

}
