package cn.com.bbut.iy.itemmaster.controller.aspect;

import cn.com.bbut.iy.itemmaster.controller.BaseAction;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.service.operatorLog.OperatorLogService;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Aspect
@Component
public class OperatorLogAspect extends BaseAction {

    @Pointcut("execution(public * cn.com.bbut.iy.itemmaster.controller..*.*(..))")//切入点描述 这个是controller包的切入点
    public void controllerLog(){}//签名，可以理解成这个切入点的一个名称

    @Pointcut("execution(public * cn.com.bbut.iy.itemmaster.controller.view.ViewRoleController.toManage(..))")//切入点描述 跟其他跳转页面不一样, 单独用一个
    public void otherPointcut(){}

    @Pointcut("execution(public * cn.com.bbut.iy.itemmaster.controller.ActorAssignmentController.view(..))")//切入点描述 跟其他跳转页面不一样, 单独用一个
    public void otherPointcut2(){}

    @Autowired
    private OperatorLogService operatorLogService;

    @After("otherPointcut() || otherPointcut2()")
    public void aspect2() {
        insertOperatorLog();
    }

    @AfterReturning(returning = "ret", pointcut = "controllerLog()")
    public void aspect1(ModelAndView ret) {
        insertOperatorLog();
    }

    private void insertOperatorLog() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        User user = this.getUser(session);
        if (user==null) {
            return;
        }
        operatorLogService.insertOperatorLog(user,request,session);
    }
}
