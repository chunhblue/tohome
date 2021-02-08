package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dao.gen.Ma8230GenMapper;
import cn.com.bbut.iy.itemmaster.entity.ma8230.Ma8230;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface Ma8230Mapper extends Ma8230GenMapper {
    Ma8230 selectList(@Param("accDate") String accDate,@Param("storeCd") String storeCd);
}