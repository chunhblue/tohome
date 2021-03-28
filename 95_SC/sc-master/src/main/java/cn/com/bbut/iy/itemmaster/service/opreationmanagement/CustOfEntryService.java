package cn.com.bbut.iy.itemmaster.service.opreationmanagement;

import cn.com.bbut.iy.itemmaster.dto.pi0100c.PI0100DTOC;
import cn.com.bbut.iy.itemmaster.dto.pi0100c.StocktakeItemDTOC;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

public interface CustOfEntryService {

//    StocktakeItemDTOC getItemInfo(String itemCode, String piCd, String piDate);
    StocktakeItemDTOC getItemInfo(String itemCode,String storeCd);

    int insert(String piCd, String piDate, List<StocktakeItemDTOC> stocktakeItemList);

    PI0100DTOC getData(String piCd);

    PI0100DTOC insertData(List<StocktakeItemDTOC> stocktakeItemList, PI0100DTOC item, HttpServletRequest request, HttpSession session);

    PI0100DTOC getDataPio(PI0100DTOC pioC);

    PI0100DTOC getDataPioIn(PI0100DTOC pioC);
}
