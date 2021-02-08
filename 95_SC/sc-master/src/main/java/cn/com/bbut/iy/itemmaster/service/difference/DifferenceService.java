package cn.com.bbut.iy.itemmaster.service.difference;


import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.difference.*;
import cn.com.bbut.iy.itemmaster.entity.od0010.OD0010;

import java.util.List;

/**
 * Created by lz on 2020/1/10.
 */
public interface DifferenceService {
    //条件查询差异
    GridDataDTO<DifferenceListResult> getDifferenceList(DifferenceListParamDTO paramDTO);

    //差异单据商品
    GridDataDTO<DifferenceItemsResult> selectByOrderId(DifferenceListParamDTO paramDTO);
    //更新理由
    Integer updateReasonBy(OD0010 od0010);
    //差异单头档
    DifferenceHeadResult getHeadQuery(String orderId);
    //根据原单号查询所有可选择商品
    List<DifferenceItemsResult> selectAllItemsBy(String orgOrderId);
    //所选商品是否存在
    List<DifferenceItemsResult> getExistItemQuery(OD0010 od0010);
    //所选商品详情
    List<DifferenceItemsResult> selectItemDetailsByItemId(String articleId,String orgOrderId);
    //更新选择已存在的商品
    Integer updateItemBy(OD0010 od0010);
    //添加商品
    Integer insertItem(OD0010 od0010);
    //删除差异商品
    Integer deleteItemBy(OD0010 od0010);

    // 打印页面列表展示
    List<DifferenceListResult> getPrintList(DifferenceListParam differenceListParam);
}
