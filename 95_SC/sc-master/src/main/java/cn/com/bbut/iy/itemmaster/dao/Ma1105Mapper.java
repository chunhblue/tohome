package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dao.gen.Ma1105GenMapper;
import cn.com.bbut.iy.itemmaster.dto.base.CommonDTO;
import cn.com.bbut.iy.itemmaster.entity.base.Ma1105;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface Ma1105Mapper extends Ma1105GenMapper {

    List<Ma1105> selectList(Ma1105 param);

    long selectListCount(Ma1105 param);

    List<String> selectMa1105(List<Ma1105> list);

    List<String> getShelf(@Param("storeCd") String storeCd);

    List<String> getSubShelf(@Param("storeCd")String storeCd,@Param("shelf")String shelf);

    Ma1105 getStoreInfo(@Param("storeCd")String storeCd, @Param("businessDate") String businessDate);

    /**
     * 添加审核数据
     * @param storeCd
     * @return
     */
    int insertAudit(String storeCd);

    int deleteMa1107(@Param("excelName") String excelName, @Param("storeCd")String storeCd);

    int deleteMa1108(Ma1105 ma1105);

    int deleteMa1109(Ma1105 ma1105);

    int deleteMa1104(@Param("pogCd") String pogCd,@Param("storeCd")String storeCd);

    // 添加货架维护履历头档信息
    int insertMa1104(@Param("pogCd") String pogCd,@Param("excelName") String excelName, @Param("storeCd")String storeCd,
                     @Param("commonDTO") CommonDTO commonDTO);
    // 添加文件下拉信息
    int insertMa1107(@Param("pogCd") String pogCd,@Param("excelName") String excelName, @Param("storeCd")String storeCd,
                     @Param("commonDTO") CommonDTO commonDTO);

    int insertMa1108(Ma1105 ma1105);

    int insertMa1109(Ma1105 ma1105);
}