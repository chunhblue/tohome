package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dao.gen.SA0000GenMapper;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.paymentMode.SA0000DetailGridDto;
import cn.com.bbut.iy.itemmaster.dto.paymentMode.SA0000DetailParamDto;
import cn.com.bbut.iy.itemmaster.dto.paymentMode.SA0005DetailGridDto;
import cn.com.bbut.iy.itemmaster.dto.sa0005.SA0005ParamDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SA0000Mapper extends SA0000GenMapper {
    /**
     * POS支付方式设定一览
     * @param param
     * @return
     */
    List<SA0000DetailGridDto> getList(SA0000DetailParamDto param);
    List<SA0000DetailGridDto> getListflag(SA0000DetailParamDto param);

    long getListCount(SA0000DetailParamDto param);

    /**
     * 获取支付方式明细
     * @param payCd
     * @param businessDate
     * @return
     */
    List<SA0005DetailGridDto> getDetailList(@Param("payCd") String payCd, @Param("businessDate") String businessDate);

    List<SA0005DetailGridDto>  getPayNameList(@Param("storeCd") String storeCd,@Param("businessDate") String businessDate);


    /**
     * 判断其他支付方式是否有找零
     * @param payCd
     * @return
     */
    int getChargeByPayment(@Param("payCd") String payCd);

     List<SA0005DetailGridDto> getDelDetailList(@Param("payCd") String payCd, @Param("businessDate") String businessDate);

    List<SA0005ParamDTO> getdetailList(@Param("payCd")String payCd, @Param("businessDate")String businessDate);
      int deleteSa0005(@Param("sa005") SA0005ParamDTO sa0005ParamDTO);
}