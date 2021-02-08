package cn.com.bbut.iy.itemmaster.dao.audit;

import cn.com.bbut.iy.itemmaster.dto.audit.ReviewInfoBean;
import cn.com.bbut.iy.itemmaster.dto.audit.ReviewInfoDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ReviewInfoBeanMapper {
    /**
     * 根据所属流程ID查询
     */
    List<ReviewInfoBean> selectReviewInfoByID(@Param("nReviewid") long nReviewid);

    /**
     * 根据所属流程ID查询：查询角色名称
     */
    List<ReviewInfoDTO> selectInfoByReviewID(@Param("nReviewid") long nReviewid);

    /**
     * 根据ID查询
     */
    ReviewInfoBean selectByID(@Param("id") long id);

    /**
     * 获取最大主键
     */
    int selectMaxId();

    /**
     * 获取最大步骤值
     */
    long selectMaxByReviewId(@Param("reviewId") long id);

    /**
     * 根据流程ID和步骤ID查询记录
     */
    ReviewInfoBean selectIdByReviewIdAndSubNo(@Param("reviewId") long id, @Param("subNo") long subNo);

    /**
     * 添加记录
     */
    void insertReviewInfo(@Param("insertBean") ReviewInfoBean reviewInfoBean);

    /**
     * 更新记录
     */
    void updateReviewInfo(@Param("updateBean") ReviewInfoBean reviewInfoBean);

    /**
     * 删除记录
     */
    void deleteReviewInfo(@Param("recordid") long recordid);

    /**
     * 根据流程ID删除步骤
     */
    void deleteAllStepById(@Param("nReviewid") long nReviewid);

    /**
     * 根据流程ID无效步骤
     */
    void invalidAllStepById(@Param("reviewid") long reviewid);
}
