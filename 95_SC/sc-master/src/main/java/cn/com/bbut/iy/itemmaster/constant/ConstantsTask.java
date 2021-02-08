package cn.com.bbut.iy.itemmaster.constant;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 待办任务
 * @author zcz
 */
public class ConstantsTask implements Serializable {

    private static final long serialVersionUID = 1L;
    /** 待办任务 类型、url、标题 对应 */

    // --------------------- 通知 待办
    public static final String TYPE_INFORM = "1";

	public static final String URL_INFORM = "/informReply";

    public static final String TITLE_INFORM = "Notification";

    // --------------------- 审核 待办
    public static final String TYPE_PENDING_APPROVAL = "1";

    public static final String URL_PENDING_APPROVAL = "/auditMessage/pendingApprovals";

    public static final String TITLE_PENDING_APPROVAL = "Pending Approvals";

    // --------------------- 审核 我发起的审核 驳回
    public static final String TYPE_MY_REJECTED = "6";

    public static final String URL_MY_REJECTED = "/auditMessage/rejectedApprovals";

    public static final String TITLE_MY_REJECTED = "Rejected Approvals";

    // --------------------- 审核 我发起的审核
    public static final String TYPE_MY_SUBMISSIONS = "1,5,6,10";

    public static final String URL_MY_SUBMISSIONS = "/auditMessage/mySubmissions";

    public static final String TITLE_MY_SUBMISSIONS = "My Submissions";

    // --------------------- 审核 我的信息
    public static final String TYPE_MY_MESSAGE = "PENDING,,SUBMISSIONS";

    public static final String URL_MY_MESSAGE = "/auditMessage/myMessage";

    public static final String TITLE_MY_MESSAGE = "Messages";

}
