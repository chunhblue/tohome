package cn.com.bbut.iy.itemmaster.serviceimpl.groupSale;

import cn.com.bbut.iy.itemmaster.dao.Cm9060Mapper;
import cn.com.bbut.iy.itemmaster.dao.GroupSaleMapper;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.groupSale.*;
import cn.com.bbut.iy.itemmaster.entity.base.Cm9060;
import cn.com.bbut.iy.itemmaster.service.groupSale.GroupSaleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Bom销售报表
 * 
 * @author mxy
 */
@Slf4j
@Service
public class GroupSaleServiceImpl implements GroupSaleService {

    @Autowired
    private GroupSaleMapper groupSaleMapper;
    @Autowired
    private Cm9060Mapper cm9060Mapper;


    /**
     * 获取当前业务日期
     */
    public String getBusinessDate() {
        Cm9060 dto =  cm9060Mapper.selectByPrimaryKey("0000");
        return dto.getSpValue();
    }

    /**
     * 条件查询数据
     *
     * @param dto
     * @return
     */
    @Override
    public List<GroupSaleDTO> getList(GroupSaleParamDTO dto) {
        dto.setBusinessDate(getBusinessDate());
        return groupSaleMapper.selectListByCondition(dto);
    }

    /**
     * 获取 Group 下拉数据
     * @param v
     * @return
     */
    @Override
    public List<AutoCompleteDTO> getGroupItemList(String v) {
        String businessDate = getBusinessDate();
        return groupSaleMapper.getGroupItemList(businessDate,v);
    }
}
