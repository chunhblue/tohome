package cn.com.bbut.iy.itemmaster.serviceimpl.priceLabel;

import cn.com.bbut.iy.itemmaster.dao.PriceLabelMapper;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.priceLabel.PriceLabelDTO;
import cn.com.bbut.iy.itemmaster.dto.priceLabel.PriceLabelParamDTO;
import cn.com.bbut.iy.itemmaster.service.CM9060Service;
import cn.com.bbut.iy.itemmaster.service.priceLabel.PriceLabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * 价签查询/打印
 */
@Service
public class PriceLabelServiceImpl implements PriceLabelService {

    @Autowired
    private PriceLabelMapper priceLabelMapper;
    @Autowired
    private CM9060Service cm9060Service;


    /**
     * 查询未来三天价签数据
     */
    @Override
    public GridDataDTO<PriceLabelDTO> search(PriceLabelParamDTO param) {
        String businessDate = cm9060Service.getValByKey("0000");

        // 业务日期
        param.setBusinessDate(businessDate);
        List<PriceLabelDTO> list;
        Integer count=0;
       if (!param.getType().equals("03")){
           // 查询总条数
           count = priceLabelMapper.searchCount(param);
           if (count==null || count == 0) {
               return new GridDataDTO<PriceLabelDTO>();
           }
           // 分页查询未来三天的数据
           list = priceLabelMapper.search(param);
       }else {
           count = priceLabelMapper.searchTypeCount(param);
           if (count==null || count == 0) {
             return new GridDataDTO<PriceLabelDTO>();
           }
           // 分页查询未来三天的数据
          list = priceLabelMapper.selectListByCondition(param);
       }
        return new GridDataDTO<PriceLabelDTO>(list,param.getPage(),count,param.getRows());
    }

    /**
     * 获取未来三天价签商品数据
     * @param v
     * @return
     */
    @Override
    public List<AutoCompleteDTO> getItemList(String v) {
        return priceLabelMapper.getItemList(v);
    }

    /**
     * 打印未来三天价签数据
     * @param param
     * @return
     */
    @Override
    public List<PriceLabelDTO> getPrintData(PriceLabelParamDTO param) {
        String businessDate = cm9060Service.getValByKey("0000");
        // 业务日期
        param.setBusinessDate(businessDate);
        if (!param.getType().equals("03")){
            return priceLabelMapper.search(param);
        }else {
            return priceLabelMapper.selectListByCondition(param);
        }
    }
}
