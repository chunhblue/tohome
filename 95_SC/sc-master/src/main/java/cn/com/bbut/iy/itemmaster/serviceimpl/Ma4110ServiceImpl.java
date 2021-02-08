package cn.com.bbut.iy.itemmaster.serviceimpl;

import cn.com.bbut.iy.itemmaster.dao.MA4110Mapper;
import cn.com.bbut.iy.itemmaster.dao.TaskMapper;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.ma4110.Ma4110DetailGridDto;
import cn.com.bbut.iy.itemmaster.dto.ma4110.Ma4110GridDto;
import cn.com.bbut.iy.itemmaster.dto.ma4110.Ma4110ParamDto;
import cn.com.bbut.iy.itemmaster.service.CM9060Service;
import cn.com.bbut.iy.itemmaster.service.Ma4110Service;
import cn.com.bbut.iy.itemmaster.service.ma1000.Ma1000Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class Ma4110ServiceImpl implements Ma4110Service {
    @Autowired
    private MA4110Mapper ma4110Mapper;
    @Autowired
    private CM9060Service cm9060Service;
    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private Ma1000Service ma1000Service;

    @Override
    public List<AutoCompleteDTO> getRegionList() {
        return ma4110Mapper.getRegionList();
    }

    @Override
    public GridDataDTO<Ma4110GridDto> insertInformAndGetList(Ma4110ParamDto param) {
        //获取业务时间
        param.setBusinessDate(cm9060Service.getValByKey("0000"));
        List<Ma4110GridDto> list = ma4110Mapper.getList(param);
        long count = ma4110Mapper.getListCount(param);
        List<String> infoIds = list.stream().map(ma4110GridDto -> ma4110GridDto.getPromotionCd()).collect(Collectors.toList());
        //通知进入 添加通知记录
        if ("2".equals(param.getInformType())){
            taskMapper.insertInformRecors(param.getUserId(),"2",infoIds);
        }
        return new GridDataDTO<>(list, param.getPage(), count,
                param.getRows());
    }

    @Override
    public GridDataDTO<Ma4110DetailGridDto> getDetailList(Ma4110ParamDto param) {
        List<Ma4110DetailGridDto> list = ma4110Mapper.getDetailList(param);
        long count = ma4110Mapper.getDetailListCount(param);
        return new GridDataDTO<>(list, param.getPage(), count,
                param.getRows());
    }
}
