package cn.com.bbut.iy.itemmaster.dao.customercount;


import cn.com.bbut.iy.itemmaster.dto.cardPaymentReport.cardPaymentDto;
import cn.com.bbut.iy.itemmaster.dto.customercount.CustomerCountDto;
import cn.com.bbut.iy.itemmaster.dto.customercount.CustomerCountParamDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerCountMapper {

    Integer searchCount(@Param("param") CustomerCountParamDto param);

    List<CustomerCountDto> searchData(@Param("param") CustomerCountParamDto param);
}
