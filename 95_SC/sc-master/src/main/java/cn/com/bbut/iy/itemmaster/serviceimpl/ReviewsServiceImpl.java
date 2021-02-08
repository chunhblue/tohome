package cn.com.bbut.iy.itemmaster.serviceimpl;

import cn.com.bbut.iy.itemmaster.dao.ReviewMapper;
import cn.com.bbut.iy.itemmaster.dao.ReviewStepMapper;
import cn.com.bbut.iy.itemmaster.dto.approval.ReviewGridDto;
import cn.com.bbut.iy.itemmaster.dto.approval.ReviewParamDto;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.service.CM9060Service;
import cn.com.bbut.iy.itemmaster.service.ReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class ReviewsServiceImpl implements ReviewService {
    @Autowired
    private ReviewMapper reviewMapper;
    @Autowired
    private ReviewStepMapper reviewStepMapper;
    @Autowired
    private CM9060Service cm9060Service;

    @Override
    public List<AutoCompleteDTO> getApprovalType() {
        return reviewMapper.getApprovalType();
    }

    @Override
    public GridDataDTO<ReviewGridDto> getReviewList(ReviewParamDto param) {
        List<ReviewGridDto> list = reviewMapper.getReviewList(param);
        long count = reviewMapper.getReviewListCount(param);
        return new GridDataDTO<>(list, param.getPage(), count,
                param.getRows());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteReviewById(Integer nReviewid) {
        reviewStepMapper.deleteByPrimaryKey(nReviewid);
        return reviewMapper.deleteByPrimaryKey(nReviewid);
    }

}
