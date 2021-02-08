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
        return mapper.getSaleAmountByPma(payDate,storeCd);
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
    /**
     * 统计上个月销售金额
     */
    private Integer getLastMonthSalesAmount(String businessDate, String storeCd) {
        SimpleDateFormat businessDateformat = new SimpleDateFormat("yyyyMMdd");
        try {
            Date parseDate = businessDateformat.parse(businessDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(parseDate);
            calendar.add(Calendar.MONTH,-1);
            Date time = calendar.getTime();
            String format = businessDateformat.format(time);
            return mapper.getLastMonthSalesAmount(format,storeCd);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

    }



    /**
     * 统计顾客数目
     */
    private Integer getcountCustomer(String businessDate, String storeCd) {
        return mapper.getcountCustomer(businessDate,storeCd);
    }

    /**
     * 获取银行缴款
     * @param businessDate
     * @param storeCd
     * @return
     */
    private BusinessDailyDto getBankDeposit(String businessDate, String storeCd) {
        return mapper.getSubtotalDue(businessDate,storeCd);
    }

    @Override
    public BusinessDailyDto getSaleData(String payDate, String storeCd,String businessDate) {
        BusinessDailyDto businessDailyDto = mapper.getSaleData(payDate,storeCd,businessDate);
        if(businessDailyDto!=null){
            businessDailyDto.setServiceAmount(//服务费显示为   服务费-充值费
                    businessDailyDto.getServiceAmount().subtract(businessDailyDto.getChargeAmount())
            );
        }
        return businessDailyDto;
    }

    @Override
    public BusinessDailyDto getPayInAmt(String payDate, String storeCd) {
        List<PaymentAmtDto> paymentAmtList = mapper.getPayInAmt(payDate,storeCd);
        BusinessDailyDto dto = new BusinessDailyDto();
        BigDecimal payInAmt = BigDecimal.ZERO;
        if(paymentAmtList!=null&&paymentAmtList.size()>0){
            for (PaymentAmtDto paymentAmtDto: paymentAmtList) {
                switch (paymentAmtDto.getPayCd()) {
                    case "01"://Cash
                        dto.setPayAmt0(paymentAmtDto.getPayInAmt());
                        break;
                    case "02"://Card Payment
                        dto.setPayAmt1(paymentAmtDto.getPayInAmt());
                        break;
                    case "03"://E-Voucher
                        dto.setPayAmt2(paymentAmtDto.getPayInAmt());
                        break;
                    case "04"://Momo
                        dto.setPayAmt3(paymentAmtDto.getPayInAmt());
                        break;
                    case "05"://Payoo
                        dto.setPayAmt4(paymentAmtDto.getPayInAmt());
                        break;
                    case "06"://Viettel
                        dto.setPayAmt5(paymentAmtDto.getPayInAmt());
                        break;
                }
                if(paymentAmtDto.getPayInAmt()!=null)
                payInAmt = payInAmt.add(paymentAmtDto.getPayInAmt());
            }
            dto.setPayAmt(payInAmt);
        }
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

}
