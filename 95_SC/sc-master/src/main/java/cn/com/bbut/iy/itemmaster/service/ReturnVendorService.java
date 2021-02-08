package cn.com.bbut.iy.itemmaster.service;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.returnOrder.OrderInfoDTO;
import cn.com.bbut.iy.itemmaster.dto.returnOrder.ReturnHeadResult;
import cn.com.bbut.iy.itemmaster.dto.returnOrder.returnVendor.*;
import cn.com.bbut.iy.itemmaster.entity.od8030.OD8030;

import java.util.List;

/**
 * Created by lz on 2020/01/09.
 */
public interface ReturnVendorService {
    /**
     * 一览页面条件查询
     */
    GridDataDTO<RVListResult> getReturnVQueryList(RVListParamDTO rvListParamDTO);

    /**
     * 供应商退货头档信息
     */
    ReturnHeadResult getRVHeadQuery(String orderId);

    // 一览打印画面
    List<RVListResult> getReturnVPrintList(RVListParamDTO param);
}
