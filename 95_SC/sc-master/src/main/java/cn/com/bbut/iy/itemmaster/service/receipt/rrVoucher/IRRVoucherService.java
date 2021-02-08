package cn.com.bbut.iy.itemmaster.service.receipt.rrVoucher;

import cn.com.bbut.iy.itemmaster.dto.base.CommonDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.receipt.rrVoucher.*;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 进退货传票Service
 *
 */
public interface IRRVoucherService {

    /**
     * 条件查询记录
     *
     */
    GridDataDTO<RRVoucherDTO> getList(RRVoucherParamDTO param);

    /**
     * 主键查询头档
     *
     */
    RRVoucherDTO getRecordByKey(RRVoucherParamDTO param);

    /**
     * 查询明细详情
     *
     */
    GridDataDTO<RRVoucherDetailsDTO> getDetail(RRVoucherParamDTO param);

    /**
     * 更新传票
     *
     */
    int updateRecord(RRVoucherDTO dto, List<RRVoucherDetailsDTO> list);

    /**
     * 快速收货
     *
     */
    int updateRecordByQuick(List<RRVoucherDTO> list, CommonDTO dto);

    /**
     * 打印功能
     * @param param
     * @return
     */
    List<RRVoucherDTO> getPrintData(RRVoucherParamDTO param);
}
