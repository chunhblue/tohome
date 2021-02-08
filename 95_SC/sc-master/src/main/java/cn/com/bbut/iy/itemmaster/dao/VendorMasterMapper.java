package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.vendor.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface VendorMasterMapper {

    /**
     * 条件查询主档记录总数
     * @param dto
     */
    int selectCountByCondition(VendorParamDTO dto);

    /**
     * 条件查询主档记录
     * @param dto
     */
    List<VendorDTO> selectListByCondition(VendorParamDTO dto);

    /**
     * 查询主档基本信息
     * @param dto
     */
    VendorDTO selectVendor(VendorParamDTO dto);

    /**
     * 查询主档配送范围
     * @param dto
     */
    List<DeliveryTypeDTO> selectDeliveryType(VendorParamDTO dto);


    /**
     * 查询主档最小订货数量/金额
     * @param dto
     */
    List<Ma2002DTO> selectMa2002(VendorParamDTO dto);

    /**
     * 查询主档大分类信息
     * @param dto
     */
    List<Ma2003DTO> selectMa2003(VendorParamDTO dto);

    /**
     * 查询主档银行信息
     * @param dto
     */
    List<Ma2004DTO> selectMa2004(VendorParamDTO dto);

    /**
     * 检索供应商
     * @param v
     */
    List<AutoCompleteDTO> selectList(@Param("businessDate") String businessDate, @Param("v") String v);

    /**
     * 查询主档商品分类信息
     * @param dto
     */
    List<Ma2005DTO> selectMa2005(VendorParamDTO dto);

    /**
     * 查询vendor email
     * @param param
     * @return
     */
    List<Ma2008DTO> selectMa2008(VendorParamDTO param);

    /**
     * 查询trading数据
     */
    List<Ma2007DTO> selectMa2007(VendorParamDTO param);

    /**
     * 查询account数据
     */
    List<Ma2009DTO> selectMa2009(VendorParamDTO param);
}
