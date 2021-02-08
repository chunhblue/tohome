package cn.com.bbut.iy.itemmaster.serviceimpl.difference;

import cn.com.bbut.iy.itemmaster.dao.Cm9060Mapper;
import cn.com.bbut.iy.itemmaster.dao.OD0000Mapper;
import cn.com.bbut.iy.itemmaster.dao.OD0010Mapper;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.difference.*;
import cn.com.bbut.iy.itemmaster.entity.base.Cm9060;
import cn.com.bbut.iy.itemmaster.entity.od0000.OD0000;
import cn.com.bbut.iy.itemmaster.entity.od0000.OD0000Example;
import cn.com.bbut.iy.itemmaster.entity.od0010.OD0010;
import cn.com.bbut.iy.itemmaster.entity.od0010.OD0010Example;
import cn.com.bbut.iy.itemmaster.service.difference.DifferenceService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @author lz
 */
@Service
public class DifferenceServiceImpl implements DifferenceService {

    @Autowired
    private OD0010Mapper od0010Mapper;
    @Autowired
    private OD0000Mapper od0000Mapper;
    @Autowired
    private Cm9060Mapper cm9060Mapper;

    /**
     * 获取当前业务日期
     */
    public String getBusinessDate() {
        Cm9060 dto =  cm9060Mapper.selectByPrimaryKey("0000");
        return dto.getSpValue();
    }

    //条件查询差异
    @Override
    public GridDataDTO<DifferenceListResult> getDifferenceList(DifferenceListParamDTO param) {

        DifferenceListParam differenceListParam = new Gson().fromJson(param.getSearchJson(),DifferenceListParam.class);

        differenceListParam.setLimitStart(param.getLimitStart());
        differenceListParam.setLimitEnd(param.getLimitEnd());
        differenceListParam.setOrderByClause(param.getOrderByClause());
        differenceListParam.setStores(param.getStores());
        String businessDate = getBusinessDate();
        differenceListParam.setBusinessDate(businessDate);

        long count = od0010Mapper.selectByConditionCountPlus(differenceListParam);

//        List<DifferenceListResult> list = od0010Mapper.selectByCondition(differenceListParam);
        List<DifferenceListResult> list = od0010Mapper.selectByConditionPlus(differenceListParam);

        return new GridDataDTO<>(list, param.getPage(), count,param.getRows());
    }

    @Override
    public GridDataDTO<DifferenceItemsResult> selectByOrderId(DifferenceListParamDTO param) {
        DifferenceListParam differenceListParam = new Gson().fromJson(param.getSearchJson(),DifferenceListParam.class);

        differenceListParam.setLimitStart(param.getLimitStart());
        differenceListParam.setLimitEnd(param.getLimitEnd());
        differenceListParam.setOrderByClause(param.getOrderByClause());

        List<DifferenceItemsResult> list = od0010Mapper.selectByOrderId(differenceListParam);

        long count = od0010Mapper.selectByOrderIdCount(differenceListParam);

        return new GridDataDTO<>(list, param.getPage(), count,param.getRows());
//        return od0010Mapper.selectByOrderId(order_id);
    }

    @Override
    public Integer updateReasonBy(OD0010 od0010) {
        //根据 od0010Example 修改 od0010_1
        OD0010 od0010_1 = new OD0010();
        od0010_1.setReasonId(od0010.getReasonId());
        od0010_1.setUpdateUserId(od0010.getUpdateUserId());
        od0010_1.setUpdateYmd(od0010.getUpdateYmd());

        //更新单据修改人和修改日期
        OD0000 od0000 = new OD0000();
        od0000.setUpdateUserId(od0010.getUpdateUserId());
        od0000.setUpdateYmd(od0010.getUpdateYmd());
        OD0000Example od0000Example = new OD0000Example();
        od0000Example.createCriteria().andOrderIdEqualTo(od0010.getOrderId());
        od0000Mapper.updateByExampleSelective(od0000,od0000Example);

        OD0010Example od0010Example = new OD0010Example();
        OD0010Example.Criteria criteria = od0010Example.createCriteria();
        criteria.andOrderIdEqualTo(od0010.getOrderId()).andArticleIdEqualTo(od0010.getArticleId());
        return od0010Mapper.updateByExampleSelective(od0010_1,od0010Example);
    }

    @Override
    public DifferenceHeadResult getHeadQuery(String orderId) {
        return od0000Mapper.selectAdjustHeadByOrderId(orderId);
    }

    @Override
    public List<DifferenceItemsResult> selectAllItemsBy(String orgOrderId) {
        return od0010Mapper.selectAllItemsBy(orgOrderId);
    }

    @Override
    public List<DifferenceItemsResult> getExistItemQuery(OD0010 od0010) {
        List<DifferenceItemsResult> list = od0010Mapper.getExistItemQuery(od0010);
        return list;
    }

    @Override
    public List<DifferenceItemsResult> selectItemDetailsByItemId(String articleId,String orgOrderId) {
        List<DifferenceItemsResult> list = od0010Mapper.selectItemDetailsByItemId(articleId,orgOrderId);
        return list;
    }

    @Override
    public Integer updateItemBy(OD0010 od0010) {
        OD0010 od0010_1 = new OD0010();
        od0010_1.setAdjustQty(od0010.getAdjustQty());
        od0010_1.setAdjustAmt(od0010.getAdjustAmt());
        od0010_1.setReasonId(od0010.getReasonId());
        od0010_1.setUpdateUserId(od0010.getUpdateUserId());
        od0010_1.setUpdateYmd(od0010.getUpdateYmd());

        OD0010Example od0010Example = new OD0010Example();
        OD0010Example.Criteria criteria = od0010Example.createCriteria();
        criteria.andOrderIdEqualTo(od0010.getOrderId()).andArticleIdEqualTo(od0010.getArticleId());
        return od0010Mapper.updateByExampleSelective(od0010_1,od0010Example);
    }

    @Override
    public Integer insertItem(OD0010 od0010) {
        return od0010Mapper.insertSelective(od0010);
    }

    @Override
    public Integer deleteItemBy(OD0010 od0010) {
        OD0010Example od0010Example = new OD0010Example();
        OD0010Example.Criteria criteria = od0010Example.createCriteria();
        criteria.andArticleIdEqualTo(od0010.getArticleId()).andOrderIdEqualTo(od0010.getOrderId());
        return od0010Mapper.deleteByExample(od0010Example);
    }

    @Override
    public List<DifferenceListResult> getPrintList(DifferenceListParam differenceListParam) {
        String businessDate = getBusinessDate();
        differenceListParam.setBusinessDate(businessDate);
        return od0010Mapper.getPrintList(differenceListParam);
    }
}
