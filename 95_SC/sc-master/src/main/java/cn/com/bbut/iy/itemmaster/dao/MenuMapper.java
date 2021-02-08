package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dao.gen.MenuGenMapper;
import cn.com.bbut.iy.itemmaster.dto.base.MenuParamDTO;
import cn.com.bbut.iy.itemmaster.entity.base.Menu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MenuMapper extends MenuGenMapper {

    /**
     * 查询数据
     * @param
     * @return
     */
    List<Menu> selectMenuByParam(@Param("param") MenuParamDTO param);

    /**
     * 查询记录数
     * @param
     * @return
     */
    int selectCountByParam(@Param("param") MenuParamDTO param);
}