package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.priceLabel.PriceLabelDTO;
import cn.com.bbut.iy.itemmaster.dto.priceLabel.PriceLabelParamDTO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PriceLabelMapper {

    /**
     * 未来三天的价签数据
     */
    List<PriceLabelDTO> search(@Param("param") PriceLabelParamDTO param);

    /**
     * 查询总条数
     */
    Integer searchCount(@Param("param")PriceLabelParamDTO param);

    /**
     * 获取未来三天价签商品数据
     * @param v
     * @return
     */
    List<AutoCompleteDTO> getItemList(@Param("v") String v);
}
