package cn.com.bbut.iy.itemmaster.serviceimpl.ma0080;

import cn.com.bbut.iy.itemmaster.dao.MA0080Mapper;
import cn.com.bbut.iy.itemmaster.entity.ma0080.MA0080;
import cn.com.bbut.iy.itemmaster.service.ma0080.MA0080Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName MA0080ServiceImpl
 * @Description TODO
 * @Author Administrator
 * @Date 2020/3/26 16:59
 * @Version 1.0
 */
@Service
public class MA0080ServiceImpl implements MA0080Service {
    @Autowired
    private MA0080Mapper ma0080Mapper;
    @Override
    public List<MA0080> getBigCatage(MA0080 ma0080) {
        return null;

    }
}
