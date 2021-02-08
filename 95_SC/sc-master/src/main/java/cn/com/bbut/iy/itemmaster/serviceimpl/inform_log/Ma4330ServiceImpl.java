package cn.com.bbut.iy.itemmaster.serviceimpl.inform_log;

import cn.com.bbut.iy.itemmaster.dao.MA4300Mapper;
import cn.com.bbut.iy.itemmaster.dao.MA4310Mapper;
import cn.com.bbut.iy.itemmaster.dao.MA4320Mapper;
import cn.com.bbut.iy.itemmaster.dao.MA4330Mapper;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.inform.*;
import cn.com.bbut.iy.itemmaster.entity.ma4330.MA4330;
import cn.com.bbut.iy.itemmaster.service.CM9060Service;
import cn.com.bbut.iy.itemmaster.service.inform_log.Ma4330Service;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Slf4j
public class Ma4330ServiceImpl implements Ma4330Service {
    @Autowired
    private MA4330Mapper ma4330Mapper;
    @Autowired
    private CM9060Service cm9060Service;

    @Override
    public GridDataDTO<Ma4330DetailGridDto> getList(Ma4330DetailParamDto param) {
        String businessDate = cm9060Service.getValByKey("0000");
        param.setBusinessDate(businessDate);
        List<Ma4330DetailGridDto> list = ma4330Mapper.getList(param);
        long count = ma4330Mapper.getListCount(param);
        return new GridDataDTO<>(list, param.getPage(), count,
                param.getRows());
    }

    @Override
    public int insertInformLog(MA4330 ma4330dto) {
        return ma4330Mapper.insertSelective(ma4330dto);
    }

}
