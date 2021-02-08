package cn.com.bbut.iy.itemmaster.dto.ReconciliationMng;

import cn.com.bbut.iy.itemmaster.dto.base.CommonDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.File;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class ReconciliationMngDto  extends GridDataDTO {

    private String transCd;

    private String storeCd;

    private String storeName;

    private String articleId;

    private String articleName;

    private String transDate;

    private String barcode;


    private BigDecimal ckQty;

    private BigDecimal ckAmount;

    private BigDecimal thirdQty;

    private BigDecimal thirdAmount;

    private BigDecimal varyQty;

    private BigDecimal varyAmount;


    //附件信息
    private String fileDetailJson;

    // 上传flg
    private String uploadFlg;

    // 上传日期
    private String uploadDate;

    // 用户信息
    private CommonDTO commonDTO;

    // 上传文件夹的类型编号
    private String documentReconCd;

    private String documentReconName;

    // 上传文件的类型编号
    private String excelGroupCd;

    private String excelGroupName;
    // 保存的文件对象
    private File file;

}
