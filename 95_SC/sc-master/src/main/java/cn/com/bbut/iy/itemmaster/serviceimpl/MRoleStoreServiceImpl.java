package cn.com.bbut.iy.itemmaster.serviceimpl;

import cn.com.bbut.iy.itemmaster.dao.MA0020DTOMapper;
import cn.com.bbut.iy.itemmaster.dao.MRoleStoreMapper;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.role.RoleStoreDTO;
import cn.com.bbut.iy.itemmaster.dto.mRoleStore.MRoleStoreParam;
import cn.com.bbut.iy.itemmaster.service.MRoleStoreService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author mxy
 */
@Service
@Slf4j
public class MRoleStoreServiceImpl implements MRoleStoreService {
 
    @Autowired
    private MRoleStoreMapper mapper;
    @Autowired
    private MA0020DTOMapper ma0020Mapper;

    /** 全权限Code */
    public static final String ALL_CODE = "999999";
    /** 组织结构level --> 大区 */
    public static final String REGION_LEVEL = "0";
    /** 组织结构level --> 城市 */
    public static final String CITY_LEVEL = "1";
    /** 组织结构level --> 区域 */
    public static final String DISTRICT_LEVEL = "2";
    /** 组织结构level --> 店铺 */
    public static final String STORE_LEVEL = "3";

    /**
     * 根据角色查询Store权限<br/>
     *
     * @param roleIds
     */
    @Override
    public List<RoleStoreDTO> getStoreByRole(Collection<Integer> roleIds) {
        return mapper.selectStoreByRole(roleIds);
    }

    /**
     * 根据已选择条件获取Store权限<br/>
     *
     * @param param
     */
    @Override
    public Collection<String> getStoreByChoose(MRoleStoreParam param) {
        Set<String> stores = new HashSet<>();
        if(StringUtils.isNotBlank(param.getStoreCd())){
            stores.add(param.getStoreCd());
        }else{
            List<String> _temp = null;
            if(StringUtils.isNotBlank(param.getDistrictCd())){
                _temp = ma0020Mapper.selectStoreByParent(CITY_LEVEL, param.getDistrictCd(),param.getStoreCds());
                stores.addAll(_temp);
            }else{
                if(StringUtils.isNotBlank(param.getCityCd())){
                    _temp = ma0020Mapper.selectStoreByParent(DISTRICT_LEVEL, param.getCityCd(),param.getStoreCds());
                    stores.addAll(_temp);
                }else{
                    String _regionCd = param.getRegionCd();
                    if("000001".equals(_regionCd)){
                        _temp = ma0020Mapper.selectStoreByParent(REGION_LEVEL, null,param.getStoreCds());
                    }else{
                        _temp = ma0020Mapper.selectStoreByParent(STORE_LEVEL, _regionCd,param.getStoreCds());
                    }
                    stores.addAll(_temp);
                }
            }
        }
        return stores;
    }

    /**
     * 获取全部Store权限<br/>
     *
     * @param param
     */
    @Override
    public Collection<String> getAllStore(MRoleStoreParam param) {
        // 查询获取权限
        List<RoleStoreDTO> _list = mapper.selectByCondition(param);
        // 分级遍历，获取店铺数据
        String temp = null;
        RoleStoreDTO dto = null;
        Set<String> stores = new HashSet<>();
        // 遍历Region
        for(RoleStoreDTO _dto : _list){
            temp = _dto.getRegionCd();
            // 拥有全部权限，直接返回全部店铺数据
            if(ALL_CODE.equals(temp)){
                return ma0020Mapper.selectStoreByParent(REGION_LEVEL, null,null);
            }
            // NationWide包含南北区，该情况等同于有全部权限，直接返回全部店铺数据
            if("000001".equals(temp) && ALL_CODE.equals(_dto.getCityCd())){
                return ma0020Mapper.selectStoreByParent(REGION_LEVEL, null,null);
            }
        }
        // 遍历City
        Iterator<RoleStoreDTO> iterator = _list.iterator();
        while(iterator.hasNext()){
            dto = iterator.next();
            temp = dto.getCityCd();
            if(ALL_CODE.equals(temp)){
                // regionCd='000001'&cityCd='999999'前面已判断过，不再处理这种情况
                List<String> _temp = ma0020Mapper.selectStoreByParent(STORE_LEVEL, dto.getRegionCd(),null);
                stores.addAll(_temp);
                iterator.remove();
            }
        }
        // 遍历District
        iterator = _list.iterator();
        while(iterator.hasNext()){
            dto = iterator.next();
            temp = dto.getDistrictCd();
            if(ALL_CODE.equals(temp)){
                List<String> _temp = ma0020Mapper.selectStoreByParent(DISTRICT_LEVEL, dto.getCityCd(),null);
                stores.addAll(_temp);
                iterator.remove();
            }
        }
        // 遍历Store
        for(RoleStoreDTO _dto : _list){
            temp = _dto.getStoreCd();
            if(ALL_CODE.equals(temp)){
                List<String> _temp = ma0020Mapper.selectStoreByParent(CITY_LEVEL, _dto.getDistrictCd(),null);
                stores.addAll(_temp);
            }else{
                stores.add(temp);
            }
        }
        return stores;
    }

    /**
     * 根据角色ID查询Region权限<br/>
     * 所有查询画面Query Condition --> Region栏位请求数据
     *
     * @param dto
     */
    @Override
    public List<AutoCompleteDTO> getRegionByRoleId(MRoleStoreParam dto) {
        // 判断是否包含全部权限
//        boolean flg = hasAll(dto);
        List<AutoCompleteDTO> _list = null;
//        if(flg){
            _list = ma0020Mapper.selectListByLevel(REGION_LEVEL,null, dto.getV());
//        }else{
//            _list = mapper.selectRegionByRoleId(dto);
//        }
        return _list;
    }

    /**
     * 根据角色ID查询City权限<br/>
     * 所有查询画面Query Condition --> City栏位请求数据
     *
     * @param dto
     */
    @Override
    public List<AutoCompleteDTO> getCityByRoleId(MRoleStoreParam dto) {
        // 判断是否包含全部权限
//        boolean flg = hasAll(dto);
        List<AutoCompleteDTO> _list = null;
        if(StringUtils.isBlank(dto.getRegionCd())){
            dto.setRegionCd("000001");
        }
//        if(flg){
            _list = ma0020Mapper.selectListByLevel(CITY_LEVEL, dto.getRegionCd(), dto.getV());
//        }else{
//            _list = mapper.selectCityByRoleId(dto);
//        }
        return _list;
    }

    /**
     * 根据角色ID查询District权限<br/>
     * 所有查询画面Query Condition --> District栏位请求数据
     *
     * @param dto
     */
    @Override
    public List<AutoCompleteDTO> getDistrictByRoleId(MRoleStoreParam dto) {
        // 判断是否包含全部权限
//        boolean flg = hasAll(dto);
        List<AutoCompleteDTO> _list = null;

        if(!StringUtils.isBlank(dto.getCityCd())) {
            _list = ma0020Mapper.selectListByLevel(DISTRICT_LEVEL, dto.getCityCd(), dto.getV());
        }else if(!StringUtils.isBlank(dto.getRegionCd()) && StringUtils.isBlank(dto.getCityCd())){
            _list = ma0020Mapper.getDistrictList(DISTRICT_LEVEL,dto.getRegionCd(), dto.getV());
        }else  if(StringUtils.isBlank(dto.getRegionCd()) && StringUtils.isBlank(dto.getCityCd())){
            _list = ma0020Mapper.getDistrictList(DISTRICT_LEVEL,"000001", dto.getV());
        }
//       if(flg){
//        _list = ma0020Mapper.selectListByLevel(DISTRICT_LEVEL, dto.getCityCd(), dto.getV());
//        }else{
//            _list = mapper.selectDistrictByRoleId(dto);
//        }
        return _list;
    }

    /**
     * 根据角色ID查询Store权限<br/>
     * 所有查询画面Query Condition --> Store栏位请求数据
     *
     * @param dto
     */
    @Override
    public List<AutoCompleteDTO> getStoreByRoleId(MRoleStoreParam dto) {
        // 判断是否包含全部权限
//        boolean flg = hasAll(dto);
        List<AutoCompleteDTO> _list = null;
        if(!StringUtils.isBlank(dto.getDistrictCd())){
            _list = ma0020Mapper.selectStoreList(dto);
        } else {
            if(StringUtils.isBlank(dto.getRegionCd())){
                dto.setRegionCd("000001");
            }
            _list = ma0020Mapper.getStoreListByLevel(dto);
        }

//        if(flg){
//            _list = ma0020Mapper.selectStoreList(dto);
//        }else{STORE_LEVEL, dto.getDistrictCd(), dto.getV()
//            _list = mapper.selectStoreByRoleId(dto);
//        }
        return _list;
    }

    /**
     * 根据角色ID查询已添加店铺权限<br/>
     * Role Management 画面
     * @param roleId
     */
    @Override
    public List<RoleStoreDTO> getListById(Integer roleId) {
        return mapper.selectListById(roleId);
    }

    /**
     * 新增记录
     *
     * @param roleId
     * @param list
     */
    @Override
    @Transactional
    public int addRecord(Integer roleId, List<RoleStoreDTO> list) {
        mapper.deleteById(roleId);
        if(list!=null && list.size()>0){
            mapper.insertRecord(roleId, list);
        }
        return 1;
    }

    /**
     * 根据角色ID删除记录
     *
     * @param roleId
     */
    @Override
    public void deleteById(Integer roleId) {
        mapper.deleteById(roleId);
    }

    /**
     * 根据角色ID、父级查询权限记录
     *
     * @param dto
     */
    public boolean hasAll(MRoleStoreParam dto) {
        // 取出角色拥有的All权限
        List<RoleStoreDTO> _list = mapper.selectListByAllCode(dto.getRoleIds());
        String regionCd = dto.getRegionCd();
        String cityCd = dto.getCityCd();
        String districtCd = dto.getDistrictCd();
        for(RoleStoreDTO d : _list){
            if(ALL_CODE.equals(d.getRegionCd())){
                return true;
            }
            if(ALL_CODE.equals(d.getCityCd())
                    && "000001".equals(d.getCityCd())){
                return true;
            }
            if(ALL_CODE.equals(d.getCityCd())
                    && d.getRegionCd().equals(regionCd)){
                return true;
            }
            if(ALL_CODE.equals(d.getDistrictCd())
                    && d.getRegionCd().equals(regionCd)
                    && d.getCityCd().equals(cityCd)){
                return true;
            }
            if(ALL_CODE.equals(d.getStoreCd())
                    && d.getRegionCd().equals(regionCd)
                    && d.getCityCd().equals(cityCd)
                    && d.getDistrictCd().equals(districtCd)){
                return true;
            }
        }
        return false;
    }
}