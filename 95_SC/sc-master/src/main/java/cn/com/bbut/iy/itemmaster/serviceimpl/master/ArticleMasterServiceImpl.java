package cn.com.bbut.iy.itemmaster.serviceimpl.master;

import cn.com.bbut.iy.itemmaster.dao.ArticleMasterMapper;
import cn.com.bbut.iy.itemmaster.dao.Cm9060Mapper;
import cn.com.bbut.iy.itemmaster.dto.article.*;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.entity.base.Cm9060;
import cn.com.bbut.iy.itemmaster.entity.cm9010.Cm9010;
import cn.com.bbut.iy.itemmaster.service.master.ArticleMasterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ArticleMasterServiceImpl implements ArticleMasterService {

    @Autowired
    private ArticleMasterMapper mapper;
    @Autowired
    private Cm9060Mapper cm9060Mapper;

    /**
     * 获取当前业务日期
     */
    public String getBusinessDate() {
        // 数据从总部系统迁移，业务日期需匹配
        Cm9060 dto =  cm9060Mapper.selectByPrimaryKey("0000");
        return dto.getSpValue();
    }

    /**
     * 条件查询主档记录
     *
     * @param dto
     */
    @Override
    public GridDataDTO<ArticleDTO> getList(ArticleParamDTO dto) {
        String businessDate = getBusinessDate();
        dto.setBusinessDate(businessDate);

        int count = mapper.selectCountByCondition(dto);
        if(count == 0){
            return new GridDataDTO<ArticleDTO>();
        }

        List<ArticleDTO> _list = mapper.selectListByCondition(dto);

        GridDataDTO<ArticleDTO> data = new GridDataDTO<ArticleDTO>(_list,
                dto.getPage(), count, dto.getRows());
        return data;
    }

    /**
     * 查询主档基本信息
     *
     * @param dto
     */
    @Override
    public ArticleDTO getBasicInfo(ArticleParamDTO dto) {
        return mapper.selectArticle(dto);
    }

    /**
     * 查询条码信息
     *
     * @param dto
     */
    @Override
    public GridDataDTO<BarcodeDTO> getBarcode(ArticleParamDTO dto) {
        GridDataDTO<BarcodeDTO> _return = new GridDataDTO<BarcodeDTO>();
        List<BarcodeDTO> _list = mapper.selectBarcode(dto);
        _return.setRows(_list);
        return _return;
    }

    /**
     * 查询进货控制信息
     *
     * @param dto
     */
    @Override
    public GridDataDTO<OrderControlDTO> getOrderControl(ArticleParamDTO dto) {
        GridDataDTO<OrderControlDTO> _return = new GridDataDTO<OrderControlDTO>();
        List<OrderControlDTO> _list = mapper.selectOrderControl(dto);
        _return.setRows(_list);
        return _return;
    }

    /**
     * 查询销售控制信息
     *
     * @param dto
     */
    @Override
    public GridDataDTO<SalesControlDTO> getSalesControl(ArticleParamDTO dto) {
        GridDataDTO<SalesControlDTO> _return = new GridDataDTO<SalesControlDTO>();
        List<SalesControlDTO> _list = mapper.selectSalesControl(dto);
        _return.setRows(_list);
        return _return;
    }

    /**
     * 查询口味关系信息
     *
     * @param dto
     */
    @Override
    public GridDataDTO<FlavorDTO> getFlavor(ArticleParamDTO dto) {
        GridDataDTO<FlavorDTO> _return = new GridDataDTO<FlavorDTO>();
        List<FlavorDTO> _list = mapper.selectFlavor(dto);
        _return.setRows(_list);
        return _return;
    }

    /**
     * 查询厨打关系信息
     *
     * @param dto
     */
    @Override
    public FoodServiceDTO getFoodService(ArticleParamDTO dto) {
        FoodServiceDTO _dto =  null;
        try{
            _dto =  mapper.selectFoodService(dto);
            _dto = _dto == null ? new FoodServiceDTO() : _dto;
        }catch (Exception e){
            log.error("查询厨打关系信息异常"+e.getMessage());
        }
        return _dto;
    }

    /**
     * 检索供应商
     *
     * @param v
     */
    @Override
    public List<AutoCompleteDTO> getList(String v) {
        String businessDate = getBusinessDate();
        return mapper.selectList(businessDate, v);
    }

    /**
     * 品牌检索
     *
     * @param v
     */
    @Override
    public List<AutoCompleteDTO> getBrand(String v) {
        return mapper.selectBrand(v);
    }

    /**
     * 材料类型
     */
    @Override
    public List<Cm9010> getMaterialType() {
        return mapper.selectMaterialType();
    }

    /**
     * 查询包装规格信息
     *
     * @param dto
     */
    @Override
    public CartonSpecificationDTO getCartonSpecification(ArticleParamDTO dto) {
        CartonSpecificationDTO _dto = null;
        try{
            _dto = mapper.selectCartonSpecification(dto);
            _dto = _dto == null ? new CartonSpecificationDTO() : _dto;
        }catch (Exception e){
            log.error("查询包装规格信息异常"+e.getMessage());
        }
        return  _dto;
    }
}
