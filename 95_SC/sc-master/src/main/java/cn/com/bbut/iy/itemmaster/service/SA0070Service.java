package cn.com.bbut.iy.itemmaster.service;


import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.base.ReturnDTO;
import cn.com.bbut.iy.itemmaster.dto.priceChange.Price;
import cn.com.bbut.iy.itemmaster.dto.priceChange.PriceDetailGridDto;
import cn.com.bbut.iy.itemmaster.dto.priceChange.PriceDetailParamDto;
import cn.com.bbut.iy.itemmaster.dto.priceChange.PriceSts;
import cn.com.bbut.iy.itemmaster.entity.sa0070.SA0070;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * SA0070
 *
 * @author zcz
 */
public interface SA0070Service {
    /**
     * 条码取出货号
     * @param barcode
     * @return
     */
    String getArticleId(String barcode);
    /**
     * 获取变价状态
     * @param articleId
     * @return
     */
    PriceSts getPriceSts(String articleId);

    /**
     * 获取变价单号信息
     * @param changeId
     * @return
     */
    String getChangeId(String changeId);

    /**
     * 获取商品列表
     * @param articleId
     * @return
     */
    Price getPrice(String articleId,String effectiveDate,String storeCd);

    /**
     *  保存紧急变价信息
     * @param param
     * @return
     */
    ReturnDTO insertChangePrice(HttpSession session, HttpServletRequest request, SA0070 param, String orderDetailJson);


    /**
     * 获取商品名称
     * @param articleId
     * @return
     */
    String getArticleName(String articleId);

    /**
     * 紧急变价一览
     * @param param
     * @return
     */
    GridDataDTO<PriceDetailGridDto> getList(PriceDetailParamDto param);

    List<Price> getArticleAndName(String v, String storeCd, String effectiveDate);

    String formatDate(String str);
}
