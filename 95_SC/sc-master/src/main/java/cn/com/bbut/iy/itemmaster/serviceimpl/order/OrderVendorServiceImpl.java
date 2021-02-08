package cn.com.bbut.iy.itemmaster.serviceimpl.order;

import cn.com.bbut.iy.itemmaster.dao.OD0000Mapper;
import cn.com.bbut.iy.itemmaster.dao.OD0010Mapper;
import cn.com.bbut.iy.itemmaster.dao.OrderMapper;
import cn.com.bbut.iy.itemmaster.dao.OrderVendorMapper;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.inventory.InventoryVouchersGridDTO;
import cn.com.bbut.iy.itemmaster.dto.order.OrderItemDetailDTO;
import cn.com.bbut.iy.itemmaster.dto.order.PromotionDTO;
import cn.com.bbut.iy.itemmaster.dto.orderVendor.OrderVendorDetailInfo;
import cn.com.bbut.iy.itemmaster.dto.orderVendor.OrderVendorGridDTO;
import cn.com.bbut.iy.itemmaster.dto.orderVendor.OrderVendorInfo;
import cn.com.bbut.iy.itemmaster.dto.orderVendor.OrderVendorParamDTO;
import cn.com.bbut.iy.itemmaster.entity.base.Ma1000Example;
import cn.com.bbut.iy.itemmaster.entity.ma2000.MA2000;
import cn.com.bbut.iy.itemmaster.entity.od0000.OD0000;
import cn.com.bbut.iy.itemmaster.entity.od0000.OD0000Example;
import cn.com.bbut.iy.itemmaster.entity.od0010.OD0010;
import cn.com.bbut.iy.itemmaster.entity.od0010.OD0010Example;
import cn.com.bbut.iy.itemmaster.exception.SystemRuntimeException;
import cn.com.bbut.iy.itemmaster.service.CM9060Service;
import cn.com.bbut.iy.itemmaster.service.SequenceService;
import cn.com.bbut.iy.itemmaster.service.order.OrderVendorService;
import cn.com.bbut.iy.itemmaster.util.CommonUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderVendorServiceImpl implements OrderVendorService {
    @Autowired
    private OrderVendorMapper orderVendorMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OD0000Mapper od0000Mapper;
    @Autowired
    private OD0010Mapper od0010Mapper;
    @Autowired
    private CM9060Service cm9060Service;
    @Autowired
    private SequenceService sequenceService;

    @Override
    public GridDataDTO<OrderVendorGridDTO> GetOrderList(OrderVendorParamDTO dto) {
        String businessDate = cm9060Service.getValByKey("0000");
        dto.setBusinessDate(businessDate);
//        //审核状态
//        String[] reviewStatus = {"1","5","6","10"};
//        //判断是否为审核状态，不是则为订单状态
//        if(StringUtils.isNotBlank(dto.getReviewStatus())){
//            if(!Arrays.asList(reviewStatus).contains(dto.getReviewStatus())){
//                dto.setOrderSts(dto.getReviewStatus());
//                dto.setReviewStatus(null);
//            }
//        }
        List<OrderVendorGridDTO> list = orderVendorMapper.getOrderList(dto);
//        list.stream().forEach(order -> {
//            //如果为审核通过的订单则显示订单状态 并 订单状态不为空
//            if(StringUtils.isNotBlank(order.getReviewStatus())
//                    &&StringUtils.isNotBlank(order.getOrderSts())
//                    &&"10".equals(order.getReviewStatus())){
//                order.setReviewStatus(dto.getOrderSts());
//            }
//        });
        long count = orderVendorMapper.getOrderListCount(dto);
        return new GridDataDTO<>(list, dto.getPage(), count,
                dto.getRows());
    }

    @Override
    public List<OrderVendorDetailInfo> getItemsByVendor(OrderVendorParamDTO dto) {
        List<OrderVendorDetailInfo> items = orderVendorMapper.getOrderDetailList(dto);
        if(items!=null&&items.size()>0){
            items.stream().forEach(item -> {
                this.setMinOrderQtyAndPromotion(item,0,null);
            });
        }
        return items;
    }

    @Override
    public GridDataDTO<OrderVendorDetailInfo> getOrderDetailListByAuto(OrderVendorParamDTO dto) {
        Integer count = orderVendorMapper.getOrderDetailListByAutoCount(dto);
        GridDataDTO<OrderVendorDetailInfo> data= new GridDataDTO<OrderVendorDetailInfo>();
        if(count < 1){
            return new GridDataDTO<OrderVendorDetailInfo>();
        }
        List<OrderVendorDetailInfo> items = orderVendorMapper.getOrderDetailListByAuto(dto);
        if(items!=null&&items.size()>0){
            //过滤不相同税率的商品 取第一个商品税率
            String purchaseVatCd = items.get(0).getPurchaseVatCd();
            items = items.stream().filter(item ->purchaseVatCd.equals(item.getPurchaseVatCd())).collect(Collectors.toList());
            items.stream().forEach(item -> {
                dto.setArticleId(item.getArticleId());
                this.setMinOrderQtyAndPromotion(item,1,dto);
            });
        }
        return new GridDataDTO<OrderVendorDetailInfo>(items,dto.getPage(),count,dto.getRows());
    }

    @Override
    public MA2000 getVendorInfo(String vendorId, String storeCd) {
        String businessDate =  cm9060Service.getValByKey("0000");
        return orderVendorMapper.getVendorInfo(businessDate,vendorId,storeCd);
    }

    @Override
    public List<AutoCompleteDTO> getItems(OrderVendorParamDTO dto) {
        List<AutoCompleteDTO> list = orderVendorMapper.getItems(dto);
        return list;
    }

    @Override
    public OrderVendorInfo updateAndGetVendorInfoByOrderId(String orderId) {
        OrderVendorInfo orderVendorInfo = orderVendorMapper.getVendorInfoByOrderId(orderId);
        return orderVendorInfo;
    }

    @Override
    public OrderVendorDetailInfo getItemInfo(OrderVendorParamDTO dto) {
        OrderVendorDetailInfo item = orderVendorMapper.getItemInfo(dto);
        return this.setMinOrderQtyAndPromotion(item,1,dto);
    }

    /**
     * 设置最小订货量 和促销例外价格
     * @param item
     * @param flg
     * @param orderVendorParamDTO
     * @return
     */
    private OrderVendorDetailInfo setMinOrderQtyAndPromotion(OrderVendorDetailInfo item,int flg,OrderVendorParamDTO orderVendorParamDTO){
        if(item!=null) {
            //start————————获取订货价格 判断是否促销例外
            if(flg>0){
                OrderItemDetailDTO dto = new OrderItemDetailDTO();
                dto.setStoreCd(orderVendorParamDTO.getStoreCd());
                dto.setArticleId(orderVendorParamDTO.getArticleId());
                dto.setVendorId(orderVendorParamDTO.getVendorId());
                dto.setOrderDate(orderVendorParamDTO.getOrderDate());
                //查询ma8040促销cd
                OrderItemDetailDTO ma8040 = orderMapper.getMa8040(dto);
                PromotionDTO promotionDTO = new PromotionDTO();
                promotionDTO.setStoreCd(dto.getStoreCd());
                promotionDTO.setArticleId(dto.getArticleId());
                promotionDTO.setEffectiveDate(dto.getOrderDate());
                promotionDTO.setVendorId(dto.getVendorId());
                //查询促销例外或增加
                PromotionDTO excludeOrAdd = orderMapper.getPromotionInfo(promotionDTO);
                if(excludeOrAdd!=null){//促销例外
                    if(StringUtils.isNotBlank(ma8040.getOrderPromotionCd())){//by city参加促销
                        if("0".equals(excludeOrAdd.getExceptType())){//促销例外排除
                            item.setOrderPrice(ma8040.getBaseOrderPrice());//设置为原价
                        }else if("1".equals(excludeOrAdd.getExceptType())){//促销增加
                            if(StringUtils.isNotBlank(ma8040.getUpdateYmd())&&StringUtils.isNotBlank(ma8040.getUpdateHms())&&
                                    StringUtils.isNotBlank(excludeOrAdd.getUpdateYmd())&&StringUtils.isNotBlank(excludeOrAdd.getUpdateHms())){
                                Long ma8040UpdateYmdHms = Long.valueOf(ma8040.getUpdateYmd()+ma8040.getUpdateHms());
                                Long ma8170UpdateYmdHms = Long.valueOf(excludeOrAdd.getUpdateYmd()+excludeOrAdd.getUpdateHms());
                                if(ma8170UpdateYmdHms>ma8040UpdateYmdHms){//取最新的促销价
                                    item.setOrderPrice(excludeOrAdd.getOrderPrice());
                                }
                            }
                        }
                    }else{//by city没有参加促销
                        if("1".equals(excludeOrAdd.getExceptType())){//促销增加设置为促销价格
                            item.setOrderPrice(excludeOrAdd.getOrderPrice());
                        }
                    }
                }
            }
            //end——————————————————————————
            BigDecimal orderPirce = item.getOrderPrice();
            BigDecimal minOrderAmtTax = item.getMinOrderAmtTax();
            //最低订货金额不为空
            if (minOrderAmtTax != null) {
                BigDecimal minOrderQty = BigDecimal.ZERO;
                if (orderPirce != null && orderPirce.compareTo(BigDecimal.ZERO) > 0) {
                    minOrderQty = minOrderAmtTax.divide(orderPirce, 2, BigDecimal.ROUND_HALF_UP);//四舍五入保留两位小数
                }
                //判断最低订货数量是否为空
                if (item.getMinOrderQty() != null) {
                    if (item.getMinOrderQty().compareTo(minOrderQty) < 0) {
                        item.setMinOrderQty(minOrderQty);
                    }
                } else {
                    item.setMinOrderQty(minOrderQty);
                }
            }
        }
        return item;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OD0000 insertOrderByVendor(OD0000 param, String orderDetailJson) {
//        Date date = new Date();
        // 设置订单id   storeCd+YYYYMMddHHmmss
//        SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMddHHmmss");
//        String orderId = param.getStoreCd()+sdf.format(date);
        if(StringUtils.isBlank(param.getOrderDate())){
            String businessDate = cm9060Service.getValByKey("0000");
            param.setOrderDate(businessDate);
        }
        // 获取序列号
        String nextval = sequenceService.getSequence("od0000_order_id_v_seq");
        if(StringUtils.isBlank(nextval)){
            //获取序列失败
            log.error("获取序列失败 getSequence: {}", "od0000_order_id_v_seq");
            RuntimeException e = new RuntimeException("获取序列失败[ od0000_order_id_v_seq ]");
            throw e;
        }
        String orderId = "PR-" + nextval + "-" + param.getStoreCd();
        param.setOrderId(orderId);
        // 订单状态
        param.setOrderSts("00");
        param.setShipment("1");
        // 多次收货状态获取
        param.setMultipleFlg(cm9060Service.getValByKey("0219"));
        // 获取订货截止时间
        String expireDate = orderVendorMapper.getExpireDate(param.getStoreCd(),param.getVendorId(),param.getOrderDate());
        param.setExpireDate(expireDate);
        param.setDeliveryDate(expireDate);
        // 保存订货明细
        if(StringUtils.isNotBlank(orderDetailJson)){
            List<OD0010> od0010List = new Gson().fromJson(orderDetailJson, new TypeToken<List<OD0010>>(){}.getType());
            if(od0010List!=null&&od0010List.size()>0){
                OrderVendorParamDTO orderVendorParamDTO = new OrderVendorParamDTO();
                orderVendorParamDTO.setStoreCd(param.getStoreCd());
                orderVendorParamDTO.setOrderDate(param.getOrderDate());
                orderVendorParamDTO.setVendorId(param.getVendorId());
                orderVendorParamDTO.setArticleId(od0010List.get(0).getArticleId());
                OrderVendorDetailInfo orderVendorDetailInfo = orderVendorMapper.getItemInfo(orderVendorParamDTO);
                if(orderVendorDetailInfo!=null){
                    param.setPurchaseVatCd(orderVendorDetailInfo.getPurchaseVatCd());//设置税区分
                    param.setTaxRate(orderVendorDetailInfo.getTaxRate());//设置税率
                    if(param.getTaxRate()!=null&&param.getOrderAmt()!=null){//页面orderAmt为不含税
                        BigDecimal orderTax = param.getOrderAmt().multiply(param.getTaxRate());
                        param.setOrderTax(orderTax);//设置税额
                        param.setOrderAmt(param.getOrderAmt().add(orderTax));//设置含税金额
                    }
                }
                // 保存订货头档
                od0000Mapper.insertSelective(param);
                for (int i = 0; i < od0010List.size(); i++) {
                    OD0010 od0010 = od0010List.get(i);
                    od0010.setOrderId(orderId);
                    od0010.setSerialNo(String.valueOf(i+1));
                    od0010.setCreateYmd(param.getCreateYmd());
                    od0010.setCreateHms(param.getCreateHms());
                    od0010.setCreateUserId(param.getCreateUserId());
                    od0010.setUpdateYmd(param.getUpdateYmd());
                    od0010.setUpdateHms(param.getUpdateHms());
                    od0010.setUpdateUserId(param.getUpdateUserId());
                    if(param.getTaxRate()!=null&&od0010.getOrderAmtNotax()!=null){
                        BigDecimal orderTax = od0010.getOrderAmtNotax().multiply(param.getTaxRate());
                        od0010.setOrderTax(orderTax);//设置税额
                        od0010.setOrderAmt(od0010.getOrderAmtNotax().add(orderTax));//设置含税金额
                    }
                    // 收货价即订货价
                    od0010.setReceivePrice(od0010.getOrderPrice());
                    od0010Mapper.insertSelective(od0010);
                }
            }else{
                throw new SystemRuntimeException("商品详细转换信息为空！");
            }
        }else{
            throw new SystemRuntimeException("商品详细信息为空！");
        }
        return param;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OD0000 updateOrderByVendor(OD0000 param, String orderDetailJson) {
        if(StringUtils.isNotBlank(orderDetailJson)){
            OD0010Example example = new OD0010Example();
            example.or().andStoreCdEqualTo(param.getStoreCd()).andOrderIdEqualTo(param.getOrderId());
            od0010Mapper.deleteByExample(example);
            List<OD0010> od0010List = new Gson().fromJson(orderDetailJson, new TypeToken<List<OD0010>>(){}.getType());
            if(od0010List!=null&&od0010List.size()>0){
                OrderVendorParamDTO orderVendorParamDTO = new OrderVendorParamDTO();
                orderVendorParamDTO.setStoreCd(param.getStoreCd());
                orderVendorParamDTO.setOrderDate(param.getOrderDate());
                orderVendorParamDTO.setVendorId(param.getVendorId());
                orderVendorParamDTO.setArticleId(od0010List.get(0).getArticleId());
                OrderVendorDetailInfo orderVendorDetailInfo = orderVendorMapper.getItemInfo(orderVendorParamDTO);
                if(orderVendorDetailInfo!=null){
                    param.setPurchaseVatCd(orderVendorDetailInfo.getPurchaseVatCd());//设置税区分
                    param.setTaxRate(orderVendorDetailInfo.getTaxRate());//设置税率
                    if(param.getTaxRate()!=null&&param.getOrderAmt()!=null){
                        BigDecimal orderTax = param.getOrderAmt().multiply(param.getTaxRate());
                        param.setOrderTax(orderTax);//设置税额
                        param.setOrderAmt(param.getOrderAmt().add(orderTax));//设置含税金额
                    }
                }
                //修改订货头档
                od0000Mapper.updateByPrimaryKeySelective(param);
                for (int i = 0; i < od0010List.size(); i++) {
                    OD0010 od0010 = od0010List.get(i);
                    od0010.setSerialNo(String.valueOf(i+1));
                    od0010.setUpdateYmd(param.getUpdateYmd());
                    od0010.setUpdateHms(param.getUpdateHms());
                    od0010.setUpdateUserId(param.getUpdateUserId());
                    if(param.getTaxRate()!=null&&od0010.getOrderAmtNotax()!=null){
                        BigDecimal orderTax = od0010.getOrderAmtNotax().multiply(param.getTaxRate());
                        od0010.setOrderTax(orderTax);//设置税额
                        od0010.setOrderAmt(od0010.getOrderAmtNotax().add(orderTax));//设置含税金额
                    }
                    // 收货价即订货价
                    od0010.setReceivePrice(od0010.getOrderPrice());
                    od0010Mapper.insertSelective(od0010);
                }
            }else{
                throw new SystemRuntimeException("商品详细转换信息为空！");
            }
        }else{
            throw new SystemRuntimeException("商品详细信息为空！");
        }
        return param;
    }

    @Override
    public int updateDiscount(String recordCd,String auditDate) {
        OD0010 od0010 = orderVendorMapper.getStoreDiscount(recordCd,auditDate);
        int flg = 0;
        //判断是否是新店，并且没有参与过订货
        if(od0010!=null&&od0010.getOrderQty().compareTo(BigDecimal.ZERO)==0){
            //获取折扣率
            String discountRate = orderVendorMapper.getDiscountRate(od0010.getVendorId(),auditDate);
            if(StringUtils.isNotBlank(discountRate)){
                //更新折扣金额
                flg = orderVendorMapper.updateOrderDiscount(recordCd,new BigDecimal(discountRate).stripTrailingZeros());
            }
        }
        return flg;
    }

    @Override
    public Integer checkUploadOrder(String orderId) {
        return orderVendorMapper.checkUploadOrder(orderId);
    }
}
