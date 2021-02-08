package cn.com.bbut.iy.itemmaster.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.com.bbut.iy.itemmaster.dao.gen.IyBmCkGenMapper;
import cn.com.bbut.iy.itemmaster.dto.bm.BmJsonParamDTO;
import cn.com.bbut.iy.itemmaster.dto.bm.BmListGridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.bm.BmOutExcel;

public interface IyBmCkMapper extends IyBmCkGenMapper {
    /**
     * 根据参数取得bmck表数据
     * 
     * @param param
     * @return
     */
    List<BmListGridDataDTO> getBmCkDataByJsonParam(BmJsonParamDTO param);

    /**
     * 根据参数ck数据量
     * 
     * @param param
     * @return
     */
    Long getBmCkCountByJsonParam(BmJsonParamDTO param);

    /**
     * 得到01 02 03类型的bm数量（ck）
     * 
     * @param bmType
     * @param bmCode
     * @return
     */
    Integer getBmCkNumber(@Param("bmType") String bmType, @Param("bmCode") String bmCode);

    /**
     * 得到04类型的bm数量（ck）
     * 
     * @param bmType
     * @param bmCode
     * @return
     */
    Integer getBmCkNumberTypeFour(@Param("bmType") String bmType, @Param("bmCode") String bmCode);

    /**
     * 根据参数得到事业部长的待审核数据
     * 
     * @param checkFlg
     *            审核状态
     * @param selectRightFlg
     *            查看权限标志位
     * @param dptList
     *            资源
     * @return
     */
    List<BmListGridDataDTO> getSecretaryCheckCount(@Param("checkFlg") String checkFlg,
            @Param("rightFlg") String selectRightFlg, @Param("resources") List<String> dptList);

    /**
     * 根据参数的到ck表中bm的excel导出数据
     * 
     * @param bmJsonParam
     * @return
     */
    List<BmOutExcel> getBmOutExcelData(BmJsonParamDTO bmJsonParam);
}