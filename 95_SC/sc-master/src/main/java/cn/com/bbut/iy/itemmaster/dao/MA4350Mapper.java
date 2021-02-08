package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dao.gen.MA4350GenMapper;
import cn.com.bbut.iy.itemmaster.dto.ma4350.Ma4350DetailGridDto;
import cn.com.bbut.iy.itemmaster.dto.ma4350.Ma4350DetailParamDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MA4350Mapper extends MA4350GenMapper {
    /**
     * 配方销售头档一览
     * @param param
     * @return
     */
    List<Ma4350DetailGridDto> getList(Ma4350DetailParamDto param);

    long getListCount(Ma4350DetailParamDto param);

    /**
     * 获取配方销售头档信息
     * @param voucherCd
     * @return
     */
    Ma4350DetailGridDto getMa4350ByVoucherCd(@Param("voucherCd") String voucherCd);
}