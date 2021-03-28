package cn.com.bbut.iy.itemmaster.serviceimpl.difference;

import cn.com.bbut.iy.itemmaster.dao.Cm9060Mapper;
import cn.com.bbut.iy.itemmaster.dao.OD0000Mapper;
import cn.com.bbut.iy.itemmaster.dao.OD0010Mapper;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.difference.*;
import cn.com.bbut.iy.itemmaster.entity.base.Cm9060;
import cn.com.bbut.iy.itemmaster.entity.od0010.OD0010;
import cn.com.bbut.iy.itemmaster.service.difference.DifferenceShoppingOrderService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DifferenceShoppingOrderServiceImpl implements DifferenceShoppingOrderService {
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

    @Override
    public GridDataDTO<DifferenceListResult> getDifferenceList(DifferenceListParam differenceListParam) {

        String businessDate = getBusinessDate();
        differenceListParam.setBusinessDate(businessDate);

        long count = od0010Mapper.selectShopDiffByConditionCount(differenceListParam);

        List<DifferenceListResult> list = od0010Mapper.selectShopDiffByCondition(differenceListParam);

        return new GridDataDTO<>(list, differenceListParam.getPage(), count,differenceListParam.getRows());
    }

    @Override
    public GridDataDTO<DifferenceItemsResult> selectByOrderId(DifferenceListParamDTO param) {
        DifferenceListParam differenceListParam = new Gson().fromJson(param.getSearchJson(),DifferenceListParam.class);

        differenceListParam.setLimitStart(param.getLimitStart());
        differenceListParam.setLimitEnd(param.getLimitEnd());
        differenceListParam.setOrderByClause(param.getOrderByClause());
        String businessDate = getBusinessDate();
        differenceListParam.setBusinessDate(businessDate);

        List<DifferenceItemsResult> list = od0010Mapper.selectShopDiffByOrderId(differenceListParam);

        long count = od0010Mapper.selectShopDiffByOrderIdCount(differenceListParam);

        return new GridDataDTO<>(list, param.getPage(), count,param.getRows());
    }


    @Override
    public DifferenceHeadResult getHeadQuery(String orderId) {
        return od0000Mapper.selectShopOrderHeadByOrderId(orderId);
    }

}
