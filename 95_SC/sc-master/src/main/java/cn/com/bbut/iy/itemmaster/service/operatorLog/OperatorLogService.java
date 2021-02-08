package cn.com.bbut.iy.itemmaster.service.operatorLog;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.base.ReturnDTO;
import cn.com.bbut.iy.itemmaster.dto.operatorLog.OperatorLogDTO;
import cn.com.bbut.iy.itemmaster.dto.operatorLog.OperatorLogParamDTO;
import cn.com.bbut.iy.itemmaster.entity.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public interface OperatorLogService {

    /**
     * 保存用户操作记录
     */
    void insertOperatorLog(User user, HttpServletRequest request, HttpSession session);

    /**
     * 查询操作记录
     * @param param
     * @return
     */
    GridDataDTO<OperatorLogDTO> search(OperatorLogParamDTO param);

    /**
     * 清空日志
     */
    void deleteOperatorLog();
}
