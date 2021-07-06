package cn.com.bbut.iy.itemmaster.dao.receipt.warehouse;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.od0000_t.OD0000TDTO;
import cn.com.bbut.iy.itemmaster.dto.receipt.warehouse.WarehouseDetailsGridDTO;
import cn.com.bbut.iy.itemmaster.dto.receipt.warehouse.WarehouseDetailsParamDTO;
import cn.com.bbut.iy.itemmaster.dto.receipt.warehouse.WarehouseReceiptGridDTO;
import cn.com.bbut.iy.itemmaster.dto.receipt.warehouse.WarehouseReceiptParamDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 仓库配送验收DAO
 *
 */
@Repository
public interface WarehouseMapper {

    /**
     * 查询仓库配送验收头档
     */
    List<WarehouseReceiptGridDTO> selectReceipt(WarehouseReceiptParamDTO param);

    /**
     * 查询满足条件的记录条数
     */
    int selectReceiptCount(WarehouseReceiptParamDTO param);

    /**
     * 查询记录
     */
    WarehouseReceiptGridDTO selectByKey(WarehouseReceiptParamDTO param);

    /**
     * 查询仓库配送验收详情
     */
    List<WarehouseDetailsGridDTO> selectDetail(WarehouseDetailsParamDTO param);

    /**
     * 查询满足条件的记录条数
     */
    int selectDetailCount(WarehouseDetailsParamDTO param);

    /**
     * 主键查询记录详情
     */
    WarehouseDetailsGridDTO selectDetailByKey(WarehouseDetailsGridDTO param);

    int modifyReviewStatus(String orderId);

    /**
     * 修改收货数量
     */
    int modifyQty(WarehouseDetailsGridDTO param);

    /**
     * 修改订单状态
     */
    int updateStatus(WarehouseReceiptParamDTO param);

    /**
     * 打印功能
     * @param param
     * @return
     */
    List<WarehouseReceiptGridDTO> getPrintData(WarehouseReceiptParamDTO param);
}
