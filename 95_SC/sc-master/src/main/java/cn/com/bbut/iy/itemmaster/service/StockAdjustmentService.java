package cn.com.bbut.iy.itemmaster.service;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.cashierDetail.SaleDetail;
import cn.com.bbut.iy.itemmaster.dto.inventory.Sk0020DTO;
import cn.com.bbut.iy.itemmaster.dto.inventory.Sk0020ParamDTO;

import java.util.Map;

public interface StockAdjustmentService {
    /**
     * 查询库存调整详情数据
     *
     */
    GridDataDTO<Sk0020DTO> getSk0020(Sk0020ParamDTO sk0020);


    Map<String,Object>  Total(Sk0020ParamDTO sk0020);
}
