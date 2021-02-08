package cn.com.bbut.iy.itemmaster.dao;


import cn.com.bbut.iy.itemmaster.dto.audit.ReviewTaskGridDTO;
import cn.com.bbut.iy.itemmaster.dto.audit.ReviewTaskParamDTO;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.cash.CashDetail;
import cn.com.bbut.iy.itemmaster.dto.expenditure.ExpenditureParamDTO;
import cn.com.bbut.iy.itemmaster.dto.inventory.Sk0010DTO;
import cn.com.bbut.iy.itemmaster.dto.receipt.warehouse.WarehouseReceiptGridDTO;
import cn.com.bbut.iy.itemmaster.dto.returnOrder.returnVendor.RVHeadResult;
import cn.com.bbut.iy.itemmaster.dto.stocktakeProcess.StocktakeProcessDTO;
import cn.com.bbut.iy.itemmaster.dto.task.TodoTaskParamDto;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

public interface TaskMapper {
    /**
     * 获取流程类型信息
     * @return
     */
    List<AutoCompleteDTO> getApprovalType();

    /**
     * 获取通知数量
     * @return
     */
    int getInformTaskCount(TodoTaskParamDto param);

    /**
     * 获取总部通知数量
     * @param userId 用户id
     * @param informType 通知类型
     * @return
     */
    int getHQInformCount(@Param("userId") String userId,@Param("businessDate") String businessDate,@Param("informType") String informType);

    /**
     * 获取总部通知数量
     * @param userId 用户id
     * @param storeCds 权限店铺
     * @return
     */
    int getNewItemCount(@Param("userId") String userId,@Param("businessDate") String businessDate,@Param("storeCds") Collection<String> storeCds);

    /**
     * 添加通知查看记录
     * @param userId 用户id
     * @param informType 通知类型
     * @return
     */
    int insertInformRecors(@Param("userId") String userId,@Param("informType") String informType,@Param("infoIds") List<String> infoIds);

    /**
     * 获取待审
     * @param paramDTO
     * @return
     */
    List<ReviewTaskGridDTO> getPendingApprovals(ReviewTaskParamDTO paramDTO);

    /**
     * 获取待审 数量
     * @param paramDTO
     * @return
     */
    long getPendingApprovalsCount(ReviewTaskParamDTO paramDTO);

    /**
     * 获取用户驳回的审核
     * @param paramDTO
     * @return
     */
    List<ReviewTaskGridDTO> getRejectedApprovals(ReviewTaskParamDTO paramDTO);

    /**
     * 获取用户驳回的审核 数量
     * @param paramDTO
     * @return
     */
    long getRejectedApprovalsCount(ReviewTaskParamDTO paramDTO);

    /**
     * 获取用户提交审核
     * @param paramDTO
     * @return
     */
    List<ReviewTaskGridDTO> getSubmissionsApprovals(ReviewTaskParamDTO paramDTO);

    /**
     * 获取用户提交审核 数量
     * @param paramDTO
     * @return
     */
    long getSubmissionsApprovalsCount(ReviewTaskParamDTO paramDTO);

    /**
     * 获取我的信息  （待审+用户提交审核）
     * @param paramDTO
     * @return
     */
    List<ReviewTaskGridDTO> getMyMessage(ReviewTaskParamDTO paramDTO);

    /**
     * 获取我的信息 数量 待审+用户提交审核）
     * @param paramDTO
     * @return
     */
    long getMyMessageCount(ReviewTaskParamDTO paramDTO);

    /**
     * 获取审核跳转传票需要的信息
     * @param voucherNo 传票号
     * @return
     */
    Sk0010DTO getStockScrap(@Param("voucherNo") String voucherNo);

    /**
     * 获取审核跳转盘点需要的信息
     * @param piCd  盘点号
     * @return
     */
    StocktakeProcessDTO getStocktake(@Param("piCd") String piCd);

    /**
     * 获取审核跳转退货需要的信息
     * @param orderId  订单号
     * @return
     */
    RVHeadResult getRefund(@Param("orderId") String orderId);

    /**
     * 获取审核跳转收货需要的信息
     * @param receiveId  订单号
     * @return
     */
    WarehouseReceiptGridDTO getReceive(@Param("receiveId") String receiveId);

    /**
     * 获取审核跳转收银登录的信息
     * @param payId
     * @return
     */
    CashDetail selectCashier(@Param("payId") String payId);

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