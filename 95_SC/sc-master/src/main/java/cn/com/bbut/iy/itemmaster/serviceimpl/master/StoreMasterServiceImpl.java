package cn.com.bbut.iy.itemmaster.serviceimpl.master;

import cn.com.bbut.iy.itemmaster.dao.Cm9060Mapper;
import cn.com.bbut.iy.itemmaster.dao.MA0050Mapper;
import cn.com.bbut.iy.itemmaster.dao.MA1020Mapper;
import cn.com.bbut.iy.itemmaster.dao.StoreMasterMapper;
import cn.com.bbut.iy.itemmaster.dto.article.*;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.store.*;
import cn.com.bbut.iy.itemmaster.entity.*;
import cn.com.bbut.iy.itemmaster.entity.base.Cm9060;
import cn.com.bbut.iy.itemmaster.service.master.ArticleMasterService;
import cn.com.bbut.iy.itemmaster.service.master.StoreMasterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class StoreMasterServiceImpl implements StoreMasterService {

    @Autowired
    private StoreMasterMapper mapper;
    @Autowired
    private Cm9060Mapper cm9060Mapper;
    @Autowired
    private MA0050Mapper ma0050Mapper;
    @Autowired
    private MA1020Mapper ma1020Mapper;

    /**
     * 获取当前业务日期
     */
    public String getBusinessDate() {
        // 数据从总部系统迁移，业务日期需匹配
        Cm9060 dto =  cm9060Mapper.selectByPrimaryKey("0000");
        return dto.getSpValue();
    }

    /**
     * 条件查询主档记录
     *
     * @param dto
     */
    @Override
    public GridDataDTO<StoreGridDTO> getList(StoreParamDTO dto) {
        String businessDate = getBusinessDate();
        dto.setBusinessDate(businessDate);
        long count = mapper.selectCountByCondition(dto);
        if(count < 1){
            return new GridDataDTO<>();
        }
        List<StoreGridDTO> _list = mapper.selectListByCondition(dto);
        GridDataDTO<StoreGridDTO> data = new GridDataDTO<>(_list,
                dto.getPage(), count, dto.getRows());
        return data;
    }

    @Override
    public StoreInfoDTO getStoreInfo(String storeCd, String effectiveStartDate) {
        return mapper.getStoreInfo(storeCd,effectiveStartDate);
    }

    @Override
    public List<MA0050> getAttachedGroup(String code, String attributeType) {
        MA0050Example ma0050Example = new MA0050Example();
        ma0050Example.or().andAdditionalAttributeCdEqualTo(code)
                .andAttributeTypeEqualTo(attributeType)
                .andEffectiveStsEqualTo("10");
        List<MA0050> list = ma0050Mapper.selectByExample(ma0050Example);
        return list;
    }

    @Override
    public List<MA1020> getAttachedInfo(String storeCd, String effectiveStartDate, String attributeType) {
        MA1020Example ma1020Example = new MA1020Example();
        ma1020Example.or().andAdditionalAttributeCdEqualTo(attributeType)
                .andStoreCdEqualTo(storeCd)
                .andEffectiveStartDateEqualTo(effectiveStartDate);
        List<MA1020> list = ma1020Mapper.selectByExample(ma1020Example);
        return list;
    }

    @Override
    public List<MA1040> getLicenseInfo(String storeCd, String effectiveStartDate) {
        return mapper.getLicenseInfo(storeCd,effectiveStartDate);
    }

    @Override
    public List<MA1050GridDTO> getAccountingInfo(String storeCd, String effectiveStartDate) {
        return mapper.getAccountingInfo(storeCd,effectiveStartDate);
    }

    @Override
    public List<MA1060GridDTO> getCompetitorInfo(String storeCd, String effectiveStartDate) {
        return mapper.getCompetitorInfo(storeCd,effectiveStartDate);
    }

    @Override
    public List<AutoCompleteDTO> getMa0010() {
        return mapper.getMa0010();
    }

    @Override
    public List<AutoCompleteDTO> getMa0200() {
        return  mapper.getMa0200();
    }

    @Override
    public List<AutoCompleteDTO> getMa0030(String v) {
        return mapper.getMa0030(v);
    }

    @Override
    public List<AutoCompleteDTO> getMa0035(String storeTypeCd,String v) {
        return mapper.getMa0035(storeTypeCd,v);
    }

    @Override
    public StoreInfoDTO getClusterByStoreCd(String storeCd) {
        return mapper.getClusterByStoreCd(storeCd);
    }

    @Override
    public StoreInfoDTO getGroupByStoreCd(String storeCd) {
        return mapper.getGroupByStoreCd(storeCd);
    }

    @Override
    public List<AutoCompleteDTO> getMa0025(String level,String adminAddressCd,String v) {
        return mapper.getMa0025(level,adminAddressCd,v);
    }

    @Override
    public List<AutoCompleteDTO> getMa0050(String attributeType) {
        return mapper.getMa0050(attributeType);
    }

}
