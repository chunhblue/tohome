package cn.com.bbut.iy.itemmaster.service.audit;

import cn.com.bbut.iy.itemmaster.dto.audit.ReviewBean;
import cn.com.bbut.iy.itemmaster.dto.audit.ReviewInfoBean;
import cn.com.bbut.iy.itemmaster.dto.audit.ReviewInfoDTO;
import cn.com.bbut.iy.itemmaster.dto.audit.ReviewTypeDTO;

import java.util.List;

public interface IReviewService {
    /**
     * 添加流程记录
     * @return
     */
    long addRecord(ReviewBean review);

    /**
     * 查询是否在流程中
     * @return
     */
    int getStatusByReviewid(String tableName, long reviewid);

    /**
     * 查询分类
     * @return
     */
    List<ReviewTypeDTO> getReviewType();

    /**
     * 条件查询流程
     * @return
     */
    List<ReviewBean> getRecordByCondition(String code, int type, String name,String nDistinguishTypeId);

    /**
     * 根据类型查询流程
     * @return
     */
    List<ReviewBean> getRecordByType(int type);

    /**
     * 根据ID查询流程
     * @return
     */
    ReviewBean getRecordById(long reviewid);

    /**
     * 查询流程主键
     * @return
     */
    int getReviewMaxID();

    /**
     * 查询流程Code是否重复
     * @return
     */
    int getRecordByCode(String code);

    /**
     * 更新流程记录
     * @return
     */
    void modifyRecord(ReviewBean review);

    /**
     * 无效流程记录
     * @return
     */
    void invalidRecord(long nReviewid);

    /**
     * 根据流程ID查询步骤
     * @return
     */
    List<ReviewInfoBean> getStepByReviewid(long nReviewid);

    /**
     * 根据流程ID查询步骤：查询角色名称
     * @return
     */
    List<ReviewInfoDTO> getStepBynReviewid(long nReviewid);

    /**
     * 根据步骤ID查询步骤
     * @return
     */
    ReviewInfoBean getStepById(long id);

    /**
     * 查询流程步骤主键
     * @return
     */
    int getReviewInfoMaxID();

    /**
     * 获取最大步骤值
     * @return
     */
    long getSubMaxByReviewId(long id);

    /**
     * 根据流程ID和步骤ID获取记录
     * @return
     */
    ReviewInfoBean getIdByReviewIdAndSubNo(long id, long subNo);

    /**
     * 添加流程步骤
     * @return
     */
    void addReviewInfo(ReviewInfoBean reviewInfoBean);


    /**
     * 更新流程步骤
     * @return
     */
    void modifyReviewInfo(ReviewInfoBean reviewInfoBean);

    /**
     * 删除流程步骤
     * @return
     */
    void delReviewStep(long recordid);

    /**
     * 删除指定流程的所有步骤
     * @return
     */
    void delAllReviewStepById(long nReviewid);

    /**
     * 根据流程ID无效步骤
     * @return
     */
    void invalidAllStepById(long reviewid);
}
