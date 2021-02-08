package cn.com.bbut.iy.itemmaster.dao.receipt.vendor;

import cn.com.bbut.iy.itemmaster.dto.receipt.vendor.VendorDetailsGridDTO;
import cn.com.bbut.iy.itemmaster.dto.receipt.vendor.VendorDetailsParamDTO;
import cn.com.bbut.iy.itemmaster.dto.receipt.vendor.VendorReceiptGridDTO;
import cn.com.bbut.iy.itemmaster.dto.receipt.vendor.VendorReceiptParamDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 供应商自送验收DAO
 *
 */
@Repository
public interface VendorMapper {

    /**
     * 供应商自送验收头档
     */
    List<VendorReceiptGridDTO> selectReceipt(VendorReceiptParamDTO param);

    /**
     * 查询满足条件的记录条数
     */
    int selectReceiptCount(VendorReceiptParamDTO param);

    /**
     * 主键查询头档
     */
    VendorReceiptGridDTO selectByKey(VendorReceiptParamDTO param);

    /**
     * 查询仓库配送验收详情
     */
    List<VendorDetailsGridDTO> selectDetail(VendorDetailsParamDTO param);


    /**
     * 查询满足条件的记录条数
     */
    int selectDetailCount(VendorDetailsParamDTO param);

    /**
     * 主键查询记录详情
     */
    VendorDetailsGridDTO selectDetailByKey(VendorDetailsGridDTO param);

    /**
     * 修改收货数量
     */
    int modifyQty(VendorDetailsGridDTO param);

    /**
     * 修改订单状态
     */
    int updateStatus(VendorReceiptParamDTO param);

    /**
     * 供应商打印
     */
    List<VendorReceiptGridDTO> getPrintData(VendorReceiptParamDTO param);
}
