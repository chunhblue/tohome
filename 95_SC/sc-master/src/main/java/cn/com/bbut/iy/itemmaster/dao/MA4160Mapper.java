package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dao.gen.MA4160GenMapper;
import cn.com.bbut.iy.itemmaster.entity.MA4160;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MA4160Mapper extends MA4160GenMapper {

    List<MA4160> selectPostByParam(@Param("param") String param);

    /**
     * 根据角色名称获取所有店铺中拥有的最大职位
     * @param userId
     * @return
     */
    int getMaxPositionByuserId(@Param("userId")String userId);

    /**
     * 查看是否拥有财务权限
     * @param userId
     * @return
     */
    int countFinancePosition(@Param("userId")String userId);
}