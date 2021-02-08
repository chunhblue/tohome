package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dao.gen.MySubbmitterGenMapper;
import cn.com.bbut.iy.itemmaster.dto.mySubmissions.MySubbmitterGridDTO;
import cn.com.bbut.iy.itemmaster.dto.mySubmissions.MySubbmitterParamDTO;

import java.util.List;

public interface MySubbmitterMapper extends MySubbmitterGenMapper {

    List<MySubbmitterGridDTO> getSubmitterList(MySubbmitterParamDTO paramDTO);

    int countSubmitter(MySubbmitterParamDTO paramDTO);
}