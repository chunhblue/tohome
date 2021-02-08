package cn.com.bbut.iy.itemmaster.serviceimpl.bomSale;

import cn.com.bbut.iy.itemmaster.dao.BomSaleMapper;
import cn.com.bbut.iy.itemmaster.dao.Cm9060Mapper;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.bomSale.BomSaleDTO;
import cn.com.bbut.iy.itemmaster.dto.bomSale.BomSaleParamDTO;
import cn.com.bbut.iy.itemmaster.entity.base.Cm9060;
import cn.com.bbut.iy.itemmaster.service.CM9060Service;
import cn.com.bbut.iy.itemmaster.service.bomSale.BomSaleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Bom销售报表
 * 
 * @author mxy
 */
@Slf4j
@Service
public class BomSaleServiceImpl implements BomSaleService {

    @Autowired
    private BomSaleMapper bomSaleMapper;
    @Autowired
    private Cm9060Mapper cm9060Mapper;


    /**
     * 获取当前业务日期
     */
    public String getBusinessDate() {
        Cm9060 dto =  cm9060Mapper.selectByPrimaryKey("0000");
        return dto.getSpValue();
    }

    /**
     * 条件查询数据
     *
     * @param dto
     * @return
     */
    @Override
    public List<BomSaleDTO> getList(BomSaleParamDTO dto) {
        dto.setBusinessDate(getBusinessDate());
        return bomSaleMapper.selectListByCondition(dto);
    }

    /**
     * 获取Bom商品
     */
    @Override
    public List<AutoCompleteDTO> getBomItemList(String v) {
        String businessDate = getBusinessDate();
        return bomSaleMapper.getBomItemList(v,businessDate);
    }
}
