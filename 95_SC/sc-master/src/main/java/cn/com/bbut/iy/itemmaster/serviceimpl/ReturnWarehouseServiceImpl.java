package cn.com.bbut.iy.itemmaster.serviceimpl;


import cn.com.bbut.iy.itemmaster.dao.MA4320Mapper;
import cn.com.bbut.iy.itemmaster.dao.OD0000Mapper;
import cn.com.bbut.iy.itemmaster.dao.OD0010Mapper;
import cn.com.bbut.iy.itemmaster.dao.ReturnWarehouseMapper;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.returnOrder.OrderInfoDTO;
import cn.com.bbut.iy.itemmaster.dto.returnOrder.ReturnHeadResult;
import cn.com.bbut.iy.itemmaster.dto.returnOrder.RealtimeStockItem;
import cn.com.bbut.iy.itemmaster.dto.returnOrder.returnWarehouse.*;
import cn.com.bbut.iy.itemmaster.dto.rtInventory.RTInventoryQueryDTO;
import cn.com.bbut.iy.itemmaster.entity.MA4320;
import cn.com.bbut.iy.itemmaster.entity.MA4320Example;
import cn.com.bbut.iy.itemmaster.entity.od0000.OD0000;
import cn.com.bbut.iy.itemmaster.entity.od0000.OD0000Example;
import cn.com.bbut.iy.itemmaster.entity.od0000.OD0000Key;
import cn.com.bbut.iy.itemmaster.entity.od0010.OD0010;
import cn.com.bbut.iy.itemmaster.entity.od0010.OD0010Example;
import cn.com.bbut.iy.itemmaster.exception.SystemRuntimeException;
import cn.com.bbut.iy.itemmaster.service.CM9060Service;
import cn.com.bbut.iy.itemmaster.service.RealTimeInventoryQueryService;
import cn.com.bbut.iy.itemmaster.service.ReturnWarehouseService;
import cn.com.bbut.iy.itemmaster.service.SequenceService;
import cn.com.bbut.iy.itemmaster.util.CommonUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author lz
 */
@Service
@Slf4j
public class ReturnWarehouseServiceImpl implements ReturnWarehouseService {
    @Autowired
    private ReturnWarehouseMapper returnWarehouseMapper;

    @Autowired
    private OD0000Mapper od0000Mapper;
    @Autowired
    private OD0010Mapper od0010Mapper;
    @Autowired
    private MA4320Mapper ma4320Mapper;
    @Autowired
    private SequenceService sequenceService;
    @Autowired
    private CM9060Service cm9060Service;
    @Autowired
    private RealTimeInventoryQueryService rtInventoryService;
    @Value("${esUrl.inventoryUrl}")
    private String inventoryUrl;

    @Override
    public List<RWHItemsGridResult> getReturnItems(String orderId, String flag) {
        List<RWHItemsGridResult> list = returnWarehouseMapper.getReturnItems(orderId, flag);
        return list;
    }

    // 20210722
    @Override
    public List<RWHItemsGridResult> getReturnItemsDetail(ReturnWarehouseParamDTO returnParamDTO) {
        return returnWarehouseMapper.getReturnVendorItemsList(returnParamDTO);
    }

    @Override
    public List<RWHItemsGridResult> getDcReturnItemsDetail(ReturnWarehouseParamDTO returnParamDTO) {
        return returnWarehouseMapper.getDcReturnWarehouseItemsList(returnParamDTO);
    }

    @Override
    public List<AutoCompleteDTO> getOrgOrderIdList(ReturnWarehouseParamDTO dto) {
        return returnWarehouseMapper.getOrgOrderIdList(dto);
    }

    @Override
    public GridDataDTO<RWHListResult> getReturnWHQueryList(RWHListParamDTO param) {
        RWHListParam rwhListParam = new Gson().fromJson(param.getSearchJson(), RWHListParam.class);
        rwhListParam.setLimitStart(param.getLimitStart());
        rwhListParam.setLimitEnd(param.getLimitEnd());
        rwhListParam.setOrderByClause(param.getOrderByClause());
        rwhListParam.setStores(param.getStores());
        List<RWHListResult> list = returnWarehouseMapper.selectWHQueryListBy(rwhListParam);
        long count = returnWarehouseMapper.selectWHQueryListCount(rwhListParam);
        return new GridDataDTO<>(list, param.getPage(), count, param.getRows());
    }
    // 打印
    @Override
    public List<RWHListResult> getPrintReturnWHList(RWHListParamDTO param) {
        RWHListParam rwhListParam = new Gson().fromJson(param.getSearchJson(), RWHListParam.class);
        rwhListParam.setLimitStart(param.getLimitStart());
        rwhListParam.setLimitEnd(param.getLimitEnd());
        rwhListParam.setOrderByClause(param.getOrderByClause());
        rwhListParam.setStores(param.getStores());
        return returnWarehouseMapper.selectPrintWHQueryListBy(rwhListParam);
    }

    @Override
    public ReturnHeadResult getRWHHeadQuery(String orderId) {
        return returnWarehouseMapper.selectWHHeadByOrderId(orderId);
    }

    @Override
    public OrderInfoDTO getOrderInfo(String orgOrderId, String returnDiff) {
        //获取业务时间
        String businessDate = cm9060Service.getValByKey("0000");
        OrderInfoDTO orderInfoDTO = returnWarehouseMapper.getOrderInfo(orgOrderId, businessDate);
//        if(orderInfoDTO!=null&&"0".equals(returnDiff)&&StringUtils.isNotBlank(orderInfoDTO.getVendorId())){
//            OrderInfoDTO returnContract = returnWarehouseMapper.getReturnContract(orderInfoDTO.getVendorId(),businessDate);
//            if(returnContract!=null){
//                orderInfoDTO.setThisReturn(returnContract.getThisReturn());
//                orderInfoDTO.setThisRemainReturn(returnContract.getThisRemainReturn());
//                orderInfoDTO.setThisExchangeReturn(returnContract.getThisExchangeReturn());
//            }
//        }
        return orderInfoDTO;
    }

    @Override
    public List<AutoCompleteDTO> getItems(ReturnWarehouseParamDTO dto) {
        return returnWarehouseMapper.getItems(dto);
    }

    @Override
    public List<AutoCompleteDTO> getdirectItems(ReturnWarehouseParamDTO param) {
        return returnWarehouseMapper.getdirectItems(param);
    }
    @Override
    public List<AutoCompleteDTO> getdirectItemsList(ReturnWarehouseParamDTO param) {
        return returnWarehouseMapper.getdirectItemsList(param);
    }


    @Override
    public ReturnWarehouseDetailInfo getdirectItemInfo(ReturnWarehouseParamDTO param) {
        //获取业务时间
        String businessDate = cm9060Service.getValByKey("0000");
        String inEsTime = cm9060Service.getValByKey("1206");
        ReturnWarehouseDetailInfo returnDetailInfo = returnWarehouseMapper.getdirectItemInfo(param,businessDate);
        //拼接url，转义参数
        String connUrl = inventoryUrl + "GetRelTimeInventory/" + param.getStoreCd() + "/"
                + param.getArticleId() + "/*/*/*/*/" + inEsTime+"/*/*";
        RTInventoryQueryDTO rTInventoryQueryDTO = rtInventoryService.getRtInventory(connUrl);
        if (rTInventoryQueryDTO != null) {//设置实时在库量
            returnDetailInfo.setOnHandQty(rTInventoryQueryDTO.getRealtimeQty());
        }else {
            returnDetailInfo.setOnHandQty(BigDecimal.ZERO);
        }
        return returnDetailInfo;

    }

    @Override
    public ReturnWarehouseDetailInfo getItemInfo(ReturnWarehouseParamDTO dto) {
        ReturnWarehouseDetailInfo rwDto = returnWarehouseMapper.getItemInfo(dto);
        if (rwDto != null) {
            String bsDate = cm9060Service.getValByKey("0000");//业务日
            String inEsTime = cm9060Service.getValByKey("1206");
            //拼接url，转义参数
            String connUrl = inventoryUrl + "GetRelTimeInventory/" + rwDto.getStoreCd() + "/"
                    + rwDto.getArticleId() + "/*/*/*/*/" + inEsTime+"/*/*";
            RTInventoryQueryDTO rTInventoryQueryDTO = rtInventoryService.getRtInventory(connUrl);
            if (rTInventoryQueryDTO != null) {//设置实时在库量
                rwDto.setRealtimeQty(rTInventoryQueryDTO.getRealtimeQty());
            }else {
                rwDto.setRealtimeQty(BigDecimal.ZERO);
            }
        }
        return rwDto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OD0000 insertReturnOrder(OD0000 param, String orderDetailJson) {
        String orderId = "";
        String seqName = "";
        String prefix = "";
        //退货区分 0 Vendor 1 DC
        String orderDiff = param.getOrderDifferentiate();
        if ("0".equals(orderDiff)) {
            seqName = "od0000_rrs_id_seq";
            prefix = "RRS";
        } else if ("1".equals(orderDiff)){
            seqName = "od0000_rrd_id_seq";
            prefix = "RRD";
        }

        if (param.getStoreCd().equals("")) {
            orderId = sequenceService.getSequence(seqName, prefix, param.getStoreCd1());
        } else {
            orderId = sequenceService.getSequence(seqName, prefix, param.getStoreCd());
        }
        if (StringUtils.isBlank(orderId)) {
            //获取序列失败
            log.error("获取序列失败 getSequence: {}", seqName);
            RuntimeException e = new RuntimeException("获取序列失败[ "+seqName+" ]");
            throw e;
        }
        if (param.getReturnType().equals("20")) {
            param.setStoreCd(param.getStoreCd());
        }else if (param.getReturnType().equals("10")){
            param.setStoreCd(param.getStoreCd1());
        }
        param.setOrderId(orderId);
        param.setOrderSts("00");//订单待收
        param.setOrderType("02");//单据类型 02退货
        param.setOrderMethod("20");//订货方式（退货单并不关注）
        if (param.getReturnType().equals("10") && StringUtils.isNotBlank(orderDetailJson)) {
            List<OD0010> od0010List1 = new Gson().fromJson(orderDetailJson, new TypeToken<List<OD0010>>() {
            }.getType());
            //新增退货头档
            param.setDeliveryCenterId(param.getDeliveryCenterId1());
            od0000Mapper.insertSelective(param);
            for (int i = 0; i < od0010List1.size(); i++) {
                OD0010 od0010 = od0010List1.get(i);
                od0010.setStoreCd(param.getStoreCd1());
                od0010.setSerialNo(String.valueOf(i + 1));
                od0010.setCreateYmd(param.getCreateYmd());
                od0010.setCreateHms(param.getCreateHms());
                od0010.setCreateUserId(param.getCreateUserId());
                od0010.setUpdateYmd(param.getUpdateYmd());
                od0010.setUpdateHms(param.getUpdateHms());
                od0010.setUpdateUserId(param.getUpdateUserId());
                od0010.setOrderId(orderId);
                od0010.setVendorId(param.getVendorId());
                od0010.setIsFreeItem("0"); // normal item
                od0010Mapper.insertSelective(od0010);
            }
        }
        if (StringUtils.isNotBlank(orderDetailJson) && param.getReturnType().equals("20")) {
            List<OD0010> od0010List = new Gson().fromJson(orderDetailJson, new TypeToken<List<OD0010>>() {
            }.getType());

            if (od0010List != null && od0010List.size() > 0) {
                ReturnWarehouseParamDTO returnWarehouseParamDTO = new ReturnWarehouseParamDTO();
                returnWarehouseParamDTO.setOrderId(param.getOrgOrderId());
                returnWarehouseParamDTO.setOrderDate(param.getOrderDate());
                returnWarehouseParamDTO.setArticleId(od0010List.get(0).getArticleId());
                ReturnWarehouseDetailInfo rwDto = returnWarehouseMapper.getItemInfo(returnWarehouseParamDTO);
                if (rwDto != null) {
                    param.setPurchaseVatCd(rwDto.getOrderTaxTypeCd());//设置税区分
                    param.setTaxRate(rwDto.getOrderTaxRate());//设置税率
                    if (param.getTaxRate() != null && param.getOrderAmt() != null) {//页面orderAmt为不含税
                        BigDecimal orderTax = param.getOrderAmt().multiply(param.getTaxRate());
                        param.setOrderTax(orderTax);//设置税额
                        param.setOrderAmt(param.getOrderAmt().add(orderTax));//设置含税金额
                    }
                }
                //新增退货头档
                od0000Mapper.insertSelective(param);
                //退货明细
                for (int i = 0; i < od0010List.size(); i++) {
                    OD0010 od0010 = od0010List.get(i);
                    od0010.setOrderId(orderId);
                    od0010.setOrderNochargeQty(od0010.getOrderNochargeQty());//搭赠量
                    od0010.setSerialNo(String.valueOf(i + 1));
                    od0010.setCreateYmd(param.getCreateYmd());
                    od0010.setCreateHms(param.getCreateHms());
                    od0010.setCreateUserId(param.getCreateUserId());
                    od0010.setUpdateYmd(param.getUpdateYmd());
                    od0010.setUpdateHms(param.getUpdateHms());
                    od0010.setUpdateUserId(param.getUpdateUserId());
                    od0010.setVendorId(param.getVendorId());
                    od0010.setIsFreeItem(rwDto.getIsFreeItem());
                    // 收货价即退货价
                    od0010.setReceivePrice(od0010.getOrderPrice());
                    if (param.getTaxRate() != null && od0010.getOrderAmtNotax() != null) {
                        BigDecimal orderTax = od0010.getOrderAmtNotax().multiply(param.getTaxRate());
                        od0010.setOrderTax(orderTax);//设置税额
                        od0010.setOrderAmt(od0010.getOrderAmtNotax().add(orderTax));//设置含税金额
                    }
                    od0010Mapper.insertSelective(od0010);
                }
            } else {
                throw new SystemRuntimeException("商品详细转换信息为空！");
            }
        } else if (!StringUtils.isNotBlank(orderDetailJson)) {
            throw new SystemRuntimeException("商品详细信息为空！");
        }
        //添加附件信息
        if (StringUtils.isNotBlank(param.getFileDetailJson())) {
            List<MA4320> ma4320List = new Gson().fromJson(param.getFileDetailJson(), new TypeToken<List<MA4320>>() {
            }.getType());
            for (int i = 0; i < ma4320List.size(); i++) {
                MA4320 ma4320 = ma4320List.get(i);
                ma4320.setInformCd(orderId);
                ma4320.setCreateUserId(param.getCreateUserId());
                ma4320.setCreateYmd(param.getCreateYmd());
                ma4320.setCreateHms(param.getCreateHms());
                ma4320Mapper.insertSelective(ma4320);
            }
        }
        return param;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OD0000 updateReturnOrder(OD0000 param, String orderDetailJson) {
        if (StringUtils.isNotBlank(orderDetailJson)) {
            OD0010Example example = new OD0010Example();
            example.or().andStoreCdEqualTo(param.getStoreCd()).andOrderIdEqualTo(param.getOrderId());
            od0010Mapper.deleteByExample(example);
            List<OD0010> od0010List = new Gson().fromJson(orderDetailJson, new TypeToken<List<OD0010>>() {
            }.getType());
            if (od0010List != null && od0010List.size() > 0) {
                ReturnWarehouseParamDTO returnWarehouseParamDTO = new ReturnWarehouseParamDTO();
                returnWarehouseParamDTO.setOrderId(param.getOrgOrderId());
                returnWarehouseParamDTO.setOrderDate(param.getOrderDate());
                returnWarehouseParamDTO.setArticleId(od0010List.get(0).getArticleId());
                ReturnWarehouseDetailInfo rwDto = returnWarehouseMapper.getItemInfo(returnWarehouseParamDTO);
                if (rwDto != null) {
                    param.setPurchaseVatCd(rwDto.getOrderTaxTypeCd());//设置税区分
                    param.setTaxRate(rwDto.getOrderTaxRate());//设置税率
                    if (param.getTaxRate() != null && param.getOrderAmt() != null) {//页面orderAmt为不含税
                        BigDecimal orderTax = param.getOrderAmt().multiply(param.getTaxRate());
                        param.setOrderTax(orderTax);//设置税额
                        param.setOrderAmt(param.getOrderAmt().add(orderTax));//设置含税金额
                    }
                }
                od0000Mapper.updateByPrimaryKeySelective(param);
                for (int i = 0; i < od0010List.size(); i++) {
                    OD0010 od0010 = od0010List.get(i);
                    od0010.setSerialNo(String.valueOf(i + 1));
                    od0010.setReceiveQty(od0010.getOrderQty());//设置实际退货数量默认为退货数量
                    od0010.setOrderNochargeQty(BigDecimal.ZERO);//搭赠量
                    od0010.setVendorId(param.getVendorId());
                    // 收货价即退货价
                    od0010.setReceivePrice(od0010.getOrderPrice());
                    od0010.setIsFreeItem("0"); // normal item
                    if (param.getTaxRate() != null && od0010.getOrderAmtNotax() != null) {
                        BigDecimal orderTax = od0010.getOrderAmtNotax().multiply(param.getTaxRate());
                        od0010.setOrderTax(orderTax);//设置税额
                        od0010.setOrderAmt(od0010.getOrderAmtNotax().add(orderTax));//设置含税金额
                    }
                    od0010Mapper.insertSelective(od0010);
                }
            } else {
                throw new SystemRuntimeException("商品详细转换信息为空！");
            }
        } else {
            throw new SystemRuntimeException("商品详细信息为空！");
        }
        //添加附件信息
        MA4320Example example = new MA4320Example();
        example.or().andInformCdEqualTo(param.getOrderId()).andFileTypeEqualTo("02");
        ma4320Mapper.deleteByExample(example);
        if (StringUtils.isNotBlank(param.getFileDetailJson())) {
            List<MA4320> ma4320List = new Gson().fromJson(param.getFileDetailJson(), new TypeToken<List<MA4320>>() {
            }.getType());
            for (int i = 0; i < ma4320List.size(); i++) {
                MA4320 ma4320 = ma4320List.get(i);
                ma4320.setInformCd(param.getOrderId());
                ma4320.setCreateUserId(param.getCreateUserId());
                ma4320.setCreateYmd(param.getCreateYmd());
                ma4320.setCreateHms(param.getCreateHms());
                ma4320Mapper.insertSelective(ma4320);
            }
        }
        return param;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateActualReturnQty(String orderDetailJson,String orderRemark) {
        List<OD0010> od0010List = new Gson().fromJson(orderDetailJson, new TypeToken<List<OD0010>>() {}.getType());
        if (od0010List != null && od0010List.size() > 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMdd");
            Date date = new Date();
            String ymd = sdf.format(date);
            OD0000Key od0000Key = new OD0000Key();
            od0000Key.setOrderId(od0010List.get(0).getOrderId());
            od0000Key.setStoreCd(od0010List.get(0).getStoreCd());
            OD0000 returnDto = od0000Mapper.selectByPrimaryKey(od0000Key);
            BigDecimal taxRate = returnDto.getTaxRate();//税率
            BigDecimal receiveAmt = BigDecimal.ZERO;//含税总金额
            BigDecimal receiveTax = BigDecimal.ZERO;//税额
            OD0000 od0000 = new OD0000();
            od0000.setOrderId(od0010List.get(0).getOrderId());
            od0000.setStoreCd(od0010List.get(0).getStoreCd());
            od0000.setOrderRemark(orderRemark);
            //添加实际退货日期
            //od0000.setReceiveDate(ymd);
            //设置退货状态为已收货
            //od0000.setOrderSts("04");
            //设置为已退货确认
            //od0000.setReviewStatus(20);
            //修改实际退货数量
            for (OD0010 od0010 : od0010List) {
                OD0010Example example = new OD0010Example();
                if (od0010.getOrderPrice() != null && od0010.getReceiveQty() != null) {
                    //设置商品未税金额
                    od0010.setReceiveTotalAmtNotax(od0010.getOrderPrice().multiply(od0010.getReceiveQty()));
                    if (taxRate != null) {
                        //设置商品税额
                        od0010.setReceiveTax(od0010.getReceiveTotalAmtNotax().multiply(taxRate));
                        receiveTax = receiveTax.add(od0010.getReceiveTax());
                    }
                    //计算含税金额
                    od0010.setReceiveTotalAmt(od0010.getReceiveTotalAmtNotax());
                    if (od0010.getReceiveTax() != null) {
                        od0010.setReceiveTotalAmt(od0010.getReceiveTotalAmt().add(od0010.getReceiveTax()));
                    }
                    //计算含税金额合计
                    receiveAmt = receiveAmt.add(od0010.getReceiveTotalAmt());
                }
                //设置合计数量 退货数量+搭赠量
                if (od0010.getReceiveQty() == null) {
                    od0010.setReceiveQty(BigDecimal.ZERO);
                }
                if (od0010.getReceiveNochargeQty() == null) {
                    od0010.setReceiveNochargeQty(BigDecimal.ZERO);
                }
                od0010.setReceiveTotalQty(od0010.getReceiveQty().add(od0010.getReceiveNochargeQty()));

                example.or().andOrderIdEqualTo(od0010.getOrderId())
                        .andArticleIdEqualTo(od0010.getArticleId());
                od0010Mapper.updateByExampleSelective(od0010, example);
            }
            od0000.setReceiveAmt(receiveAmt);
            od0000.setReceiveTax(receiveTax);
            od0000Mapper.updateByPrimaryKeySelective(od0000);
            //添加退货确认审核信息
            od0000Mapper.insertOd0005(od0000);
        } else {
            throw new RuntimeException("实际退货商品为空或转换出错！");
        }
        return 1;
    }

    @Override
    public RealtimeStockItem getRealTimeStock(String storeCd, String stockDate, String articleId) {
        return returnWarehouseMapper.getRealTimeStock(storeCd, stockDate, articleId);
    }


    @Override
    public List<AutoCompleteDTO> getDc() {
        return returnWarehouseMapper.getDc();
    }


}
