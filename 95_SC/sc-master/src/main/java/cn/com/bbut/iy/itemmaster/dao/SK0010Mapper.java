package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dao.gen.SK0010GenMapper;
import cn.com.bbut.iy.itemmaster.entity.SK0010;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface SK0010Mapper extends SK0010GenMapper {
    SK0010 getStoreSeries(String storeCd);

    List<SK0010> getApprovedInfo(String voucherNo);
}