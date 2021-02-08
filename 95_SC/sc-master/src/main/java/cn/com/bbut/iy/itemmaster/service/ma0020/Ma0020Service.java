package cn.com.bbut.iy.itemmaster.service.ma0020;

import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.ma0020.MA0020DTO;
import cn.com.bbut.iy.itemmaster.dto.ma0020.Ma0020ParamDTO;

import java.util.Collection;
import java.util.List;

public interface Ma0020Service {

    Collection<MA0020DTO> getAllMenus();

    GridDataDTO<MA0020DTO> selectAMa0020C(Ma0020ParamDTO ma0020ParamDTO);

    MA0020DTO selectDetailMa0020(String structureCd);

    List<AutoCompleteDTO> getListStructName(String v);

    /**
     * 角色权限设置时，根据组织结构等级查询
     *
     */
    List<AutoCompleteDTO> getListByLevel(String level, String p, String v);

    /**
     * 直接获取角色全部店铺
     *
     */
    List<AutoCompleteDTO> getAllStore(Collection<String> stores, String v);

    GridDataDTO<MA0020DTO> getData(Ma0020ParamDTO  param);

//    GridDataDTO<MA0020DTO> selectNationWideMa0020C(Ma0020ParamDTO ma0020ParamDT);
}
