package cn.com.bbut.iy.itemmaster.dto.hhtReport;


import cn.com.bbut.iy.itemmaster.dto.base.CommonDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @ClassName hhtReportDto
 * @Description TODO
 * @Author Ldd
 * @Date 2021/3/17 17:46
 * @Version 1.0
 * @Description
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class hhtReportDto  {
    // 数据来源类型区分
    private String type;

    // 收货编号
    private String receiveId;

    // 店铺编号
    private String storeCd;

    // 店铺编号
    private String storeName;


    private String orderDate;


    //收货审核状态
    private String reveiveStatus;


    private String businessDate;
    private  Integer totalOpenPo;
    private  Integer grpoOfQty;
    private  Integer noOfConfiPo;
    private  Integer noOfPendingGrpo;

   private String  reviewStu;
}
