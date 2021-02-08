package cn.com.bbut.iy.itemmaster.service.master;

import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.vendor.*;

import java.util.List;


public interface VendorMasterService {

    /**
     *
     * 条件查询主档记录
     */
    GridDataDTO<VendorDTO> getList(VendorParamDTO dto);

    /**
     *
     * 查询主档基本信息
     */
    VendorDTO getBasicInfo(VendorParamDTO dto);

    /**
     *
     * 查询主档配送范围
     */
    GridDataDTO<DeliveryTypeDTO> getDeliveryType(VendorParamDTO dto);

    /**
     *
     * 查询主档最小订货数量/金额
     */
    GridDataDTO<Ma2002DTO> getMa2002List(VendorParamDTO dto);

    /**
     *
     * 查询主档大分类信息
     */
    GridDataDTO<Ma2003DTO> getMa2003List(VendorParamDTO dto);

    /**
     *
     * 查询主档银行信息
     */
    GridDataDTO<Ma2004DTO> getMa2004List(VendorParamDTO dto);

    /**
     * 检索供应商
     *
     */
    List<AutoCompleteDTO> getList(String v);

    /**
     *
     * 查询主档商品分类信息
     */
    GridDataDTO<Ma2005DTO> getMa2005List(VendorParamDTO dto);

    /**
     * 获取 vendor email
     */
    GridDataDTO<Ma2008DTO> getVendorEmail(VendorParamDTO param);

    /**
     * 获取trading term
     */
    GridDataDTO<Ma2007DTO> getTradingTerm(VendorParamDTO param);

    /**
     * 获取account
     */
    GridDataDTO<Ma2009DTO> getAccountInfo(VendorParamDTO param);
}
