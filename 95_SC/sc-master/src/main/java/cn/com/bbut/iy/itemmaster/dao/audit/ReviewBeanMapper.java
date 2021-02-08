package cn.com.bbut.iy.itemmaster.dao.audit;

import cn.com.bbut.iy.itemmaster.dto.audit.ReviewBean;
import cn.com.bbut.iy.itemmaster.dto.audit.ReviewTypeDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ReviewBeanMapper {
    /**
     * 条件查询
     */
    List<ReviewBean> selectByCondition(@Param("code") String code, @Param("type") int type,
                                       @Param("name") String name, @Param("nDistinguishTypeId")String nDistinguishTypeId);

    /**
     * 查询分类
     */
    List<ReviewTypeDTO> selectReviewType();

    /**
     * 根据类型查询
     */
    List<ReviewBean> selectByType(@Param("type") int type);

    /**
     * 根据ID查询
     */
    ReviewBean selectById(@Param("reviewid") long reviewid);

    /**
     * 查询Code是否重复
     */
    int selectReviewByCode(@Param("code") String code);

    /**
     * 查询是否在流程中
     */
    int selectStatusByReviewid(@Param("tableName") String tableName, @Param("reviewId") long reviewid);

    /**
     * 查询主键最大值
     * @param
     * @return
     */
    int selectMaxID();

    /**
     * 添加记录
     * @param review
     * @return
     */
    void insertReview(ReviewBean review);

    /**
     * 更新记录
     * @param review
     * @return
     */
    void updateReview(@Param("updateBean") ReviewBean review);

    /**
     * 记录设为无效
     * @param nReviewid
     * @return
     */
    void invalidReview(@Param("nReviewid") long nReviewid);
}
