package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dao.gen.OD0000GenMapper;
import cn.com.bbut.iy.itemmaster.dto.difference.DifferenceHeadResult;
import cn.com.bbut.iy.itemmaster.dto.returnOrder.returnVendor.*;
import cn.com.bbut.iy.itemmaster.dto.returnOrder.returnWarehouse.*;
import cn.com.bbut.iy.itemmaster.entity.od0000.OD0000;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface OD0000Mapper extends OD0000GenMapper {
    //差异单头档
    DifferenceHeadResult selectAdjustHeadByOrderId(@Param("orderId") String orderId);

    //退供应商一览条件查询
    List<RVListResult> selectVPrintList(RVListParam rvListParam);

    /**
     * 添加退货确认审核信息
     * @param od0000
     * @return
     */
    int insertOd0005(OD0000 od0000);

    //发货单，收货单差异单头档
    DifferenceHeadResult selectShopOrderHeadByOrderId(@Param("orderId") String orderId);
}