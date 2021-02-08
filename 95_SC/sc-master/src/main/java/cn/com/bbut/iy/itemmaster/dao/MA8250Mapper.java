package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dao.gen.MA8250GenMapper;
import cn.com.bbut.iy.itemmaster.entity.MA8250;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface MA8250Mapper extends MA8250GenMapper {
    MA8250 selectList(@Param("accDate") String accDate, @Param("storeCd") String storeCd);
}