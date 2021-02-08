package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dao.gen.MA0020GenMapper;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.mRoleStore.MRoleStoreParam;
import cn.com.bbut.iy.itemmaster.dto.ma0020.MA0020DTO;
import cn.com.bbut.iy.itemmaster.dto.ma0020.Ma0020ParamDTO;
import cn.com.bbut.iy.itemmaster.dto.store.MA4160ParamDTO;
import cn.com.bbut.iy.itemmaster.entity.ma0020.MA0020C;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Mapper
@Component
public interface MA0020DTOMapper extends MA0020GenMapper {
    //根据pid查询所有相对应的子集
    List<MA0020C> getNextSubSet(MA0020C ma0020C);

//    Collection<MA0020C> selectSameLevel(@Param("adminStructureCd") String adminStructureCd, @Param("structureLevel") String structurelLevel);
//    GridDataDTO<MA0020C> selectParentMenus(@Param("structureCd") String structureCd);
//    GridDataDTO<MA0020C> selectInformation(@Param("structureName") String structureName);

     Integer selectCountByCondition(Ma0020ParamDTO  ma0020ParamDTO);

//       MA0020DTO selectAMa0020C(Ma0020ParamDTO ma0020ParamDTO);

    List<MA0020DTO> selectListByCondition(Ma0020ParamDTO ma0020ParamDTO);

    Integer selectCountByCondition(MA4160ParamDTO ma4160ParamDTO);

    MA0020DTO selectDetailMa0020(@Param("structureCd") String structureCd);

    GridDataDTO<MA0020DTO> selectNationWideMa0020C(Ma0020ParamDTO  ma0020ParamDTO);

    //查询Ma0020Dto
    List<MA0020DTO> selectMa0020DTO(MA0020DTO ma0020DTO);

    List<AutoCompleteDTO> getListStructName(String v);

    /**
     * 根据组织结构等级、父级ID查询
     *
     */
    List<AutoCompleteDTO> selectListByLevel(@Param("level") String level,
                                            @Param("p") String p, @Param("v") String v);

    /**
     * 获取所有的city权限
     * @param level
     * @param v
     * @return
     */
    List<AutoCompleteDTO> getDistrictList(@Param("level") String level,
                                          @Param("p") String p,@Param("v") String v);
    /**
     * 直接获取角色全部店铺
     *
     */
    List<AutoCompleteDTO> selectAllStore(@Param("stores") Collection<String> stores, @Param("v") String v);

    /**
     * 查询子级
     *
     */
    List<String> selectChildrenByParent(@Param("parent") String parent);

    /**
     * 根据父级编号查询店铺编号<br/>
     * 通过父级编号类型区分查询等级<br/>
     * Region(NationWide) --> '0'<br/>
     * District --> '1'<br/>
     * City --> '2'<br/>
     * Region(Other) --> '3'
     * @param level 父级编号类型
     * @param parent 父级编号
     * @return 返回店铺编号List
     */
    List<String> selectStoreByParent(@Param("level") String level, @Param("parent") String parent, @Param("storeCds") Collection<String> storeCds);

    List<MA0020DTO> getData(@Param("param") Ma0020ParamDTO param);

    int getDataCount(@Param("param") Ma0020ParamDTO param);

    /**
     * 查询对应店铺
     * @param dto
     * @return
     */
    List<AutoCompleteDTO> selectStoreList(MRoleStoreParam dto);
    // 街道为空，对应的店铺
    List<AutoCompleteDTO> getStoreListByLevel(MRoleStoreParam dto);
}