package cn.com.bbut.iy.itemmaster.service;

import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.cash.CashDetail;
import cn.com.bbut.iy.itemmaster.dto.cash.CashDetailParam;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 金种
 * @author zcz
 */
public interface CashService {
    GridDataDTO<CashDetail> getCashDetailyList(CashDetailParam dto);
    List<CashDetail> getCashDetailList(CashDetailParam dto);

    String getUserName(String userId);

    CashDetail getCashDetail(CashDetail cashDetail);

    List<CashDetail> getPayList(CashDetail cashDetail,List<CashDetail> cashDetails);

    List<CashDetail> getCashList(CashDetail cashDetail);

    CashDetail insertOrUpdate(CashDetail cashDetail,String payList,String cashList);

    List<AutoCompleteDTO> getCashierList(String v, String storeCd);

    CashDetail getDetailBypayId(String payId);

    /**
     * 获取营业员一览
     * @param v
     * @param stores 店铺权限
     * @return
     */
    List<AutoCompleteDTO> getStaff(String v, Collection<String> stores);
}
