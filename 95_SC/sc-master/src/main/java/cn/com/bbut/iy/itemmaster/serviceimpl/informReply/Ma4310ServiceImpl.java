package cn.com.bbut.iy.itemmaster.serviceimpl.informReply;

import cn.com.bbut.iy.itemmaster.dao.MA4310Mapper;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.base.role.RoleStoreDTO;
import cn.com.bbut.iy.itemmaster.dto.inform.Ma4310DetailGridDto;
import cn.com.bbut.iy.itemmaster.dto.inform.Ma4310DetailParamDto;
import cn.com.bbut.iy.itemmaster.dto.inform.Ma4310ResultDto;
import cn.com.bbut.iy.itemmaster.dto.mRoleStore.MRoleStoreParam;
import cn.com.bbut.iy.itemmaster.entity.ma4310.MA4310;
import cn.com.bbut.iy.itemmaster.entity.ma4310.MA4310Example;
import cn.com.bbut.iy.itemmaster.entity.ma4330.MA4330;
import cn.com.bbut.iy.itemmaster.service.CM9060Service;
import cn.com.bbut.iy.itemmaster.service.inform_log.Ma4330Service;
import cn.com.bbut.iy.itemmaster.service.inform_reply.Ma4310Service;
import cn.com.bbut.iy.itemmaster.service.ma1000.Ma1000Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Slf4j
public class Ma4310ServiceImpl implements Ma4310Service {

    @Autowired
    private MA4310Mapper ma4310Mapper;
    @Autowired
    private CM9060Service cm9060Service;
    @Autowired
    private Ma1000Service ma1000Service;

    @Override
    public GridDataDTO<Ma4310DetailGridDto> getReplyList(Ma4310DetailParamDto param) {
        //获取业务时间
        param.setBusinessDate(cm9060Service.getValByKey("0000"));

        List<Ma4310DetailGridDto> list = ma4310Mapper.getReplyList(param);
        long count = ma4310Mapper.getReplyListCount(param);
        return new GridDataDTO<>(list, param.getPage(), count,
                param.getRows());
    }

    @Override
    public Ma4310ResultDto getInformReply(String informCd, String storeCd) {
        //获取业务时间
        String businessDate = cm9060Service.getValByKey("0000");
        return ma4310Mapper.getInformReply(informCd,storeCd,businessDate);
    }


}
