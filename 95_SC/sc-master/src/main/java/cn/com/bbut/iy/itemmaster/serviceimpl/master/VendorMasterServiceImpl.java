package cn.com.bbut.iy.itemmaster.serviceimpl.master;

import cn.com.bbut.iy.itemmaster.dao.Cm9060Mapper;
import cn.com.bbut.iy.itemmaster.dao.VendorMasterMapper;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.vendor.*;
import cn.com.bbut.iy.itemmaster.entity.base.Cm9060;
import cn.com.bbut.iy.itemmaster.service.master.VendorMasterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class VendorMasterServiceImpl implements VendorMasterService {

    @Autowired
    private VendorMasterMapper mapper;
    @Autowired
    private Cm9060Mapper cm9060Mapper;

    /**
     * 条件查询主档记录
     *
     * @param dto
     */
    @Override
    public GridDataDTO<VendorDTO> getList(VendorParamDTO dto) {
        String businessDate = getBusinessDate();
        dto.setBusinessDate(businessDate);

        int count = mapper.selectCountByCondition(dto);
        if(count == 0){
            return new GridDataDTO<VendorDTO>();
        }

        List<VendorDTO> _list = mapper.selectListByCondition(dto);

        GridDataDTO<VendorDTO> data = new GridDataDTO<VendorDTO>(_list,
                dto.getPage(), count, dto.getRows());
        return data;
    }

    /**
     * 查询主档基本信息
     *
     * @param dto
     */
    @Override
    public VendorDTO getBasicInfo(VendorParamDTO dto) {
        return mapper.selectVendor(dto);
    }

    /**
     * 查询主档配送范围
     *
     * @param dto
     */
    @Override
    public GridDataDTO<DeliveryTypeDTO> getDeliveryType(VendorParamDTO dto) {
        GridDataDTO<DeliveryTypeDTO> _return = new GridDataDTO<DeliveryTypeDTO>();
        List<DeliveryTypeDTO> _list = mapper.selectDeliveryType(dto);
        if(_list != null && _list.size() > 0){
            for(DeliveryTypeDTO bean : _list){
                String weeklyDeliveryDay = bean.getWeeklyDeliveryDay();
                String[] deliveryDays = weeklyDeliveryDay.split(",");
                for (int i = 0; i < deliveryDays.length; i++) {
                    String deliveryDay = deliveryDays[i];
                    // 如果是*则没有备货期
                    if("*".equals(deliveryDay)) {
                        continue;
                    }
                    int column = i + 1;
                    String method = "setDeliveryDay" + column;
                    try {
                        // 获取set方法设置参数
                        bean.getClass().getMethod(method, new Class[]{String.class}).invoke(bean, new Object[] {deliveryDay});
                    } catch (Exception e) {
                        log.error("计算备货期异常---"+e.getMessage());
                        continue;
                    }
                }
            }
        }
        _return.setRows(_list);
        return _return;
    }

    /**
     * 查询主档最小订货数量/金额
     *
     * @param dto
     */
    @Override
    public GridDataDTO<Ma2002DTO> getMa2002List(VendorParamDTO dto) {
        GridDataDTO<Ma2002DTO> _return = new GridDataDTO<Ma2002DTO>();
        List<Ma2002DTO> _list = mapper.selectMa2002(dto);
        _return.setRows(_list);
        return _return;
    }

    /**
     * 查询主档大分类信息
     *
     * @param dto
     */
    @Override
    public GridDataDTO<Ma2003DTO> getMa2003List(VendorParamDTO dto) {
        GridDataDTO<Ma2003DTO> _return = new GridDataDTO<Ma2003DTO>();
        List<Ma2003DTO> _list = mapper.selectMa2003(dto);
        _return.setRows(_list);
        return _return;
    }

    /**
     * 查询主档银行信息
     *
     * @param dto
     */
    @Override
    public GridDataDTO<Ma2004DTO> getMa2004List(VendorParamDTO dto) {
        GridDataDTO<Ma2004DTO> _return = new GridDataDTO<Ma2004DTO>();
        List<Ma2004DTO> _list = mapper.selectMa2004(dto);
        _return.setRows(_list);
        return _return;
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
     * 查询主档商品分类信息
     *
     * @param dto
     */
    @Override
    public GridDataDTO<Ma2005DTO> getMa2005List(VendorParamDTO dto) {
        GridDataDTO<Ma2005DTO> _return = new GridDataDTO<Ma2005DTO>();
        List<Ma2005DTO> _list = mapper.selectMa2005(dto);
        _return.setRows(_list);
        return _return;
    }

    /**
     * 获取vendor email
     */
    @Override
    public GridDataDTO<Ma2008DTO> getVendorEmail(VendorParamDTO param) {
        GridDataDTO<Ma2008DTO> _return = new GridDataDTO<Ma2008DTO>();
        List<Ma2008DTO> _list = mapper.selectMa2008(param);
        _return.setRows(_list);
        return _return;
    }

    /**
     * 获取trading term
     */
    @Override
    public GridDataDTO<Ma2007DTO> getTradingTerm(VendorParamDTO param) {
        GridDataDTO<Ma2007DTO> _return = new GridDataDTO<Ma2007DTO>();
        List<Ma2007DTO> _list = mapper.selectMa2007(param);
        _return.setRows(_list);
        return _return;
    }

    /**
     * 获取account
     */
    @Override
    public GridDataDTO<Ma2009DTO> getAccountInfo(VendorParamDTO param) {
        GridDataDTO<Ma2009DTO> _return = new GridDataDTO<Ma2009DTO>();
        List<Ma2009DTO> _list = mapper.selectMa2009(param);
        _return.setRows(_list);
        return _return;
    }

    /**
     * 获取当前业务日期
     */
    public String getBusinessDate() {
        // 数据从总部系统迁移，业务日期需匹配
        Cm9060 dto =  cm9060Mapper.selectByPrimaryKey("0000");
        return dto.getSpValue();
    }
}
