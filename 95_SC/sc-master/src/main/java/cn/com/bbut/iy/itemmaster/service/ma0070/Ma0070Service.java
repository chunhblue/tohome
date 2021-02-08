package cn.com.bbut.iy.itemmaster.service.ma0070;


import cn.com.bbut.iy.itemmaster.constant.ConstantsCache;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.role.ResourceViewDTO;
import cn.com.bbut.iy.itemmaster.entity.ma0070.MA0070;
import cn.com.bbut.iy.itemmaster.entity.ma0080.MA0080;
import cn.com.bbut.iy.itemmaster.entity.ma0090.MA0090;
import cn.com.bbut.iy.itemmaster.entity.ma0100.MA0100;
import org.springframework.cache.annotation.Cacheable;

import java.util.Collection;
import java.util.List;

public interface Ma0070Service {
    @Cacheable(value = ConstantsCache.CACHE_DEP)
    List<MA0070> getList();

    @Cacheable(value = ConstantsCache.CACHE_PMA, key = "#ma0080.toString()")
    List<MA0080> getList(MA0080 ma0080);

    @Cacheable(value = ConstantsCache.CACHE_CATEGORY, key = "#ma0090.toString()")
    List<MA0090> getList(MA0090 ma0090);

    @Cacheable(value = ConstantsCache.CACHE_SUB_CATEGORY, key = "#ma0100.toString()")
    List<MA0100> getList(MA0100 ma0100);

    List<AutoCompleteDTO> getListDepartMent(Collection<Integer> roleId, String v);

    List<AutoCompleteDTO> getListDepartMentAll(String v);

    List<AutoCompleteDTO> getListByDepart(String v);
}
