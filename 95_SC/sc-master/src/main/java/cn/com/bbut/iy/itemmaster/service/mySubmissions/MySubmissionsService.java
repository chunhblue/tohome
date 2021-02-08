package cn.com.bbut.iy.itemmaster.service.mySubmissions;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.mySubmissions.MySubmissionsGridDTO;
import cn.com.bbut.iy.itemmaster.dto.mySubmissions.MySubmissionsParamDTO;

public interface MySubmissionsService {

    GridDataDTO<MySubmissionsGridDTO> getSubmissionsList(MySubmissionsParamDTO subParam);
}
