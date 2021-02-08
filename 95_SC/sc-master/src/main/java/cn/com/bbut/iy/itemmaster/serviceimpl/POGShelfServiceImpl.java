package cn.com.bbut.iy.itemmaster.serviceimpl;


import cn.com.bbut.iy.itemmaster.dao.PogShelfMapper;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.pogShelf.PogShelfDto;
import cn.com.bbut.iy.itemmaster.dto.pogShelf.PogShelfParamDto;
import cn.com.bbut.iy.itemmaster.service.POGShelfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class POGShelfServiceImpl implements POGShelfService {

    @Autowired
    private PogShelfMapper pogShelfMapper;
    @Override
    public GridDataDTO<PogShelfDto> getPogShelfList(PogShelfParamDto pogShelfParamDto) {

        Integer count = pogShelfMapper.pogShelfListCount(pogShelfParamDto);
        if(count<1){
            return new GridDataDTO<>();
        }
        List<PogShelfDto> list = pogShelfMapper.getPogShelfList(pogShelfParamDto);
        GridDataDTO<PogShelfDto> data = new GridDataDTO<PogShelfDto>(list,
                pogShelfParamDto.getPage(), count, pogShelfParamDto.getRows());
        return data;
    }

    // 明细
    @Override
    public GridDataDTO<PogShelfDto> pogShelfDetail(PogShelfParamDto pogShelfParamDto) {
        Integer count = pogShelfMapper.pogShelfDetailCount(pogShelfParamDto);
        if(count<1){
            return new GridDataDTO<>();
        }
        List<PogShelfDto> list = pogShelfMapper.pogShelfDetail(pogShelfParamDto);
        GridDataDTO<PogShelfDto> data = new GridDataDTO<PogShelfDto>(list,
                pogShelfParamDto.getPage(), count, pogShelfParamDto.getRows());
        return data;
    }
}
