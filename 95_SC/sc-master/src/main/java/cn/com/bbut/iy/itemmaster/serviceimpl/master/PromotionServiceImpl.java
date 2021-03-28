package cn.com.bbut.iy.itemmaster.serviceimpl.master;

import cn.com.bbut.iy.itemmaster.dao.Cm9060Mapper;
import cn.com.bbut.iy.itemmaster.dao.PromotionMapper;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.promotion.*;
import cn.com.bbut.iy.itemmaster.entity.base.Cm9060;
import cn.com.bbut.iy.itemmaster.service.master.PromotionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@Slf4j
public class PromotionServiceImpl implements PromotionService {

    @Autowired
    private PromotionMapper mapper;
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
     * 条件查询主档记录
     *
     * @param dto
     */
    @Override
    public GridDataDTO<PromotionDTO> getList(PromotionParamDTO dto) {

        int count = mapper.selectCountByCondition(dto);
        if(count == 0){
            return new GridDataDTO<PromotionDTO>();
        }

        List<PromotionDTO> _list = mapper.selectListByCondition(dto);

        GridDataDTO<PromotionDTO> data = new GridDataDTO<PromotionDTO>(_list,
                dto.getPage(), count, dto.getRows());
        return data;
    }

    /**
     * 查询主档基本信息
     *
     * @param dto
     */
    @Override
    public PromotionDTO getBasicInfo(PromotionParamDTO dto) {
        return mapper.selectPromotion(dto);
    }

    /**
     * 查询促销条件设定
     *
     * @param dto
     */
    @Override
    public GridDataDTO<Ma4060DTO> getMa4060(PromotionParamDTO dto) {
        GridDataDTO<Ma4060DTO> _return = new GridDataDTO<Ma4060DTO>();
        List<Ma4060DTO> _list = mapper.selectMa4060(dto);
        _return.setRows(_list);
        return _return;
    }

    /**
     * 查询促销商品设定
     *
     * @param dto
     */
    @Override
    public GridDataDTO<Ma4070DTO> getMa4070(PromotionParamDTO dto) {
        GridDataDTO<Ma4070DTO> _return = new GridDataDTO<Ma4070DTO>();
        String mmPromotionPattern = mapper.getMMPromotionPattern(dto.getPromotionCd());
        if(mmPromotionPattern == null){
            List<Ma4070DTO> _list = mapper.selectMa4070(dto);
            _return.setRows(_list);
            return _return;
        }
        if (mmPromotionPattern.equals("04")){
            List<Ma4070DTO> _list = mapper.selectMa4070(dto);
            HashSet<Ma4070DTO> ma4070dtos2 = new HashSet<Ma4070DTO>(_list);
            _list.clear();
            _list.addAll(ma4070dtos2);
            _return.setRows(_list);
        }else {
            List<Ma4070DTO> _list = mapper.selectMa4070(dto);
            _return.setRows(_list);
        }
        return _return;
    }

    /**
     * 查询促销类别设定
     *
     * @param dto
     */
    @Override
    public GridDataDTO<Ma4080DTO> getMa4080(PromotionParamDTO dto) {
        GridDataDTO<Ma4080DTO> _return = new GridDataDTO<Ma4080DTO>();
        List<Ma4080DTO> _list = mapper.selectMa4080(dto);
        _return.setRows(_list);
        return _return;
    }

    /**
     * 查询促销类别例外设定
     *
     * @param dto
     */
    @Override
    public GridDataDTO<Ma4081DTO> getMa4081(PromotionParamDTO dto) {
        GridDataDTO<Ma4081DTO> _return = new GridDataDTO<Ma4081DTO>();
        List<Ma4081DTO> _list = mapper.selectMa4081(dto);
        _return.setRows(_list);
        return _return;
    }

    /**
     * 查询促销品牌设定
     *
     * @param dto
     */
    @Override
    public GridDataDTO<Ma4085DTO> getMa4085(PromotionParamDTO dto) {
        GridDataDTO<Ma4085DTO> _return = new GridDataDTO<Ma4085DTO>();
        List<Ma4085DTO> _list = mapper.selectMa4085(dto);
        _return.setRows(_list);
        return _return;
    }

    /**
     * 查询促销品牌例外设定
     *
     * @param dto
     */
    @Override
    public GridDataDTO<Ma4086DTO> getMa4086(PromotionParamDTO dto) {
        GridDataDTO<Ma4086DTO> _return = new GridDataDTO<Ma4086DTO>();
        List<Ma4086DTO> _list = mapper.selectMa4086(dto);
        _return.setRows(_list);
        return _return;
    }

    /**
     * 查询促销区域设定
     *
     * @param dto
     */
    @Override
    public GridDataDTO<Ma4090DTO> getMa4090(PromotionParamDTO dto) {
        GridDataDTO<Ma4090DTO> _return = new GridDataDTO<Ma4090DTO>();
        List<Ma4090DTO> _list = mapper.selectMa4090(dto);
        _return.setRows(_list);
        return _return;
    }

    /**
     * 查询促销例外店铺设定
     *
     * @param dto
     */
    @Override
    public GridDataDTO<Ma4100DTO> getMa4100(PromotionParamDTO dto) {
        GridDataDTO<Ma4100DTO> _return = new GridDataDTO<Ma4100DTO>();
        List<Ma4100DTO> _list = mapper.selectMa4100(dto);
        _return.setRows(_list);
        return _return;
    }

    /**
     * 查询直接促销
     *
     */
    @Override
    public GridDataDTO<Ma4150DTO> getMa4150(PromotionParamDTO dto) {
        GridDataDTO<Ma4150DTO> _return = new GridDataDTO<Ma4150DTO>();
        List<Ma4150DTO> _list = mapper.getMa4150(dto);
        _return.setRows(_list);
        return _return;
    }

    /**
     * 查询bill value
     *
     */
    @Override
    public GridDataDTO<Ma4155DTO> getMa4155(PromotionParamDTO dto) {
        GridDataDTO<Ma4155DTO> _return = new GridDataDTO<Ma4155DTO>();
        List<Ma4155DTO> _list = mapper.getMa4155(dto);
        _return.setRows(_list);
        return _return;
    }

}
