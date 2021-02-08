package cn.com.bbut.iy.itemmaster.serviceimpl.mySubmissions;

import cn.com.bbut.iy.itemmaster.dao.MySubmissionsMapper;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.mySubmissions.MySubmissionsGridDTO;
import cn.com.bbut.iy.itemmaster.dto.mySubmissions.MySubmissionsParamDTO;
import cn.com.bbut.iy.itemmaster.service.mySubmissions.MySubmissionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("MySubmissionsServiceImpl")
public class MySubmissionsServiceImpl implements MySubmissionsService {

    @Autowired
    MySubmissionsMapper submissionsMapper;

    @Override
    public GridDataDTO<MySubmissionsGridDTO> getSubmissionsList(MySubmissionsParamDTO subParam) {
        List<MySubmissionsGridDTO> list = submissionsMapper.getShortcutList(subParam);
        int count = submissionsMapper.countSubmissions(subParam);

        return new GridDataDTO<>(list,subParam.getPage(),subParam.getRows(),count);
    }
}
