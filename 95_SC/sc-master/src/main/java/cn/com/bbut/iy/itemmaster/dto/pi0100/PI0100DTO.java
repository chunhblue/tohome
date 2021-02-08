package cn.com.bbut.iy.itemmaster.dto.pi0100;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import lombok.Data;

import java.util.List;

@Data
public class PI0100DTO extends GridDataDTO {

    private String piCd;
    private String piDate;
    private String storeCd;
    private String piType;
    private String piMethod;
    private String piStatus;
    private String piStartTime;
    private String piEndTime;
    private String remarks;
    private String createYmd;
    private String createHms;
    private String createUserId;
    private String flag;
    private String storeName;
    private String updateUserId;
    private String updateYmd;
    private String updateHms;
    private String piAccountFlg; // 是否扎帐
    private String piFirstFinishFlg; // 是否初盘完成
    private String piCommitFlg; // 是否认列
    private String reviewStatus;
    private String piStatusCode;
    private String piTypeCode;
    private String exportFlg;
    private Integer dayEndOfNow; // 距离下次盘点/下月末天数

    // 明细
    private List<PI0110DTO> details;

    // 商品明细
    private List<StocktakeItemDTO> itemList;

    //附件信息
    private String fileDetailJson;
}
