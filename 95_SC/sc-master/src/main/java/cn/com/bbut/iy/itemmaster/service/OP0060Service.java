package cn.com.bbut.iy.itemmaster.service;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.op0060.OP0060GridDto;
import cn.com.bbut.iy.itemmaster.dto.op0060.OP0060ParamDto;
import cn.com.bbut.iy.itemmaster.entity.op0060.OP0060;

import java.io.OutputStream;
import java.util.List;

/**
 * OP0060
 *
 * @author zcz
 */
public interface OP0060Service {
    GridDataDTO<OP0060GridDto> getList(OP0060ParamDto param);

    int insertOrUpdate(OP0060 op0060,String flg);

    int deleteByParam(OP0060 op0060);

    List<OP0060GridDto> depositList(OP0060ParamDto param, OutputStream outputStream);

    Integer searhSameDayInsert(OP0060ParamDto param);
}
