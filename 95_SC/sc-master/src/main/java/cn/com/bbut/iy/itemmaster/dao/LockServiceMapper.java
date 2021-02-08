package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dto.LockServiceDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface LockServiceMapper {
    /**
     * 保存排他数据
     * @return
     */
    int Save(LockServiceDTO lockDTO);

    /**
     * 删除排他数据
     * @param userId 用户id
     * @param lockKey 排他key
     * @param seesionId
     * @return
     */
    int Delete(@Param("userId")String userId, @Param("lockKey")String lockKey, @Param("sessionId")String seesionId);

    /**
     * @Description:查询排他锁
     * @param userId 用户id
     * @param lockKey 排他key
     * @param seesionId
     * @return
     */
    LockServiceDTO Get(@Param("userId")String userId, @Param("lockKey")String lockKey, @Param("sessionId")String seesionId);
}
