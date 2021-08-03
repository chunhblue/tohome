package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.cash.CashDetail;
import cn.com.bbut.iy.itemmaster.dto.cash.CashDetailParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

@Mapper
@Component
public interface CashMapper {
    List<CashDetail> getCashDetailyList(CashDetailParam dto);

    int getCashDetailycount(@Param("stores")List<String> stores,@Param("vStartDate")String vStartDate,
                                        @Param("vEndDate")String vEndDate,@Param("am")String am);

    int getCustomerQty(@Param("payDate")String payDate,@Param("storeCd")String storeCd);

    CashDetail getPayAmt(@Param("payDate")String payDate,@Param("storeCd")String storeCd,@Param("voucherDate")String voucherDate);

    /**
     * 获取实际销售金额List
     * @param dto
     * @return
     */
    List<CashDetail> getPayAmtList(CashDetailParam dto);

    /**
     * 获取营业员一览
     * @param v
     * @param stores 店铺权限
     * @return
     */
    List<AutoCompleteDTO> getStaff(String v, Collection<String> stores);

    String getUserName(@Param("userId")String userId);

    /**
     * 获取支付类型
     * @return
     */
    List<CashDetail> getPayList();

    List<CashDetail> getPayListByCondition(CashDetail cashDetail);

    /**
     * 获取现金支付方式列表
     * @return
     */
    List<CashDetail> getCashList();

    List<CashDetail> getCashListByCondition(CashDetail cashDetail);

    List<CashDetail> getPayListByVoucherNo(CashDetail cashDetail);

    /**
     * 获取金种明细
     * @return
     */
    CashDetail getCashDetail(CashDetail cashDetail);

    /**
     * 获取头档信息
     * @return
     */
    CashDetail selectBaseInfo(CashDetail cashDetail);

    int insertSa0170(CashDetail cashDetail);


    int insertSa0180(CashDetail cashDetail);

    int insertSa0190(CashDetail cashDetail);

    int updateSa0170(CashDetail cashDetail);

    int updateSa0180(CashDetail cashDetail);

    int updateSa0190(CashDetail cashDetail);

    List<AutoCompleteDTO> getCashierList(@Param("v") String v, @Param("storeCd") String storeCd);

    CashDetail getDetailBypayId(String payId);

    CashDetail getPayListByVoucherList(CashDetail cash);
}
