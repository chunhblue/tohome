package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dto.orderReport.OrderReportDTO;
import cn.com.bbut.iy.itemmaster.dto.orderReport.OrderReportParamDTO;
import cn.com.bbut.iy.itemmaster.dto.vendorReceiptDaily.VendorReceiptDailyParamDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderReportMapper {


    List<OrderReportDTO> getOrderId(@Param("param") OrderReportDTO param);

    List<OrderReportDTO> getItemInfo(@Param("param") OrderReportDTO orderdto);
    List<OrderReportDTO>  getData01(@Param("param") OrderReportParamDTO orderdto);
      int getDataCount(@Param("param") OrderReportParamDTO orderdto);
    OrderReportDTO getList(@Param("param") String orderDate);
    int getListCount(@Param("param") String orderDate);

    List<OrderReportDTO> getOrderInfo(@Param("param") OrderReportParamDTO param);
}
