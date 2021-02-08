package cn.com.bbut.iy.itemmaster.serviceimpl;

import cn.com.bbut.iy.itemmaster.dao.MA4360Mapper;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.ma4360.Ma4360DetailGridDto;
import cn.com.bbut.iy.itemmaster.service.CM9060Service;
import cn.com.bbut.iy.itemmaster.service.Ma4360Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class Ma4360ServiceImpl implements Ma4360Service {
    @Autowired
    private MA4360Mapper ma4360Mapper;


    @Override
    public List<Ma4360DetailGridDto> getList(String voucherCd) {
        return ma4360Mapper.getList(voucherCd);
    }
}
