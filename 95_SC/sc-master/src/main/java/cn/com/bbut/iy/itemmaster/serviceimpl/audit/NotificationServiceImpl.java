package cn.com.bbut.iy.itemmaster.serviceimpl.audit;

import cn.com.bbut.iy.itemmaster.dao.audit.NotificationBeanMapper;
import cn.com.bbut.iy.itemmaster.dto.audit.NotificationBean;
import cn.com.bbut.iy.itemmaster.dto.audit.NotificationDTO;
import cn.com.bbut.iy.itemmaster.dto.audit.ShortcutDTO;
import cn.com.bbut.iy.itemmaster.service.audit.INotificationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
public class NotificationServiceImpl implements INotificationService {
    @Autowired
    private NotificationBeanMapper notificationBeanMapper;

    /**
     * 获取待办事务
     */
    @Override
    public List<NotificationDTO> getNotificationList(List<Long> roleid) {
        return notificationBeanMapper.selectByRoleId(roleid);
    }

    @Value("${spring.mail.username}")
    private String from;

    @Autowired
    private JavaMailSender javaMailSender;

    /**
     * 添加待办事务  发送邮件
     */
    @Override
    public int addNotification(NotificationBean notificationBean) {
        long roleId = notificationBean.getNRoleid();
        String storeCd = notificationBean.getStoreCd();
        List<String> emails = new ArrayList<>();
        if(roleId < 4){ // AM OM OP
            emails = notificationBeanMapper.selectMailByMd(roleId,storeCd);
        }else if(roleId == 4){ //SM
            emails = notificationBeanMapper.selectMailBySm(storeCd);
        }else{ // <4  的角色
            if(roleId == 5){ // Operations Head
                emails = notificationBeanMapper.selectMailByPostion(roleId,null);
            }else{
                emails = notificationBeanMapper.selectMailByPostion(roleId,storeCd);
            }
        }
        String[] sendEmails = emails.stream().filter(email -> StringUtils.isNotBlank(email)).toArray(String[]::new);
        if(sendEmails!=null&&sendEmails.length>0){
            String typeName = notificationBeanMapper.getTypeName(notificationBean.getNTypeid());
            StringBuilder htmlContent = new StringBuilder()
                    .append("<html>")
                    .append("<head>")
                    .append("</head>")
                    .append("<body>")
                    .append("<p>Approval ID: "+notificationBean.getCRecordCd()+"<p/>")
//                .append("<p>Reason: "+auditContent+"<p/>")
                    .append("<p>Approval Type: "+typeName+"<p/>")
                    .append("</body>")
                    .append("</html>");
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = null;
            try {
                helper = new MimeMessageHelper(message, true);
                helper.setFrom(from);
                helper.setTo(sendEmails);
                helper.setSubject("The approval for ID: "+notificationBean.getCRecordCd()+" !");
                helper.setText(htmlContent.toString(), true);
                javaMailSender.send(message);
            } catch (MessagingException e) {
                log.error("邮件发送失败 Record Cd: {}",notificationBean.getCRecordCd());
                e.printStackTrace();
            }
        }
        return notificationBeanMapper.insertNotification(notificationBean);
    }

    /**
     * 更新待办事务
     */
    @Override
    public int updateNotification(NotificationBean notificationBean) {
        return notificationBeanMapper.updateNotification(notificationBean);
    }

    /**
     * 查询主键
     */
    @Override
    public long getMaxId() {
        return notificationBeanMapper.selectMaxId();
    }

    /**
     * 根据主键查询记录
     */
    @Override
    public NotificationBean getRecordById(long recordiId) {
        return notificationBeanMapper.selectRecordById(recordiId);
    }

    /**
     * 查询URL
     */
    @Override
    public String getUrlByTypeId(int typeId) {
        return notificationBeanMapper.selectUrlByTypeId(typeId);
    }

    /**
     * 根据类型ID、记录ID和适用开始日查询
     */
    @Override
    public List<NotificationBean> getListByKey(int typeId, String recordCd, String startDate) {
        return notificationBeanMapper.selectListByKey(typeId, recordCd, startDate);
    }

    /**
     * 根据类型ID、记录ID、适用开始日、步骤ID查询
     */
    @Override
    public NotificationBean getByKeyAndSubNo(int typeId, int subNo, String recordCd, String startDate) {
        return notificationBeanMapper.selectByKeyAndSubNo(typeId, subNo, recordCd, startDate);
    }

    /**
     * 无效记录
     */
    @Override
    public void updateVoidNotification(NotificationBean notificationBean) {
        notificationBeanMapper.invalidNotification(notificationBean);
    }

    @Override
    public List<NotificationDTO> getNotificationList(long id) {
        return notificationBeanMapper.selectByOperatorId(id);
    }

    /**
     * (non-Javadoc)
     * getShortcutList
     */
    @Override
    public List<ShortcutDTO> getShortcutList(long id, List<String> params) {
        List<ShortcutDTO> shortcutList = null;
        if(params.get(0).equals("1") && params.size() == 1) {
            shortcutList = notificationBeanMapper.getShortcutList(id, params);
        }
        else if(params.get(0).equals("5") && params.size() == 1) {
            shortcutList = notificationBeanMapper.getShortcutListByUser(id, params);
        }
        else if(params.get(0).equals("1") && params.size() == 3) {
            shortcutList = notificationBeanMapper.getShortcutListByUser(id, params);
        }
        else{
            shortcutList = notificationBeanMapper.getShortcutList(id, params);

            List<ShortcutDTO> shortcutList2 = notificationBeanMapper.getShortcutListByUser(id, params);

            shortcutList.addAll(shortcutList2);
        }

        return shortcutList;
    }
}
