package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dao.gen.Cm9010GenMapper;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface Cm9010Mapper extends Cm9010GenMapper {

    List<AutoCompleteDTO> getWriteOffReasonValue(@Param("v") String v);

    // 店间调拨数量不一致原因
    List<AutoCompleteDTO> getDifferenceReason(@Param("v") String v);

    // 退货数量、收货不一致原因
    List<AutoCompleteDTO> getReturnDifferReason(@Param("v") String v);

    // 收货数量、订货不一致原因
    List<AutoCompleteDTO> getReceiptDifferReason(@Param("v") String v);

    // 传票类型自动下拉
    List<AutoCompleteDTO> getTypeList(@Param("v") String v);

    List<AutoCompleteDTO> getReasonValue(@Param("v")String v);

    /**
     * 获取所有的库存异动原因
     * @param v
     * @return
     */
    List<AutoCompleteDTO> getAllReasonValue(@Param("v")String v);

    List<AutoCompleteDTO> getItemOutInReasonCode(@Param("v") String v);
}