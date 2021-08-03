package cn.com.bbut.iy.itemmaster.serviceimpl.receipt.warehouse;

import cn.com.bbut.iy.itemmaster.dao.Cm9060Mapper;
import cn.com.bbut.iy.itemmaster.dao.receipt.warehouse.WarehouseMapper;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.od0000_t.OD0000TDTO;
import cn.com.bbut.iy.itemmaster.dto.receipt.warehouse.WarehouseDetailsGridDTO;
import cn.com.bbut.iy.itemmaster.dto.receipt.warehouse.WarehouseDetailsParamDTO;
import cn.com.bbut.iy.itemmaster.dto.receipt.warehouse.WarehouseReceiptGridDTO;
import cn.com.bbut.iy.itemmaster.dto.receipt.warehouse.WarehouseReceiptParamDTO;
import cn.com.bbut.iy.itemmaster.entity.base.Cm9060;
import cn.com.bbut.iy.itemmaster.service.receipt.warehouse.IWarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * 仓库配送验收实现
 *
 */
@Service
public class WarehouseServiceImpl implements IWarehouseService {

    @Autowired
    private WarehouseMapper warehouseMapper;
    @Autowired
    private Cm9060Mapper cm9060Mapper;

    /**
     * 查询仓库配送验收头档
     */
    @Override
    public GridDataDTO<WarehouseReceiptGridDTO> getReceipt(WarehouseReceiptParamDTO param) {
        String businessDate = getBusinessDate();
        param.setBusinessDate(businessDate);

        int count = warehouseMapper.selectReceiptCount(param);
        if(count<1){
            return new GridDataDTO<WarehouseReceiptGridDTO>();
        }

        List<WarehouseReceiptGridDTO> _list = warehouseMapper.selectReceipt(param);

        GridDataDTO<WarehouseReceiptGridDTO> data = new GridDataDTO<WarehouseReceiptGridDTO>(_list,
                param.getPage(), count, param.getRows());
        return data;
    }

    /**
     * 条件查询仓库配送验收头档数量
     *
     * @param param
     */
    @Override
    public int getReceiptCount(WarehouseReceiptParamDTO param) {
        return warehouseMapper.selectReceiptCount(param);
    }

    /**
     * 主键查询头档
     *
     * @param param
     */
    @Override
    public WarehouseReceiptGridDTO getReceiptByKey(WarehouseReceiptParamDTO param) {
        String businessDate = getBusinessDate();
        param.setBusinessDate(businessDate);
        return warehouseMapper.selectByKey(param);
    }

    /**
     * 查询供应商自送验收详情
     *
     * @param param
     */
    @Override
    public GridDataDTO<WarehouseDetailsGridDTO> getReceiptDetail(WarehouseDetailsParamDTO param) {
        GridDataDTO<WarehouseDetailsGridDTO> data = new GridDataDTO<WarehouseDetailsGridDTO>();
        String businessDate = getBusinessDate();
        param.setBusinessDate(businessDate);

        List<WarehouseDetailsGridDTO> _list = warehouseMapper.selectDetail(param);
        for (WarehouseDetailsGridDTO dto :_list) {
            dto.setReceiveNoChargeQty1(dto.getReceiveQty());
            dto.setReceiveQty1(dto.getReceiveQty());
        }
        data.setRows(_list);

        return data;
    }

    /**
     * 修改收货数量
     *
     * @param param
     */
    @Override
    public int updateQty(WarehouseDetailsGridDTO param) {
        WarehouseDetailsGridDTO _old = warehouseMapper.selectDetailByKey(param);
        if(_old == null){
            return 0;
        }
        // 收货单价
        BigDecimal receivePrice = _old.getReceivePrice();
        if(receivePrice == null){
            return 0;
        }
        // 收货搭赠量
        BigDecimal receiveFreeQty = param.getReceiveNoChargeQty();
        // 收货数量
        BigDecimal receiveQty = param.getReceiveQty();
        // 更新实收总数量
        param.setReceiveTotalQty(receiveQty.add(receiveFreeQty));
        // 计算实收总金额
        param.setReceiveTotalAmt(receiveQty.multiply(receivePrice));
        // 原系统在更新明细时，仅更新了收货数量、搭赠数量、总收货数量、总金额，未更新税额相关字段
        return warehouseMapper.modifyQty(param);
    }

    /**
     * 修改订单状态
     *
     * @param param
     */
    @Override
    public int updateSts(WarehouseReceiptParamDTO param) {
        return warehouseMapper.updateStatus(param);
    }

    // 打印
    @Override
    public List<WarehouseReceiptGridDTO> getPrintData(WarehouseReceiptParamDTO param) {
        String businessDate = getBusinessDate();
        param.setBusinessDate(businessDate);
        return warehouseMapper.selectReceipt(param);
    }

    @Override
    public List<WarehouseReceiptGridDTO> getSoTransfer(List<WarehouseReceiptGridDTO> _list) {

        List<WarehouseReceiptGridDTO> re_list = warehouseMapper.getSoTransfer(_list);

        return re_list;
    }

    /**
     * 获取当前业务日期
     */
    public String getBusinessDate() {
        Cm9060 dto =  cm9060Mapper.selectByPrimaryKey("0000");
        return dto.getSpValue();
    }
}
