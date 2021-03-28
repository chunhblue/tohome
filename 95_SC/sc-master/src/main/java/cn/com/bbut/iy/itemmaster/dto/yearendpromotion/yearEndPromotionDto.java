package cn.com.bbut.iy.itemmaster.dto.yearendpromotion;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @ClassName yearEndPromotionDto
 * @Description TODO
 * @Author Ldd
 * @Date 2021/3/2 16:15
 * @Version 1.0
 * @Description
 */

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class yearEndPromotionDto extends GridDataDTO {
private String Date;
private String DateTime;
private String articleId;
private String pinCode;
private String articleName;
private BigDecimal qty;
private BigDecimal comfirmationNumber;


}
