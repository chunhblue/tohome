package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dao.gen.MA2000GenMapper;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.vendor.VendorDTO;
import cn.com.bbut.iy.itemmaster.entity.ma2000.MA2000;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MA2000Mapper extends MA2000GenMapper {

    String selectByVendorId(@Param("vendorId") String vendorId);

    MA2000 selectById(@Param("businessDate") String businessDate,@Param("vendorId") String vendorId);

    List<AutoCompleteDTO> getListAll(@Param("businessDate") String businessDate, @Param("vendorId") String vendorId,@Param("storeCd")String storeCd);

    List<AutoCompleteDTO> getVendor(@Param("vendorId") String vendorId);
}