package cn.com.bbut.iy.itemmaster.service;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.rtInventory.RTInventoryQueryDTO;
import cn.com.bbut.iy.itemmaster.dto.rtInventory.RTInventoryQueryParamDTO;
import cn.com.bbut.iy.itemmaster.dto.rtInventory.RtInvContent;
import cn.com.bbut.iy.itemmaster.dto.rtInventory.SaveInventoryQty;

import java.util.Collection;
import java.util.List;

public interface RealTimeInventoryQueryService {
    /*
     * 条件查询
     */
    GridDataDTO<RTInventoryQueryDTO> getInventoryList(RTInventoryQueryParamDTO rTParamDTO);


    /**
     * 获取实时库存数量
     * @param connUrl
     * @return
     */
    RTInventoryQueryDTO getRtInventory(String connUrl);
    /**
     * 获取多条item实时库存
     * @param connUrl
     * @return
     */
    List<RTInventoryQueryDTO> getRtInventoryList(String connUrl);
    // post请求多条实时库存数据
    List<RTInventoryQueryDTO> getStockList(List<String> articles, String connUrl);

    /**
     * 连接ES，返回json字符串
     * @param connUrl
     * @return
     */
    String getConnUrlData(String connUrl);

    /**
     * 调用ES库存保存方法
     * @return
     */
    RtInvContent saveRtQtyListToEs(List<SaveInventoryQty> saveRtQtyList);

    String saveBIrtQty(List<SaveInventoryQty> saveRtQtyList,String detailType,String storeCd);
}
