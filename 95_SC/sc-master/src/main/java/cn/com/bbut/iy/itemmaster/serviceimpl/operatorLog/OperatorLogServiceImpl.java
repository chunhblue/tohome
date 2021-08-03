package cn.com.bbut.iy.itemmaster.serviceimpl.operatorLog;

import cn.com.bbut.iy.itemmaster.dao.MA4320Mapper;
import cn.com.bbut.iy.itemmaster.dao.OperatorLogMapper;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.operatorLog.OperatorLogDTO;
import cn.com.bbut.iy.itemmaster.dto.operatorLog.OperatorLogParamDTO;
import cn.com.bbut.iy.itemmaster.dto.pi0100.PI0100DTO;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.entity.base.Menu;
import cn.com.bbut.iy.itemmaster.service.Ma4320Service;
import cn.com.bbut.iy.itemmaster.service.operatorLog.OperatorLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class OperatorLogServiceImpl implements OperatorLogService {

    @Autowired
    private OperatorLogMapper operatorLogMapper;
    @Autowired
    private MA4320Mapper ma4320Mapper;

    /**
     * 保存用户的操作记录
     */
    @Override
    public void insertOperatorLog(User user, HttpServletRequest request, HttpSession session) {
        // 获取请求路径中的url  /a/index -> index
        String requestUrl = request.getServletPath();
        String menuUrl = requestUrl.substring(requestUrl.indexOf("/", 1)+1);

        // 获取菜单信息
        Menu menu = operatorLogMapper.getMenuByUrl(menuUrl);

        if (menu==null) {
            return;
        }
        // 封装数据
//        String ymdhms = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());
//        String ymd = ymdhms.split("-")[0];
//        String hms = ymdhms.split("-")[1];
        String nowDate = ma4320Mapper.getNowDate();
        String ymd = nowDate.substring(0,8);
        String hms = nowDate.substring(8,14);
        OperatorLogDTO dto = new OperatorLogDTO();
        dto.setUserId(user.getUserId());
        dto.setUserName(user.getUserName());
        dto.setMenuId(menu.getId());
        dto.setMenuName(menu.getName());
        dto.setOperateYmd(ymd);
        dto.setOperateHms(hms);
        operatorLogMapper.save(dto);
    }

    /**
     * 查询操作记录
     * @param param
     * @return
     */
    @Override
    public GridDataDTO<OperatorLogDTO> search(OperatorLogParamDTO param) {
        // 查询总条数
        int count = operatorLogMapper.searchCount(param);

        if (count<1) {
            return new GridDataDTO<OperatorLogDTO>();
        }

        List<OperatorLogDTO> list = operatorLogMapper.search(param);
        GridDataDTO<OperatorLogDTO> data = new GridDataDTO<OperatorLogDTO>(list, param.getPage(), count, param.getRows());
        return data;
    }

    /**
     * 清空日志
     */
    @Override
    public void deleteOperatorLog() {
        // 全部删除
        operatorLogMapper.deleteOperatorLog();
    }
}
