package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dao.gen.MA4311GenMapper;
import cn.com.bbut.iy.itemmaster.dto.base.role.RoleStoreDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MA4311Mapper extends MA4311GenMapper {
    /**
     * 根据通报id查询已添加店铺权限
     * @param informCd
     */
    List<RoleStoreDTO> selectListByInformCd(@Param("informCd")String informCd);
}