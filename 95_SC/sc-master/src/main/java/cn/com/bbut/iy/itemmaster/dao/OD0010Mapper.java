package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dao.gen.OD0010GenMapper;
import cn.com.bbut.iy.itemmaster.dto.difference.DifferenceItemsResult;
import cn.com.bbut.iy.itemmaster.dto.difference.DifferenceListParam;
import cn.com.bbut.iy.itemmaster.dto.difference.DifferenceListResult;
import cn.com.bbut.iy.itemmaster.entity.od0010.OD0010;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OD0010Mapper extends OD0010GenMapper {
    //差异条件查询
    List<DifferenceListResult> selectByCondition(DifferenceListParam differenceListParam);

    long selectByConditionCount(DifferenceListParam differenceListParam);

    //单据差异商品
    List<DifferenceItemsResult> selectByOrderId(DifferenceListParam differenceListParam);

    long selectByOrderIdCount(DifferenceListParam differenceListParam);

    List<DifferenceItemsResult> selectAllItemsBy(@Param("orgOrderId") String orgOrderId);

    //查询是否已存在单据商品
    List<DifferenceItemsResult> getExistItemQuery(OD0010 od0010);

    //根据articleId 查询原单据商品详细信息
    List<DifferenceItemsResult> selectItemDetailsByItemId(@Param("articleId") String articleId, @Param("orgOrderId") String orgOrderId);

    List<DifferenceListResult> getPrintList(DifferenceListParam differenceListParam);

    List<DifferenceListResult> selectByConditionPlus(DifferenceListParam differenceListParam);

    long selectByConditionCountPlus(DifferenceListParam differenceListParam);

    int updateLastCorr(String orderId);

    List<OD0010> getOD0010List(String orderId);
}