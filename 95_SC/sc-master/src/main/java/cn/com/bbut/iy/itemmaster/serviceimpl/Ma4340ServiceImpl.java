package cn.com.bbut.iy.itemmaster.serviceimpl;

import cn.com.bbut.iy.itemmaster.dao.MA4330Mapper;
import cn.com.bbut.iy.itemmaster.dao.MA4340Mapper;
import cn.com.bbut.iy.itemmaster.dao.TaskMapper;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.inform.Ma4330DetailGridDto;
import cn.com.bbut.iy.itemmaster.dto.inform.Ma4330DetailParamDto;
import cn.com.bbut.iy.itemmaster.dto.ma4340.Ma4340DetailGridDto;
import cn.com.bbut.iy.itemmaster.dto.ma4340.Ma4340DetailParamDto;
import cn.com.bbut.iy.itemmaster.entity.ma4330.MA4330;
import cn.com.bbut.iy.itemmaster.service.CM9060Service;
import cn.com.bbut.iy.itemmaster.service.Ma4340Service;
import cn.com.bbut.iy.itemmaster.service.inform_log.Ma4330Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class Ma4340ServiceImpl implements Ma4340Service {
    @Autowired
    private MA4340Mapper ma4340Mapper;
    @Autowired
    private CM9060Service cm9060Service;
    @Autowired
    private TaskMapper taskMapper;

    @Override
    public GridDataDTO<Ma4340DetailGridDto> insertGetList(Ma4340DetailParamDto param) {
        String businessDate = cm9060Service.getValByKey("0000");
        param.setBusinessDate(businessDate);
        List<Ma4340DetailGridDto> list = ma4340Mapper.getList(param);
        List<String> infoIds = list.stream().map(ma4340DetailGridDto -> ma4340DetailGridDto.getRowNum()).collect(Collectors.toList());
        long count = ma4340Mapper.getListCount(param);
        if ("2".equals(param.getInformType())){
            taskMapper.insertInformRecors(param.getUserId(),"1",infoIds);
        }
        return new GridDataDTO<>(list, param.getPage(), count,
                param.getRows());
    }

}
