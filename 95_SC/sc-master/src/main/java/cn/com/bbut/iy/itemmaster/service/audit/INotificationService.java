package cn.com.bbut.iy.itemmaster.service.audit;

import cn.com.bbut.iy.itemmaster.dto.audit.NotificationBean;
import cn.com.bbut.iy.itemmaster.dto.audit.NotificationDTO;
import cn.com.bbut.iy.itemmaster.dto.audit.ShortcutDTO;

import java.util.List;

public interface INotificationService {
    /**
     * 获取待办事务
     * @return
     */
    List<NotificationDTO> getNotificationList(List<Long> roleid);

    /**
     * 查询主键
     * @param
     * @return
     */
    long getMaxId();

    /**
     * 查询URL
     * @param
     * @return
     */
    String getUrlByTypeId(int typeId);

    /**
     * 根据主键查询记录
     * @param recordiId
     * @return
     */
    NotificationBean getRecordById(long recordiId);

    /**
     * 根据类型ID、记录ID和适用开始日查询
     * @param
     * @return
     */
    List<NotificationBean> getListByKey(int typeId, String recordCd, String startDate);

    /**
     * 根据类型ID、记录ID、适用开始日、步骤ID查询
     * @param
     * @return
     */
    NotificationBean getByKeyAndSubNo(int typeId, int subNo, String recordCd, String startDate);

    /**
     * 添加待办事务
     * @param notificationBean
     * @return
     */
    int addNotification(NotificationBean notificationBean);

    /**
     * 更新待办事务
     * @param notificationBean
     * @return
     */
    int updateNotification(NotificationBean notificationBean);

    /**
     * 无效记录
     * @param notificationBean
     * @return
     */
    void updateVoidNotification(NotificationBean notificationBean);

    /**
     * @Description:
     * @param @param id
     * @param @return 参数类型
     * @return List<NotificationDTO> 返回类型
     * @param id
     * @return
     */
    List<NotificationDTO> getNotificationList(long id);

    /**
     * @Description:
     * @param @param id
     * @param @param params
     * @param @return 参数类型
     * @return List<ShortcutDTO> 返回类型
     * @param id
     * @param params
     * @param _page
     * @return
     */
    List<ShortcutDTO> getShortcutList(long id, List<String> params);
}
