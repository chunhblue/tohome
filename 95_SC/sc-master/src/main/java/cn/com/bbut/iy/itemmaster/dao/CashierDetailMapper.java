package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dto.cashierDetail.*;
import cn.com.bbut.iy.itemmaster.entity.sa0050.SA0050;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface CashierDetailMapper {
    List<PayMethod> getAllPay();

    List<SaleHead> getSaleHead(CashierDetailParam param);

    long getSaleHeadCount(CashierDetailParam param);

    List<PayMethod> getPayDetail(SaleHeadParam param);

    List<SaleDetail> getSaleDetail(SaleHeadParam param);

    List<SaleDetail> getSaleDetailTotal(SaleHeadParam param);

    long getSaleDetailCount(SaleHeadParam param);

    List<SA0050> getCashier(@Param("storeCd") String storeCd, @Param("posId")String posId);

    BigDecimal getSaleHeadAmount(CashierDetailParam param);
}
