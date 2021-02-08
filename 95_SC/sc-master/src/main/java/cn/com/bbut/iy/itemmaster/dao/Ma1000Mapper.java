package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dao.gen.Ma1000GenMapper;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.role.RoleStoreDTO;
import cn.com.bbut.iy.itemmaster.entity.base.Ma1000;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Mapper
@Component
public interface Ma1000Mapper extends Ma1000GenMapper {
    Integer getIsStoreAll(@Param("roleIds")Collection<Integer> roleIds);

    List<AutoCompleteDTO> getListByStorePm(@Param("stores")Collection<String> storeCds, @Param("v")String v, @Param("businessDate")String businessDate);

    List<AutoCompleteDTO> getListByPM(@Param("roleIds")Collection<Integer> roleIds, @Param("storeCd")String storeCd, @Param("businessDate")String businessDate);

    List<AutoCompleteDTO> getListAll(@Param("storeCd")String storeCd,@Param("businessDate")String businessDate);

    List<RoleStoreDTO> getListByRoleId(@Param("roleId")Integer roleId,@Param("businessDate")String businessDate);

    int deleteByRoleId(@Param("roleId")Integer roleId);

    int addStorebyRole(@Param("roleId")Integer roleId,@Param("stores")List<String> stores);
    List<Ma1000> selectStoreByStoreCd(@Param("storeCd") String storeCd);

    List<AutoCompleteDTO> getAMByPM(@Param("storeCds")Collection<String> storeCds, @Param("storeCd")String storeCd, @Param("businessDate")String businessDate);

    List<AutoCompleteDTO> getOm(@Param("storeCds")Collection<String> stores,@Param("v") String v,  @Param("businessDate") String businessDate);

    List<AutoCompleteDTO> getOc(@Param("storeCds") Collection<String> stores,@Param("v")  String v,@Param("businessDate") String businessDate);

}