package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.returnOrder.OrderInfoDTO;
import cn.com.bbut.iy.itemmaster.dto.returnOrder.ReturnHeadResult;
import cn.com.bbut.iy.itemmaster.dto.returnOrder.returnVendor.RVListParam;
import cn.com.bbut.iy.itemmaster.dto.returnOrder.returnVendor.RVListResult;
import cn.com.bbut.iy.itemmaster.dto.returnOrder.returnWarehouse.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReturnVendorMapper {
    /**
     * 退供应商一览条件查询
     * @param rwhListParam
     * @return
     */
    List<RVListResult> selectVQueryListBy(RVListParam rwhListParam);
    /**
     * 退供应商一览条件查询总数
     * @param rwhListParam
     * @return
     */
    long selectVQueryListCount(RVListParam rwhListParam);

    /**
     * 退供应商情根据单据编号查询头档
     * @param orderId
     * @return
     */
    ReturnHeadResult selectVHeadByOrderId(@Param("orderId") String orderId);


}
