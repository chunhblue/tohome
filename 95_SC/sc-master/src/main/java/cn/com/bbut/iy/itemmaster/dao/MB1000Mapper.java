package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dao.gen.MB1000GenMapper;
import cn.com.bbut.iy.itemmaster.entity.MB1000;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public interface MB1000Mapper extends MB1000GenMapper {

    List<String> selectMB1000(List<MB1000> list);
}