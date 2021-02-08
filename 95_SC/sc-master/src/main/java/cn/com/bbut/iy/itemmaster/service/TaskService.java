package cn.com.bbut.iy.itemmaster.service;

import cn.com.bbut.iy.itemmaster.dto.audit.ReviewTaskGridDTO;
import cn.com.bbut.iy.itemmaster.dto.audit.ReviewTaskParamDTO;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.cash.CashDetail;
import cn.com.bbut.iy.itemmaster.dto.expenditure.ExpenditureParamDTO;
import cn.com.bbut.iy.itemmaster.dto.inventory.Sk0010DTO;
import cn.com.bbut.iy.itemmaster.dto.receipt.warehouse.WarehouseReceiptGridDTO;
import cn.com.bbut.iy.itemmaster.dto.returnOrder.returnVendor.RVHeadResult;
import cn.com.bbut.iy.itemmaster.dto.stocktakeProcess.StocktakeProcessDTO;
import cn.com.bbut.iy.itemmaster.dto.task.TodoTaskGridDto;

import java.util.Collection;
import java.util.List;

public interface TaskService {
    /**
     * 获取流程类型
     * @return
     */
    List<AutoCompleteDTO> getApprovalType();

    /**
     * 通知回复提示
     * @return 需要回复数量
     */
    int getNotificationsCount(Collection<Integer> roleId,Collection<String> storeCds,String userId);

    /**
     * 通知 mm促销
     * @return 通知数量
     */
    int getMMPromotionCount(String userId);

    /**
     * 通知 新商品
     * @return 通知数量
     */
    int getNewItemCount(String userId,Collection<String> storeCds);

    /**
     * 获取待办任务
     * @param roleId 角色集合
     * @return
     */
    List<TodoTaskGridDto> getTodoTask(Collection<Integer> roleId,Collection<String> storeCds,String userId);

    /**
     * 获取待审
     * @param param
     * @return
     */
    GridDataDTO<ReviewTaskGridDTO> getPendingApprovals(ReviewTaskParamDTO param);

    /**
     * 获取用户提交审核被驳回
     * @param param
     * @return
     */
    GridDataDTO<ReviewTaskGridDTO> getRejectedApprovals(ReviewTaskParamDTO param);

    /**
     * 获取用户提交审核
     * @param param
     * @return
     */
    GridDataDTO<ReviewTaskGridDTO> getSubmissionsApprovals(ReviewTaskParamDTO param);

    /**
     * 审核 获取我的信息
     * @param param
     * @return
     */
    GridDataDTO<ReviewTaskGridDTO> getMyMessage(ReviewTaskParamDTO param);


    /**
     * 获取审核跳转传票需要的信息
     * @param voucherNo 传票号
     * @return
     */
    Sk0010DTO getStockScrap(String voucherNo);


    /**
     * 获取审核跳转收货单需要的信息
     * @param recordCd 订单号
     * @return
     */
    WarehouseReceiptGridDTO getReceive(String recordCd);

    /**
     * 获取审核跳转盘点需要的信息
     * @param piCd  盘点号
     * @return
     */
    StocktakeProcessDTO getStocktake(String piCd);

    /**
     * 获取审核跳转收银登录的信息
     * @param payId
     * @return
     */
    CashDetail getCashier(String payId);

    /**
     * 获取审核调整 订单店铺号
     * @param orderId
     * @return
     */
    String getStoreCdByOrderId(String orderId);

    /**
     * 获取转移调整信息
     * @param voucherNo
     * @return
     */
    Sk0010DTO getSk0010Info(String voucherNo);

    /**
     * 获取支出信息
     * @param voucherNo
     * @return
     */
    ExpenditureParamDTO getExpenditureInfo(String voucherNo);
}
