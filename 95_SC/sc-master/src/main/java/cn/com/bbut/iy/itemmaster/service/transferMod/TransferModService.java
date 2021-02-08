package cn.com.bbut.iy.itemmaster.service.transferMod;


import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.inventory.Sk0010DTO;
import cn.com.bbut.iy.itemmaster.dto.inventory.Sk0020DTO;
import cn.com.bbut.iy.itemmaster.dto.inventory.Sk0020ParamDTO;
import cn.com.bbut.iy.itemmaster.entity.SK0010;
import cn.com.bbut.iy.itemmaster.entity.SK0010Key;

import java.util.List;

/**
 * 店间调拨修正
 * 
 * @author
 */
public interface TransferModService {

    /**
     * 查询头档数据
     *
     */
    SK0010 getSk0010(SK0010Key sk0010);

    /**
     * 查询详情数据
     *
     */
    GridDataDTO<Sk0020DTO> getSk0020(Sk0020ParamDTO sk0020);

    /**
     * 查询传票下拉
     *
     */
    List<AutoCompleteDTO> getOrgOrderList(String storeCd, String type, String v);

    /**
     * 保存数据
     *
     * @return
     */
    String insert(Sk0010DTO sk0010, List<Sk0020DTO> list);

}
