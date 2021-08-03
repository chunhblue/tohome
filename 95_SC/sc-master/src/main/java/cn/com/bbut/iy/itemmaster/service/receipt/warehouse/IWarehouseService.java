package cn.com.bbut.iy.itemmaster.service.receipt.warehouse;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.od0000_t.OD0000TDTO;
import cn.com.bbut.iy.itemmaster.dto.receipt.warehouse.WarehouseDetailsGridDTO;
import cn.com.bbut.iy.itemmaster.dto.receipt.warehouse.WarehouseDetailsParamDTO;
import cn.com.bbut.iy.itemmaster.dto.receipt.warehouse.WarehouseReceiptGridDTO;
import cn.com.bbut.iy.itemmaster.dto.receipt.warehouse.WarehouseReceiptParamDTO;

import java.util.List;

/**
 * 仓库配送验收Service
 *
 */
public interface IWarehouseService {

    /**
     * 查询仓库配送验收头档
     *
     */
    GridDataDTO<WarehouseReceiptGridDTO> getReceipt(WarehouseReceiptParamDTO param);

    /**
     * 条件查询仓库配送验收头档数量
     *
     */
    int getReceiptCount(WarehouseReceiptParamDTO param);

    /**
     * 主键查询头档
     *
     */
    WarehouseReceiptGridDTO getReceiptByKey(WarehouseReceiptParamDTO param);

    /**
     * 查询供应商自送验收详情
     *
     */
    GridDataDTO<WarehouseDetailsGridDTO> getReceiptDetail(WarehouseDetailsParamDTO param);

    /**
     * 修改收货数量
     *
     */
    int updateQty(WarehouseDetailsGridDTO param);

    /**
     * 修改订单状态
     *
     */
    int updateSts(WarehouseReceiptParamDTO param);

    /**
     * 打印功能
     * @param param
     * @return
     */
    List<WarehouseReceiptGridDTO> getPrintData(WarehouseReceiptParamDTO param);

    /**
     * 查询SoTransferQty
     *
     */
    List<WarehouseReceiptGridDTO> getSoTransfer(List<WarehouseReceiptGridDTO> _list);
}
