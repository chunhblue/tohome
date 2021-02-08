package cn.com.bbut.iy.itemmaster.serviceimpl.receipt.vendor;

import cn.com.bbut.iy.itemmaster.dao.Cm9060Mapper;
import cn.com.bbut.iy.itemmaster.dao.receipt.vendor.VendorMapper;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.receipt.vendor.VendorDetailsGridDTO;
import cn.com.bbut.iy.itemmaster.dto.receipt.vendor.VendorDetailsParamDTO;
import cn.com.bbut.iy.itemmaster.dto.receipt.vendor.VendorReceiptGridDTO;
import cn.com.bbut.iy.itemmaster.dto.receipt.vendor.VendorReceiptParamDTO;
import cn.com.bbut.iy.itemmaster.dto.receipt.warehouse.WarehouseDetailsGridDTO;
import cn.com.bbut.iy.itemmaster.entity.base.Cm9060;
import cn.com.bbut.iy.itemmaster.service.receipt.vendor.IVendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * 供应商自送验收实现
 *
 */
@Service
public class VendorServiceImpl implements IVendorService {

    @Autowired
    private VendorMapper vendorMapper;
    @Autowired
    private Cm9060Mapper cm9060Mapper;

    /**
     * 查询供应商自送验收头档
     *
     * @param param
     */
    @Override
    public GridDataDTO<VendorReceiptGridDTO> getReceipt(VendorReceiptParamDTO param) {
        String businessDate = getBusinessDate();
        param.setBusinessDate(businessDate);

        int count = vendorMapper.selectReceiptCount(param);
        if(count<1){
            return new GridDataDTO<VendorReceiptGridDTO>();
        }

        List<VendorReceiptGridDTO> _list = vendorMapper.selectReceipt(param);

        GridDataDTO<VendorReceiptGridDTO> data = new GridDataDTO<VendorReceiptGridDTO>(_list,
                param.getPage(), count, param.getRows());
        return data;
    }

    /**
     * 条件查询供应商自送验收头档数量
     *
     * @param param
     */
    @Override
    public int getReceiptCount(VendorReceiptParamDTO param) {
        return vendorMapper.selectReceiptCount(param);
    }

    /**
     * 主键查询头档
     *
     * @param param
     */
    @Override
    public VendorReceiptGridDTO getReceiptByKey(VendorReceiptParamDTO param) {
        String businessDate = getBusinessDate();
        param.setBusinessDate(businessDate);
      
        return vendorMapper.selectByKey(param);
    }

    /**
     * 查询供应商自送验收详情
     *
     * @param param
     */
    @Override
    public GridDataDTO<VendorDetailsGridDTO> getReceiptDetail(VendorDetailsParamDTO param) {
        GridDataDTO<VendorDetailsGridDTO> data = new GridDataDTO<VendorDetailsGridDTO>();
        String businessDate = getBusinessDate();
        param.setBusinessDate(businessDate);

        List<VendorDetailsGridDTO> _list = vendorMapper.selectDetail(param);
        for (VendorDetailsGridDTO dto :_list) {
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
    public int updateQty(VendorDetailsGridDTO param) {
        VendorDetailsGridDTO _old = vendorMapper.selectDetailByKey(param);
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
        return vendorMapper.modifyQty(param);
    }

    /**
     * 修改订单状态
     *
     * @param param
     */
    @Override
    public int updateSts(VendorReceiptParamDTO param) {
        return vendorMapper.updateStatus(param);
    }

    /**
     * 打印
     * @param param
     * @return
     */
    @Override
    public List<VendorReceiptGridDTO> getPrintData(VendorReceiptParamDTO param) {
        String businessDate = getBusinessDate();
        param.setBusinessDate(businessDate);
        return vendorMapper.selectReceipt(param);
    }

    /**
     * 获取当前业务日期
     */
    public String getBusinessDate() {
        Cm9060 dto =  cm9060Mapper.selectByPrimaryKey("0000");
        return dto.getSpValue();
    }
}
