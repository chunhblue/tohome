package cn.com.bbut.iy.itemmaster.serviceimpl.audit;

import cn.com.bbut.iy.itemmaster.dao.audit.ReviewBeanMapper;
import cn.com.bbut.iy.itemmaster.dao.audit.ReviewInfoBeanMapper;
import cn.com.bbut.iy.itemmaster.dto.audit.ReviewBean;
import cn.com.bbut.iy.itemmaster.dto.audit.ReviewInfoBean;
import cn.com.bbut.iy.itemmaster.dto.audit.ReviewInfoDTO;
import cn.com.bbut.iy.itemmaster.dto.audit.ReviewTypeDTO;
import cn.com.bbut.iy.itemmaster.service.audit.IReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
public class ReviewServiceImpl implements IReviewService {
    @Autowired
    private ReviewBeanMapper reviewBeanMapper;
    @Autowired
    private ReviewInfoBeanMapper reviewInfoBeanMapper;

    /**
     * 添加流程记录
     */
    @Override
    public long addRecord(ReviewBean review) {
        reviewBeanMapper.insertReview(review);
        // 返回生成的主键
        return review.getNReviewid();
    }

    /**
     * 查询流程主键
     */
    @Override
    public int getReviewMaxID() {
        return reviewBeanMapper.selectMaxID();
    }

    /**
     * 更新流程记录
     */
    @Override
    public void modifyRecord(ReviewBean review) {
        reviewBeanMapper.updateReview(review);
    }

    /**
     * 查询流程Code是否重复
     */
    @Override
    public int getRecordByCode(String code) {
        return reviewBeanMapper.selectReviewByCode(code);
    }

    /**
     * 根据流程类型查询流程
     */
    @Override
    public List<ReviewBean> getRecordByType(int type) {
        return reviewBeanMapper.selectByType(type);
    }

    /**
     * 条件查询流程
     */
    @Override
    public List<ReviewBean> getRecordByCondition(String code, int type, String name,String nDistinguishTypeId) {
        return reviewBeanMapper.selectByCondition(code, type, name,nDistinguishTypeId);
    }

    /**
     * 无效流程记录
     */
    @Override
    public void invalidRecord(long nReviewid) {
        reviewBeanMapper.invalidReview(nReviewid);
    }

    /**
     * 根据流程ID查询步骤
     */
    @Override
    public List<ReviewInfoBean> getStepByReviewid(long nReviewid) {
        return reviewInfoBeanMapper.selectReviewInfoByID(nReviewid);
    }

    /**
     * 根据流程ID查询步骤：查询角色名称
     * @return
     */
    public List<ReviewInfoDTO> getStepBynReviewid(long nReviewid){
        return reviewInfoBeanMapper.selectInfoByReviewID(nReviewid);
    }

    /**
     * 查询流程步骤主键
     */
    @Override
    public int getReviewInfoMaxID() {
        return reviewInfoBeanMapper.selectMaxId();
    }

    /**
     * 添加流程步骤
     */
    @Override
    public void addReviewInfo(ReviewInfoBean reviewInfoBean) {
        reviewInfoBeanMapper.insertReviewInfo(reviewInfoBean);
    }

    /**
     * 更新流程步骤
     */
    @Override
    public void modifyReviewInfo(ReviewInfoBean reviewInfoBean) {
        reviewInfoBeanMapper.updateReviewInfo(reviewInfoBean);
    }

    /**
     * 删除流程步骤
     */
    @Override
    public void delReviewStep(long recordid) {
        reviewInfoBeanMapper.deleteReviewInfo(recordid);
    }

    /**
     * 根据流程ID删除步骤
     */
    @Override
    public void delAllReviewStepById(long nReviewid) {
        reviewInfoBeanMapper.deleteAllStepById(nReviewid);
    }

    /**
     * 根据流程ID无效步骤
     */
    @Override
    public void invalidAllStepById(long reviewid) {
        reviewInfoBeanMapper.invalidAllStepById(reviewid);
    }

    /**
     * 根据ID查询
     */
    @Override
    public ReviewBean getRecordById(long reviewid) {
        return reviewBeanMapper.selectById(reviewid);
    }

    /**
     * 根据步骤ID查询步骤
     */
    @Override
    public ReviewInfoBean getStepById(long id) {
        return reviewInfoBeanMapper.selectByID(id);
    }

    /**
     * 获取最大步骤值
     */
    @Override
    public long getSubMaxByReviewId(long id) {
        return reviewInfoBeanMapper.selectMaxByReviewId(id);
    }

    /**
     * 根据流程ID和步骤ID获取记录
     */
    @Override
    public ReviewInfoBean getIdByReviewIdAndSubNo(long id, long subNo) {
        return reviewInfoBeanMapper.selectIdByReviewIdAndSubNo(id, subNo);
    }

    @Override
    public List<ReviewTypeDTO> getReviewType() {
        return reviewBeanMapper.selectReviewType();
    }

    /**
     * 查询是否在流程中
     * @return
     */
    public int getStatusByReviewid(String tableName, long reviewid){
        return reviewBeanMapper.selectStatusByReviewid(tableName, reviewid);
    }
}
