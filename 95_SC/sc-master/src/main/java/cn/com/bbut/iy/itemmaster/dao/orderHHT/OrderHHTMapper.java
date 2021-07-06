package cn.com.bbut.iy.itemmaster.dao.orderHHT;

import cn.com.bbut.iy.itemmaster.dto.orderHHT.OrderHHTGridDTO;
import cn.com.bbut.iy.itemmaster.dto.orderHHT.OrderHHTParamDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface OrderHHTMapper {


    List<OrderHHTGridDTO> getDataList(@Param("param") OrderHHTParamDto param);

    Integer getDataListCount(@Param("param")OrderHHTParamDto param);
}
