package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dto.fsInventory.PI0100DTOD;
import cn.com.bbut.iy.itemmaster.dto.fsInventory.PI0100ParamDTOD;
import cn.com.bbut.iy.itemmaster.dto.fsInventory.PI0110DTOD;
import cn.com.bbut.iy.itemmaster.dto.pi0100.StocktakeItemDTO;
import cn.com.bbut.iy.itemmaster.dto.pi0100c.PI0100DTOC;
import cn.com.bbut.iy.itemmaster.dto.pi0100c.PI0100ParamDTOC;
import cn.com.bbut.iy.itemmaster.dto.pi0100c.PI0110DTOC;
import cn.com.bbut.iy.itemmaster.dto.pi0100c.StocktakeItemDTOC;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
@Component
public interface fsInventoryPlanMapper {
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

    PI0100DTOC getPI0145ByPrimary(@Param("piCd") String piCd);

    List<Map<String, String>> getfsInventoryParam(@Param("type") String type);

    List<StocktakeItemDTOC> getDetailList(String storeCd, @Param("articles")Collection<String> articles);
}
