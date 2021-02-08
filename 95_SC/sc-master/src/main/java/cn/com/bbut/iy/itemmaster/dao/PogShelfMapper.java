package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dto.pogShelf.PogShelfDto;
import cn.com.bbut.iy.itemmaster.dto.pogShelf.PogShelfParamDto;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface PogShelfMapper {

    List<PogShelfDto> getPogShelfList(PogShelfParamDto pogShelfParamDto);

    Integer pogShelfListCount(PogShelfParamDto pogShelfParamDto);

    List<PogShelfDto> pogShelfDetail(PogShelfParamDto pogShelfParamDto);

    Integer pogShelfDetailCount(PogShelfParamDto pogShelfParamDto);
}
