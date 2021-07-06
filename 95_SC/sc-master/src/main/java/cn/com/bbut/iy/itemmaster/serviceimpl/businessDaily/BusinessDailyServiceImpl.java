package cn.com.bbut.iy.itemmaster.serviceimpl.businessDaily;

import cn.com.bbut.iy.itemmaster.dao.BusinessDailyMapper;
import cn.com.bbut.iy.itemmaster.dto.base.role.ResourceViewDTO;
import cn.com.bbut.iy.itemmaster.dto.businessDaily.BusinessDailyDto;
import cn.com.bbut.iy.itemmaster.dto.businessDaily.ExpenditureAmtDto;
import cn.com.bbut.iy.itemmaster.dto.businessDaily.PaymentAmtDto;
import cn.com.bbut.iy.itemmaster.service.businessDaily.BusinessDailyService;
import cn.com.bbut.iy.itemmaster.service.CM9060Service;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class BusinessDailyServiceImpl implements BusinessDailyService {
    @Autowired
    private BusinessDailyMapper mapper;
    @Autowired
    private CM9060Service cm9060Service;


    @Override
    public List<BusinessDailyDto> getSaleAmountByPma(String payDate, String storeCd) {
        return mapper.getSaleAmountByPma(getTimeStamp(payDate),storeCd);
    }

    @Override
    public Map getData(String storeCd,String businessDate) {
        if(StringUtils.isBlank(businessDate)){
            businessDate = cm9060Service.getValByKey("0000");
        }
        Map<String,Object> map = new HashMap<String, Object>();
        // 上月销售金额
        map.put("lastMonthSalesAmount",this.getLastMonthSalesAmount(businessDate,storeCd));
        // 顾客数量
        map.put("countCustomer",this.getcountCustomer(businessDate,storeCd));
        //销售数据
        map.put("saleData",this.getSaleData(businessDate,storeCd,businessDate));
        //支付方式
        map.put("payAmt",this.getPayInAmt(businessDate,storeCd));
        // 充值费 Services Revenue – Breakdown (Cash Only)
        map.put("serviceAmt",this.getServiceInAmt(businessDate,storeCd));
        //经费
        map.put("expenditureAmt",this.getExpenditureAmt(businessDate,storeCd));
        //银行缴款
        map.put("bankDeposit",this.getBankDeposit(businessDate,storeCd));
        //分类销售
        map.put("saleAmountByPma",this.getSaleAmountByPma(businessDate,storeCd));
        // 业务日期
        map.put("businessDate",businessDate);
        return map;
    }

    // "yyyyMMdd" -->  "yyyy-MM-dd"
    public String getTimeStamp(String date){
        String str = "";
        if(date == null || "".equals(date) || date.length()<8){
            return str;
        }
        str = date.substring(0,4)+"-"+date.substring(4,6)+"-"+date.substring(6,8);
        return str;
    }
    /**
     * 统计上个月销售金额
     */
    private Double getLastMonthSalesAmount(String businessDate, String storeCd) {
        SimpleDateFormat businessDateformat = new SimpleDateFormat("yyyyMMdd");
        try {
            Date parseDate = businessDateformat.parse(businessDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(parseDate);
            calendar.add(Calendar.DATE,-28);
            Date time = calendar.getTime();
            String startDate = getTimeStamp(businessDateformat.format(time));
            return mapper.getLastMonthSalesAmount(startDate,getTimeStamp(businessDate),storeCd);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

    }



    /**
     * 统计顾客数目
     */
    private Integer getcountCustomer(String businessDate, String storeCd) {
        return mapper.getcountCustomer(getTimeStamp(businessDate),storeCd);
    }

    /**
     * 获取银行缴款
     * @param businessDate
     * @param storeCd
     * @return
     */
    private BusinessDailyDto getBankDeposit(String businessDate, String storeCd) {
        return mapper.getSubtotalDue(getTimeStamp(businessDate),storeCd,businessDate);
    }

    @Override
    public BusinessDailyDto getSaleData(String payDate, String storeCd,String businessDate) {
        // 营业额
        String grossSaleAmount = mapper.getGrossSaleAmount(getTimeStamp(payDate),storeCd,businessDate);
        // 顾客退款金额
        String refundAmount = mapper.getRefundAmount(getTimeStamp(payDate),storeCd,businessDate);
        BusinessDailyDto businessDailyDto = mapper.getSaleData(getTimeStamp(payDate),storeCd,businessDate);
        if(businessDailyDto!=null){
            businessDailyDto.setGrossSaleAmount(new BigDecimal(grossSaleAmount));
            businessDailyDto.setRefundAmount(new BigDecimal(refundAmount));
            /*businessDailyDto.setServiceAmount(//服务费
                    businessDailyDto.getServiceAmount().subtract(businessDailyDto.getChargeAmount())
            );*/
        }
        return businessDailyDto;
    }

    @Override
    public BusinessDailyDto getPayInAmt(String payDate, String storeCd) {
        PaymentAmtDto paymentAmtDto = mapper.getPayInAmt(getTimeStamp(payDate),storeCd);
        List<PaymentAmtDto> payCdList = mapper.getPayName();

        BusinessDailyDto dto = new BusinessDailyDto();
        dto.setPayAmt0(paymentAmtDto.getPayInAmt0()); // Cash
        dto.setPayAmt1(paymentAmtDto.getPayInAmt1()); // Card
        dto.setPayAmt2(paymentAmtDto.getPayInAmt2()); // E-Voucher
        dto.setPayAmt3(paymentAmtDto.getPayInAmt3()); // Momo
        dto.setPayAmt4(paymentAmtDto.getPayInAmt4()); // Zalo
        dto.setPayAmt5(paymentAmtDto.getPayInAmt5()); // VNPAY
        dto.setPayAmt6(paymentAmtDto.getPayInAmt6()); // GrabMoca
        for(PaymentAmtDto payDto:payCdList){
            switch (payDto.getPayCd()){
                case "01":
                    dto.setPayCd0(payDto.getPayName());
                    break;
                case "02":
                    dto.setPayCd1(payDto.getPayName());
                    break;
                case "03":
                    dto.setPayCd2(payDto.getPayName());
                    break;
                case "04":
                    dto.setPayCd3(payDto.getPayName());
                    break;
                case "06":
                    dto.setPayCd4(payDto.getPayName());
                    break;
                case "07":
                    dto.setPayCd5(payDto.getPayName());
                    break;
                case "18":
                    dto.setPayCd6(payDto.getPayName());
                    break;
                default:

            }
        }

        dto.setPayAmt(dto.getPayAmt0().add(dto.getPayAmt1()).add(dto.getPayAmt2())
                .add(dto.getPayAmt3()).add(dto.getPayAmt4()).add(dto.getPayAmt5()).add(dto.getPayAmt6()));

        List<PaymentAmtDto> _countList = mapper.getCountCustomerByPayCd(getTimeStamp(payDate),storeCd);
        Integer count = 0;
        for(PaymentAmtDto _count:_countList){
            switch (_count.getPayCd()){
                case "01":
                    dto.setCustomerCount0(_count.getCustomerCount());
                    break;
                case "02":
                    dto.setCustomerCount1(_count.getCustomerCount());
                    break;
                case "03":
                    dto.setCustomerCount2(_count.getCustomerCount());
                    break;
                case "04":
                    dto.setCustomerCount3(_count.getCustomerCount());
                    break;
                case "06":
                    dto.setCustomerCount4(_count.getCustomerCount());
                    break;
                case "07":
                    dto.setCustomerCount5(_count.getCustomerCount());
                    break;
                case "18":
                    dto.setCustomerCount6(_count.getCustomerCount());
                    break;
                default:
            }
            count += _count.getCustomerCount();
        }
        dto.setCustomerCount(count);
        return dto;
    }

    private BusinessDailyDto getServiceInAmt(String payDate, String storeCd) {
        BusinessDailyDto dto = new BusinessDailyDto();
        PaymentAmtDto serviceAmtDto = mapper.getServiceAmt(getTimeStamp(payDate),storeCd);
        PaymentAmtDto countServiceDto = mapper.getCountByService(getTimeStamp(payDate),storeCd);
        dto.setMomoCashInAmt(serviceAmtDto.getMomoCashInAmt());
        dto.setPayooBillAmt(serviceAmtDto.getPayooBillAmt());
        dto.setPayooCodeAmt(serviceAmtDto.getPayooCodeAmt());
        dto.setPayooAmt(serviceAmtDto.getPayooCodeAmt().add(serviceAmtDto.getPayooBillAmt()));
        dto.setViettelAmt(serviceAmtDto.getViettelAmt());
        dto.setOtherServiceAmt(serviceAmtDto.getOtherServiceAmt());
        dto.setServicePayAmt(serviceAmtDto.getMomoCashInAmt().add(serviceAmtDto.getPayooBillAmt())
                .add(serviceAmtDto.getPayooCodeAmt()).add(serviceAmtDto.getViettelAmt()));

        dto.setMomoCashIncount(countServiceDto.getMomoCashIncount());
        dto.setPayooCodeCount(countServiceDto.getPayooCodeCount());
        dto.setPayooBillCount(countServiceDto.getPayooBillCount());
        dto.setPayooCount(countServiceDto.getPayooCodeCount()+countServiceDto.getPayooBillCount());
        dto.setViettelCount(countServiceDto.getViettelCount());
        dto.setServiceCount(countServiceDto.getMomoCashIncount()+countServiceDto.getPayooCodeCount()
                +countServiceDto.getPayooBillCount()+countServiceDto.getViettelCount());
        return dto;
    }

    @Override
    public List<ExpenditureAmtDto> getExpenditureAmt(String accDate, String storeCd) {
        return mapper.getExpenditureAmt(accDate,storeCd);
    }

    @Override
    public BusinessDailyDto getPayAmt(String payDate, String storeCd) {
        BusinessDailyDto businessDailyDto = mapper.getPayAmt(payDate,storeCd);
        if(businessDailyDto!=null){
            businessDailyDto.setPayAmt(businessDailyDto.getPayAmt0()
                    .add(businessDailyDto.getPayAmt1())
                    .add(businessDailyDto.getPayAmt2())
                    .add(businessDailyDto.getPayAmt3())
                    .add(businessDailyDto.getPayAmt4())
                    .add(businessDailyDto.getPayAmt5()));
        }
        return businessDailyDto;
    }

    @Override
    public int getCashBalanceCount(String businessDate,String storeCd){
        return mapper.getCashBalanceCount(businessDate,storeCd);
    }
}
