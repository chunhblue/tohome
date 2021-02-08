package cn.com.bbut.iy.itemmaster.serviceimpl;

import cn.com.bbut.iy.itemmaster.dao.MA4350Mapper;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.ma4350.Ma4350DetailGridDto;
import cn.com.bbut.iy.itemmaster.dto.ma4350.Ma4350DetailParamDto;
import cn.com.bbut.iy.itemmaster.service.CM9060Service;
import cn.com.bbut.iy.itemmaster.service.Ma4350Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class Ma4350ServiceImpl implements Ma4350Service {
    @Autowired
    private MA4350Mapper ma4350Mapper;
    @Autowired
    private CM9060Service cm9060Service;

    @Override
    public GridDataDTO<Ma4350DetailGridDto> getList(Ma4350DetailParamDto param) {
        String businessDate = cm9060Service.getValByKey("0000");
        param.setBusinessDate(businessDate);
        List<Ma4350DetailGridDto> list = ma4350Mapper.getList(param);
        long count = ma4350Mapper.getListCount(param);
        return new GridDataDTO<>(list, param.getPage(), count,
                param.getRows());
    }

    @Override
    public Ma4350DetailGridDto getMa4350ByVoucherCd(String voucherCd) {
        return ma4350Mapper.getMa4350ByVoucherCd(voucherCd);
    }

}
