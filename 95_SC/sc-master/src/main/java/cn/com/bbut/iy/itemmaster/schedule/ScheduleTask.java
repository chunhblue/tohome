package cn.com.bbut.iy.itemmaster.schedule;

import cn.com.bbut.iy.itemmaster.service.operatorLog.OperatorLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import cn.com.bbut.iy.itemmaster.service.base.IymActorAssignmentService;
import cn.com.bbut.iy.itemmaster.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;

@Component // 用于对那些比较中立的类进行注释
@EnableScheduling // 1.开启定时任务
@EnableAsync // 2.开启多线程
@Slf4j
public class ScheduleTask {

    @Autowired
    private IymActorAssignmentService aaService;
//    @Autowired
//    private OperatorLogService operatorLogService;

    @Async
    @Scheduled(cron = "0 0 1 * * ?")
    public void first() throws InterruptedException {
        log.debug("定时清除超期的代审授权信息开始......");
        aaService.updateReviewRoleStatus(TimeUtil.getDate());
        log.debug("定时清除超期的代审授权信息结束......");
    }

//    @Async
//    @Scheduled(cron = "0 0 0 */3 * ?") // 每三天凌晨执行一次
//    public void deleteOperatorLog() {
//        log.debug("定时清除操作日志开始......");
//        operatorLogService.deleteOperatorLog();
//        log.debug("定时清除操作日志结束......");
//    }
}
