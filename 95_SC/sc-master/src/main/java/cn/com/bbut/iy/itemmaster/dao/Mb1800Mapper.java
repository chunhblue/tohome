package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dao.gen.Mb1800GenMapper;
import cn.com.bbut.iy.itemmaster.entity.Mb1700;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
public interface Mb1800Mapper extends Mb1800GenMapper {
    int deleteByrequestId(@Param("requestId") String requestId);

    int insertmb1800Bymb1700(Mb1700 mb1700);
}