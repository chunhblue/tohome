package cn.com.bbut.iy.itemmaster.service.inform_log;


import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.inform.*;
import cn.com.bbut.iy.itemmaster.entity.ma4330.MA4330;

public interface Ma4330Service {
    /**
     * 通报日志一览
     * @param param
     * @return
     */
    GridDataDTO<Ma4330DetailGridDto> getList(Ma4330DetailParamDto param);
    /**
     *  通报日志新增加
     * @param ma4330dto
     * @return
     */
    int insertInformLog(MA4330 ma4330dto);
}
