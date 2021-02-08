package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dao.gen.OP0060GenMapper;
import cn.com.bbut.iy.itemmaster.dto.op0060.OP0060GridDto;
import cn.com.bbut.iy.itemmaster.dto.op0060.OP0060ParamDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OP0060Mapper extends OP0060GenMapper {
    List<OP0060GridDto> getList(OP0060ParamDto param);

    long getListCount(OP0060ParamDto param);
}