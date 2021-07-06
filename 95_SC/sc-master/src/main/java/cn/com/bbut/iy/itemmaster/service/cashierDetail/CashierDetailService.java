package cn.com.bbut.iy.itemmaster.service.cashierDetail;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.cashierDetail.*;
import cn.com.bbut.iy.itemmaster.entity.sa0050.SA0050;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

public interface CashierDetailService {
    /**
     * 查询支付类型
     * @return
     */
    List<PayMethod> getAllPay();

    /**
     * 查询销售头档
     * @param dto
     * @return
     */
    GridDataDTO<SaleHead> getSaleHeadList(CashierDetailParam dto);

    /**
     * 查询销售详细
     * @param param
     * @return
     */
    GridDataDTO<SaleDetail> getSaleDetailList(SaleHeadParam param);

    // 统计销售商品总数量，总金额
    SaleDetail getSaleDetailTotal(SaleHeadParam param);

    /**
     * 获取支付类型详细信息
     * @param param
     * @return
     */
    List<PayMethod> getPayDetail(SaleHeadParam param);

    /**
     * 根据选中得 店铺和pos id 获得收银员信息
     */
    List<SA0050> getCashier(String storeCd, String posId);

    Map getTotalAmount(String  userId, CashierDetailParam param);
}
