package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dao.gen.Mb1700GenMapper;
import cn.com.bbut.iy.itemmaster.entity.Mb1700;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public interface Mb1700Mapper extends Mb1700GenMapper {

    List<String> selectMb1700(List<Mb1700> list);
}