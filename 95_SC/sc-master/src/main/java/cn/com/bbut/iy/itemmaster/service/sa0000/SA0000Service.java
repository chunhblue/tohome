package cn.com.bbut.iy.itemmaster.service.sa0000;


import cn.com.bbut.iy.itemmaster.dto.AjaxResultDto;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.paymentMode.SA0000DetailGridDto;
import cn.com.bbut.iy.itemmaster.dto.paymentMode.SA0000DetailParamDto;
import cn.com.bbut.iy.itemmaster.dto.paymentMode.SA0005DetailGridDto;
import cn.com.bbut.iy.itemmaster.dto.sa0005.SA0005ParamDTO;
import cn.com.bbut.iy.itemmaster.entity.SA0000;
import cn.com.bbut.iy.itemmaster.entity.ma4180.MA4180;
import cn.com.bbut.iy.itemmaster.entity.sa0160.SA0160;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

public interface SA0000Service {
    /**
     * SA0000查询
     *
     */
    List<SA0000> getList();


    /**
     * POS支付设定一览
     * @param param
     * @return
     */
    GridDataDTO<SA0000DetailGridDto> getList(SA0000DetailParamDto param);

    /**
     * 获取posid列表
     * @param storeCd
     * @return
     */
    List<SA0160> getPosId(String storeCd);

    /**
     *  POS支付方式信息保存
     * @param param
     * @return
     */
    AjaxResultDto insertPaymentMode(SA0000 param, String paymentModeDetailJson);

    /**
     *  POS支付方式信息修改
     * @param param
     * @return
     */
    AjaxResultDto updatePaymentMode(SA0000 param, String paymentModeDetailJson);

    /**
     * POS支付方式详细信息一览
     * @param payCd
     * @return
     */
    List<SA0005DetailGridDto> getDetailList(String payCd);

    /**
     * POS支付方式详细信息头档
     * @param payCd
     * @return
     */
    SA0000 getPaymentInfo(String payCd);

    /**
     * 查询可选店铺信息
     *
     */
    List<AutoCompleteDTO> getStoreList(String v);

     int insertSa0005(SA0005ParamDTO sa0005);
     int insertoSa0005(String paymentModeDetailJson);
    int deleteSa005(SA0005ParamDTO sa0005);
    List<SA0005DetailGridDto> getDeldetailList(String payCd);
    List<SA0005ParamDTO> getdeldetailList(String payCd);

    int inserttoSa0005(SA0005ParamDTO sa0005);
}
