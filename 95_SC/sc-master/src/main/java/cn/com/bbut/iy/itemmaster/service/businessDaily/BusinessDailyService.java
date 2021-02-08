package cn.com.bbut.iy.itemmaster.service.businessDaily;

import cn.com.bbut.iy.itemmaster.dto.base.role.ResourceViewDTO;
import cn.com.bbut.iy.itemmaster.dto.businessDaily.BusinessDailyDto;
import cn.com.bbut.iy.itemmaster.dto.businessDaily.ExpenditureAmtDto;
import cn.com.bbut.iy.itemmaster.dto.businessDaily.PaymentAmtDto;

import java.util.List;
import java.util.Map;

public interface BusinessDailyService {
    /**
     * 获取销售数据
     * @param payDate 业务日期
     * @param storeCd 店铺号
     * @return
     */
    BusinessDailyDto getSaleData(String payDate, String storeCd,String businessDate);

    /**
     * 获取支付类型应付金额
     * @param payDate 业务日期
     * @param storeCd 店铺号
     * @return
     */
    BusinessDailyDto getPayAmt(String payDate,String storeCd);

    /**
     * 获取支付类型收款金额
     * @param payDate 业务日期
     * @param storeCd 店铺号
     * @return
     */
    BusinessDailyDto getPayInAmt(String payDate, String storeCd);

    /**
     *  获取经费
     * @param accDate 业务日期
     * @param storeCd 店铺号
     * @return
     */
    List<ExpenditureAmtDto> getExpenditureAmt(String accDate, String storeCd);

    List<BusinessDailyDto> getSaleAmountByPma(String payDate, String storeCd);

    Map getData(String storeCd,String businessDate);
}
