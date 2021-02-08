package cn.com.bbut.iy.itemmaster.serviceimpl.dailysaleMa0020ServiceImpl;

import cn.com.bbut.iy.itemmaster.dao.MA0020DTOMapper;
import cn.com.bbut.iy.itemmaster.dao.Ma1000DtoMapper;
import cn.com.bbut.iy.itemmaster.dto.ma0020.MA0020DTO;
import cn.com.bbut.iy.itemmaster.dto.ma100Ld.Ma1000DTO;
import cn.com.bbut.iy.itemmaster.service.dailysalereferma0020.dailysaleMa0020Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName dailysaleMa0020ServiceImpl
 * @Description TODO
 * @Author Administrator
 * @Date 2020/3/24 17:17
 * @Version 1.0
 */

@Service
public class dailysaleMa0020ServiceImpl implements dailysaleMa0020Service {

    @Autowired
    private MA0020DTOMapper ma0020DTOMapper;
    @Autowired
    private Ma1000DtoMapper ma1000Mapper;

    @Override
    public List<MA0020DTO> getMA0020Dto(MA0020DTO ma0020DTO) {
        return ma0020DTOMapper.selectMa0020DTO(ma0020DTO);
    }

    @Override
    public List<Ma1000DTO> getMa1000(Ma1000DTO ma1000) {

       return  ma1000Mapper.selectStoreCd(ma1000);

    }
}
