package cn.com.bbut.iy.itemmaster.serviceimpl;

import cn.com.bbut.iy.itemmaster.constant.ConstantsTask;
import cn.com.bbut.iy.itemmaster.dao.TaskMapper;
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
import cn.com.bbut.iy.itemmaster.dto.task.TodoTaskParamDto;
import cn.com.bbut.iy.itemmaster.service.CM9060Service;
import cn.com.bbut.iy.itemmaster.service.TaskService;
import cn.com.bbut.iy.itemmaster.service.ma1000.Ma1000Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Slf4j
public class TaskServiceImpl implements TaskService {
    @Autowired
    private CM9060Service cm9060Service;
    @Autowired
    private TaskMapper taskMapper;

    @Override
    public List<AutoCompleteDTO> getApprovalType() {
        return taskMapper.getApprovalType();
    }

    @Override
    public int getNotificationsCount(Collection<Integer> roleId,Collection<String> storeCds,String userId) {
        //设置通报 ---------------------------------------------
        TodoTaskParamDto param = new TodoTaskParamDto();
        //用户id
        param.setUserId(userId);
        //角色
        param.setRoleList(roleId);
        //获取角色对应的店铺
        param.setStoreList(storeCds);
        //获取业务时间
        param.setBusinessDate(cm9060Service.getValByKey("0000"));
        //设置未回复
        param.setIsReply("0");
        return taskMapper.getInformTaskCount(param);
    }

    @Override
    public int getMMPromotionCount(String userId) {
        String businessDate = cm9060Service.getValByKey("0000");
        int count = taskMapper.getHQInformCount(userId,businessDate,"2");
        return count;
    }

    @Override
    public int getNewItemCount(String userId,Collection<String> storeCds) {
        String businessDate = cm9060Service.getValByKey("0000");
        int count = taskMapper.getNewItemCount(userId,businessDate,storeCds);
        return count;
    }

    @Override
    public List<TodoTaskGridDto> getTodoTask(Collection<Integer> roleId,Collection<String> storeCds,String userId) {
        //设置通报 ---------------------------------------------
        TodoTaskParamDto param = new TodoTaskParamDto();
        //用户id
        param.setUserId(userId);
        //角色
        param.setRoleList(roleId);
        //获取角色对应的店铺
        param.setStoreList(storeCds);
        //获取业务时间
        param.setBusinessDate(cm9060Service.getValByKey("0000"));
        //设置未回复
        param.setIsReply("0");
        //todoTask列表
        List<TodoTaskGridDto> list = new ArrayList<>();
        //设置通知待办
        TodoTaskGridDto informTask = TodoTaskGridDto.builder()
                .type(ConstantsTask.TYPE_INFORM)
                .url(ConstantsTask.URL_INFORM)
                .notificationTitle(ConstantsTask.TITLE_INFORM)
                .notificationCount(taskMapper.getInformTaskCount(param))
                .build();
        //设置审核待办 ---------------------------------------------
        ReviewTaskParamDTO reviewTaskParamDTO = new ReviewTaskParamDTO();
        //设置登录用户角色
        reviewTaskParamDTO.setOperatorId(userId);
//        reviewTaskParamDTO.setOperatorId(userId);
        TodoTaskGridDto pendingTask = TodoTaskGridDto.builder()
                .type(ConstantsTask.TYPE_PENDING_APPROVAL)
                .url(ConstantsTask.URL_PENDING_APPROVAL)
                .notificationTitle(ConstantsTask.TITLE_PENDING_APPROVAL)
                .notificationCount(taskMapper.getPendingApprovalsCount(reviewTaskParamDTO))
                .build();
        //设置用户发起被驳回审核 ---------------------------------------------
        ReviewTaskParamDTO rejectedTaskParamDTO = new ReviewTaskParamDTO();
        //设置登录用户id
        rejectedTaskParamDTO.setOperatorId(userId);
        TodoTaskGridDto rejectedTask = TodoTaskGridDto.builder()
                .type(ConstantsTask.TYPE_MY_REJECTED)
                .url(ConstantsTask.URL_MY_REJECTED)
                .notificationTitle(ConstantsTask.TITLE_MY_REJECTED)
                .notificationCount(taskMapper.getRejectedApprovalsCount(rejectedTaskParamDTO))
                .build();
        //设置用户发起审核 ---------------------------------------------
        ReviewTaskParamDTO submissionTaskParamDTO = new ReviewTaskParamDTO();
//        submissionTaskParamDTO.setRoleIds(roleId);
        //设置登录用户id
        submissionTaskParamDTO.setOperatorId(userId);
        TodoTaskGridDto submissionTask = TodoTaskGridDto.builder()
                .type(ConstantsTask.TYPE_MY_SUBMISSIONS)
                .url(ConstantsTask.URL_MY_SUBMISSIONS)
                .notificationTitle(ConstantsTask.TITLE_MY_SUBMISSIONS)
                .notificationCount(taskMapper.getSubmissionsApprovalsCount(submissionTaskParamDTO))
                .build();
        //设置审核用户的信息 ---------------------------------------------
        ReviewTaskParamDTO myMessageParamDTO = new ReviewTaskParamDTO();
        //设置登录用户id
        myMessageParamDTO.setOperatorId(userId);
        TodoTaskGridDto myMessageTask = TodoTaskGridDto.builder()
                .type(ConstantsTask.TYPE_MY_MESSAGE)
                .url(ConstantsTask.URL_MY_MESSAGE)
                .notificationTitle(ConstantsTask.TITLE_MY_MESSAGE)
                .notificationCount(taskMapper.getMyMessageCount(myMessageParamDTO))
                .build();
        //添加todotask
        list.add(informTask);
        list.add(pendingTask);
        list.add(rejectedTask);
        list.add(submissionTask);
        list.add(myMessageTask);
        return list;
    }

    @Override
    public GridDataDTO<ReviewTaskGridDTO> getPendingApprovals(ReviewTaskParamDTO param) {
        List<ReviewTaskGridDTO> list = taskMapper.getPendingApprovals(param);
        long count = taskMapper.getPendingApprovalsCount(param);
        return new GridDataDTO<>(list, param.getPage(), count, param.getRows());
    }

    @Override
    public GridDataDTO<ReviewTaskGridDTO> getRejectedApprovals(ReviewTaskParamDTO param) {
        List<ReviewTaskGridDTO> list = taskMapper.getRejectedApprovals(param);
        long count = taskMapper.getRejectedApprovalsCount(param);
        return new GridDataDTO<>(list, param.getPage(), count, param.getRows());
    }

    @Override
    public GridDataDTO<ReviewTaskGridDTO> getSubmissionsApprovals(ReviewTaskParamDTO param) {
        List<ReviewTaskGridDTO> list = taskMapper.getSubmissionsApprovals(param);
        long count = taskMapper.getSubmissionsApprovalsCount(param);
        return new GridDataDTO<>(list, param.getPage(), count, param.getRows());
    }

    @Override
    public GridDataDTO<ReviewTaskGridDTO> getMyMessage(ReviewTaskParamDTO param) {
        List<ReviewTaskGridDTO> list = taskMapper.getMyMessage(param);
        long count = taskMapper.getMyMessageCount(param);
        return new GridDataDTO<>(list, param.getPage(), count, param.getRows());
    }

    @Override
    public Sk0010DTO getStockScrap(String voucherNo) {
        return taskMapper.getStockScrap(voucherNo);
    }

    @Override
    public WarehouseReceiptGridDTO getReceive(String recordCd) {
        return taskMapper.getReceive(recordCd);
    }

    @Override
    public StocktakeProcessDTO getStocktake(String piCd) {
        return taskMapper.getStocktake(piCd);
    }

    @Override
    public CashDetail getCashier(String payId) {
        return taskMapper.selectCashier(payId);
    }

    @Override
    public String getStoreCdByOrderId(String orderId) {
        return taskMapper.getStoreCdByOrderId(orderId);
    }

    @Override
    public Sk0010DTO getSk0010Info(String voucherNo) {
        return taskMapper.getSk0010Info(voucherNo);
    }

    @Override
    public ExpenditureParamDTO getExpenditureInfo(String voucherNo) {
        return taskMapper.getExpenditureInfo(voucherNo);
    }

}
