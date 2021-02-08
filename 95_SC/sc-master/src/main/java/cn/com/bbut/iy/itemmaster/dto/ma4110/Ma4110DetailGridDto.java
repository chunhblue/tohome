package cn.com.bbut.iy.itemmaster.dto.ma4110;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 *  促销详细信息 grid
 *
 * @author zcz
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ma4110DetailGridDto extends GridDataDTO {
    private String location;//地区

    private String department;//中分类

    private String iteamCode;//商品id

    private String barcode;//条码

    private String descriptionEN;//商品名称

    private String descriptionVN;//商品名称(越南)

    private String materialType;//材料类型名称

    private String dcItem;//【控制】DC商品

    private String startDate;//客户计划开始时间

    private String endDate;//客户计划结束时间

    private String details;//客户计划详细

    private Double normal;//售卖金额（正常）+增值税

    private Double promotion;//售卖金额（促销）+增值税

    private Double vatOutput;//增值税

    private String vendorId;//供应商Id

    private String vendorName;//供应商名称

}
