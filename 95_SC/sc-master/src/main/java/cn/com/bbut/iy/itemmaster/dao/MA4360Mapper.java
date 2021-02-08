package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dao.gen.MA4360GenMapper;
import cn.com.bbut.iy.itemmaster.dto.ma4360.Ma4360DetailGridDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MA4360Mapper extends MA4360GenMapper {
    /**
     * 获取配方销售单商品详细信息
     * @param voucherCd
     * @return
     */
    List<Ma4360DetailGridDto> getList(@Param("voucherCd") String voucherCd);
}