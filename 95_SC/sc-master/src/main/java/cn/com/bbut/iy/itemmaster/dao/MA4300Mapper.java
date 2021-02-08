package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dao.gen.MA4300GenMapper;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.inform.Ma4300DetailGridDto;
import cn.com.bbut.iy.itemmaster.dto.inform.Ma4300DetailParamDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MA4300Mapper extends MA4300GenMapper {
    /**
     * 通报消息一览
     * @param param
     * @return
     */
    List<Ma4300DetailGridDto> getList(Ma4300DetailParamDto param);

    long getListCount(Ma4300DetailParamDto param);

    /**
     * 获取角色下拉
     * @param v
     * @return
     */
    List<AutoCompleteDTO> getRoleListAll(@Param("roleId") String v);
}