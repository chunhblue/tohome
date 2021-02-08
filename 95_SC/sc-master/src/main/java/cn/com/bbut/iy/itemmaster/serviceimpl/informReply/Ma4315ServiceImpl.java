package cn.com.bbut.iy.itemmaster.serviceimpl.informReply;

import cn.com.bbut.iy.itemmaster.dao.MA4315Mapper;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.inform.Ma4315DetailGridDto;
import cn.com.bbut.iy.itemmaster.dto.inform.Ma4315DetailParamDto;
import cn.com.bbut.iy.itemmaster.entity.MA4315;
import cn.com.bbut.iy.itemmaster.entity.ma4330.MA4330;
import cn.com.bbut.iy.itemmaster.service.CM9060Service;
import cn.com.bbut.iy.itemmaster.service.inform_log.Ma4330Service;
import cn.com.bbut.iy.itemmaster.service.inform_reply.Ma4315Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Service
@Slf4j
public class Ma4315ServiceImpl implements Ma4315Service {

    @Autowired
    private CM9060Service cm9060Service;
    @Autowired
    private MA4315Mapper ma4315Mapper;
    @Autowired
    private Ma4330Service ma4330Service;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertInformReply(MA4315 param) {
        //添加通报日志信息
        MA4330 ma4330 = MA4330.builder()
                .informCd(param.getInformCd())
                .storeCd(param.getStoreCd())
                .createUserId(param.getReplyUserId())
                .createYmd(param.getCreateYmd())
                .createHms(param.getCreateHms())
                .logType("02")
                .build();
        ma4330Service.insertInformLog(ma4330);
        return ma4315Mapper.insertSelective(param);
    }

    @Override
    public GridDataDTO<Ma4315DetailGridDto> getReplyList(Ma4315DetailParamDto param, Collection<Integer> roleId) {
        //角色
        param.setRoleList(roleId);
        //获取业务时间
        param.setBusinessDate(cm9060Service.getValByKey("0000"));
        List<Ma4315DetailGridDto> list = ma4315Mapper.getReplyList(param);
        long count = ma4315Mapper.getReplyListCount(param);
        return new GridDataDTO<>(list, param.getPage(), count,
                param.getRows());
    }
}
