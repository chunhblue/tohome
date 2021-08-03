package cn.com.bbut.iy.itemmaster.dao;

        import cn.com.bbut.iy.itemmaster.dto.base.role.ResourceViewDTO;
        import cn.com.bbut.iy.itemmaster.dto.businessDaily.BusinessDailyDto;
        import cn.com.bbut.iy.itemmaster.dto.businessDaily.ExpenditureAmtDto;
        import cn.com.bbut.iy.itemmaster.dto.businessDaily.PaymentAmtDto;
        import org.apache.ibatis.annotations.Mapper;
        import org.apache.ibatis.annotations.Param;
        import org.springframework.stereotype.Repository;

        import java.math.BigDecimal;
        import java.text.SimpleDateFormat;
        import java.util.List;

@Repository
public interface BusinessDailyMapper {
    /**
     * 获取销售数据
     * @param payDate 业务日期
     * @param storeCd 店铺号
     * @return
     */
    BusinessDailyDto getSaleData(@Param("payDate")String payDate, @Param("storeCd")String storeCd,@Param("businessDate")String businessDate);

    /**
     * 营业金额
     * @return
     */
    String getGrossSaleAmount(@Param("payDate")String payDate, @Param("storeCd")String storeCd, @Param("businessDate")String businessDate);
    /**
     * 退款金额
     * @return
     */
    String getRefundAmount(@Param("payDate")String payDate, @Param("storeCd")String storeCd, @Param("businessDate")String businessDate);

    /**
     * 获取支付类型应付金额
     * @param payDate 业务日期
     * @param storeCd 店铺号
     * @return
     */
    BusinessDailyDto getPayAmt(@Param("payDate")String payDate, @Param("storeCd")String storeCd);

    /**
     * 获取支付类型收款金额
     * @param payDate 业务日期
     * @param storeCd 店铺号
     * @return
     */
    PaymentAmtDto getPayInAmt(@Param("payDate")String payDate, @Param("storeCd")String storeCd);


    /**
     *  获取经费
     * @param accDate 业务日期
     * @param storeCd 店铺号
     * @return
     */
    List<ExpenditureAmtDto> getExpenditureAmt(@Param("accDate")String accDate, @Param("storeCd")String storeCd);

    /**
     * 获取销售金额by大分类
     * @param payDate
     * @param storeCd
     * @return
     */
    List<BusinessDailyDto> getSaleAmountByPma(@Param("payDate")String payDate, @Param("storeCd")String storeCd);

    /**
     * 获取当日应收金额
     * @param businessDate
     * @param storeCd
     * @param
     * @return
     */
    BusinessDailyDto getSubtotalDue(@Param("accDate")String businessDate, @Param("storeCd")String storeCd,@Param("payDate")String payDate);

    Integer getcountCustomer(@Param("accDate") String businessDate, @Param("storeCd") String storeCd);

    Double getLastMonthSalesAmount(@Param("startDate") String startDate,@Param("endDate") String endDate, @Param("storeCd") String storeCd);

    List<PaymentAmtDto> getPayName();

    List<PaymentAmtDto> getCountCustomerByPayCd(@Param("accDate") String businessDate, @Param("storeCd") String storeCd);

    /**
     * 获取service金额
     * @param payDate
     * @param storeCd
     * @return
     */
    PaymentAmtDto getServiceAmt(@Param("payDate")String payDate, @Param("storeCd")String storeCd,
                                @Param("momoList")List<String> momoList,@Param("payCodeList")List<String> payCodeList,
                                @Param("payBillList")List<String> payBillList,@Param("payViettelList")List<String> payViettelList);

    PaymentAmtDto getCountByService(@Param("payDate")String payDate, @Param("storeCd")String storeCd,
                                    @Param("momoList")List<String> momoList,@Param("payCodeList")List<String> payCodeList,
                                    @Param("payBillList")List<String> payBillList,@Param("payViettelList")List<String> payViettelList);

    int getCashBalanceCount(@Param("businessDate")String businessDate, @Param("storeCd")String storeCd);

    // Momo Cash In itemList
    List<String> getMomoList();
    // Payoo Pay Code itemList
    List<String> getPayCodeList();
    // Payoo Pay Bill itemList
    List<String> getPayBillList();
    // VIETTEL PHONE CARD itemList
    List<String> getViettelList();
}
