package cn.com.bbut.iy.itemmaster.dao;

        import cn.com.bbut.iy.itemmaster.dto.base.role.ResourceViewDTO;
        import cn.com.bbut.iy.itemmaster.dto.businessDaily.BusinessDailyDto;
        import cn.com.bbut.iy.itemmaster.dto.businessDaily.ExpenditureAmtDto;
        import cn.com.bbut.iy.itemmaster.dto.businessDaily.PaymentAmtDto;
        import org.apache.ibatis.annotations.Mapper;
        import org.apache.ibatis.annotations.Param;

        import java.text.SimpleDateFormat;
        import java.util.List;

@Mapper
public interface BusinessDailyMapper {
    /**
     * 获取销售数据
     * @param payDate 业务日期
     * @param storeCd 店铺号
     * @return
     */
    BusinessDailyDto getSaleData(@Param("payDate")String payDate, @Param("storeCd")String storeCd,@Param("businessDate")String businessDate);

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
    List<PaymentAmtDto> getPayInAmt(@Param("payDate")String payDate, @Param("storeCd")String storeCd);

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
    BusinessDailyDto getSubtotalDue(@Param("accDate")String businessDate, @Param("storeCd")String storeCd);

    Integer getcountCustomer(@Param("accDate") String businessDate, @Param("storeCd") String storeCd);

    Integer getLastMonthSalesAmount(@Param("accDate") String businessDateformat, @Param("storeCd") String storeCd);
}