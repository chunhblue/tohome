package cn.com.bbut.iy.itemmaster.dao.audit;

import cn.com.bbut.iy.itemmaster.dto.audit.NotificationBean;
import cn.com.bbut.iy.itemmaster.dto.audit.NotificationDTO;
import cn.com.bbut.iy.itemmaster.dto.audit.ShortcutDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface NotificationBeanMapper {
    /**
     * 查询角色待办事务
     * @param roleid
     * @return
     */
    List<NotificationDTO> selectByRoleId(@Param("roleId") List<Long> roleId);

    /**
     * 查询主键
     * @param
     * @return
     */
    long selectMaxId();

    /**
     * 查询URL
     * @param
     * @return
     */
    String selectUrlByTypeId(@Param("typeId") int typeId);

    /**
     * 根据主键查询记录
     * @param recordiId
     * @return
     */
    NotificationBean selectRecordById(@Param("recordiId") long recordiId);

    /**
     * 根据类型ID、记录ID和适用开始日查询
     * @param
     * @return
     */
    List<NotificationBean> selectListByKey(@Param("typeId") int typeId,
                                           @Param("recordCd") String recordCd, @Param("startDate") String startDate);

    /**
     * 根据类型ID、记录ID、适用开始日、步骤ID查询
     * @param
     * @return
     */
    NotificationBean selectByKeyAndSubNo(@Param("typeId") int typeId, @Param("subNo") int subNo,
                                         @Param("recordCd") String recordCd, @Param("startDate") String startDate);

    /**
     * 添加待办事务
     * @param notificationBean
     * @return
     */
    int insertNotification(@Param("insertBean") NotificationBean notificationBean);

    /**
     * 更新待办事务
     * @param notificationBean
     * @return
     */
    int updateNotification(@Param("updateBean") NotificationBean notificationBean);

    /**
     * 无效记录
     * @param notificationBean
     * @return
     */
    void invalidNotification(@Param("invalidBean") NotificationBean notificationBean);

    //	add by dxq 20191209 start
    /**
     * @Description:
     * @param @param id
     * @param @return 参数类型
     * @return List<NotificationDTO> 返回类型
     * @param id
     * @return
     */
    List<NotificationDTO> selectByOperatorId(@Param("operatorId") long id);
    //	add by dxq 20191209 end

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
    List<ShortcutDTO> getShortcutList(@Param("id")long id, @Param("status")List<String> params);

    /**
     * @Description:
     * @param @param id
     * @param @param params
     * @param @param _page
     * @param @return 参数类型
     * @return List<ShortcutDTO> 返回类型
     * @param id
     * @param params
     * @return
     */
    List<ShortcutDTO> getShortcutListByUser(@Param("id")long id, @Param("status")List<String> params);


    /**
     * 获取审核类型名称
     * @param typeId
     * @return
     */
    String getTypeName(int typeId);
    /**
     * 查询用户对应邮件
     * @param position
     * @param storeCd
     * @return
     */
    List<String> selectMailByMd(long position, String storeCd);

    List<String> selectMailBySm(String storeCd);

    List<String> selectMailByPostion(long position, String storeCd);
}
