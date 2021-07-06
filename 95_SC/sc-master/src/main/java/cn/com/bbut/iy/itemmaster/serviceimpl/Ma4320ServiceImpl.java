package cn.com.bbut.iy.itemmaster.serviceimpl;

import cn.com.bbut.iy.itemmaster.dao.*;
import cn.com.bbut.iy.itemmaster.entity.MA4320;
import cn.com.bbut.iy.itemmaster.entity.MA4320Example;
import cn.com.bbut.iy.itemmaster.service.Ma4320Service;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class Ma4320ServiceImpl implements Ma4320Service {
    @Autowired
    private MA4320Mapper ma4320Mapper;

    @Override
    public List<MA4320> getMa4320List(String informCd,String fileType) {
        MA4320Example ma4320Example = new MA4320Example();
        MA4320Example.Criteria criteria = ma4320Example.createCriteria();
        criteria.andInformCdEqualTo(informCd);
        if(StringUtils.isNotBlank(fileType))
        criteria.andFileTypeEqualTo(fileType);
        List<MA4320> list = ma4320Mapper.selectByExample(ma4320Example);
        list.forEach(ma4320 -> {
            ma4320.setFilePath(ma4320.getFileName()+","+ma4320.getFilePath());
            ma4320.setFilePath1(ma4320.getFilePath());
        });
        return list;
    }

    @Override
    public String getNowDate() {
        return ma4320Mapper.getNowDate();
    }

}
