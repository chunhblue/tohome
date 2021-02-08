package cn.com.bbut.iy.itemmaster.serviceimpl;

import cn.com.bbut.iy.itemmaster.dao.OD8030Mapper;
import cn.com.bbut.iy.itemmaster.entity.od8030.OD8030;
import cn.com.bbut.iy.itemmaster.entity.od8030.OD8030Example;
import cn.com.bbut.iy.itemmaster.service.OD8030Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lz
 */
@Service
public class OD8030ServiceImpl implements OD8030Service {

    @Autowired
    private OD8030Mapper od8030Mapper;

    @Override
    public Integer insert(OD8030 od8030) {
        return od8030Mapper.insertSelective(od8030);
    }

    @Override
    public int delete(String return_id, String article_id) {
        OD8030Example od8030Example = new OD8030Example();
        OD8030Example.Criteria criteria = od8030Example.createCriteria();
        criteria.andArticleIdEqualTo(article_id).andReturnIdEqualTo(return_id);
        return od8030Mapper.deleteByExample(od8030Example);
    }

    @Override
    public List<OD8030> isInItem(String storeCd, String returnId) {
        OD8030Example od8030Example = new OD8030Example();
        OD8030Example.Criteria criteria = od8030Example.createCriteria();
        criteria.andStoreCdEqualTo(storeCd).andReturnIdEqualTo(returnId);
        return od8030Mapper.selectByExample(od8030Example);
    }

    @Override
    public Integer updateByRA(OD8030 od8030) {//根据 od8030Example 修改 od8030_1
        OD8030 od8030_1 = new OD8030();
        od8030_1.setOrderQty(od8030.getOrderQty());
        od8030_1.setOrderAmount(od8030.getOrderAmount());
        od8030_1.setReason(od8030.getReason());
        od8030_1.setUpdateUserId(od8030.getUpdateUserId());
        od8030_1.setUpdateYmd(od8030.getUpdateYmd());
        od8030_1.setOrderActualQty(od8030.getOrderActualQty());

        OD8030Example od8030Example = new OD8030Example();
        OD8030Example.Criteria criteria = od8030Example.createCriteria();
        criteria.andReturnIdEqualTo(od8030.getReturnId()).andArticleIdEqualTo(od8030.getArticleId());
        return od8030Mapper.updateByExampleSelective(od8030_1,od8030Example);
    }
}
