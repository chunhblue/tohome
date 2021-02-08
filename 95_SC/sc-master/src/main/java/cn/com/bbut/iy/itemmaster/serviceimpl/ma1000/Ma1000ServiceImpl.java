package cn.com.bbut.iy.itemmaster.serviceimpl.ma1000;

import cn.com.bbut.iy.itemmaster.dao.Cm9060Mapper;
import cn.com.bbut.iy.itemmaster.dao.Ma1000Mapper;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.role.RoleStoreDTO;
import cn.com.bbut.iy.itemmaster.entity.base.Cm9060;
import cn.com.bbut.iy.itemmaster.entity.base.Ma1000;
import cn.com.bbut.iy.itemmaster.entity.base.Ma1000Example;
import cn.com.bbut.iy.itemmaster.service.CM9060Service;
import cn.com.bbut.iy.itemmaster.service.ma1000.Ma1000Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 店铺
 * @author zcz
 */
@Slf4j
@Service
public class Ma1000ServiceImpl implements Ma1000Service {
    @Autowired
    private Ma1000Mapper ma1000Mapper;
    @Autowired
    private CM9060Service cm9060Service;

    /**
     * 获取店铺数据一览
     * @return
     */
    @Override
    public List<Ma1000> getList() {
        String businessDate =  cm9060Service.getValByKey("0000");
        Ma1000Example example = new Ma1000Example();
        example.setDistinct(true);
        example.or().andEffectiveStartDateLessThanOrEqualTo(businessDate)
                .andEffectiveEndDateGreaterThanOrEqualTo(businessDate);
        List<Ma1000> list= ma1000Mapper.selectByExample(example);
        return list;
    }

    @Override
    public List<Ma1000> selectByStoreCd(String storeCd) {
        Ma1000Example ma1000Example = new Ma1000Example();
        Ma1000Example.Criteria criteria = ma1000Example.createCriteria();
        criteria.andStoreCdEqualTo(storeCd);
        return ma1000Mapper.selectByExample(ma1000Example);
    }

    /**
     * 检查是否拥有全店铺权限  大于0拥有
     * @param roleIds
     * @return
     */
    @Override
    public Integer getIsStoreAll(Collection<Integer> roleIds) {
        return ma1000Mapper.getIsStoreAll(roleIds);
    }

    @Override
    public List<AutoCompleteDTO> getListByStorePm(Collection<String> storeCds, String v) {
        String businessDate = cm9060Service.getValByKey("0000");
        List<AutoCompleteDTO> list = ma1000Mapper.getListByStorePm(storeCds,v,businessDate);
        return list;
    }

    @Override
    public List<AutoCompleteDTO> getListByPM(Collection<Integer> roleIds, String v) {
        String businessDate = cm9060Service.getValByKey("0000");
        List<AutoCompleteDTO> list = null;
        Integer isStoreAll = this.getIsStoreAll(roleIds);
        if(isStoreAll!=null && isStoreAll>0){
            list = ma1000Mapper.getListAll(v,businessDate);
        }else{
            list = ma1000Mapper.getListByPM(roleIds,v,businessDate);
        }
        return list;
    }

    @Override
    public Collection<String> getStoreListByRole(Collection<Integer> roleId) {
        List<AutoCompleteDTO> storelist = this.getListByPM(roleId,null);
        Set<String> storeSet = storelist.stream().map(AutoCompleteDTO::getK).collect(Collectors.toSet());
        return storeSet;
    }

    @Override
    public List<AutoCompleteDTO> getListAll(String v) {
        String businessDate = cm9060Service.getValByKey("0000");
        List<AutoCompleteDTO> list = ma1000Mapper.getListAll(v,businessDate);
        return list;
    }

    @Override
    public List<RoleStoreDTO> getListByRoleId(Integer roleId) {
        String businessDate = cm9060Service.getValByKey("0000");
        List<RoleStoreDTO> list = ma1000Mapper.getListByRoleId(roleId,businessDate);
        list.forEach(dto ->{
            if("999999".equals(dto.getStoreCd())){
                dto.setStoreName("All Store");
            }
        });
        return list;
    }

    @Override
    public Integer addStorebyRole(Integer roleId, List<String> stores) {
        ma1000Mapper.deleteByRoleId(roleId);
        if(stores!=null&&stores.size()>0){
            ma1000Mapper.addStorebyRole(roleId,stores);
        }
        return 1;
    }

    @Override
    public void delRoleStroe(int roleId) {
        ma1000Mapper.deleteByRoleId(roleId);
    }

    @Override
    public List<Ma1000> selectStoreByStoreCd(String storeCd) {
       List<Ma1000> list= ma1000Mapper.selectStoreByStoreCd(storeCd);
        return list;
    }

    @Override
    public List<AutoCompleteDTO> getAMByPM(Collection<String> stores, String v) {
        String businessDate = cm9060Service.getValByKey("0000");
        List<AutoCompleteDTO> list = ma1000Mapper.getAMByPM(stores,v,businessDate);
        return list;
    }

    @Override
    public List<AutoCompleteDTO> getOm(Collection<String> stores, String v) {
        String businessDate = cm9060Service.getValByKey("0000");
        List<AutoCompleteDTO> list = ma1000Mapper.getOm(stores,v,businessDate);
        return list;
    }

    @Override
    public List<AutoCompleteDTO> getOc(Collection<String> stores, String v) {
        String businessDate = cm9060Service.getValByKey("0000");
        List<AutoCompleteDTO> list = ma1000Mapper.getOc(stores,v,businessDate);
        return list;
    }
}
