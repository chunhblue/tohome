package cn.com.bbut.iy.itemmaster.service.bomSale;

import java.util.List;

import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.bomSale.BomSaleDTO;
import cn.com.bbut.iy.itemmaster.dto.bomSale.BomSaleParamDTO;

/**
 * @author mxy
 */
public interface BomSaleService {

    /**
     * 条件查询数据
     * @param dto
     * @return
     */
    List<BomSaleDTO> getList(BomSaleParamDTO dto);

    /**
     * bom 商品 下拉
     */
    List<AutoCompleteDTO> getBomItemList(String v);
}
