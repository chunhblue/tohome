package cn.com.bbut.iy.itemmaster.service.difference;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.difference.*;
import cn.com.bbut.iy.itemmaster.entity.od0010.OD0010;

import java.util.List;

public interface DifferenceShoppingOrderService {

    //条件查询差异
    GridDataDTO<DifferenceListResult> getDifferenceList(DifferenceListParam differenceListParam);

    //差异单据商品
    GridDataDTO<DifferenceItemsResult> selectByOrderId(DifferenceListParamDTO paramDTO);
    //差异单头档
    DifferenceHeadResult getHeadQuery(String orderId);


}
