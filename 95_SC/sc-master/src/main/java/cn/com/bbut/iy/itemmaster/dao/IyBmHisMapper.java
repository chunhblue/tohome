package cn.com.bbut.iy.itemmaster.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.com.bbut.iy.itemmaster.dao.gen.IyBmHisGenMapper;
import cn.com.bbut.iy.itemmaster.dto.bm.BmOutExcel;
import cn.com.bbut.iy.itemmaster.dto.bmhis.BmHisJsonParamDTO;
import cn.com.bbut.iy.itemmaster.dto.bmhis.BmHisListGridDataDTO;

public interface IyBmHisMapper extends IyBmHisGenMapper {

    /**
     * 根据参数得到his grid数据
     * 
     * @param bmJsonParam
     * @return
     */
    List<BmHisListGridDataDTO> getBmHisListByParam(BmHisJsonParamDTO bmJsonParam);

    /**
     * 得到数据量
     * 
     * @param bmJsonParam
     * @return
     */
    long getBmHisCountByParam(BmHisJsonParamDTO bmJsonParam);

    /**
     * 根据key 得到04类型的bm数量
     * 
     * @param newNo
     * @param newNoSub
     * @return@Param("bmType") String bmType, @Param("bmCode") String bmCode
     */
    Integer getBmNumberTypeFour(@Param("newNo") String newNo, @Param("newNoSub") String newNoSub);

    /**
     * 得到excel导出数据
     * 
     * @param bmJsonParam
     * @return
     */
    List<BmOutExcel> getBmOutExcelData(BmHisJsonParamDTO bmJsonParam);
}