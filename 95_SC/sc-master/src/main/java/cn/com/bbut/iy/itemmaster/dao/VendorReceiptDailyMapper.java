package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.vendorReceiptDaily.VendorReceiptDailyDTO;
import cn.com.bbut.iy.itemmaster.dto.vendorReceiptDaily.VendorReceiptDailyParamDTO;
import cn.com.bbut.iy.itemmaster.entity.base.Ma1000;
import cn.com.bbut.iy.itemmaster.entity.ma0020.MA0020C;
import cn.com.bbut.iy.itemmaster.entity.ma0080.MA0080;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface VendorReceiptDailyMapper {

    // 查询
    // 获取收货商品(Form 供应商)
    List<VendorReceiptDailyDTO> search(@Param("receiptDaily") VendorReceiptDailyParamDTO receiptDaily);
    List<VendorReceiptDailyDTO> search1(@Param("receiptDaily") VendorReceiptDailyParamDTO receiptDaily);

    List<MA0080> getPmaList();

    List<AutoCompleteDTO> getAMList(@Param("flag")String flag,@Param("v")String v);

    List<AutoCompleteDTO> getOMList(@Param("flag")String flag,@Param("v")String v);

    List<MA0020C> getCity();

    // 获取收货商品(Form DC)
    List<VendorReceiptDailyDTO> searchDcReceipt(@Param("receiptDaily") VendorReceiptDailyParamDTO receiptDaily);

    // 获取收货商品(Form DC) 打印
    List<VendorReceiptDailyDTO> getprintData(@Param("receiptDaily") VendorReceiptDailyParamDTO receiptDaily);

    int searchCount(@Param("receiptDaily")VendorReceiptDailyParamDTO param);

    int searchDcReceiptCount(@Param("receiptDaily")VendorReceiptDailyParamDTO param);

    int getItemCount(@Param("receiptDaily") VendorReceiptDailyParamDTO param);

    void deleteTempTable(@Param("tableName") String tmp_order_article);

    int SearchCount(@Param("receiptDaily") VendorReceiptDailyParamDTO param);
    int SearchCount1(@Param("receiptDaily") VendorReceiptDailyParamDTO param);

}
