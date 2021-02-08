package cn.com.bbut.iy.itemmaster.serviceimpl.ma0070;

import cn.com.bbut.iy.itemmaster.constant.ConstantsCache;
import cn.com.bbut.iy.itemmaster.constant.ConstantsDB;
import cn.com.bbut.iy.itemmaster.dao.MA0070Mapper;
import cn.com.bbut.iy.itemmaster.dao.MA0080Mapper;
import cn.com.bbut.iy.itemmaster.dao.MA0090Mapper;
import cn.com.bbut.iy.itemmaster.dao.MA0100Mapper;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.role.ResourceViewDTO;
import cn.com.bbut.iy.itemmaster.entity.ma0070.MA0070;
import cn.com.bbut.iy.itemmaster.entity.ma0070.MA0070Example;
import cn.com.bbut.iy.itemmaster.entity.ma0080.MA0080;
import cn.com.bbut.iy.itemmaster.entity.ma0080.MA0080Example;
import cn.com.bbut.iy.itemmaster.entity.ma0090.MA0090;
import cn.com.bbut.iy.itemmaster.entity.ma0090.MA0090Example;
import cn.com.bbut.iy.itemmaster.entity.ma0100.MA0100;
import cn.com.bbut.iy.itemmaster.entity.ma0100.MA0100Example;
import cn.com.bbut.iy.itemmaster.service.ma0070.Ma0070ByPmService;
import cn.com.bbut.iy.itemmaster.service.ma0070.Ma0070Service;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class Ma0070ByPmServiceImpl implements Ma0070ByPmService {
    @Autowired
    private Ma0070Service ma0070Service;

    /**
     * 根据权限查询对应top department
     * @param resources
     * @return
     */
    @Override
    public List<MA0070> getListByPm(List<ResourceViewDTO> resources) {
        /*List<MA0070> list = new ArrayList<>();
        if(resources != null){
            list = ma0070Service.getList();
            Set<String> depSet = resources.stream().map(ResourceViewDTO::getDepCd).collect(Collectors.toSet());
            if(!depSet.contains(ConstantsDB.ALL_DPT)){
                list = list.stream().filter(dto -> depSet.contains(dto.getDepCd())).collect(Collectors.toList());
            }
        }
        return list;*/
        return ma0070Service.getList();
    }

    /**
     * 根据权限查询对应 department
     * @param resources
     * @return
     */
    @Override
    public List<MA0080> getListByPm(MA0080 ma0080,List<ResourceViewDTO> resources) {
        /*List<MA0080> list = new ArrayList<>();
        if(resources != null){
            list = ma0070Service.getList(ma0080);
            Set<String> pmaSet = resources.stream().map(ResourceViewDTO::getPmaCd).collect(Collectors.toSet());
            if(!pmaSet.contains(null)){
                list = list.stream().filter(dto -> pmaSet.contains(dto.getPmaCd())).collect(Collectors.toList());
            }
        }
        return list;*/
        return ma0070Service.getList(ma0080);
    }

    /**
     * 根据权限查询对应Category
     * @param resources
     * @return
     */
    @Override
    public List<MA0090> getListByPm(MA0090 ma0090,List<ResourceViewDTO> resources) {
        /*List<MA0090> list = new ArrayList<>();
        if(resources != null){
            list = ma0070Service.getList(ma0090);
            Set<String> categorySet = resources.stream().map(ResourceViewDTO::getCategoryCd).collect(Collectors.toSet());
            //判断是否拥有全资源
            if(!categorySet.contains(null)){
                list = list.stream().filter(dto -> categorySet.contains(dto.getCategoryCd())).collect(Collectors.toList());
            }
        }
        return list;*/
        return ma0070Service.getList(ma0090);
    }

    /**
     * 根据权限查询对应Sub-Category
     * @param resources
     * @return
     */
    @Override
    public List<MA0100> getListByPm(MA0100 ma0100,List<ResourceViewDTO> resources) {
        /*List<MA0100> list = new ArrayList<>();
        if(resources != null){
            list = ma0070Service.getList(ma0100);
            Set<String> subCategorySet = resources.stream().map(ResourceViewDTO::getSubCategoryCd).collect(Collectors.toSet());
            //判断是否拥有全资源
            if(!subCategorySet.contains(null)){
                list = list.stream().filter(dto -> subCategorySet.contains(dto.getSubCategoryCd())).collect(Collectors.toList());
            }
        }
        return list;*/
        return ma0070Service.getList(ma0100);
    }

}
