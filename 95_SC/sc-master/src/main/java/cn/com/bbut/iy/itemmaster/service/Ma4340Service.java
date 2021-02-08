package cn.com.bbut.iy.itemmaster.service;


import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.ma4340.Ma4340DetailGridDto;
import cn.com.bbut.iy.itemmaster.dto.ma4340.Ma4340DetailParamDto;

public interface Ma4340Service {
    /**
     * 新品信息一览
     * @param param
     * @return
     */
    GridDataDTO<Ma4340DetailGridDto> insertGetList(Ma4340DetailParamDto param);
}
