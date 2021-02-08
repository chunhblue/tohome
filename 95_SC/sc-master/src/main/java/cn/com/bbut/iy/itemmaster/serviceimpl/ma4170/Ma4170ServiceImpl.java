package cn.com.bbut.iy.itemmaster.serviceimpl.ma4170;

import cn.com.bbut.iy.itemmaster.dao.MA4170Mapper;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.base.ReturnDTO;
import cn.com.bbut.iy.itemmaster.dto.ma4170.Ma4170DetailGridDto;
import cn.com.bbut.iy.itemmaster.dto.ma4170.Ma4170DetailParamDto;
import cn.com.bbut.iy.itemmaster.entity.ma4170.MA4170;
import cn.com.bbut.iy.itemmaster.entity.ma4170.MA4170Example;
import cn.com.bbut.iy.itemmaster.service.ma4170.Ma4170Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class Ma4170ServiceImpl implements Ma4170Service {

    @Autowired
    private MA4170Mapper ma4170Mapper;

    @Override
    public GridDataDTO<Ma4170DetailGridDto> getList(Ma4170DetailParamDto param) {
        List<Ma4170DetailGridDto> list = ma4170Mapper.getList(param);
        long count = ma4170Mapper.getListCount(param);
        return new GridDataDTO<>(list, param.getPage(), count,
                param.getRows());
    }

    @Override
    public int updateMa4170(MA4170 ma4170) {
        return ma4170Mapper.updateByPrimaryKeySelective(ma4170);
    }

    @Override
    public int insertMa4170(MA4170 ma4170) {
        return ma4170Mapper.insertSelective(ma4170);
    }

    @Override
    public MA4170 selectMa4170(MA4170 ma4170) {
        return ma4170Mapper.selectByPrimaryKey(ma4170.getReasonCd());
    }

    @Override
    public int deleteMa4170(String reasonCd) {
        return ma4170Mapper.deleteByPrimaryKey(reasonCd);
    }

    /**
     * 校验唯一
     */
    @Override
    public ReturnDTO checkUnique(String reasonCd) {
        if (StringUtils.isEmpty(reasonCd)) {
            return new ReturnDTO(false,"Parameter is empty!");
        }

        MA4170 ma4170 = ma4170Mapper.selectByPrimaryKey(reasonCd);
        if (ma4170==null) {
            return new ReturnDTO(true,"Unique");
        }

        return new ReturnDTO(false,"Not unique");
    }
}
