package cn.com.bbut.iy.itemmaster.service.cardPaymentReport;


import cn.com.bbut.iy.itemmaster.dto.cardPaymentReport.cardPaymentParamDto;


import java.util.Map;


public interface CardPaymentService {
    Map<String, Object> search(cardPaymentParamDto param);
}
