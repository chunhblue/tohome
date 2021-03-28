package cn.com.bbut.iy.itemmaster.dao.cardpayment;

import cn.com.bbut.iy.itemmaster.dto.cardPaymentReport.cardPaymentDto;
import cn.com.bbut.iy.itemmaster.dto.cardPaymentReport.cardPaymentParamDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardPaymentMapper {

    Integer DataCount(@Param("param") cardPaymentParamDto param);


    List<cardPaymentDto> searchData(@Param("param") cardPaymentParamDto param);

    List<cardPaymentDto> searchExportData(@Param("param") cardPaymentParamDto param);
}
