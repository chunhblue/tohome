package cn.com.bbut.iy.itemmaster.directive;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;

import cn.com.bbut.iy.itemmaster.annotation.FreemarkerComponent;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.ConstantsDB;
import cn.shiy.common.pmgr.service.PermissionService;
import freemarker.core.Environment;
import freemarker.template.ObjectWrapper;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * 画面中 permission 权限注解标签实现
 * 
 * @author songxz
 * @date: 2019年10月10日 - 下午4:05:40
 */
@FreemarkerComponent("permission")
public class PermissionDirective implements TemplateDirectiveModel {

    @Setter
    @Autowired
    private PermissionService permService;

    @Autowired
    private HttpServletRequest request;

    /**
     * @see freemarker.template.TemplateDirectiveModel#execute(freemarker.core.Environment,
     *      java.util.Map, freemarker.template.TemplateModel[],
     *      freemarker.template.TemplateDirectiveBody)
     */
    @SuppressWarnings("rawtypes")
    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars,
            TemplateDirectiveBody body) throws TemplateException, IOException {
        String code = params.get("code").toString();
        String[] codes = code.split(",");
        HttpSession session = request.getSession();// 从session中拿到当前登录人的角色id集合
        @SuppressWarnings("unchecked")
        Collection<Integer> roleId = (Collection<Integer>) session
                .getAttribute(Constants.SESSION_ROLES);
        boolean b = false;
        for (int i = 0; i < codes.length; i++) {
            b = permService.isRolesHasPermission(roleId, codes[i]);
            if (b) {
                break;
            }
        }
        // true=1；false=0
        env.setVariable("displayVar", ObjectWrapper.DEFAULT_WRAPPER.wrap(b ? ConstantsDB.COMMON_ONE
                : ConstantsDB.COMMON_TWO));

        if (body != null) {
            body.render(env.getOut());
        }
    }
}