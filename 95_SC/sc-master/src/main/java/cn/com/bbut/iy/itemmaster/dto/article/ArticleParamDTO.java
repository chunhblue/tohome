package cn.com.bbut.iy.itemmaster.dto.article;

import cn.com.bbut.iy.itemmaster.dto.base.CommonDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;
import cn.com.bbut.iy.itemmaster.dto.base.role.ResourceViewDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * 商品主档查询对象
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class ArticleParamDTO extends GridParamDTO {

    private int limitStart;

    private String businessDate;

    private String articleId;

    private String effectiveStartDate;

    private String barcode;

    private String articleName;

    private String deliveryTypeCd;

    private String articleType;

    private String materialType;

    private String lifecycleStatus;

    private String brand;

    private String depCd;

    private String pmaCd;

    private String categoryCd;

    private String subCategoryCd;

    private String vendorId;

    // 是否需要分页
    private boolean flg = true;

    //资源组
    private List<ResourceViewDTO> resources;
}
