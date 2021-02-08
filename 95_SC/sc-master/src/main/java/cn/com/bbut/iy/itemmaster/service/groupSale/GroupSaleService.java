package cn.com.bbut.iy.itemmaster.service.groupSale;

import java.util.List;

import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.groupSale.*;

/**
 * @author mxy
 */
public interface GroupSaleService {

    /**
     * 条件查询数据
     * @param dto
     * @return
     */
    List<GroupSaleDTO> getList(GroupSaleParamDTO dto);

    /**
     * 获取 Group 下拉数据
     * @param v
     * @return
     */
    List<AutoCompleteDTO> getGroupItemList(String v);
}
