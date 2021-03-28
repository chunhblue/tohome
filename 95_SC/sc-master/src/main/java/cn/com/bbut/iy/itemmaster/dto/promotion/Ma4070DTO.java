package cn.com.bbut.iy.itemmaster.dto.promotion;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * MM促销商品设定对象
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ma4070DTO extends GridDataDTO {

    private String promotionCd;

    private String promotionTermGroup;

    private String articleId;
    private String articleName;

    private String barcode;

    private String subCategoryCd;
    private String subCategoryName;

    private BigDecimal articleSalePrice;

    private BigDecimal displaySeq;

    private String effectiveSts;

    private String cityName;

    private String maName;

    /**
     * (non-Javadoc)
     * hashCode
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((articleId == null) ? 0 : articleId.hashCode());
        result = prime * result + ((cityName == null) ? 0 : cityName.hashCode());
        result = prime * result + ((maName == null) ? 0 : maName.hashCode());
        return result;
    }

    /**
     * (non-Javadoc)
     * equals
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Ma4070DTO other = (Ma4070DTO) obj;
        if (articleId == null) {
            if (other.articleId != null)
                return false;
        } else if (!articleId.equals(other.articleId))
            return false;
        if (cityName == null) {
            if (other.cityName != null)
                return false;
        } else if (!cityName.equals(other.cityName))
            return false;
        if (maName == null) {
            if (other.maName != null)
                return false;
        } else if (!maName.equals(other.maName))
            return false;
        return true;
    }
}
