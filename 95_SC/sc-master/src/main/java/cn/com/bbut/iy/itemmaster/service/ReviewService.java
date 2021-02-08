package cn.com.bbut.iy.itemmaster.service;


import cn.com.bbut.iy.itemmaster.constant.ConstantsCache;
import cn.com.bbut.iy.itemmaster.dto.approval.ReviewGridDto;
import cn.com.bbut.iy.itemmaster.dto.approval.ReviewParamDto;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

public interface ReviewService {
    /**
     * 获取流程类型 下拉
     * @return
     */
    List<AutoCompleteDTO> getApprovalType();

    /**
     * 审核流程信息一览
     * @param param
     * @return
     */
    @Cacheable(value = ConstantsCache.CACHE_REVIEW, key = "#param + '-Page:' + #param.limitStart")
    GridDataDTO<ReviewGridDto> getReviewList(ReviewParamDto param);


    /**
     * 删除审核流程
     * @param nReviewid 流程id
     * @return
     */
    @CacheEvict(value = ConstantsCache.CACHE_REVIEW, allEntries=true)
    int deleteReviewById(Integer nReviewid);
}
