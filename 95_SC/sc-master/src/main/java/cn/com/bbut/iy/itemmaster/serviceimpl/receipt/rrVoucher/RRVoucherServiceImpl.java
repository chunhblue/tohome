package cn.com.bbut.iy.itemmaster.serviceimpl.receipt.rrVoucher;

import cn.com.bbut.iy.itemmaster.dao.Cm9060Mapper;
import cn.com.bbut.iy.itemmaster.dao.RRVoucherMapper;
import cn.com.bbut.iy.itemmaster.dto.base.CommonDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.receipt.rrVoucher.RRVoucherDTO;
import cn.com.bbut.iy.itemmaster.dto.receipt.rrVoucher.RRVoucherDetailsDTO;
import cn.com.bbut.iy.itemmaster.dto.receipt.rrVoucher.RRVoucherParamDTO;
import cn.com.bbut.iy.itemmaster.entity.base.Cm9060;
import cn.com.bbut.iy.itemmaster.service.receipt.rrVoucher.IRRVoucherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 进退货传票实现
 *
 */
@Service
@Slf4j
public class RRVoucherServiceImpl implements IRRVoucherService {

    @Autowired
    private RRVoucherMapper rrMapper;
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
     * 条件查询记录
     *
     * @param param
     */
    @Override
    public GridDataDTO<RRVoucherDTO> getList(RRVoucherParamDTO param) {
        // 获取记录总数
        // 条件查询数据
        String businessDate = getBusinessDate();
        param.setBusinessDate(businessDate);
        // 特定审核状态分离为两步退货审核状态
        switch (param.getOrderSts()){
            case "21":
                param.setOrderSts(null);
                param.setStatus(1);
                break;
            case "5":
                param.setOrderSts("5");
                param.setStatus(5);
                break;
            case "6":
                param.setOrderSts("6");
                param.setStatus(6);
                break;
            case "7":
                param.setOrderSts("7");
                param.setStatus(7);
                break;
            default:
                param.setOrderSts(param.getOrderSts());
                param.setStatus(0);
        }

        int count = rrMapper.selectCountByCondition(param);
        if(count < 1){
            return new GridDataDTO<RRVoucherDTO>();
        }

        List<RRVoucherDTO> _list = rrMapper.selectListByCondition(param);
        if(_list != null){
            for (RRVoucherDTO rrVoucherDTO : _list) {
                if(rrVoucherDTO.getReviewStsText() != null){
                    if(rrVoucherDTO.getReviewStsText().equals("1")){
                        rrVoucherDTO.setReviewStsText("Return Request Pending");
                    }else if(rrVoucherDTO.getReviewStsText().equals("5")){
                        rrVoucherDTO.setReviewStsText("Rejected");
                    }else if(rrVoucherDTO.getReviewStsText().equals("6")){
                        rrVoucherDTO.setReviewStsText("Withdrawn");
                    }else if(rrVoucherDTO.getReviewStsText().equals("7")){
                        rrVoucherDTO.setReviewStsText("Expired");
                    }else if(rrVoucherDTO.getReviewStsText().equals("10")){
                        // 这里考虑实际退货的时候的pending/Rejected/Withdrawn/Expired状态
                        switch (rrVoucherDTO.getStatus()){
                            case 1:
                                rrVoucherDTO.setReviewStsText("Return Pending");
                                break;
                            case 5:
                                rrVoucherDTO.setReviewStsText("Return Rejected");
                                break;
                            case 6:
                                rrVoucherDTO.setReviewStsText("Return Withdrawn");
                                break;
                            case 7:
                                rrVoucherDTO.setReviewStsText("Return Expired");
                                break;
                            default:
                                rrVoucherDTO.setReviewStsText("Return Request Approved");
                        }
                    }else if(rrVoucherDTO.getReviewStsText().equals("15")){
                        rrVoucherDTO.setReviewStsText("Receiving Pending");
                    }else if(rrVoucherDTO.getReviewStsText().equals("20")){
                        if(rrVoucherDTO.getOrderType().equals("0")){
                            rrVoucherDTO.setReviewStsText("Returned");
                        }else{
                            rrVoucherDTO.setReviewStsText("Received");
                        }
                    }else {
                        rrVoucherDTO.setReviewStsText("");
                    }
                }
            }
        }
        GridDataDTO<RRVoucherDTO> data = new GridDataDTO<RRVoucherDTO>(_list,
                param.getPage(), count, param.getRows());
        return data;
    }

    /**
     * 主键查询头档
     *
     * @param param
     */
    @Override
    public RRVoucherDTO getRecordByKey(RRVoucherParamDTO param) {
        String businessDate = getBusinessDate();
        param.setBusinessDate(businessDate);
        return rrMapper.selectVoucherByKey(param);
    }

    /**
     * 查询明细详情
     *
     * @param param
     */
    @Override
    public GridDataDTO<RRVoucherDetailsDTO> getDetail(RRVoucherParamDTO param) {
        // 获取记录总数
        int count = rrMapper.selectDetailCount(param);
        if(count < 1){
            return new GridDataDTO<RRVoucherDetailsDTO>();
        }
        // 条件查询数据
        String businessDate = getBusinessDate();
        param.setBusinessDate(businessDate);
        List<RRVoucherDetailsDTO> _list = rrMapper.selectDetail(param);
        GridDataDTO<RRVoucherDetailsDTO> data = new GridDataDTO<RRVoucherDetailsDTO>(_list,
                param.getPage(), count, param.getRows());
        return data;
    }

    /**
     * 更新传票
     *
     * @param dto
     * @param list
     */
    @Override
    @Transactional
    public int updateRecord(RRVoucherDTO dto, List<RRVoucherDetailsDTO> list) {
        try{
            CommonDTO commonDTO = dto.getCommonDTO();
            // 更新头档信息
            dto.setOrderSts("04");
            dto.setReceiveDate(commonDTO.getUpdateYmd());
            rrMapper.updateVoucher(dto, null);
            // 更新明细信息
            for(RRVoucherDetailsDTO bean : list){
                bean.setCommonDTO(commonDTO);
                rrMapper.updateDetails(bean, null);
            }
        }catch (RuntimeException e){
            log.error("执行更新传票异常====="+e.getMessage());
            return 0;
        }
        return 1;
    }

    /**
     * 快速收货
     *
     * @param list
     */
    @Override
    @Transactional
    public int updateRecordByQuick(List<RRVoucherDTO> list, CommonDTO dto) {
        int i = 0;
        try{
            RRVoucherParamDTO paramDTO = new RRVoucherParamDTO();
            RRVoucherDetailsDTO _dto = new RRVoucherDetailsDTO();
            _dto.setCommonDTO(dto);
            for(RRVoucherDTO bean : list){
                // 判断订单状态
                paramDTO.setStoreCd(bean.getStoreCd());
                paramDTO.setOrderId(bean.getOrderId());
                RRVoucherDTO temp = rrMapper.selectVoucherByKey(paramDTO);
                if(temp == null || !"00".equals(temp.getOrderSts())){
                    continue;
                }
                // 执行收货---更新头档、明细
                bean.setOrderSts("04");
                bean.setCommonDTO(dto);
                bean.setReceiveDate(dto.getUpdateYmd());
                rrMapper.updateVoucher(bean, "quick");
                _dto.setStoreCd(bean.getStoreCd());
                _dto.setOrderId(bean.getOrderId());
                rrMapper.updateDetails(_dto, "quick");
                i++;
            }
        }catch (RuntimeException e){
            log.error("执行更新传票异常====="+e.getMessage());
            return -1;
        }
        return i;
    }

    // 打印
    @Override
    public List<RRVoucherDTO> getPrintData(RRVoucherParamDTO param) {
        // 条件查询数据
        String businessDate = getBusinessDate();
        param.setBusinessDate(businessDate);
        // 特定审核状态分离为两步退货审核状态
        switch (param.getOrderSts()){
            case "21":
                param.setOrderSts(null);
                param.setStatus(1);
                break;
            case "5":
                param.setOrderSts("5");
                param.setStatus(5);
                break;
            case "6":
                param.setOrderSts("6");
                param.setStatus(6);
                break;
            case "7":
                param.setOrderSts("7");
                param.setStatus(7);
                break;
            default:
                param.setOrderSts(param.getOrderSts());
                param.setStatus(0);
        }

        List<RRVoucherDTO> _list = rrMapper.selectListByCondition(param);
        if(_list != null){
            for (RRVoucherDTO rrVoucherDTO : _list) {
                if(rrVoucherDTO.getReviewStsText() != null){
                    if(rrVoucherDTO.getReviewStsText().equals("1")){
                        rrVoucherDTO.setReviewStsText("Return Request Pending");
                    }else if(rrVoucherDTO.getReviewStsText().equals("5")){
                        rrVoucherDTO.setReviewStsText("Rejected");
                    }else if(rrVoucherDTO.getReviewStsText().equals("6")){
                        rrVoucherDTO.setReviewStsText("Withdrawn");
                    }else if(rrVoucherDTO.getReviewStsText().equals("7")){
                        rrVoucherDTO.setReviewStsText("Expired");
                    }else if(rrVoucherDTO.getReviewStsText().equals("10")){
                        // 这里考虑实际退货的时候的pending/Rejected/Withdrawn/Expired状态
                        switch (rrVoucherDTO.getStatus()){
                            case 1:
                                rrVoucherDTO.setReviewStsText("Return Pending");
                                break;
                            case 5:
                                rrVoucherDTO.setReviewStsText("Return Rejected");
                                break;
                            case 6:
                                rrVoucherDTO.setReviewStsText("Return Withdrawn");
                                break;
                            case 7:
                                rrVoucherDTO.setReviewStsText("Return Expired");
                                break;
                            default:
                                rrVoucherDTO.setReviewStsText("Return Request Approved");
                        }
                    }else if(rrVoucherDTO.getReviewStsText().equals("15")){
                        rrVoucherDTO.setReviewStsText("Receiving Pending");
                    }else if(rrVoucherDTO.getReviewStsText().equals("20")){
                        if(rrVoucherDTO.getOrderType().equals("0")){
                            rrVoucherDTO.setReviewStsText("Returned");
                        }else{
                            rrVoucherDTO.setReviewStsText("Received");
                        }
                    }else {
                        rrVoucherDTO.setReviewStsText("");
                    }
                }
            }
        }
        return _list;
    }
}
