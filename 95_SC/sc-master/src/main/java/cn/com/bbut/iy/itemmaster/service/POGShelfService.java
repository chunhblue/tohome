package cn.com.bbut.iy.itemmaster.service;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.pogShelf.PogShelfDto;
import cn.com.bbut.iy.itemmaster.dto.pogShelf.PogShelfParamDto;


public interface POGShelfService {

    GridDataDTO<PogShelfDto> getPogShelfList(PogShelfParamDto pogShelfParamDto);

    GridDataDTO<PogShelfDto> pogShelfDetail(PogShelfParamDto pogShelfParamDto);
}
