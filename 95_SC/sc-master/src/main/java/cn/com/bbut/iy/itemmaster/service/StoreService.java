package cn.com.bbut.iy.itemmaster.service;

import java.util.Collection;
import java.util.List;

import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.LabelDTO;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.entity.base.Ma1000;

/**
 * @author songxz
 */
public interface StoreService {

    /**
     * 取得店铺集合
     * 
     * @return
     */
    List<Ma1000> getAllStore();

    /**
     * 根据输入的店铺号或店铺名，取得相应店铺集合（包含iy_stroe的全部店铺集合）<br>
     * 此方法适用于类似默认角色授权画面，其他画面的店铺下拉内容，仅为0000x-99999
     * 
     * @param param
     * @return
     */
    List<AutoCompleteDTO> getStoreAutoByParam(String param);

    /**
     * 根据code得到名称等内容
     * 
     * @param store
     * @return
     */
    LabelDTO getStoreName(String store);

    /**
     * 根据角色和权限得到所属的店铺集合
     * 
     * @param roleIds
     *            角色集合
     * @param pcode
     *            权限
     * @return
     */
    List<AutoCompleteDTO> getStoreByRoleAndPcode(Collection<Integer> roleIds, User u, String pcode);
}
