package cn.com.bbut.iy.itemmaster.serviceimpl;


import cn.com.bbut.iy.itemmaster.dao.MA5321Mapper;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.service.MA5321Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 */
@Service
public class MA5321ServiceImpl implements MA5321Service {

    @Autowired
    private MA5321Mapper mapper;

    /**
     * 查询待选
     *
     * @param v
     */
    @Override
    public List<AutoCompleteDTO> getWarehouse(String v) {
        return mapper.selectWarehouse(v);
    }
}
