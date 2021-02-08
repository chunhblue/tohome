/**
 * ClassName  ExportActionBean
 * <p>
 * History
 * Create User: Shiy
 * Create Date: 2013-9-29
 * Update User:
 * Update Date:
 */
package cn.com.bbut.iy.itemmaster.excel;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.com.bbut.iy.itemmaster.config.ContextPathConfig;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.controller.BaseAction;
import cn.shiy.common.baseutil.Container;

/**
 *
 * @author guok
 */
@Slf4j
@Controller
@RequestMapping(value = Constants.REQ_HEADER)
public class ExportController extends BaseAction {

    @Autowired
    private ContextPathConfig contextPathConfig;

    @RequestMapping(value = "/expcheck", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ExportStatus expcheck(String key) {
        ExController controller = Container.getBean(ExController.class);
        ExStatus innerStatus = controller.getStatus(key);
        if (innerStatus != null) {
            ExportStatus status = new ExportStatus();
            status.setStatus(innerStatus.getStatus());
            status.setFilename(innerStatus.getFilename());
            return status;
        } else {
            return null;
        }
    }

    /**
     * 下载生成的excel
     *
     * @return
     */
    @RequestMapping(value = "/export/{key}")
    public String download(@PathVariable(value = "key") String key, HttpSession session,
            HttpServletResponse response) {
        ExController controller = Container.getBean(ExController.class);
        ExStatus innerStatus = controller.getStatus(key);
        if (innerStatus == null || innerStatus.getStatus() != ExStatus.STATUS_FINISHED) {
            log.error("{} is not finished to download by {}", key, this.getUserId(session));
        } else {
            response.setHeader("Content-Type",
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");// .xlsx

            // 这里要对应nginx配置中的localtion
            response.setHeader("X-Accel-Redirect", contextPathConfig.getContextPath()
                    + Constants.EXPORT_DOWNLOAD_URL + innerStatus.getFilename());
            String escaped = null;
            try {
                escaped = URLEncoder.encode(innerStatus.getExFileName(), "UTF-8");
                StringBuilder header = new StringBuilder().append("inline;filename=\"")
                        .append(escaped).append("\"");
                response.setHeader("Content-Disposition", header.toString());
            } catch (UnsupportedEncodingException e) {
                log.error("下载,文件名称错误 user:{}", this.getUserId(session));
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        }
        return null;
    }
}
