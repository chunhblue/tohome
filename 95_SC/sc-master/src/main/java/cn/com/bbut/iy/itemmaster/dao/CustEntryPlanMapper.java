package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dto.pi0100.ItemInStoreDto;
import cn.com.bbut.iy.itemmaster.dto.pi0100c.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CustEntryPlanMapper {
    /**
     * 保存盘点计划主档表
     * @param pi0100
     */
    void savePI0100(@Param("pi0100") PI0100DTOC pi0100);

    /**
     * 保存盘点计划明细表
     * @param pi0110List
     */
    void savePI0110(@Param("pi0110List") List<PI0110DTOC> pi0110List);

    /**
     * 查询数据
     * @param pi0100Param
     * @return
     */
    List<PI0100DTOC> search(@Param("pi0100Param") PI0100ParamDTOC pi0100Param);

    /**
     * 查询记录数
     * @param pi0100Param
     * @return
     */
    int selectCountByParam(@Param("pi0100Param") PI0100ParamDTOC pi0100Param);

    /**
     * 获得pmaList数据
     * @return
     */
    List<Map<String, String>> getPmaList();

    PI0100DTOC getPI0100ByPrimary(@Param("piCd") String piCd);

    List<PI0110DTOC> getPI0110ByPrimary(@Param("piCd") String piCd, @Param("piDate") String piDate);

    List<Map<String, String>> getStocktakeParam(@Param("type") String type);

    void updatePI0100(@Param("pi0100") PI0100DTOC pi0100);

    void deletePI0110(@Param("piCd") String piCd, @Param("piDate") String piDate);

    int checkPicd(@Param("piCd") String piCd);

    List<StocktakeItemDTOC> queryExport(@Param("piCd") String piCd, @Param("piDate") String piDate);

    void savePI0100C(PI0100DTOC pi0100c);

    List<PI0100DTOC> getPrintData(@Param("pi0100Param") PI0100ParamDTOC pi0100Param);

    List<CostOfDTO> getAllItem(@Param("param")ItemInStoreDto param);
}
