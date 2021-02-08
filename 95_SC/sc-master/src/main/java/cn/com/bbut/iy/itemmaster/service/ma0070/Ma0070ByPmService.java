package cn.com.bbut.iy.itemmaster.service.ma0070;


import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.role.ResourceViewDTO;
import cn.com.bbut.iy.itemmaster.entity.ma0070.MA0070;
import cn.com.bbut.iy.itemmaster.entity.ma0080.MA0080;
import cn.com.bbut.iy.itemmaster.entity.ma0090.MA0090;
import cn.com.bbut.iy.itemmaster.entity.ma0100.MA0100;

import java.util.Collection;
import java.util.List;

public interface Ma0070ByPmService {
    List<MA0070> getListByPm(List<ResourceViewDTO> resources);

    List<MA0080> getListByPm(MA0080 ma0080, List<ResourceViewDTO> resources);

    List<MA0090> getListByPm(MA0090 ma0090, List<ResourceViewDTO> resources);

    List<MA0100> getListByPm(MA0100 ma0100, List<ResourceViewDTO> resources);
}
