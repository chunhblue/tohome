package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dao.gen.ReviewGenMapper;
import cn.com.bbut.iy.itemmaster.dto.approval.ReviewGridDto;
import cn.com.bbut.iy.itemmaster.dto.approval.ReviewParamDto;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;

import java.util.List;

public interface ReviewMapper extends ReviewGenMapper {
    /**
     * 获取流程类型信息
     * @return
     */
    List<AutoCompleteDTO> getApprovalType();

    /**
     * 审核流程信息一览
     * @param param
     * @return
     */
    List<ReviewGridDto> getReviewList(ReviewParamDto param);

    long getReviewListCount(ReviewParamDto param);

}