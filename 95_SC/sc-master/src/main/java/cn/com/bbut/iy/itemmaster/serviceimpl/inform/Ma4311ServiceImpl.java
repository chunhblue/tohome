package cn.com.bbut.iy.itemmaster.serviceimpl.inform;

import cn.com.bbut.iy.itemmaster.dao.*;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.base.role.RoleStoreDTO;
import cn.com.bbut.iy.itemmaster.dto.inform.Ma4300DetailGridDto;
import cn.com.bbut.iy.itemmaster.dto.inform.Ma4300DetailParamDto;
import cn.com.bbut.iy.itemmaster.dto.inform.Ma4310DetailGridDto;
import cn.com.bbut.iy.itemmaster.dto.mRoleStore.MRoleStoreParam;
import cn.com.bbut.iy.itemmaster.entity.MA4305;
import cn.com.bbut.iy.itemmaster.entity.MA4305Example;
import cn.com.bbut.iy.itemmaster.entity.MA4311;
import cn.com.bbut.iy.itemmaster.entity.ma4300.MA4300;
import cn.com.bbut.iy.itemmaster.entity.ma4310.MA4310;
import cn.com.bbut.iy.itemmaster.entity.ma4310.MA4310Example;
import cn.com.bbut.iy.itemmaster.entity.ma4330.MA4330;
import cn.com.bbut.iy.itemmaster.service.CM9060Service;
import cn.com.bbut.iy.itemmaster.service.SequenceService;
import cn.com.bbut.iy.itemmaster.service.cm9070.Cm9070Service;
import cn.com.bbut.iy.itemmaster.service.inform.Ma4300Service;
import cn.com.bbut.iy.itemmaster.service.inform.Ma4311Service;
import cn.com.bbut.iy.itemmaster.service.inform_log.Ma4330Service;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Service
@Slf4j
public class Ma4311ServiceImpl implements Ma4311Service {
    @Autowired
    private MA4311Mapper ma4311Mapper;
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

    @Override
    public Collection<String> getAllStore(List<MA4311> _list) {
        // 分级遍历，获取店铺数据
        String temp = null;
        MA4311 dto = null;
        Set<String> stores = new HashSet<>();
        // 遍历Region
        for(MA4311 _dto : _list){
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
        Iterator<MA4311> iterator = _list.iterator();
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
        for(MA4311 _dto : _list){
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

    @Override
    public List<RoleStoreDTO> selectListByInformCd(String informCd) {
        return ma4311Mapper.selectListByInformCd(informCd);
    }

}
