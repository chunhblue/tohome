package cn.com.bbut.iy.itemmaster.dto.bm;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import cn.com.bbut.iy.itemmaster.entity.IyBmCk;
import cn.com.bbut.iy.itemmaster.entity.IyBmItemCk;

/**
 * order 明细数据展示 基础内容部分
 * 
 * @author songxz
 * @date:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BmViewMainDTO {

    // 基本信息 根据优惠店铺切分了集合
    private IyBmCk baseBm;
    // 促销店铺集合 code+name
    private List<String> stroes;
    // 促销店铺集合 code1,code2,code3
    private String stroeStr;

    // bm数量
    private Integer bmCount;
    // bm折扣
    private BigDecimal bmDiscount;
    // bm商品价格
    private BigDecimal bmPrice;
    // Item Barcode集合
    private List<String> items;
    // Item Barcode字符串 逗号拼接
    private String itemCodeStr;
    // 购买数量
    private Integer buyCount;
    // 05 A组合任意购买数量
    private Integer numA;
    // 05 B组合任意购买数量
    private Integer numB;

    // 可以评审标记，0代表可以，1不可以。
    private String canReview;
    // 评审标记为0时，此字段保存可以评审的资源标记，事业部级别，例如199,299等，使用逗号分隔
    private String checkResources;
    // 商品+dpt,商品+dpt，该字段用于前端在评审后，传入后台用于处理操作人所属的资源审核状态，只有在采购确认中时才有的内容
    private String allItemAndDpt;

    // 单品集合信息
    private List<IyBmItemCk> bmItems;
    // <input type="text" id="in_${bmItem.store!}_${bmItem.item!}"
    // store="${bmItem.store!}" bmcode="${bmItem.bmCode!}" itemsystem="000000"
    // itemcode="${bmItem.item!}" itemname="农夫山泉４Ｌ" dpt="512" costtax="7.5"
    // pricetax="8.5" status="${bmItem.bmItemFlg!}"
    // bmprice="622.6" disprice="28.3" profitrate="0.70" buycount="&amp;nbsp;"
    // discount="&amp;nbsp;" ab="&nbsp;" value="6921168509270">
    // 单品集合信息
    private List<BmViewDetailDTO> bmItemInput;
}
