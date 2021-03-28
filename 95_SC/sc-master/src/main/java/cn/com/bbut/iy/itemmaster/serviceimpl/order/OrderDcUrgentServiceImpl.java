package cn.com.bbut.iy.itemmaster.serviceimpl.order;

import cn.com.bbut.iy.itemmaster.dao.*;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.order.OrderDcInfo;
import cn.com.bbut.iy.itemmaster.dto.order.OrderDcParamDTO;
import cn.com.bbut.iy.itemmaster.dto.order.OrderItemDetailDTO;
import cn.com.bbut.iy.itemmaster.dto.order.PromotionDTO;
import cn.com.bbut.iy.itemmaster.dto.orderVendor.OrderVendorDetailInfo;
import cn.com.bbut.iy.itemmaster.dto.orderVendor.OrderVendorGridDTO;
import cn.com.bbut.iy.itemmaster.dto.orderVendor.OrderVendorInfo;
import cn.com.bbut.iy.itemmaster.dto.orderVendor.OrderVendorParamDTO;
import cn.com.bbut.iy.itemmaster.entity.ma2000.MA2000;
import cn.com.bbut.iy.itemmaster.entity.od0000.OD0000;
import cn.com.bbut.iy.itemmaster.entity.od0010.OD0010;
import cn.com.bbut.iy.itemmaster.entity.od0010.OD0010Example;
import cn.com.bbut.iy.itemmaster.exception.SystemRuntimeException;
import cn.com.bbut.iy.itemmaster.service.CM9060Service;
import cn.com.bbut.iy.itemmaster.service.SequenceService;
import cn.com.bbut.iy.itemmaster.service.order.OrderDcUrgentService;
import cn.com.bbut.iy.itemmaster.service.order.OrderVendorService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderDcUrgentServiceImpl implements OrderDcUrgentService {
    @Autowired
    private OrderDcUrgentMapper orderDcUrgentMapper;
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
    /**
     * 订单流水号补0
     */
    private static final String STR_FORMAT = "0000";

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
        List<OrderVendorGridDTO> list = orderDcUrgentMapper.getOrderList(dto);
//        list.stream().forEach(order -> {
//            //如果为审核通过的订单则显示订单状态 并 订单状态不为空
//            if(StringUtils.isNotBlank(order.getReviewStatus())
//                    &&StringUtils.isNotBlank(order.getOrderSts())
//                    &&"10".equals(order.getReviewStatus())){
//                order.setReviewStatus(dto.getOrderSts());
//            }
//        });
        long count = orderDcUrgentMapper.getOrderListCount(dto);
        return new GridDataDTO<>(list, dto.getPage(), count,
                dto.getRows());
    }

    @Override
    public List<OrderVendorDetailInfo> getItemsByDc(OrderDcParamDTO dto) {
        List<OrderVendorDetailInfo> items = orderDcUrgentMapper.getOrderDetailList(dto);
        if(items!=null&&items.size()>0){
            items.stream().forEach(item -> {
                this.setMinOrderQtyAndPromotion(item,0,null);
            });
        }
        return items;
    }

    @Override
    public GridDataDTO<OrderVendorDetailInfo> getOrderDetailListByAuto(OrderVendorParamDTO dto) {
        Integer count = orderDcUrgentMapper.getOrderDetailListByAutoCount(dto);
        GridDataDTO<OrderVendorDetailInfo> data= new GridDataDTO<OrderVendorDetailInfo>();
        if(count < 1){
            return new GridDataDTO<OrderVendorDetailInfo>();
        }
        List<OrderVendorDetailInfo> items = orderDcUrgentMapper.getOrderDetailListByAuto(dto);
        if(items!=null&&items.size()>0){
            //过滤不相同税率的商品 取第一个商品税率
            //String purchaseVatCd = items.get(0).getPurchaseVatCd();
            //items = items.stream().filter(item ->purchaseVatCd.equals(item.getPurchaseVatCd())).collect(Collectors.toList());
            items.stream().forEach(item -> {
                dto.setArticleId(item.getArticleId());
                this.setMinOrderQtyAndPromotion(item,0,dto);
            });
        }
        return new GridDataDTO<>(items,dto.getPage(),count,dto.getRows());
    }

    @Override
    public List<AutoCompleteDTO> getOrderTaxRate(String orderDate, String storeCd) {
        return orderDcUrgentMapper.getOrderTaxRate(orderDate,storeCd);
    }

    @Override
    public List<AutoCompleteDTO> getItems(OrderVendorParamDTO dto) {
        List<AutoCompleteDTO> list = orderDcUrgentMapper.getItems(dto);
        return list;
    }

    @Override
    public OrderDcInfo getOrderDcInfo(String orderId) {
        OrderDcInfo orderDcInfo = orderDcUrgentMapper.getDcInfoByOrderId(orderId);
        return orderDcInfo;
    }

    @Override
    public OrderVendorDetailInfo getItemInfo(OrderVendorParamDTO dto) {
        OrderVendorDetailInfo item = orderDcUrgentMapper.getItemInfo(dto);
        return this.setMinOrderQtyAndPromotion(item,0,dto);
    }

    @Override
    public List<OrderVendorDetailInfo> getFreeItemInfo(OrderItemDetailDTO dto) {
        //查询ma8040促销cd
        OrderItemDetailDTO ma8040 = orderMapper.getMa8040(dto);
        PromotionDTO promotionDTO = new PromotionDTO();
        promotionDTO.setStoreCd(dto.getStoreCd());
        promotionDTO.setArticleId(dto.getArticleId());
        promotionDTO.setEffectiveDate(dto.getOrderDate());
        promotionDTO.setVendorId(dto.getVendorId());
        //查询促销例外或增加
        PromotionDTO excludeOrAdd = orderMapper.getPromotionInfo(promotionDTO);
        List<OrderVendorDetailInfo> freeitems = new ArrayList<>();
        if(excludeOrAdd!=null){//促销例外
            if(StringUtils.isNotBlank(ma8040.getOrderPromotionCd())){//by city参加促销
                String promotionCd = ma8040.getOrderPromotionCd();
                if("0".equals(excludeOrAdd.getExceptType())){//促销例外排除
                }else if("1".equals(excludeOrAdd.getExceptType())){//促销增加
                    if(StringUtils.isNotBlank(ma8040.getUpdateYmd())&&StringUtils.isNotBlank(ma8040.getUpdateHms())&&
                            StringUtils.isNotBlank(excludeOrAdd.getUpdateYmd())&&StringUtils.isNotBlank(excludeOrAdd.getUpdateHms())){
                        Long ma8040UpdateYmdHms = Long.valueOf(ma8040.getUpdateYmd()+ma8040.getUpdateHms());
                        Long ma8170UpdateYmdHms = Long.valueOf(excludeOrAdd.getUpdateYmd()+excludeOrAdd.getUpdateHms());
                        if(ma8170UpdateYmdHms>ma8040UpdateYmdHms){//取最新的促销价
                            promotionCd = excludeOrAdd.getPromotionCd();
                        }
                    }
                    //修改搭赠量
                    freeitems = this.getPresentItems(promotionCd,dto);
                }else{
                    //修改搭赠量
                    freeitems = this.getPresentItems(promotionCd,dto);
                }
            }else{//by city没有参加促销
                if("1".equals(excludeOrAdd.getExceptType())){//促销增加设置为促销价格
                    //修改搭赠量
                    freeitems = this.getPresentItems(excludeOrAdd.getPromotionCd(),dto);
                }
            }
        }else{
            if(StringUtils.isNotBlank(ma8040.getOrderPromotionCd())){//参加促销
                //修改搭赠量
                freeitems = this.getPresentItems(ma8040.getOrderPromotionCd(),dto);
            }
        }
        return freeitems;
    }

    /**
     * 修改搭赠量
     * @param promotionCd 促销cd
     * @param dto 订货商品
     */
    private List<OrderVendorDetailInfo> getPresentItems(String promotionCd,OrderItemDetailDTO dto){
        String presentFlg = "0";//订货搭赠量状态
        //搭赠量
        BigDecimal conditionQty = orderMapper.getConditionQty(promotionCd,dto.getArticleId());
        if("2".equals(dto.getOrderFlg())){//有历史订货
            if(conditionQty!=null){ //有搭赠量
                if(dto.getOrderQty().compareTo(dto.getOldOrderQty())>0){//新订货大于过去订货
                    if(dto.getOrderQty().compareTo(conditionQty)>=0&&
                            dto.getOldOrderQty().compareTo(conditionQty)<0){//旧订货没有搭赠量 新订货满足搭赠量
                        presentFlg = "1";//新增搭赠量
                    }
                }else if(dto.getOrderQty().compareTo(dto.getOldOrderQty())<0){//新订货小于过去订货
                    if(dto.getOldOrderQty().compareTo(conditionQty)>=0&&
                            dto.getOrderQty().compareTo(conditionQty)<0){//旧订货满足搭赠量 新订货不满足搭赠量
                        presentFlg = "2";//减去搭赠量
                    }
                }
            }
        }else{//没有历史订货
            if(conditionQty!=null && dto.getOrderQty().compareTo(conditionQty)>=0){
                presentFlg = "1";//新增搭赠量
            }
        }
        List<PromotionDTO> articles = orderMapper.getPresentArticle(promotionCd,dto.getArticleId());
        List<OrderVendorDetailInfo> freeItems = new ArrayList<>();
        if("1".equals(presentFlg)){//新增搭赠量
            for (PromotionDTO article : articles) {
                if(article.getPresentQty()!=null&&article.getPresentQty().compareTo(BigDecimal.ZERO)>0) {
                    //获取搭赠商品信息
                    OrderVendorDetailInfo freeItemInfo = orderDcUrgentMapper.getItemInfo(
                    OrderVendorParamDTO.builder()
                            .articleId(article.getArticleId())
                            .storeCd(dto.getStoreCd())
                            .orderDate(dto.getOrderDate())
                    .build());
                    freeItemInfo.setOrderNochargeQty(article.getPresentQty());
                    freeItems.add(freeItemInfo);
                }
            }
        }else if("2".equals(presentFlg)){//减去搭赠量
            for (PromotionDTO article : articles) {
                if(article.getPresentQty()!=null&&article.getPresentQty().compareTo(BigDecimal.ZERO)>0) {
                    //获取搭赠商品信息
                    OrderVendorDetailInfo freeItemInfo = orderDcUrgentMapper.getItemInfo(
                            OrderVendorParamDTO.builder()
                                    .articleId(article.getArticleId())
                                    .storeCd(dto.getStoreCd())
                                    .orderDate(dto.getOrderDate())
                                    .build());
                    freeItemInfo.setOrderNochargeQty(article.getPresentQty().negate());//减去搭赠量
                    freeItems.add(freeItemInfo);
                }
            }
        }
        return freeItems;
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
    public OD0000 insertOrderByDc(OD0000 param, String orderDetailJson) {
        if(StringUtils.isBlank(param.getOrderDate())){
            String businessDate = cm9060Service.getValByKey("0000");
            param.setOrderDate(businessDate);
        }
        // 获取序列号 设置订单id
        String nextval = sequenceService.getSequence("od0000_order_id_v_seq");
        //DecimalFormat df = new DecimalFormat(STR_FORMAT);
        //nextval = df.format(Integer.parseInt(nextval));
        if(StringUtils.isBlank(nextval)){
            //获取序列失败
            log.error("获取序列失败 getSequence: {}", "od0000_order_id_v_seq");
            RuntimeException e = new RuntimeException("获取序列失败[ od0000_order_id_v_seq ]");
            throw e;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String date = sdf.format(new Date()).substring(2);
        String orderId = date + param.getStoreCd().substring(2) + ".EO" + nextval ;
        param.setOrderId(orderId);
        // 订单状态
        param.setOrderSts("00");
        param.setShipment("1");
        // 多次收货状态获取
        //param.setMultipleFlg(cm9060Service.getValByKey("0219"));
        // 获取订货截止时间
        String expireDate = this.getExpireDate(param.getStoreCd(),param.getOrderDate());
        param.setExpireDate(expireDate);
        param.setDeliveryDate(expireDate);
        BigDecimal orderAmt = BigDecimal.ZERO;
        // 保存订货明细
        if(StringUtils.isNotBlank(orderDetailJson)){
            List<OD0010> od0010List = new Gson().fromJson(orderDetailJson, new TypeToken<List<OD0010>>(){}.getType());
            if(od0010List!=null&&od0010List.size()>0){
                BigDecimal orderTax = BigDecimal.ZERO;//总税额
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
                    if(od0010.getTaxRate()!=null&&od0010.getOrderAmtNotax()!=null){
                        BigDecimal articleOrderTax = od0010.getOrderAmtNotax().multiply(od0010.getTaxRate());//商品税额
                        orderTax = orderTax.add(articleOrderTax);//合计总税额
                        od0010.setOrderTax(articleOrderTax);//设置税额
                        od0010.setOrderAmt(od0010.getOrderAmtNotax().add(articleOrderTax));//设置含税金额
                    }
                    orderAmt = orderAmt.add(od0010.getOrderAmt());
                    // 收货价即订货价
                    od0010.setReceivePrice(od0010.getOrderPrice());
                    od0010Mapper.insertSelective(od0010);
                }
                param.setOrderAmt(orderAmt); // 计算含税金额
                param.setOrderTax(orderTax);//设置税额
                // 保存订货头档
                od0000Mapper.insertSelective(param);
            }else{
                throw new SystemRuntimeException("商品详细转换信息为空！");
            }
        }else{
            throw new SystemRuntimeException("商品详细信息为空！");
        }
        return param;
    }

    /**
     * 获取订单失效时间
     * @param storeCd
     * @param orderDate
     * @return
     */
    private String getExpireDate(String storeCd,String orderDate){
        String expireDate = orderDate;
        String regionCd = orderDcUrgentMapper.getStoreRegion(storeCd);
        if(StringUtils.isNotBlank(regionCd)){
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
            try {
                Date orderDate1 = sdf1.parse(orderDate);
                Calendar cl = Calendar.getInstance();
                cl.setTime(orderDate1);
                if("000002".equals(regionCd)){//South region
                    //失效日7天
                    cl.add(Calendar.DATE,7);
                }else{//North region
                    //14天失效
                    cl.add(Calendar.DATE,14);
                }
                expireDate = sdf1.format(cl.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return expireDate;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OD0000 updateOrderByDc(OD0000 param, String orderDetailJson) {
        if(StringUtils.isNotBlank(orderDetailJson)){
            OD0010Example example = new OD0010Example();
            example.or().andStoreCdEqualTo(param.getStoreCd()).andOrderIdEqualTo(param.getOrderId());
            od0010Mapper.deleteByExample(example);
            List<OD0010> od0010List = new Gson().fromJson(orderDetailJson, new TypeToken<List<OD0010>>(){}.getType());
            if(od0010List!=null&&od0010List.size()>0){
                BigDecimal orderTax = BigDecimal.ZERO;//总税额
                for (int i = 0; i < od0010List.size(); i++) {
                    OD0010 od0010 = od0010List.get(i);
                    od0010.setSerialNo(String.valueOf(i+1));
                    od0010.setCreateYmd(param.getCreateYmd());
                    od0010.setCreateHms(param.getCreateHms());
                    od0010.setCreateUserId(param.getCreateUserId());
                    od0010.setUpdateYmd(param.getUpdateYmd());
                    od0010.setUpdateHms(param.getUpdateHms());
                    od0010.setUpdateUserId(param.getUpdateUserId());
                    if(od0010.getTaxRate()!=null&&od0010.getOrderAmtNotax()!=null){
                        BigDecimal articleOrderTax = od0010.getOrderAmtNotax().multiply(od0010.getTaxRate());//商品税额
                        orderTax = orderTax.add(articleOrderTax);//合计总税额
                        od0010.setOrderTax(articleOrderTax);//设置税额
                        od0010.setOrderAmt(od0010.getOrderAmtNotax().add(articleOrderTax));//设置含税金额
                    }
                    // 收货价即订货价
                    od0010.setReceivePrice(od0010.getOrderPrice());
                    od0010Mapper.insertSelective(od0010);
                }
                param.setOrderTax(orderTax);//设置税额
                param.setOrderAmt(param.getOrderAmtNotax().add(orderTax));//设置含税金额
                //修改订货头档
                od0000Mapper.updateByPrimaryKeySelective(param);
            }else{
                throw new SystemRuntimeException("商品详细转换信息为空！");
            }
        }else{
            throw new SystemRuntimeException("商品详细信息为空！");
        }
        return param;
    }

    /**
     * 针对向供应商第一次订货
     * @param recordCd 订单cd
     * @param auditDate 审核通过时间
     * @return
     */
    @Override
    public int updateDiscount(String recordCd,String auditDate) {
        OD0010 od0010 = orderDcUrgentMapper.getStoreDiscount(recordCd,auditDate);
        int flg = 0;
        //判断是否是新店，并且没有参与过订货
        if(od0010!=null&&od0010.getOrderQty().compareTo(BigDecimal.ZERO)==0){
            //获取折扣率
            String discountRate = orderDcUrgentMapper.getDiscountRate(od0010.getVendorId(),auditDate);
            if(StringUtils.isNotBlank(discountRate)){
                //更新折扣金额
                flg = orderDcUrgentMapper.updateOrderDiscount(recordCd,new BigDecimal(discountRate).stripTrailingZeros());
            }
        }
        return flg;
    }

    @Override
    public Integer checkUploadOrder(String orderId) {
        return orderDcUrgentMapper.checkUploadOrder(orderId);
    }

    @Override
    public OD0000 getDcInfo(String storeCd) {
        return orderDcUrgentMapper.getDcInfo(storeCd);
    }

    @Override
    public BigDecimal getInventoryQty(String storeCd, String articleId) {
        return orderDcUrgentMapper.getInvertoryQty(storeCd,articleId);
    }

    @Override
    public Collection<String> getDcStores() {
        return orderDcUrgentMapper.getDcStores();
    }
}
