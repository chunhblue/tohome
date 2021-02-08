package cn.com.bbut.iy.itemmaster.serviceimpl.base;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.bbut.iy.itemmaster.dao.IyOccupMapper;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.LabelDTO;
import cn.com.bbut.iy.itemmaster.entity.base.IyOccup;
import cn.com.bbut.iy.itemmaster.entity.base.IyOccupExample;
import cn.com.bbut.iy.itemmaster.service.base.OccupService;
import cn.shiy.common.baseutil.Container;

/**
 * @author songxz
 */
@Slf4j
@Service
public class OccupServiceImpl implements OccupService {

    @Autowired
    private IyOccupMapper occupMapper;

    @Override
    public List<IyOccup> getOccupByParam(String param) {
        IyOccupExample example = new IyOccupExample();
        example.or().andOccupCodeLike("%" + param + "%");
        example.or().andOccupNameLike("%" + param + "%");
        example.setOrderByClause("occup_code");
        List<IyOccup> list = occupMapper.selectByExample(example);
        return list;
    }

    @Override
    public List<AutoCompleteDTO> getOccupAutoByParam(String param) {
        OccupService service = Container.getBean(OccupService.class);
        List<IyOccup> list = service.getOccupByParam(param);
        List<AutoCompleteDTO> resList = new ArrayList<AutoCompleteDTO>();
        if (list != null && list.size() > 0) {
            for (IyOccup ls : list) {
                AutoCompleteDTO t = new AutoCompleteDTO();
                t.setK(ls.getOccupCode());
                t.setV(ls.getOccupCode() + " " + ls.getOccupName().trim());
                resList.add(t);
            }
        }
        return resList;
    }

    @Override
    public LabelDTO getName(String occupCode) {
        IyOccupExample example = new IyOccupExample();
        example.or().andOccupCodeEqualTo(occupCode);
        List<IyOccup> list = occupMapper.selectByExample(example);
        LabelDTO rest = new LabelDTO();
        if (list != null && list.size() > 0) {
            IyOccup o = list.get(0);
            rest.setCode(o.getOccupCode());
            rest.setName(o.getOccupName());
            rest.setCodeName(o.getOccupCode() + " " + o.getOccupName().trim());
        }
        return rest;
    }

}
