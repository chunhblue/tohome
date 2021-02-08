package cn.com.bbut.iy.itemmaster.serviceimpl.od0010;

import cn.com.bbut.iy.itemmaster.dao.Cm9060Mapper;
import cn.com.bbut.iy.itemmaster.dao.OD0010CMapper;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.od0010.od0010DTO;
import cn.com.bbut.iy.itemmaster.dto.od0010.od0010ParamDTO;
import cn.com.bbut.iy.itemmaster.dto.od0010.od0010viewDTO;
import cn.com.bbut.iy.itemmaster.entity.OD0010C;
import cn.com.bbut.iy.itemmaster.entity.base.Cm9060;
import cn.com.bbut.iy.itemmaster.service.od0010.directOd0010Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * @ClassName directOd0010ServiceImpl
 * @Description TODO
 * @Author Administrator
 * @Date 2020/3/17 11:44
 * @Version 1.0
 */

@Service
public class directOd0010ServiceImpl implements directOd0010Service {

    @Autowired
    private OD0010CMapper od0010CMapper;
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
     * 订货查询明细
     * @return
     */
    @Override
    public GridDataDTO<od0010DTO> getOrderInformation(od0010ParamDTO param) {
        param.setBusinessDate(getBusinessDate());

        int count = od0010CMapper.getOrderInformationCount(param);
        if (count<1) {
            return new GridDataDTO<od0010DTO>();
        }
        List<od0010DTO> result = od0010CMapper.getOrderInformation(param);

        if (result==null||result.isEmpty()) {
            return new GridDataDTO<od0010DTO>();
        }
        int i = 1;
        // 设置收货差异
        for (od0010DTO dto : result) {
            BigDecimal orderQty = dto.getOrderQty().stripTrailingZeros();
            BigDecimal orderNochargeQty = dto.getOrderNochargeQty().stripTrailingZeros();
            String receiveQty = dto.getReceiveQty();
            // 计算差异数量
            dto.setReceivingDifferences(orderQty,orderNochargeQty,receiveQty);
            dto.setSerialNo(String.valueOf(i++));
        }
        GridDataDTO<od0010DTO> data = new GridDataDTO<od0010DTO>(result, param.getPage(), count, param.getRows());
        return data;
    }

    /**
     * 供应商订货查询
     * @param param
     * @return
     */
    @Override
    public List<od0010DTO> getSupplierPrintData(od0010ParamDTO param) {
        param.setBusinessDate(getBusinessDate());
        List<od0010DTO> result = od0010CMapper.getSupplierOrderPrintInformation(param);
        if (result==null||result.isEmpty()) {
            return null;
        }
        return result;
    }

    /**
     * 仓库订货查询
     * @param param
     * @return
     */
    @Override
    public List<od0010DTO> getDCPrintData(od0010ParamDTO param) {
        param.setBusinessDate(getBusinessDate());
        List<od0010DTO> result = od0010CMapper.getDCOrderPrintInformation(param);
        if (result==null||result.isEmpty()) {
            return null;
        }
        return result;
    }
    @Override
    public List<OD0010C> getItemsByOrder(od0010ParamDTO param) {
        List<OD0010C> result = od0010CMapper.getOrderDetailList(param);
        int i = 1;
        for (OD0010C dto : result) {
            BigDecimal orderQty = dto.getOrderQty().stripTrailingZeros();
            BigDecimal orderNochargeQty = dto.getOrderNochargeQty().stripTrailingZeros();
            String receiveQty = dto.getReceiveQty();
            // 计算差异数量
            dto.setReceivingDifferences(orderQty,orderNochargeQty,receiveQty);
            dto.setSerialNo(String.valueOf(i++));
        }
        return result;
    }

    @Override
    public int getOrderDetailListCount(od0010ParamDTO param) {
        return  od0010CMapper.getOrderDetailListCount(param);
    }


    @Override
    public GridDataDTO<od0010DTO> getdirectOrderInformation(od0010ParamDTO param) {
        param.setBusinessDate(getBusinessDate());
        int count = od0010CMapper.getdirectOrderInformationCount(param);
        if (count<1) {
            return new GridDataDTO<od0010DTO>();
        }
        List<od0010DTO> result = od0010CMapper.getdirectOrderInformation(param);

        if (result==null||result.isEmpty()) {
            return new GridDataDTO<od0010DTO>();
        }
        GridDataDTO<od0010DTO> data = new GridDataDTO<od0010DTO>(result, param.getPage(), count, param.getRows());
        return data;
    }
    @Override
    public GridDataDTO<od0010DTO> getCdOrderInformation(od0010ParamDTO paramDTO) {
        paramDTO.setBusinessDate(getBusinessDate());

        int count = od0010CMapper.getcdOrderInformationCount(paramDTO);
        if (count<1) {
            return new GridDataDTO<od0010DTO>();
        }
        List<od0010DTO> result = od0010CMapper.getcdOrderInformation(paramDTO);


        if (result==null||result.isEmpty()) {
            return new GridDataDTO<od0010DTO>();
        }
        GridDataDTO<od0010DTO> data = new GridDataDTO<od0010DTO>(result, paramDTO.getPage(), count, paramDTO.getRows());
        return data;
    }

    @Override
    public List<AutoCompleteDTO> getList(String v) {
        String businessDate = getBusinessDate();
        return od0010CMapper.selectList(businessDate, v);
    }

}
