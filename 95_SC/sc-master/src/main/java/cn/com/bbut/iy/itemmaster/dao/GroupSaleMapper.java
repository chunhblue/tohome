package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.groupSale.*;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupSaleMapper {

    /**
     * 条件查询记录
     * @param dto
     */
    List<GroupSaleDTO> selectListByCondition(GroupSaleParamDTO dto);

    /**
     * 获取 Group 商品数据
     * @param businessDate
     * @param v
     * @return
     */
    List<AutoCompleteDTO> getGroupItemList(String businessDate, String v);
}
