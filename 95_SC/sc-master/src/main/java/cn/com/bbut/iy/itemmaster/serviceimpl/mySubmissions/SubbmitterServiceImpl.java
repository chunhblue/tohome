package cn.com.bbut.iy.itemmaster.serviceimpl.mySubmissions;

import cn.com.bbut.iy.itemmaster.dao.MySubbmitterMapper;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.mySubmissions.MySubbmitterGridDTO;
import cn.com.bbut.iy.itemmaster.dto.mySubmissions.MySubbmitterParamDTO;
import cn.com.bbut.iy.itemmaster.dto.mySubmissions.MySubmissionsGridDTO;
import cn.com.bbut.iy.itemmaster.service.mySubmissions.SubbmitterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("SubbmitterServiceImpl")
public class SubbmitterServiceImpl implements SubbmitterService {

    @Autowired
    MySubbmitterMapper mySubbmitterMapper;
    @Override
    public GridDataDTO<MySubbmitterGridDTO> getSubmitterList(MySubbmitterParamDTO subbmitterParamDTO) {
        List<MySubbmitterGridDTO> list = mySubbmitterMapper.getSubmitterList(subbmitterParamDTO);
        int count = mySubbmitterMapper.countSubmitter(subbmitterParamDTO);

        return new GridDataDTO<>(list,subbmitterParamDTO.getPage(),subbmitterParamDTO.getRows(),count);
    }
}
