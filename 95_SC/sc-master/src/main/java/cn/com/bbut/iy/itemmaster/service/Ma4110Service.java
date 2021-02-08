package cn.com.bbut.iy.itemmaster.service;


import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.ma4110.Ma4110DetailGridDto;
import cn.com.bbut.iy.itemmaster.dto.ma4110.Ma4110GridDto;
import cn.com.bbut.iy.itemmaster.dto.ma4110.Ma4110ParamDto;

import java.util.Collection;
import java.util.List;

public interface Ma4110Service {
    /**
     * 获取区域信息 下拉
     * @return
     */
    List<AutoCompleteDTO> getRegionList();


    /**
     * 促销信息一览
     * @param param
     * @return
     */
    GridDataDTO<Ma4110GridDto> insertInformAndGetList(Ma4110ParamDto param);
    /**
     * 促销详细信息信息一览
     * @param param
     * @return
     */
    GridDataDTO<Ma4110DetailGridDto> getDetailList(Ma4110ParamDto param);
}
