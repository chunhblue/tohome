package cn.com.bbut.iy.itemmaster.service.fsInvenoryentry;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.fsInventory.PI0100DTOD;
import cn.com.bbut.iy.itemmaster.dto.fsInventory.StocktakeItemDTOD;
import cn.com.bbut.iy.itemmaster.dto.pi0100c.PI0100DTOC;
import cn.com.bbut.iy.itemmaster.dto.pi0100c.PI0100ParamDTOC;
import cn.com.bbut.iy.itemmaster.dto.pi0100c.StocktakeItemDTOC;
import cn.com.bbut.iy.itemmaster.dto.receipt.rrVoucher.RRVoucherDTO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

public interface fsInventoryEntryService {
    StocktakeItemDTOC getItemInfo(String itemCode,String storeCd);

    PI0100DTOC getData(String piCd);

    PI0100DTOC insertData(List<StocktakeItemDTOC> stocktakeItemList, PI0100DTOC item, HttpServletRequest request, HttpSession session);

    GridDataDTO<PI0100DTOC> search(PI0100ParamDTOC pi0100Param);

    String fileUpload(MultipartFile file, HttpServletRequest request, HttpSession session,String storeCd);

    List<PI0100DTOC> getPrintData(PI0100ParamDTOC param);
}
