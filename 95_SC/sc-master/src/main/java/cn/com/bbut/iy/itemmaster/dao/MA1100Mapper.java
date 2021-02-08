package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dao.gen.MA1100GenMapper;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.entity.ma1100.MA1100;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public interface MA1100Mapper extends MA1100GenMapper {

    String selectByarticleName(String articleId);

    List<AutoCompleteDTO> getItemAll(@Param("businessDate") String businessDate,
                                     @Param("articleId") String articleId,@Param("storeCd") String storeCd);
    List<MA1100> selectItem();
}