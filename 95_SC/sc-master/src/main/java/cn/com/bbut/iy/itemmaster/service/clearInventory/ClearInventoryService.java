package cn.com.bbut.iy.itemmaster.service.clearInventory;

import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.base.ReturnDTO;
import cn.com.bbut.iy.itemmaster.dto.clearInventory.ClearInventoryDTO;
import cn.com.bbut.iy.itemmaster.entity.User;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

public interface ClearInventoryService {

    /**
     * 上传商品
     */
    String insertImportExcel(User user, MultipartFile file, HttpServletRequest request, HttpSession session);

    /**
     * 查询记录
     */
    GridDataDTO<ClearInventoryDTO> inquire(User user, String searchJson, int page, int rows, HttpServletRequest request, HttpSession session);

    /**
     * 获取商品List
     */
    List<AutoCompleteDTO> getItemList(String v);

    /**
     * 获得商品详细信息
     */
    ReturnDTO getItemInfo(String articleId, HttpServletRequest request, HttpSession session);

    /**
     * 按钮点击添加商品
     */
    ReturnDTO insert(ClearInventoryDTO dto, User user, HttpServletRequest request, HttpSession session);
}
