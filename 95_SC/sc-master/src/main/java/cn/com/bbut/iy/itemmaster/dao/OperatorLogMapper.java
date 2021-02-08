package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dto.operatorLog.OperatorLogDTO;
import cn.com.bbut.iy.itemmaster.dto.operatorLog.OperatorLogParamDTO;
import cn.com.bbut.iy.itemmaster.entity.base.Menu;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OperatorLogMapper {

    /**
     * 根据url 查询菜单
     * @param menuUrl
     * @return
     */
    Menu getMenuByUrl(@Param("menuUrl") String menuUrl);

    /**
     * 保存操作记录
     * @param dto
     */
    void save(@Param("dto") OperatorLogDTO dto);

    /**
     * 查询操作记录
     * @param param
     * @return
     */
    List<OperatorLogDTO> search(@Param("param") OperatorLogParamDTO param);

    /**
     * 查询条数
     * @param param
     * @return
     */
    int searchCount(@Param("param")OperatorLogParamDTO param);

    /**
     * 清空日志表
     */
    void deleteOperatorLog();
}
