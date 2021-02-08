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
import cn.com.bbut.iy.itemmaster.service.ma0070.Ma0070Service;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class Ma0070ServiceImpl implements Ma0070Service {
    @Autowired
    private MA0070Mapper ma0070Mapper;
    @Autowired
    private MA0080Mapper ma0080Mapper;
    @Autowired
    private MA0090Mapper ma0090Mapper;
    @Autowired
    private MA0100Mapper ma0100Mapper;

    @Override
    public List<MA0070> getList() {
        MA0070Example example = new MA0070Example();
        example.or().andEffectiveStsEqualTo("10");
        List<MA0070> list = ma0070Mapper.selectByExample(example);
        return list;
    }


    @Override
    public List<MA0080> getList(MA0080 ma0080) {
        MA0080Example example = new MA0080Example();
        if(StringUtils.isNotBlank(ma0080.getDepCd())){
            example.or().andDepCdEqualTo(ma0080.getDepCd()).andEffectiveStsEqualTo("10");
        }else{
            example.or().andEffectiveStsEqualTo("10");
        }
        return ma0080Mapper.selectByExample(example);
    }

    @Override
    public List<MA0090> getList(MA0090 ma0090) {
        MA0090Example example = new MA0090Example();
        if(StringUtils.isNotBlank(ma0090.getDepCd())){
            example.or().andDepCdEqualTo(ma0090.getDepCd())
                    .andPmaCdEqualTo(ma0090.getPmaCd())
                    .andEffectiveStsEqualTo("10");
        }else{
            example.or().andPmaCdEqualTo(ma0090.getPmaCd())
                    .andEffectiveStsEqualTo("10");
        }
        return ma0090Mapper.selectByExample(example);
    }

    @Override
    public List<MA0100> getList(MA0100 ma0100) {
        MA0100Example example = new MA0100Example();
        if(StringUtils.isNotBlank(ma0100.getDepCd())){
            example.or().andDepCdEqualTo(ma0100.getDepCd())
                    .andPmaCdEqualTo(ma0100.getPmaCd())
                    .andCategoryCdEqualTo(ma0100.getCategoryCd())
                    .andEffectiveStsEqualTo("10");
        }else{
            example.or().andPmaCdEqualTo(ma0100.getPmaCd())
                    .andCategoryCdEqualTo(ma0100.getCategoryCd())
                    .andEffectiveStsEqualTo("10");
        }
        return ma0100Mapper.selectByExample(example);
    }

    @Override
    public List<AutoCompleteDTO> getListDepartMent(Collection<Integer> roleId, String v) {
        return null;
    }

    @Override
    public List<AutoCompleteDTO> getListDepartMentAll(String v) {
        return null;
    }

    @Override
    public List<AutoCompleteDTO> getListByDepart(String v) {
        List<AutoCompleteDTO> list=ma0070Mapper.getListAll(v);
        return list;
    }


}
