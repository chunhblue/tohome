package cn.com.bbut.iy.itemmaster.serviceimpl;


import cn.com.bbut.iy.itemmaster.dao.OD0000Mapper;
import cn.com.bbut.iy.itemmaster.dao.ReturnVendorMapper;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.returnOrder.ReturnHeadResult;
import cn.com.bbut.iy.itemmaster.dto.returnOrder.returnVendor.*;
import cn.com.bbut.iy.itemmaster.service.ReturnVendorService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lz
 */
@Service
public class ReturnVendorServiceImpl implements ReturnVendorService {

    @Autowired
    private ReturnVendorMapper returnVendorMapper;

    @Autowired
    private OD0000Mapper od0000Mapper;

    @Override
    public GridDataDTO<RVListResult> getReturnVQueryList(RVListParamDTO param) {
        RVListParam rvListParam = new Gson().fromJson(param.getSearchJson(),RVListParam.class);
        rvListParam.setLimitStart(param.getLimitStart());
        rvListParam.setLimitEnd(param.getLimitEnd());
        rvListParam.setOrderByClause(param.getOrderByClause());
        rvListParam.setStores(param.getStores());
        List<RVListResult> list = returnVendorMapper.selectVQueryListBy(rvListParam);
          long count = returnVendorMapper.selectVQueryListCount(rvListParam);
        return new GridDataDTO<>(list, param.getPage(), count,param.getRows());
    }

    @Override
    public ReturnHeadResult getRVHeadQuery(String orderId) {
        return returnVendorMapper.selectVHeadByOrderId(orderId);
    }


    @Override
    public List<RVListResult> getReturnVPrintList(RVListParamDTO param) {
        RVListParam rvListParam = new Gson().fromJson(param.getSearchJson(),RVListParam.class);
        rvListParam.setOrderByClause(param.getOrderByClause());
        rvListParam.setStores(param.getStores());
        return od0000Mapper.selectVPrintList(rvListParam);
    }
}
