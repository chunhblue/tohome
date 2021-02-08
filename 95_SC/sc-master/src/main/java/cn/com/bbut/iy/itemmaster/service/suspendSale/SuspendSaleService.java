package cn.com.bbut.iy.itemmaster.service.suspendSale;

import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.priceChange.Price;
import cn.com.bbut.iy.itemmaster.dto.priceChange.PriceDetailGridDto;
import cn.com.bbut.iy.itemmaster.dto.priceChange.PriceDetailParamDto;
import cn.com.bbut.iy.itemmaster.entity.sa0070.SA0070;
import org.apache.ibatis.annotations.Param;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

public interface SuspendSaleService {

    // 查询商品基本信息
    Price getPrice(String articleId, String effectiveDate);

    // 查询商品在当前城市的条码
    String getBarcodeByInfo(String articleId,String storeCd,String businessDate);

    // 保存暂停销售商品
    boolean insertSuppendSaleItem(HttpSession session, HttpServletRequest request, SA0070 param, String orderDetailJson);

    String getDocId(String docId);

    // 查询一览数据
    GridDataDTO<PriceDetailGridDto> getList(PriceDetailParamDto param);

    // 获取组织架构数据
    List<AutoCompleteDTO> selectListByLevel(String level, String adminId, String articleId,String accDate, String v);

    // 根据区域 cd获取下面所有的店铺
    List<AutoCompleteDTO> getStoreListByDistrictCd(String cityCd,String districtCd, String articleId, String accDate);
}
