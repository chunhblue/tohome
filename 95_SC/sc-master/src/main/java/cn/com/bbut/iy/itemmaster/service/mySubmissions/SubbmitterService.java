package cn.com.bbut.iy.itemmaster.service.mySubmissions;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.mySubmissions.MySubbmitterGridDTO;
import cn.com.bbut.iy.itemmaster.dto.mySubmissions.MySubbmitterParamDTO;

public interface SubbmitterService {

    GridDataDTO<MySubbmitterGridDTO> getSubmitterList(MySubbmitterParamDTO subbmitterParamDTO);
    
}
