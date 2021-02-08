package cn.com.bbut.iy.itemmaster.service.receipt.vendor;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.receipt.vendor.VendorDetailsGridDTO;
import cn.com.bbut.iy.itemmaster.dto.receipt.vendor.VendorDetailsParamDTO;
import cn.com.bbut.iy.itemmaster.dto.receipt.vendor.VendorReceiptGridDTO;
import cn.com.bbut.iy.itemmaster.dto.receipt.vendor.VendorReceiptParamDTO;

import java.util.List;

/**
 * 供应商自送验收Service
 *
 */
public interface IVendorService {

    /**
     * 查询供应商自送验收头档
     *
     */
    GridDataDTO<VendorReceiptGridDTO> getReceipt(VendorReceiptParamDTO param);

    /**
     * 条件查询供应商自送验收头档数量
     *
     */
    int getReceiptCount(VendorReceiptParamDTO param);

    /**
     * 主键查询头档
     *
     */
    VendorReceiptGridDTO getReceiptByKey(VendorReceiptParamDTO param);

    /**
     * 查询供应商自送验收详情
     *
     */
    GridDataDTO<VendorDetailsGridDTO> getReceiptDetail(VendorDetailsParamDTO param);

    /**
     * 修改收货数量
     *
     */
    int updateQty(VendorDetailsGridDTO param);

    /**
     * 修改订单状态
     *
     */
    int updateSts(VendorReceiptParamDTO param);

    /**
     * 打印
     *
     */
    List<VendorReceiptGridDTO> getPrintData(VendorReceiptParamDTO param);
}
