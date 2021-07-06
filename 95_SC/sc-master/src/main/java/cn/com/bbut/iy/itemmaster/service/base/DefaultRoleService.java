package cn.com.bbut.iy.itemmaster.service.base;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import cn.com.bbut.iy.itemmaster.dto.AjaxResultDto;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.base.DefaultRole.DefaultRoleEditDTO;
import cn.com.bbut.iy.itemmaster.dto.base.DefaultRole.DefaultRoleGridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.base.DefaultRole.DefaultRoleParamDTO;
import cn.com.bbut.iy.itemmaster.entity.IymDefAssignment;

/**
 * 默认角色授权 业务service
 * 
 * @author songxz
 */
public interface DefaultRoleService {

    /**
     * 根据参数得到数据
     * 
     * @param param
     */
    GridDataDTO<DefaultRoleGridDataDTO> getDataByParam(DefaultRoleParamDTO param);

    /**
     * 编辑默认角色授权，添加与修改均以此方法为入口，通过id是否为空来决定Add、修改
     * 
     * @param param
     * @return
     */
    AjaxResultDto updateDataByParam(DefaultRoleEditDTO param);

    /**
     * Add默认角色授权
     * 
     * @param inlist
     * @return Add数量
     */
    int insertDataByListEntity(List<IymDefAssignment> inlist);

    /**
     * 修改默认角色授权
     * 
     * @param upEntity
     * @return
     */
    int updateDataByListEntity(IymDefAssignment upEntity);

    /**
     * inlist中的对象是否存在
     * 
     * @param inlist
     * @return 0 不存在 大于0存在
     */
    int isDataExist(List<IymDefAssignment> inlist);

    /**
     * 取消默认角色授权
     * 
     * @param id
     * @return
     */
    AjaxResultDto updateCancelDataById(Integer id);

    /**
     * 根据 参数取得默认角色id集合
     *
     * @return
     */
    Collection<Integer> getDefRoleId(String jobTypeCd);

    /**
     * 判断：角色id是否被默认角色分配使用
     * 
     * @param roleId
     *            角色id
     * @return 已使用 true，未使用false
     */
    boolean isRoleUse(Integer roleId);

    /**
     * 根据角色名称模糊查询得到角色auto集合
     * 
     * @param v
     * @return
     */
    List<AutoCompleteDTO> getRolesAutoByParam(String v);

    /**
     * 根据角色名称获取所有店铺中拥有的最大职位
     * @param userId
     * @return
     */
    int getMaxPosition(String userId);

    int countFinancePosition(String userId);

    int judgeSMPosition(String userId,String StoreCd);
}
