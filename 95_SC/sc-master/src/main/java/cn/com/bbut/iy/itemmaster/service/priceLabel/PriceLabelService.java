package cn.com.bbut.iy.itemmaster.service.priceLabel;

import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.priceLabel.PriceLabelDTO;
import cn.com.bbut.iy.itemmaster.dto.priceLabel.PriceLabelParamDTO;

import java.util.List;

public interface PriceLabelService {

    /**
     * 查询未来三天价签数据
     * @param param
     * @return
     */
    GridDataDTO<PriceLabelDTO> search(PriceLabelParamDTO param);

    /**
     * 获取未来三天价签商品数据
     * @param v
     * @return
     */
    List<AutoCompleteDTO> getItemList(String v);

    /**
     * 打印未来三天价签数据
     * @param param
     * @return
     */
    List<PriceLabelDTO> getPrintData(PriceLabelParamDTO param);
}
