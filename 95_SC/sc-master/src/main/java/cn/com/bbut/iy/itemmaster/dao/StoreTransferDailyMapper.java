package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dto.inventory.Sk0020DTO;
import cn.com.bbut.iy.itemmaster.dto.inventory.Sk0020ParamDTO;
import cn.com.bbut.iy.itemmaster.dto.vendorReceiptDaily.VendorReceiptDailyParamDTO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface StoreTransferDailyMapper {

    List<Sk0020DTO> search(@Param("param") VendorReceiptDailyParamDTO param);

    List<Sk0020DTO> getList(@Param("param") VendorReceiptDailyParamDTO param);

    int getListCount(@Param("param")VendorReceiptDailyParamDTO param);

    int searchCount(@Param("param")VendorReceiptDailyParamDTO param);
}
