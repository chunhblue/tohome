package cn.com.bbut.iy.itemmaster.service;

import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.inventory.*;
import cn.com.bbut.iy.itemmaster.entity.SK0010;

import java.util.List;

public interface ItemTransferService {

    GridDataDTO<InventoryVouchersGridDTO> getByTypeCondition(InventoryVouchersParamDTO param);

    // 自动加载传票类型 下拉
    List<AutoCompleteDTO> getTypeList(String v);

    /**
     * 保存传票
     *
     * @return
     */
    String insert(Sk0010DTO sk0010, List<Sk0020DTO> sk0020List);

    /**
     * 修改传票
     *
     */
    int update(SK0010 sk0010, List<Sk0020DTO> sk0020List);

    /**
     * 查询详情数据
     *
     */
    GridDataDTO<Sk0020DTO> getSk0020(Sk0020ParamDTO sk0020);
}
