package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dto.materialentry.PI0100DTOE;
import cn.com.bbut.iy.itemmaster.dto.materialentry.PI0100ParamDTOE;
import cn.com.bbut.iy.itemmaster.dto.materialentry.PI0110DTOE;
import cn.com.bbut.iy.itemmaster.dto.materialentry.StocktakeItemDTOE;
import cn.com.bbut.iy.itemmaster.dto.pi0100.PI0100DTO;
import cn.com.bbut.iy.itemmaster.dto.pi0100.PI0100ParamDTO;
import cn.com.bbut.iy.itemmaster.dto.pi0100.PI0110DTO;
import cn.com.bbut.iy.itemmaster.dto.pi0100.StocktakeItemDTO;
import cn.com.bbut.iy.itemmaster.dto.pi0100c.PI0100DTOC;
import cn.com.bbut.iy.itemmaster.dto.pi0100c.PI0100ParamDTOC;
import cn.com.bbut.iy.itemmaster.dto.pi0100c.PI0110DTOC;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface materialPlanMapper {
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

    List<PI0110DTOC> getPI0140ByPrimary(@Param("piCd") String piCd, @Param("piDate") String piDate);

    List<Map<String, String>> getfsInventoryParam(@Param("type") String type);
}
