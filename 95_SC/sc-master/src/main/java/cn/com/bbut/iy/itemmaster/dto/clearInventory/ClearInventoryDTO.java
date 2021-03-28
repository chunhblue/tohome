package cn.com.bbut.iy.itemmaster.dto.clearInventory;

import cn.com.bbut.iy.itemmaster.dto.AjaxResultDto;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.pi0100.StocktakeItemDTO;
import cn.com.bbut.iy.itemmaster.service.clearInventory.ClearInventoryService;
import cn.com.bbut.iy.itemmaster.util.ExcelBaseUtil;
import com.google.gson.Gson;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.List;

@Data
public class ClearInventoryDTO extends GridDataDTO {

    private String accDate;

    private String articleId;

    private String articleName;

    private String spec;

    private String uom;

    private BigDecimal onHandQty;

    /**
     * 录入日期
     */
    private String createYmd;

    /**
     * 录入时间
     */
    private String createHms;

    /**
     * 录入人id
     */
    private String createUserId;

    /**
     * 修改日期
     */
    private String updateYmd;

    /**
     * 修改时间
     */
    private String updateHms;

    /**
     * 修改人id
     */
    private String updateUserId;

}
