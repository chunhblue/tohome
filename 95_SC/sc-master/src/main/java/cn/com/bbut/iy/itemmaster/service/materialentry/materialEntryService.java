package cn.com.bbut.iy.itemmaster.service.materialentry;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.materialentry.PI0100DTOE;
import cn.com.bbut.iy.itemmaster.dto.materialentry.StocktakeItemDTOE;
import cn.com.bbut.iy.itemmaster.dto.pi0100.ItemInStoreDto;
import cn.com.bbut.iy.itemmaster.dto.pi0100.PI0100DTO;
import cn.com.bbut.iy.itemmaster.dto.pi0100.StocktakeItemDTO;
import cn.com.bbut.iy.itemmaster.dto.pi0100c.MaterialDTO;
import cn.com.bbut.iy.itemmaster.dto.pi0100c.PI0100DTOC;
import cn.com.bbut.iy.itemmaster.dto.pi0100c.PI0100ParamDTOC;
import cn.com.bbut.iy.itemmaster.dto.pi0100c.StocktakeItemDTOC;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

public interface materialEntryService {
    StocktakeItemDTOC getItemInfo(String itemCode,String storeCd);

    PI0100DTOC getData(String piCd);
//    PI0100DTOC getInvenData(String piCd);

    PI0100DTOC insertData(List<StocktakeItemDTOC> stocktakeItemList, PI0100DTOC item, HttpServletRequest request, HttpSession session,String detailType);

    GridDataDTO<PI0100DTOC> search(PI0100ParamDTOC pi0100Param);

    GridDataDTO<MaterialDTO> storeAllItem(ItemInStoreDto param);

    PI0100DTOC getInvenDataPio(PI0100DTOC pioC);

    PI0100DTOC getInvenDataPioIn(PI0100DTOC pioC);
}
