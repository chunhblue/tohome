package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.inventory.Sk0020DTO;
import cn.com.bbut.iy.itemmaster.dto.inventory.Sk0020ParamDTO;
import cn.com.bbut.iy.itemmaster.entity.SK0010;
import cn.com.bbut.iy.itemmaster.entity.SK0010Key;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
 
@Repository
public interface TransferModMapper {

    /**
     * 查询头档数据
     *
     */
    SK0010 selectSk0010(SK0010Key sk0010);

    /**
     * 查询详情数据
     *
     */
    List<Sk0020DTO> selectSk0020(Sk0020ParamDTO sk0020);


    /**
     * 查询传票下拉
     *
     */
    List<AutoCompleteDTO> selectOrgOrder(@Param("storeCd") String storeCd,
                                         @Param("businessDate") String businessDate,
                                         @Param("type") String type,
                                         @Param("v") String v);

    List<SK0010> getCorrList(@Param("voucherNos") Collection<String> voucherNos, @Param("type")String type);
}