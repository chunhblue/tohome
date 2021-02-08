package cn.com.bbut.iy.itemmaster.dto.store;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class MA1060GridDTO extends GridDataDTO {
    //店铺竞争者Id
    private Integer competitorTabCd;
    //店铺CD
    private String storeCd;
    //店铺适用开始日
    private String effectiveStartDate;
    //店铺适用结束日
    private String effectiveEndDate;

    private String oldEffectiveStartDate;

    //竞争者Id
    private String competitorId;
    //竞争者名称
    private String competitorName;
    //竞争者地址
    private String address;
    //距离ck距离
    private Integer distanceCk;
    //大约开放日期
    private String approxOpen;
    //具体开放时间
    private String openDate;
    //备注
    private String remarks;

    private String createUserId;

    private String createYmd;

    private String createHms;

    private String updateUserId;

    private String updateYmd;

    private String updateHms;

    private String updateScreenId;

    private String updateIpAddress;

    private String nrUpdateFlg;

    private String nrYmd;

    private String nrHms;

}
