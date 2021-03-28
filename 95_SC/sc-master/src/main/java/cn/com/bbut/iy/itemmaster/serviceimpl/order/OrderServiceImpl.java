package cn.com.bbut.iy.itemmaster.serviceimpl.order;

import cn.com.bbut.iy.itemmaster.dao.*;
import cn.com.bbut.iy.itemmaster.dto.AjaxResultDto;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.order.*;
import cn.com.bbut.iy.itemmaster.dto.pi0100c.StocktakeItemDTOC;
import cn.com.bbut.iy.itemmaster.dto.rtInventory.RTInventoryQueryDTO;
import cn.com.bbut.iy.itemmaster.entity.MA8250;
import cn.com.bbut.iy.itemmaster.entity.base.Cm9060;
import cn.com.bbut.iy.itemmaster.entity.base.Ma1000;
import cn.com.bbut.iy.itemmaster.entity.base.Ma1000Example;
import cn.com.bbut.iy.itemmaster.entity.ma8230.Ma8230;
import cn.com.bbut.iy.itemmaster.entity.ma8240.Ma8240;
import cn.com.bbut.iy.itemmaster.entity.ma8240.Ma8240Key;
import cn.com.bbut.iy.itemmaster.service.CM9060Service;
import cn.com.bbut.iy.itemmaster.service.RealTimeInventoryQueryService;
import cn.com.bbut.iy.itemmaster.service.order.OrderService;
import cn.com.bbut.iy.itemmaster.util.CommonUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private Ma1000Mapper ma1000Mapper;
    @Autowired
    private Cm9060Mapper cm9060Mapper;
    @Autowired
    private Ma8230Mapper ma8230Mapper;
    @Autowired
    private MA8250Mapper ma8250Mapper;
    @Autowired
    private Ma8240Mapper ma8240Mapper;
    @Autowired
    private CM9060Service cm9060Service;
    @Value("${esUrl.inventoryUrl}")
    private String inventoryUrl;
    @Autowired
    private RealTimeInventoryQueryService rtInventoryService;

    /**
     * 获取订货一览
     *
     * @param param
     * @return
     */
    @Override
    public GridDataDTO<OrderItemDTO> GetOrderList(OrderItemParamDTO param) {
        if ("1".equals(param.getOrderType())) {
            List<OrderItemDTO> list = orderMapper.getOrderListByDpt(param);
            long count = orderMapper.getOrderListByDptCount(param);
            return new GridDataDTO<>(list, param.getPage(), count,
                    param.getRows());
        } else if ("2".equals(param.getOrderType())) {
            List<OrderItemDTO> list = orderMapper.getOrderListByShelf(param);
            long count = orderMapper.getOrderListByShelfCount(param);
            return new GridDataDTO<>(list, param.getPage(), count,
                    param.getRows());
        }
        return null;
    }

    /**
     * 获取订货详情信息
     *
     * @param dto
     * @return
     */
    @Override
    public GridDataDTO<OrderItemDetailDTO> GetOrderDetailList(OrderItemParamDTO dto) {
        if (StringUtils.isNotBlank(dto.getOrderType())) {

            if ("1".equals(dto.getOrderType())) {
                dto.setShelf(null);
                dto.setSubShelf(null);
                List<String> articles = new ArrayList<>();
                List<OrderItemDetailDTO> _list = orderMapper.getOrderListDetailByDpt(dto);
                for(OrderItemDetailDTO item:_list){
                    articles.add(item.getArticleId());
                }
                String inEsTime = cm9060Service.getValByKey("1206");
                //拼接url，转义参数
                String connUrl = inventoryUrl + "GetRelTimeInventory/" + dto.getStoreCd() + "/"
                        + "*" + "/*/*/*/*/" + inEsTime+"/*/*";
                List<RTInventoryQueryDTO> rTdTOList = rtInventoryService.getStockList(articles,connUrl);

                if(rTdTOList.size()>0){
                    for(RTInventoryQueryDTO rtDto:rTdTOList){
                        for(OrderItemDetailDTO orderDto:_list){
                            if(rtDto.getItemCode().equals(orderDto.getArticleId())){
                                orderDto.setRealtimeStock(rtDto.getRealtimeQty().toString()); // 实时库存
                            }
                        }
                    }
                }
                long count = orderMapper.getOrderListDetailByDptCount(dto);

                GridDataDTO<OrderItemDetailDTO> grid = new GridDataDTO<>(_list,
                        dto.getPage(), count, dto.getRows());
                return grid;
            } else if ("2".equals(dto.getOrderType())) {
                dto.setDepCd(null);
                dto.setPmaCd(null);
                dto.setCategoryCd(null);
                dto.setSubCategoryCd(null);
                List<String> articles = new ArrayList<>();
                List<OrderItemDetailDTO> _list = orderMapper.getOrderListDetail(dto);
                for(OrderItemDetailDTO item:_list){
                    articles.add(item.getArticleId());
                }
                String inEsTime = cm9060Service.getValByKey("1206");
                //拼接url，转义参数
                String connUrl = inventoryUrl + "GetRelTimeInventory/" + dto.getStoreCd() + "/"
                        + "*" + "/*/*/*/*/" + inEsTime+"/*/*";
                List<RTInventoryQueryDTO> rTdTOList = rtInventoryService.getStockList(articles,connUrl);

                if(rTdTOList.size()>0){
                    for(RTInventoryQueryDTO rtDto:rTdTOList){
                        for(OrderItemDetailDTO orderDto:_list){
                            if(rtDto.getItemCode().equals(orderDto.getArticleId())){
                                orderDto.setRealtimeStock(rtDto.getRealtimeQty().toString()); // 实时库存
                            }
                        }
                    }
                }

                long count = orderMapper.getOrderListDetailCount(dto);

                GridDataDTO<OrderItemDetailDTO> grid = new GridDataDTO<>(_list,
                        dto.getPage(), count, dto.getRows());
                return grid;
            }

        }
        return new GridDataDTO<>();
    }

    @Override
    public Ma1000 getStoreInfoByCode(String code) {
        Cm9060 cm9060 = cm9060Mapper.selectByPrimaryKey("0000");
        Ma1000Example example = new Ma1000Example();
        example.setDistinct(true);
        example.or().andStoreCdEqualTo(code)
                .andEffectiveStartDateLessThanOrEqualTo(cm9060.getSpValue())
                .andEffectiveEndDateGreaterThanOrEqualTo(cm9060.getSpValue());
        List<Ma1000> list = ma1000Mapper.selectByExample(example);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public OrderDetailInfo getOrderItemDetail(OrderItemParamDTO dto) {
        OrderDetailInfo orderDetailInfo = orderMapper.getOrderItemDetail(dto);
        if (orderDetailInfo != null) {
            BigDecimal orderPirce = orderDetailInfo.getOrderPrice();
            BigDecimal minOrderAmtTax = orderDetailInfo.getMinOrderAmtTax();
            //最低订货金额不为空
            if (minOrderAmtTax != null) {
                BigDecimal minOrderQty = BigDecimal.ZERO;
                if (orderPirce != null && orderPirce.compareTo(BigDecimal.ZERO) > 0) {
                    minOrderQty = minOrderAmtTax.divide(orderPirce, 2, BigDecimal.ROUND_HALF_UP);//四舍五入保留两位小数
                }
                //判断最低订货数量是否为空
                if (orderDetailInfo.getMinOrderQty() != null) {
                    if (orderDetailInfo.getMinOrderQty().compareTo(minOrderQty) < 0) {
                        orderDetailInfo.setMinOrderQty(minOrderQty);
                    }
                } else {
                    orderDetailInfo.setMinOrderQty(minOrderQty);
                }
            }
        }

        return orderDetailInfo;
    }

    @Override
    @Transactional
    public int updateOrder(OrderItemDetailDTO dto) {
        List<OrderItemDetailDTO> list = orderMapper.selectOrder(dto);
        dto.setShipmentCd("1");
        int flag;
        //start————————获取订货价格 判断是否促销例外
        //查询ma8040促销cd
        /*OrderItemDetailDTO ma8040 = orderMapper.getMa8040(dto);
        PromotionDTO promotionDTO = new PromotionDTO();
        promotionDTO.setStoreCd(dto.getStoreCd());
        promotionDTO.setArticleId(dto.getArticleId());
        promotionDTO.setEffectiveDate(dto.getOrderDate());
        promotionDTO.setVendorId(dto.getVendorId());
        //查询促销例外或增加
        PromotionDTO excludeOrAdd = orderMapper.getPromotionInfo(promotionDTO);
        if(excludeOrAdd!=null){//促销例外
            if(StringUtils.isNotBlank(ma8040.getOrderPromotionCd())){//by city参加促销
                String promotionCd = ma8040.getOrderPromotionCd();
                if("0".equals(excludeOrAdd.getExceptType())){//促销例外排除
                    dto.setOrderPrice(ma8040.getBaseOrderPrice());//设置为原价
                }else if("1".equals(excludeOrAdd.getExceptType())){//促销增加
                    if(StringUtils.isNotBlank(ma8040.getUpdateYmd())&&StringUtils.isNotBlank(ma8040.getUpdateHms())&&
                            StringUtils.isNotBlank(excludeOrAdd.getUpdateYmd())&&StringUtils.isNotBlank(excludeOrAdd.getUpdateHms())){
                        Long ma8040UpdateYmdHms = Long.valueOf(ma8040.getUpdateYmd()+ma8040.getUpdateHms());
                        Long ma8170UpdateYmdHms = Long.valueOf(excludeOrAdd.getUpdateYmd()+excludeOrAdd.getUpdateHms());
                        if(ma8170UpdateYmdHms>ma8040UpdateYmdHms){//取最新的促销价
                            dto.setOrderPrice(excludeOrAdd.getOrderPrice());
                            promotionCd = excludeOrAdd.getPromotionCd();
                        }
                    }
                    //修改搭赠量
                    this.updatePresent(list,promotionCd,dto);
                }else{
                    //修改搭赠量
                    this.updatePresent(list,promotionCd,dto);
                }
            }else{//by city没有参加促销
                if("1".equals(excludeOrAdd.getExceptType())){//促销增加设置为促销价格
                    dto.setOrderPrice(excludeOrAdd.getOrderPrice());
                    //修改搭赠量
                    this.updatePresent(list,excludeOrAdd.getPromotionCd(),dto);
                }
            }
        }else{
            if(StringUtils.isNotBlank(ma8040.getOrderPromotionCd())){//参加促销
                //修改搭赠量
                this.updatePresent(list,ma8040.getOrderPromotionCd(),dto);
            }
        }*/
        //end——————————————————————————
        if (list != null && list.size() > 0) {
            if(dto.getOrderQty().equals(BigDecimal.ZERO)){
                flag = orderMapper.deleteOrder(dto);
            }else {
                flag = orderMapper.updateOrder(dto);
                if ("0".equals(dto.getDcItem())) {//供应商订货商品
                    //供应商订货商品失效
                    orderMapper.updateOrderExpStsByVendor(dto.getOrderDate(), dto.getVendorId(), dto.getStoreCd());
                }
            }
        } else {
            dto.setOrderNochargeQty(new BigDecimal("0"));
            flag = orderMapper.insertOrder(dto);
        }
        return flag;
    }

    /**
     * 修改搭赠量
     *
     * @param list        历史订货
     * @param promotionCd 促销cd
     * @param dto         订货商品
     */
    private void updatePresent(List<OrderItemDetailDTO> list, String promotionCd, OrderItemDetailDTO dto) {
        String presentFlg = "0";//订货搭赠量状态
        //搭赠量
        BigDecimal conditionQty = orderMapper.getConditionQty(promotionCd, dto.getArticleId());
        BigDecimal presentCoeff = new BigDecimal("1");//搭赠系数
        if (list != null && list.size() > 0) {//有历史订货
            if (conditionQty != null && conditionQty.compareTo(BigDecimal.ZERO) > 0) { //有搭赠量
                OrderItemDetailDTO oldOrderArticle = list.get(0);
                if (dto.getUploadOrderQty() == null) {
                    dto.setUploadOrderQty(BigDecimal.ZERO);
                }
                //新订货数量
                BigDecimal orderQty = dto.getOrderQty();
                presentCoeff = orderQty.divide(conditionQty, 0, BigDecimal.ROUND_DOWN);
                if (presentCoeff.compareTo(new BigDecimal("1")) < 0) {
                    presentCoeff = new BigDecimal("1");
                }
                //过去订货数量
                BigDecimal oldOrderQty = oldOrderArticle.getOrderQty();
                if (orderQty.compareTo(oldOrderQty) > 0) {//新订货大于过去订货
                    if (orderQty.compareTo(conditionQty) >= 0 &&
                            oldOrderQty.compareTo(conditionQty) < 0) {//旧订货没有搭赠量 新订货满足搭赠量
                        presentFlg = "1";//新增搭赠量
                    }
                } else if (orderQty.compareTo(oldOrderQty) < 0) {
                    if (oldOrderQty.compareTo(conditionQty) >= 0 &&
                            orderQty.compareTo(conditionQty) < 0) {//旧订货满足搭赠量 新订货不满足搭赠量
                        presentFlg = "2";//减去搭赠量
                    }
                }
            }
        } else {//没有历史订货
            presentFlg = "1";//新增搭赠量
        }
        List<PromotionDTO> articles = orderMapper.getPresentArticle(promotionCd, dto.getArticleId());
        if ("1".equals(presentFlg)) {//新增搭赠量
            for (PromotionDTO article : articles) {
                if (article.getPresentQty() != null && article.getPresentQty().compareTo(BigDecimal.ZERO) > 0) {
                    OrderItemDetailDTO presentDto = OrderItemDetailDTO.builder()
                            .articleId(article.getArticleId())
                            .storeCd(dto.getStoreCd())
                            .orderDate(dto.getOrderDate())
                            .shipmentCd("1")
                            .purchaseUnitId(article.getUnitId())
                            .orderNochargeQty(article.getPresentQty().multiply(presentCoeff))
                            .vendorId(dto.getVendorId())
                            .dcItem(dto.getDcItem())
                            .createUserId(dto.getCreateUserId())
                            .createYmd(dto.getCreateYmd())
                            .createHms(dto.getCreateHms())
                            .updateUserId(dto.getUpdateUserId())
                            .updateYmd(dto.getUpdateYmd())
                            .updateHms(dto.getUpdateHms())
                            .build();
                    List<OrderItemDetailDTO> presentlist = orderMapper.selectOrder(presentDto);
                    if (presentlist != null && presentlist.size() > 0) {
                        orderMapper.updateOrder(presentDto);
                    } else {
                        //presentDto.setDcItem();
                        if ("1".equals(dto.getDcItem())) {
                            presentDto.setOrderSts("2");
                        } else {
                            presentDto.setOrderSts("1");
                        }
                        presentDto.setOrderQty(new BigDecimal("0"));
                        presentDto.setOrderPrice(new BigDecimal("0"));
                        orderMapper.insertOrder(presentDto);
                    }
                }
            }
        } else if ("2".equals(presentFlg)) {//减去搭赠量
            for (PromotionDTO article : articles) {
                if (article.getPresentQty() != null && article.getPresentQty().compareTo(BigDecimal.ZERO) > 0) {
                    OrderItemDetailDTO presentDto = OrderItemDetailDTO.builder()
                            .articleId(article.getArticleId())
                            .storeCd(dto.getStoreCd())
                            .orderDate(dto.getOrderDate())
                            .shipmentCd("1")
                            .purchaseUnitId(article.getUnitId())
                            .orderNochargeQty(article.getPresentQty().multiply(presentCoeff).negate())//减去搭赠量
                            .vendorId(dto.getVendorId())
                            .dcItem(dto.getDcItem())
                            .createUserId(dto.getCreateUserId())
                            .createYmd(dto.getCreateYmd())
                            .createHms(dto.getCreateHms())
                            .updateUserId(dto.getUpdateUserId())
                            .updateYmd(dto.getUpdateYmd())
                            .updateHms(dto.getUpdateHms())
                            .build();
                    List<OrderItemDetailDTO> presentlist = orderMapper.selectOrder(presentDto);
                    if (presentlist != null && presentlist.size() > 0) {
                        orderMapper.updateOrder(presentDto);
                    } else {
                        //presentDto.setDcItem();
                        //presentDto.setOrderSts();
                        if ("1".equals(dto.getDcItem())) {
                            presentDto.setOrderSts("2");
                        } else {
                            presentDto.setOrderSts("1");
                        }
                        presentDto.setOrderPrice(new BigDecimal("0"));
                        presentDto.setOrderQty(new BigDecimal("0"));
                        orderMapper.insertOrder(presentDto);
                    }
                }
            }
        }
    }

    @Override
    public List<OrderInvoicingDTO> getReferenceInfo(OrderItemParamDTO dto) {
        //获取日期
        OrderInvoicingDTO date = this.getDate(dto.getOrderDate());
        //获取天气
        OrderInvoicingDTO weather = this.getWeather(dto.getOrderDate(), dto.getStoreCd());

        OrderInvoicingDTO airQuality = this.getAirQuality(dto.getOrderDate(), dto.getStoreCd());
        //进销存基本信息
        List<OrderInvoicingDTO> referenceInfo = this.getReferenceInfo(dto.getOrderDate(), dto.getStoreCd(), dto.getArticleId());
        List<OrderInvoicingDTO> list = new ArrayList<>();
        list.add(date);
        list.add(weather);
        list.add(airQuality);
        list.addAll(referenceInfo);
        return list;
    }

    private OrderInvoicingDTO getDate(String orderDate) {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        DateFormat dateFormat1 = new SimpleDateFormat("MM/dd");
        Date date = null;
        try {
            date = dateFormat.parse(orderDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String[] weekArr = {"(Mon)", "(Tue)", "(Wed)", "(Thu)", "(Fri)", "(Sat)", "(Sun)"};
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        OrderInvoicingDTO tr1 = new OrderInvoicingDTO();
        calendar.add(calendar.DATE, -7);
        tr1.setO1("Date");
//        calendar.add(calendar.DATE,1);
        String tr1date1 = dateFormat1.format(calendar.getTime());
        String week1 = weekArr[calendar.get(Calendar.DAY_OF_WEEK) - 1];
        tr1.setO2(tr1date1 + week1);
        calendar.add(calendar.DATE, 1);
        String tr1date2 = dateFormat1.format(calendar.getTime());
        String week2 = weekArr[calendar.get(Calendar.DAY_OF_WEEK) - 1];
        tr1.setO3(tr1date2 + week2);
        calendar.add(calendar.DATE, 1);
        String tr1date3 = dateFormat1.format(calendar.getTime());
        String week3 = weekArr[calendar.get(Calendar.DAY_OF_WEEK) - 1];
        tr1.setO4(tr1date3 + week3);
        calendar.add(calendar.DATE, 1);
        String tr1date4 = dateFormat1.format(calendar.getTime());
        String week4 = weekArr[calendar.get(Calendar.DAY_OF_WEEK) - 1];
        tr1.setO5(tr1date4 + week4);
        calendar.add(calendar.DATE, 1);
        String tr1date5 = dateFormat1.format(calendar.getTime());
        String week5 = weekArr[calendar.get(Calendar.DAY_OF_WEEK) - 1];
        tr1.setO6(tr1date5 + week5);
        calendar.add(calendar.DATE, 1);
        String tr1date6 = dateFormat1.format(calendar.getTime());
        String week6 = weekArr[calendar.get(Calendar.DAY_OF_WEEK) - 1];
        tr1.setO7(tr1date6 + week6);
        calendar.add(calendar.DATE, 1);
        String tr1date7 = dateFormat1.format(calendar.getTime());
        String week7 = weekArr[calendar.get(Calendar.DAY_OF_WEEK) - 1];
        tr1.setO8(tr1date7 + week7);
        calendar.add(calendar.DATE, 1);
        String tr1date8 = dateFormat1.format(calendar.getTime());
        String week8 = weekArr[calendar.get(Calendar.DAY_OF_WEEK) - 1];
        tr1.setO9(tr1date8 + week8);
        calendar.add(calendar.DATE, 1);
        String tr1date9 = dateFormat1.format(calendar.getTime());
        String week9 = weekArr[calendar.get(Calendar.DAY_OF_WEEK) - 1];
        tr1.setO10(tr1date9 + week9);
        calendar.add(calendar.DATE, 1);
        String tr1date10 = dateFormat1.format(calendar.getTime());
        String week10 = weekArr[calendar.get(Calendar.DAY_OF_WEEK) - 1];
        tr1.setO11(tr1date10 + week10);
        calendar.add(calendar.DATE, 1);
        String tr1date11 = dateFormat1.format(calendar.getTime());
        String week11 = weekArr[calendar.get(Calendar.DAY_OF_WEEK) - 1];
        tr1.setO12(tr1date11 + week11);
        calendar.add(calendar.DATE, 1);
        String tr1date12 = dateFormat1.format(calendar.getTime());
        String week12 = weekArr[calendar.get(Calendar.DAY_OF_WEEK) - 1];
        tr1.setO13(tr1date12 + week12);
        String tr1date13 = dateFormat1.format(calendar.getTime());
        String week13 = weekArr[calendar.get(Calendar.DAY_OF_WEEK) - 1];
        tr1.setO14(tr1date13 + week13);
        String tr1date14 = dateFormat1.format(calendar.getTime());
        String week14 = weekArr[calendar.get(Calendar.DAY_OF_WEEK) - 1];
        tr1.setO15(tr1date14 + week14);
        String tr1date15 = dateFormat1.format(calendar.getTime());
        String week15 = weekArr[calendar.get(Calendar.DAY_OF_WEEK) - 1];
        tr1.setO16(tr1date15 + week15);
        return tr1;
    }

    private OrderInvoicingDTO getWeather(String orderDate, String storeCd) {
        OrderInvoicingDTO tr2 = new OrderInvoicingDTO();
        Ma8230 ma8230 = ma8230Mapper.selectList(orderDate, storeCd);
        tr2.setO1("Weather<br>Temperature");
        if (ma8230 != null) {
            tr2.setO2(ma8230.getWeather7b() + "<br>" + ma8230.getMintemp7b() + "℃～" + ma8230.getMaxtemp7b() + "℃");
            tr2.setO3(ma8230.getWeather6b() + "<br>" + ma8230.getMintemp6b() + "℃～" + ma8230.getMaxtemp6b() + "℃");
            tr2.setO4(ma8230.getWeather5b() + "<br>" + ma8230.getMintemp5b() + "℃～" + ma8230.getMaxtemp5b() + "℃");
            tr2.setO5(ma8230.getWeather4b() + "<br>" + ma8230.getMintemp4b() + "℃～" + ma8230.getMaxtemp4b() + "℃");
            tr2.setO6(ma8230.getWeather3b() + "<br>" + ma8230.getMintemp3b() + "℃～" + ma8230.getMaxtemp3b() + "℃");
            tr2.setO7(ma8230.getWeather2b() + "<br>" + ma8230.getMintemp2b() + "℃～" + ma8230.getMaxtemp2b() + "℃");
            tr2.setO8(ma8230.getWeather1b() + "<br>" + ma8230.getMintemp1b() + "℃～" + ma8230.getMaxtemp1b() + "℃");
            tr2.setO9(ma8230.getWeather() + "<br>" + ma8230.getMintemp7b() + "℃～" + ma8230.getMaxtemp7b() + "℃");
            tr2.setO10(ma8230.getWeather1a() + "<br>" + ma8230.getMintemp1a() + "℃～" + ma8230.getMaxtemp1a() + "℃");
            tr2.setO11(ma8230.getWeather2a() + "<br>" + ma8230.getMintemp2a() + "℃～" + ma8230.getMaxtemp2a() + "℃");
            tr2.setO12(ma8230.getWeather3a() + "<br>" + ma8230.getMintemp3a() + "℃～" + ma8230.getMaxtemp3a() + "℃");
            tr2.setO13(ma8230.getWeather4a() + "<br>" + ma8230.getMintemp4a() + "℃～" + ma8230.getMaxtemp4a() + "℃");
            tr2.setO14(ma8230.getWeather5a() + "<br>" + ma8230.getMintemp5a() + "℃～" + ma8230.getMaxtemp5a() + "℃");
            tr2.setO15(ma8230.getWeather6a() + "<br>" + ma8230.getMintemp6a() + "℃～" + ma8230.getMaxtemp6a() + "℃");
            tr2.setO16(ma8230.getWeather7a() + "<br>" + ma8230.getMintemp7a() + "℃～" + ma8230.getMaxtemp7a() + "℃");
        }
        return tr2;
    }

    private OrderInvoicingDTO getAirQuality(String orderDate, String storeCd) {
        OrderInvoicingDTO tr3 = new OrderInvoicingDTO();
        MA8250 ma8250 = ma8250Mapper.selectList(orderDate, storeCd);
        tr3.setO1("Air Quality<br>Index");
        if (ma8250 != null) {
            tr3.setO2(ma8250.getAqi7b());
            tr3.setO3(ma8250.getAqi6b());
            tr3.setO4(ma8250.getAqi5b());
            tr3.setO5(ma8250.getAqi4b());
            tr3.setO6(ma8250.getAqi3b());
            tr3.setO7(ma8250.getAqi2b());
            tr3.setO8(ma8250.getAqi1b());
            tr3.setO9(ma8250.getAqi());
            tr3.setO10(ma8250.getAqi1a());
            tr3.setO11(ma8250.getAqi2a());
            tr3.setO12(ma8250.getAqi3a());
            tr3.setO13(ma8250.getAqi4a());
            tr3.setO14(ma8250.getAqi5a());
            tr3.setO15(ma8250.getAqi6a());
            tr3.setO16(ma8250.getAqi7a());
        }
        return tr3;
    }

    private List<OrderInvoicingDTO> getReferenceInfo(String orderDate, String storeCd, String articleId) {
        List<OrderInvoicingDTO> list = new ArrayList<>();
        OrderInvoicingDTO tr4 = new OrderInvoicingDTO();
        OrderInvoicingDTO tr5 = new OrderInvoicingDTO();
        OrderInvoicingDTO tr6 = new OrderInvoicingDTO();
        OrderInvoicingDTO tr7 = new OrderInvoicingDTO();
        tr4.setO1("Sales");
        tr5.setO1("Purchasing");
        tr6.setO1("Adjustment");
        tr7.setO1("Stock on Hand");
        Ma8240Key ma8240Key = new Ma8240Key();
        ma8240Key.setAccDate(orderDate);
        ma8240Key.setStoreCd(storeCd);
        ma8240Key.setArticleId(articleId);
        Ma8240 ma8240 = ma8240Mapper.selectByPrimaryKey(ma8240Key);
        if (ma8240 != null) {
            tr4.setO2(ma8240.getSaleQty7b() == null ? "" : ma8240.getSaleQty7b().toString());
            tr5.setO2(ma8240.getReceiveQty7b() == null ? "" : ma8240.getReceiveQty7b().toString());
            tr6.setO2(ma8240.getAdjustQty7b() == null ? "" : ma8240.getAdjustQty7b().toString());
            tr7.setO2(ma8240.getOnHandQty7b() == null ? "" : ma8240.getOnHandQty7b().toString());

            tr4.setO3(ma8240.getSaleQty6b() == null ? "" : ma8240.getSaleQty6b().toString());
            tr5.setO3(ma8240.getReceiveQty6b() == null ? "" : ma8240.getReceiveQty6b().toString());
            tr6.setO3(ma8240.getAdjustQty6b() == null ? "" : ma8240.getAdjustQty6b().toString());
            tr7.setO3(ma8240.getOnHandQty6b() == null ? "" : ma8240.getOnHandQty6b().toString());

            tr4.setO4(ma8240.getSaleQty5b() == null ? "" : ma8240.getSaleQty5b().toString());
            tr5.setO4(ma8240.getReceiveQty5b() == null ? "" : ma8240.getReceiveQty5b().toString());
            tr6.setO4(ma8240.getAdjustQty5b() == null ? "" : ma8240.getAdjustQty5b().toString());
            tr7.setO4(ma8240.getOnHandQty5b() == null ? "" : ma8240.getOnHandQty5b().toString());

            tr4.setO5(ma8240.getSaleQty4b() == null ? "" : ma8240.getSaleQty4b().toString());
            tr5.setO5(ma8240.getReceiveQty4b() == null ? "" : ma8240.getReceiveQty4b().toString());
            tr6.setO5(ma8240.getAdjustQty4b() == null ? "" : ma8240.getAdjustQty4b().toString());
            tr7.setO5(ma8240.getOnHandQty4b() == null ? "" : ma8240.getOnHandQty4b().toString());

            tr4.setO6(ma8240.getSaleQty3b() == null ? "" : ma8240.getSaleQty3b().toString());
            tr5.setO6(ma8240.getReceiveQty3b() == null ? "" : ma8240.getReceiveQty3b().toString());
            tr6.setO6(ma8240.getAdjustQty3b() == null ? "" : ma8240.getAdjustQty3b().toString());
            tr7.setO6(ma8240.getOnHandQty3b() == null ? "" : ma8240.getOnHandQty3b().toString());

            tr4.setO7(ma8240.getSaleQty2b() == null ? "" : ma8240.getSaleQty2b().toString());
            tr5.setO7(ma8240.getReceiveQty2b() == null ? "" : ma8240.getReceiveQty2b().toString());
            tr6.setO7(ma8240.getAdjustQty2b() == null ? "" : ma8240.getAdjustQty2b().toString());
            tr7.setO7(ma8240.getOnHandQty2b() == null ? "" : ma8240.getOnHandQty2b().toString());

            tr4.setO8(ma8240.getSaleQty1b() == null ? "" : ma8240.getSaleQty1b().toString());
            tr5.setO8(ma8240.getReceiveQty1b() == null ? "" : ma8240.getReceiveQty1b().toString());
            tr6.setO8(ma8240.getAdjustQty1b() == null ? "" : ma8240.getAdjustQty1b().toString());
            tr7.setO8(ma8240.getOnHandQty1b() == null ? "" : ma8240.getOnHandQty1b().toString());

            tr4.setO9(ma8240.getSaleQty() == null ? "" : ma8240.getSaleQty().toString());
            tr5.setO9(ma8240.getReceiveQty() == null ? "" : ma8240.getReceiveQty().toString());
            tr6.setO9(ma8240.getAdjustQty() == null ? "" : ma8240.getAdjustQty().toString());
            tr7.setO9(ma8240.getOnHandQty() == null ? "" : ma8240.getOnHandQty().toString());

            tr4.setO10(ma8240.getSaleQty1a() == null ? "" : ma8240.getSaleQty1a().toString());
            tr5.setO10(ma8240.getReceiveQty1a() == null ? "" : ma8240.getReceiveQty1a().toString());
            tr6.setO10(ma8240.getAdjustQty1a() == null ? "" : ma8240.getAdjustQty1a().toString());
            tr7.setO10(ma8240.getOnHandQty1a() == null ? "" : ma8240.getOnHandQty1a().toString());

            tr4.setO11(ma8240.getSaleQty2a() == null ? "" : ma8240.getSaleQty2a().toString());
            tr5.setO11(ma8240.getReceiveQty2a() == null ? "" : ma8240.getReceiveQty2a().toString());
            tr6.setO11(ma8240.getAdjustQty2a() == null ? "" : ma8240.getAdjustQty2a().toString());
            tr7.setO11(ma8240.getOnHandQty2a() == null ? "" : ma8240.getOnHandQty2a().toString());

            tr4.setO12(ma8240.getSaleQty3a() == null ? "" : ma8240.getSaleQty3a().toString());
            tr5.setO12(ma8240.getReceiveQty3a() == null ? "" : ma8240.getReceiveQty3a().toString());
            tr6.setO12(ma8240.getAdjustQty3a() == null ? "" : ma8240.getAdjustQty3a().toString());
            tr7.setO12(ma8240.getOnHandQty3a() == null ? "" : ma8240.getOnHandQty3a().toString());

            tr4.setO13(ma8240.getSaleQty4a() == null ? "" : ma8240.getSaleQty4a().toString());
            tr5.setO13(ma8240.getReceiveQty4a() == null ? "" : ma8240.getReceiveQty4a().toString());
            tr6.setO13(ma8240.getAdjustQty4a() == null ? "" : ma8240.getAdjustQty4a().toString());
            tr7.setO13(ma8240.getOnHandQty4a() == null ? "" : ma8240.getOnHandQty4a().toString());

            tr4.setO14(ma8240.getSaleQty5a() == null ? "" : ma8240.getSaleQty5a().toString());
            tr5.setO14(ma8240.getReceiveQty5a() == null ? "" : ma8240.getReceiveQty5a().toString());
            tr6.setO14(ma8240.getAdjustQty5a() == null ? "" : ma8240.getAdjustQty5a().toString());
            tr7.setO14(ma8240.getOnHandQty5a() == null ? "" : ma8240.getOnHandQty5a().toString());

            tr4.setO15(ma8240.getSaleQty6a() == null ? "" : ma8240.getSaleQty6a().toString());
            tr5.setO15(ma8240.getReceiveQty6a() == null ? "" : ma8240.getReceiveQty6a().toString());
            tr6.setO15(ma8240.getAdjustQty6a() == null ? "" : ma8240.getAdjustQty6a().toString());
            tr7.setO15(ma8240.getOnHandQty6a() == null ? "" : ma8240.getOnHandQty6a().toString());

            tr4.setO16(ma8240.getSaleQty7a() == null ? "" : ma8240.getSaleQty7a().toString());
            tr5.setO16(ma8240.getReceiveQty7a() == null ? "" : ma8240.getReceiveQty7a().toString());
            tr6.setO16(ma8240.getAdjustQty7a() == null ? "" : ma8240.getAdjustQty7a().toString());
            tr7.setO16(ma8240.getOnHandQty7a() == null ? "" : ma8240.getOnHandQty7a().toString());
        }
        list.add(tr4);
        list.add(tr5);
        list.add(tr6);
        list.add(tr7);
        return list;
    }

    @Override
    public List<AutoCompleteDTO> getStoreListNotOrder(Collection<String> storeCds, String v) {
        String businessDate = cm9060Service.getValByKey("0000");
        List<AutoCompleteDTO> list = orderMapper.getStoreListNotOrder(storeCds, v, businessDate);
        return list;
    }

    @Override
    public int checkNewStore(String storeCd) {
        //获取下订单数量
        String businessDate = cm9060Service.getValByKey("0000");
        // storeOpenDate为开店日期+120天之后的日期
        String storeOpenDate = orderMapper.getOrderOpenDate(storeCd, businessDate);
        if (StringUtils.isNotBlank(storeOpenDate) && StringUtils.isNotBlank(businessDate)) {
            int storeOpenDate1 = Integer.parseInt(storeOpenDate);
            int businessDate1 = Integer.parseInt(businessDate);
            if (businessDate1 <= storeOpenDate1) {
                return 1;
            }
        }
        return 0;
    }

    @Override
    public String updateOrderSts(String storeCd) {
        String businessDate = cm9060Service.getValByKey("0000");
        StringBuilder sb = new StringBuilder("Supplier :");
        //查询未确认的供应商订货商品
        List<OrderItemDetailDTO> orderVendorList = orderMapper.getOrderInfoByVendor(storeCd, businessDate);

        if (orderVendorList != null && orderVendorList.size() > 0) {
            for (OrderItemDetailDTO orderItemDetailDTO : orderVendorList) {
                boolean updateStsFlg = true;
                //获取供应商MOQ 和 MOA
                OrderVendorDetailDTO orderVendorDetailDTO = orderMapper.getVendorDetailByCity(storeCd, orderItemDetailDTO.getVendorId(), businessDate);
                if (orderVendorDetailDTO != null) {
                    if (orderVendorDetailDTO.getMinOrderQty().compareTo(orderItemDetailDTO.getOrderQty()) > 0) {
                        updateStsFlg = false;
                    }
                    if (orderVendorDetailDTO.getMinOrderAmt().compareTo(orderItemDetailDTO.getOrderPrice()) > 0) {
                        updateStsFlg = false;
                    }
                }
                // 获取供应商商品分组的MOQ 和 MOA
                List<OrderVendorDetailDTO> priceGroup = orderMapper.getGroupDetailByVendor(storeCd, orderItemDetailDTO.getVendorId(), businessDate);
                if (priceGroup != null && priceGroup.size() > 0) {
                    for (OrderVendorDetailDTO vendorDetailDTO : priceGroup) {
                        OrderItemDetailDTO orderItemQty = orderMapper.getOrderInfoByVendorGroup(storeCd,businessDate,vendorDetailDTO.getVendorId(),vendorDetailDTO.getGroupCode());
                        if (vendorDetailDTO.getMinOrderAmt().compareTo(orderItemQty.getOrderPrice()) <= 0) {
                            if (updateStsFlg)
                                orderMapper.updateOrderEffStsByGroup(businessDate, orderItemDetailDTO.getVendorId(), storeCd, vendorDetailDTO.getGroupCode());
                        }
                    }
                } else {
                    if (updateStsFlg)
                        orderMapper.updateOrderEffStsByVendor(businessDate, orderItemDetailDTO.getVendorId(), storeCd);
                }
            }
        }
        return null;
    }

    @Override
    public String checkItem(String storeCd) {
        boolean checkFlg = true;

        String businessDate = cm9060Service.getValByKey("0000");
        StringBuilder sb = new StringBuilder("Supplier :");
        DecimalFormat df = new DecimalFormat("###,###");

        //查询未确认的供应商订货商品
        List<OrderItemDetailDTO> orderVendorList = orderMapper.getOrderInfoByVendor(storeCd, businessDate);
        if (orderVendorList != null && orderVendorList.size() > 0) {
            for (OrderItemDetailDTO orderItemDetailDTO : orderVendorList) {
                // 获取供应商商品分组的MOQ 和 MOA
                List<OrderVendorDetailDTO> priceGroup = orderMapper.getGroupDetailByVendor(storeCd, orderItemDetailDTO.getVendorId(), businessDate);
                if (priceGroup != null && priceGroup.size() > 0) {
                    for(OrderVendorDetailDTO vendorGroupDetail : priceGroup) {
                        // 查询订货中属于供应商分组的商品总数量，总金额
                        OrderItemDetailDTO orderItemQty = orderMapper.getOrderInfoByVendorGroup(storeCd,businessDate,vendorGroupDetail.getVendorId(),vendorGroupDetail.getGroupCode());
                        if (vendorGroupDetail.getMinOrderQty().compareTo(orderItemQty.getOrderQty()) > 0) {
                            sb.append(vendorGroupDetail.getVendorId()).append(" ").append(vendorGroupDetail.getVendorName()).append("<br>")
                                    .append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp")
                                    .append(" Product Group : ").append(vendorGroupDetail.getGroupCode()).append("<br>")
                                    .append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp")
                                    .append("  Order Quantity can not be less than Supplier MOQ(").append(df.format(vendorGroupDetail.getMinOrderQty())).append(")!")
                                    .append("&nbsp;Still need (")
                                    .append(df.format(vendorGroupDetail.getMinOrderQty().
                                            subtract(orderItemQty.getOrderQty()))).
                                    append(")!<br>");
                            checkFlg = false;
                        }
                        if (vendorGroupDetail.getMinOrderAmt().compareTo(orderItemQty.getOrderPrice()) > 0) {
                            sb.append(vendorGroupDetail.getVendorId()).append(" ").append(vendorGroupDetail.getVendorName()).append("<br>")
                                    .append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp")
                                    .append(" Product Group : ").append(vendorGroupDetail.getGroupCode()).append("<br>")
                                    .append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp")
                                    .append("  Order Amt can not be less than Supplier MOA(").append(df.format(vendorGroupDetail.getMinOrderAmt())).append(")!")
                                    .append("&nbsp;Still need (")
                                    .append(df.format(vendorGroupDetail.getMinOrderAmt().
                                            subtract(orderItemQty.getOrderPrice()))).
                                    append(")!<br>");
                            checkFlg = false;
                        }
                    }
                }

                //获取供应商MOQ 和 MOA
                OrderVendorDetailDTO orderVendorDetailDTO = orderMapper.getVendorDetailByCity(storeCd, orderItemDetailDTO.getVendorId(), businessDate);
                if (orderVendorDetailDTO != null && orderItemDetailDTO.getOrderQty().compareTo(BigDecimal.ZERO)>0
                  && orderItemDetailDTO.getOrderPrice().compareTo(BigDecimal.ZERO)>0) {
                    if (orderVendorDetailDTO.getMinOrderQty().compareTo(orderItemDetailDTO.getOrderQty()) > 0) {
                        sb.append(orderVendorDetailDTO.getVendorId()).append(" ").append(orderVendorDetailDTO.getVendorName()).append("<br>")
                                .append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp")
                                .append("Order Qty can not be less than Supplier MOQ(").append(df.format(orderVendorDetailDTO.getMinOrderQty())).append(")!")
                                .append("&nbsp;Still need (")
                                .append(df.format(orderVendorDetailDTO.getMinOrderQty().
                                        subtract(orderItemDetailDTO.getOrderQty()))).
                                append(")!<br>");
                        checkFlg = false;
                    }
                    if (orderVendorDetailDTO.getMinOrderAmt().compareTo(orderItemDetailDTO.getOrderPrice()) > 0) {
                        sb.append(orderVendorDetailDTO.getVendorId()).append(" ").append(orderVendorDetailDTO.getVendorName()).append("<br>")
                                .append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp")
                                .append("Order Amt can not be less than Supplier MOA(").append(df.format(orderVendorDetailDTO.getMinOrderAmt())).append(")!")
                                .append("&nbsp;Still need (")
                                .append(df.format(orderVendorDetailDTO.getMinOrderAmt().
                                        subtract(orderItemDetailDTO.getOrderPrice()))).
                                append(")!<br>");
                        checkFlg = false;
                    }
                }

            }
        }
        // 查询订货的单个商品的数量，金额
        List<OrderItemDetailDTO> orderList = orderMapper.getOrderInfoByItem(storeCd, businessDate);
        if (orderList != null && orderList.size() > 0) {
            for(OrderItemDetailDTO itemDetail:orderList){
                // 获取单个商品的MOQ
                OrderVendorDetailDTO orderItemDetail = orderMapper.getItemDerailByCity(storeCd, itemDetail.getVendorId(), businessDate, itemDetail.getArticleId());
                if(orderItemDetail != null && itemDetail.getOrderQty().compareTo(BigDecimal.ZERO)>0
                        && itemDetail.getOrderPrice().compareTo(BigDecimal.ZERO)>0){
                    if (orderItemDetail.getMinOrderQty().compareTo(itemDetail.getOrderQty()) > 0) {
                        sb.append(itemDetail.getVendorId()).append(" ").append(itemDetail.getVendorName()).append("<br>")
                                .append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp")
                                .append(" Item : ").append(itemDetail.getArticleId()).append(" ").append(itemDetail.getArticleName()).append("<br>")
                                .append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp")
                                .append(" Order Quantity can not be less than item MOQ(").append(df.format(orderItemDetail.getMinOrderQty())).append(")!")
                                .append("&nbsp;Still need (")
                                .append(df.format(orderItemDetail.getMinOrderQty().
                                        subtract(itemDetail.getOrderQty()))).
                                append(")!<br>");
                        checkFlg = false;
                    }
                }
            }
        }
        //失败提示
        if (!checkFlg) {
            // sb.delete(sb.lastIndexOf("<br>"), sb.length());
            return sb.toString();
        }
        return null;
    }

    @Override
    public String checkOrder(String storeCd, String orderDate) {
        StringBuilder sb = new StringBuilder();
        if (orderMapper.checkVendorOrder(storeCd, orderDate) > 0) {
            List<OrderItemDetailDTO> checkOrderList = orderMapper.checkOrder(storeCd, orderDate);
            if (checkOrderList.size() > 0) {
                if(checkOrderList.size()>=2){
                    sb.append(checkOrderList.get(0).getArticleId()).append(" ").append(checkOrderList.get(0).getArticleName()).append("<br>")
                            .append(checkOrderList.get(1).getArticleId()).append(" ").append(checkOrderList.get(1).getArticleName()).append("<br>")
                            .append("&nbsp;&nbsp;&nbsp;&nbsp;").append("didn't satisfy supplier MOA/MOQ requirement");
                }else {
                    sb.append(checkOrderList.get(0).getArticleId()).append(" ").append(checkOrderList.get(0).getArticleName()).append("<br>")
                            .append("&nbsp;&nbsp;&nbsp;&nbsp;").append("didn't satisfy supplier MOA/MOQ requirement");
                }
                return sb.toString();
            }
            ;
        }
        ;
        return null;
    }

    @Override
    public int insertAudit(String storeCd, String orderDate) {
        return orderMapper.insertAudit(storeCd, orderDate);
    }

    @Override
    public List<AutoCompleteDTO> getMa1107(String storeCd, String v) {
        return orderMapper.getMa1107(storeCd,v);
    }

    @Override
    public List<AutoCompleteDTO> getMa1108(String storeCd,String excelName,String v) {
        return orderMapper.getMa1108(storeCd,excelName,v);
    }

    @Override
    public List<AutoCompleteDTO> getMa1109(String storeCd,String excelName,String paramShelf,String v) {
        return orderMapper.getMa1109(storeCd,excelName,paramShelf,v);
    }

    @Override
    public List<OrderItemDTO> getUserPosition(String storeCd, String userId) {
        return orderMapper.getUserPosition(storeCd,userId);
    }


    @Override
    public GridDataDTO<OrderItemDTO> GetOrderSpecialList(OrderItemParamDTO param) {
        if ("1".equals(param.getOrderType())) {
            List<OrderItemDTO> list = orderMapper.getOrderSpecialListByDpt(param);
            long count = orderMapper.getOrderSpecialListByDptCount(param);
            return new GridDataDTO<>(list, param.getPage(), count,
                    param.getRows());
        } else if ("2".equals(param.getOrderType())) {
            List<OrderItemDTO> list = orderMapper.getOrderSpecialListByShelf(param);
            long count = orderMapper.getOrderSpecialListByShelfCount(param);
            return new GridDataDTO<>(list, param.getPage(), count,
                    param.getRows());
        }
        return null;
    }

    /**
     * 获取跨天订货详情信息
     *
     * @param dto
     * @return
     */
    @Override
    public GridDataDTO<OrderItemDetailDTO> GetOrderSpecialListDetail(OrderItemParamDTO dto) {
        if (StringUtils.isNotBlank(dto.getOrderType())) {

            if ("1".equals(dto.getOrderType())) {
                dto.setShelf(null);
                dto.setSubShelf(null);
                List<String> articles = new ArrayList<>();
                List<OrderItemDetailDTO> _list = orderMapper.getOrderSpecialListDetailByDpt(dto);
                for(OrderItemDetailDTO item:_list){
                    articles.add(item.getArticleId());
                }
                String inEsTime = cm9060Service.getValByKey("1206");
                //拼接url，转义参数
                String connUrl = inventoryUrl + "GetRelTimeInventory/" + dto.getStoreCd() + "/"
                        + "*" + "/*/*/*/*/" + inEsTime+"/*/*";
                List<RTInventoryQueryDTO> rTdTOList = rtInventoryService.getStockList(articles,connUrl);
                if(rTdTOList.size()>0){
                    for(RTInventoryQueryDTO rtDto:rTdTOList){
                        for(OrderItemDetailDTO orderDto:_list){
                            if(rtDto.getItemCode().equals(orderDto.getArticleId())){
                                orderDto.setRealtimeStock(rtDto.getRealtimeQty().toString()); // 实时库存
                            }
                        }
                    }
                }
                long count = orderMapper.getOrderSpecialListDetailByDptCount(dto);

                GridDataDTO<OrderItemDetailDTO> grid = new GridDataDTO<>(_list,
                        dto.getPage(), count, dto.getRows());
                return grid;
            } else if ("2".equals(dto.getOrderType())) {
                dto.setDepCd(null);
                dto.setPmaCd(null);
                dto.setCategoryCd(null);
                dto.setSubCategoryCd(null);
                List<String> articles = new ArrayList<>();
                List<OrderItemDetailDTO> _list = orderMapper.getOrderSpecialListDetail(dto);
                for(OrderItemDetailDTO item:_list){
                    articles.add(item.getArticleId());
                }
                String inEsTime = cm9060Service.getValByKey("1206");
                //拼接url，转义参数
                String connUrl = inventoryUrl + "GetRelTimeInventory/" + dto.getStoreCd() + "/"
                        + "*" + "/*/*/*/*/" + inEsTime+"/*/*";
                List<RTInventoryQueryDTO> rTdTOList = rtInventoryService.getStockList(articles,connUrl);
                if(rTdTOList.size()>0){
                    for(RTInventoryQueryDTO rtDto:rTdTOList){
                        for(OrderItemDetailDTO orderDto:_list){
                            if(rtDto.getItemCode().equals(orderDto.getArticleId())){
                                orderDto.setRealtimeStock(rtDto.getRealtimeQty().toString()); // 实时库存
                            }
                        }
                    }
                }
                long count = orderMapper.getOrderSpecialListDetailCount(dto);

                GridDataDTO<OrderItemDetailDTO> grid = new GridDataDTO<>(_list,
                        dto.getPage(), count, dto.getRows());
                return grid;
            }

        }
        return new GridDataDTO<>();
    }

    /**
     * 获取跨天商品详细信息
     * @param dto
     * @return
     */
    @Override
    public OrderDetailInfo getOrderSecialItemDetail(OrderItemParamDTO dto) {
        OrderDetailInfo orderDetailInfo = orderMapper.getOrderSpecialItemDetail(dto);
        if (orderDetailInfo != null) {
            BigDecimal orderPirce = orderDetailInfo.getOrderPrice();
            BigDecimal minOrderAmtTax = orderDetailInfo.getMinOrderAmtTax();
            //最低订货金额不为空
            if (minOrderAmtTax != null) {
                BigDecimal minOrderQty = BigDecimal.ZERO;
                if (orderPirce != null && orderPirce.compareTo(BigDecimal.ZERO) > 0) {
                    minOrderQty = minOrderAmtTax.divide(orderPirce, 2, BigDecimal.ROUND_HALF_UP);//四舍五入保留两位小数
                }
                //判断最低订货数量是否为空
                if (orderDetailInfo.getMinOrderQty() != null) {
                    if (orderDetailInfo.getMinOrderQty().compareTo(minOrderQty) < 0) {
                        orderDetailInfo.setMinOrderQty(minOrderQty);
                    }
                } else {
                    orderDetailInfo.setMinOrderQty(minOrderQty);
                }
            }
        }
        return orderDetailInfo;
    }
    /**
     * 修改订单信息
     */
    @Override
    @Transactional
    public int updateSpecialOrder(OrderItemDetailDTO dto) {
        List<OrderItemDetailDTO> list = orderMapper.selectSpecialOrder(dto);
        dto.setShipmentCd("1");
        int flag;

        if (list != null && list.size() > 0) {
            if(dto.getOrderQty().equals(BigDecimal.ZERO)){
                flag = orderMapper.deleteSpecialOrder(dto);
            }else {
                flag = orderMapper.updateSpecialOrder(dto);
                if ("0".equals(dto.getDcItem())) {//供应商订货商品
                    //供应商订货商品失效
                    orderMapper.updateSpecialOrderExpStsByVendor(dto.getOrderDate(), dto.getVendorId(), dto.getStoreCd());
                }
            }
        } else {
            dto.setOrderNochargeQty(new BigDecimal("0"));
            flag = orderMapper.insertSpecialOrder(dto);
        }
        return flag;
    }
    // 修改跨天订货状态
    @Override
    public String updateSpecialOrderSts(String storeCd) {
        String businessDate = cm9060Service.getValByKey("0000");
        StringBuilder sb = new StringBuilder("Supplier :");
        //查询未确认的供应商订货商品
        List<OrderItemDetailDTO> orderVendorList = orderMapper.getSpecialOrderInfoByVendor(storeCd, businessDate);

        if (orderVendorList != null && orderVendorList.size() > 0) {
            for (OrderItemDetailDTO orderItemDetailDTO : orderVendorList) {
                boolean updateStsFlg = true;
                //获取供应商MOQ 和 MOA
                OrderVendorDetailDTO orderVendorDetailDTO = orderMapper.getVendorDetailByCity(storeCd, orderItemDetailDTO.getVendorId(), businessDate);
                if (orderVendorDetailDTO != null) {
                    if (orderVendorDetailDTO.getMinOrderQty().compareTo(orderItemDetailDTO.getOrderQty()) > 0) {
                        updateStsFlg = false;
                    }
                    if (orderVendorDetailDTO.getMinOrderAmt().compareTo(orderItemDetailDTO.getOrderPrice()) > 0) {
                        updateStsFlg = false;
                    }
                }
                // 获取供应商商品分组的MOQ 和 MOA
                List<OrderVendorDetailDTO> priceGroup = orderMapper.getGroupDetailByVendor(storeCd, orderItemDetailDTO.getVendorId(), businessDate);
                if (priceGroup != null && priceGroup.size() > 0) {
                    for (OrderVendorDetailDTO vendorDetailDTO : priceGroup) {
                        OrderItemDetailDTO orderItemQty = orderMapper.getSpecialOrderInfoByVendorGroup(storeCd,businessDate,vendorDetailDTO.getVendorId(),vendorDetailDTO.getGroupCode());
                        if (vendorDetailDTO.getMinOrderAmt().compareTo(orderItemQty.getOrderPrice()) <= 0) {
                            if (updateStsFlg)
                                orderMapper.updateSpecialOrderEffStsByGroup(businessDate, orderItemDetailDTO.getVendorId(), storeCd, vendorDetailDTO.getGroupCode());
                        }
                    }
                } else {
                    if (updateStsFlg)
                        orderMapper.updateSpecialOrderEffStsByVendor(businessDate, orderItemDetailDTO.getVendorId(), storeCd);
                }
            }
        }
        return null;
    }

    @Override
    public String checkSpecialOrder(String storeCd, String orderDate) {
        StringBuilder sb = new StringBuilder();
        if (orderMapper.checkVendorSpecialOrder(storeCd, orderDate) > 0) {
            List<OrderItemDetailDTO> checkOrderList = orderMapper.checkSpecialOrder(storeCd, orderDate);
            if (checkOrderList.size() > 0) {
                if(checkOrderList.size()>=2){
                    sb.append(checkOrderList.get(0).getArticleId()).append(" ").append(checkOrderList.get(0).getArticleName()).append("<br>")
                            .append(checkOrderList.get(1).getArticleId()).append(" ").append(checkOrderList.get(1).getArticleName()).append("<br>")
                            .append("&nbsp;&nbsp;&nbsp;&nbsp;").append("didn't satisfy supplier MOA/MOQ requirement");
                }else {
                    sb.append(checkOrderList.get(0).getArticleId()).append(" ").append(checkOrderList.get(0).getArticleName()).append("<br>")
                            .append("&nbsp;&nbsp;&nbsp;&nbsp;").append("didn't satisfy supplier MOA/MOQ requirement");
                }
                return sb.toString();
            }
            ;
        }
        ;
        return null;
    }

    @Override
    public String checkSpecialItem(String storeCd) {
        boolean checkFlg = true;

        String businessDate = cm9060Service.getValByKey("0000");
        StringBuilder sb = new StringBuilder("Supplier :");
        DecimalFormat df = new DecimalFormat("###,###");

        //查询未确认的供应商订货商品
        List<OrderItemDetailDTO> orderVendorList = orderMapper.getSpecialOrderInfoByVendor(storeCd, businessDate);
        if (orderVendorList != null && orderVendorList.size() > 0) {
            for (OrderItemDetailDTO orderItemDetailDTO : orderVendorList) {
                // 获取供应商商品分组的MOQ 和 MOA
                List<OrderVendorDetailDTO> priceGroup = orderMapper.getGroupDetailByVendor(storeCd, orderItemDetailDTO.getVendorId(), businessDate);
                if (priceGroup != null && priceGroup.size() > 0) {
                    for(OrderVendorDetailDTO vendorGroupDetail : priceGroup) {
                        // 查询订货中属于供应商分组的商品总数量，总金额
                        OrderItemDetailDTO orderItemQty = orderMapper.getSpecialOrderInfoByVendorGroup(storeCd,businessDate,vendorGroupDetail.getVendorId(),vendorGroupDetail.getGroupCode());
                        if (vendorGroupDetail.getMinOrderQty().compareTo(orderItemQty.getOrderQty()) > 0) {
                            sb.append(vendorGroupDetail.getVendorId()).append(" ").append(vendorGroupDetail.getVendorName()).append("<br>")
                                    .append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp")
                                    .append(" Product Group : ").append(vendorGroupDetail.getGroupCode()).append("<br>")
                                    .append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp")
                                    .append("  Order Quantity can not be less than Supplier MOQ(").append(df.format(vendorGroupDetail.getMinOrderQty())).append(")!")
                                    .append("&nbsp;Still need (")
                                    .append(df.format(vendorGroupDetail.getMinOrderQty().
                                            subtract(orderItemQty.getOrderQty()))).
                                    append(")!<br>");
                            checkFlg = false;
                        }
                        if (vendorGroupDetail.getMinOrderAmt().compareTo(orderItemQty.getOrderPrice()) > 0) {
                            sb.append(vendorGroupDetail.getVendorId()).append(" ").append(vendorGroupDetail.getVendorName()).append("<br>")
                                    .append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp")
                                    .append(" Product Group : ").append(vendorGroupDetail.getGroupCode()).append("<br>")
                                    .append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp")
                                    .append("  Order Amt can not be less than Supplier MOA(").append(df.format(vendorGroupDetail.getMinOrderAmt())).append(")!")
                                    .append("&nbsp;Still need (")
                                    .append(df.format(vendorGroupDetail.getMinOrderAmt().
                                            subtract(orderItemQty.getOrderPrice()))).
                                    append(")!<br>");
                            checkFlg = false;
                        }
                    }
                }

                //获取供应商MOQ 和 MOA
                OrderVendorDetailDTO orderVendorDetailDTO = orderMapper.getVendorDetailByCity(storeCd, orderItemDetailDTO.getVendorId(), businessDate);
                if (orderVendorDetailDTO != null) {
                    if (orderVendorDetailDTO.getMinOrderQty().compareTo(orderItemDetailDTO.getOrderQty()) > 0) {
                        sb.append(orderVendorDetailDTO.getVendorId()).append(" ").append(orderVendorDetailDTO.getVendorName()).append("<br>")
                                .append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp")
                                .append("Order Qty can not be less than Supplier MOQ(").append(df.format(orderVendorDetailDTO.getMinOrderQty())).append(")!")
                                .append("&nbsp;Still need (")
                                .append(df.format(orderVendorDetailDTO.getMinOrderQty().
                                        subtract(orderItemDetailDTO.getOrderQty()))).
                                append(")!<br>");
                        checkFlg = false;
                    }
                    if (orderVendorDetailDTO.getMinOrderAmt().compareTo(orderItemDetailDTO.getOrderPrice()) > 0) {
                        sb.append(orderVendorDetailDTO.getVendorId()).append(" ").append(orderVendorDetailDTO.getVendorName()).append("<br>")
                                .append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp")
                                .append("Order Amt can not be less than Supplier MOA(").append(df.format(orderVendorDetailDTO.getMinOrderAmt())).append(")!")
                                .append("&nbsp;Still need (")
                                .append(df.format(orderVendorDetailDTO.getMinOrderAmt().
                                        subtract(orderItemDetailDTO.getOrderPrice()))).
                                append(")!<br>");
                        checkFlg = false;
                    }
                }

            }
        }
        // 查询订货的单个商品的数量，金额
        List<OrderItemDetailDTO> orderList = orderMapper.getSpecialOrderInfoByItem(storeCd, businessDate);
        if (orderList != null && orderList.size() > 0) {
            for(OrderItemDetailDTO itemDetail:orderList){
                // 获取单个商品的MOQ
                OrderVendorDetailDTO orderItemDetail = orderMapper.getItemDerailByCity(storeCd, itemDetail.getVendorId(), businessDate, itemDetail.getArticleId());
                if(orderItemDetail != null){
                    if (orderItemDetail.getMinOrderQty().compareTo(itemDetail.getOrderQty()) > 0) {
                        sb.append(itemDetail.getVendorId()).append(" ").append(itemDetail.getVendorName()).append("<br>")
                                .append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp")
                                .append(" Item : ").append(itemDetail.getArticleId()).append(" ").append(itemDetail.getArticleName()).append("<br>")
                                .append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp")
                                .append(" Order Quantity can not be less than item MOQ(").append(df.format(orderItemDetail.getMinOrderQty())).append(")!")
                                .append("&nbsp;Still need (")
                                .append(df.format(orderItemDetail.getMinOrderQty().
                                        subtract(itemDetail.getOrderQty()))).
                                append(")!<br>");
                        checkFlg = false;
                    }
                }
            }
        }
        //失败提示
        if (!checkFlg) {
            // sb.delete(sb.lastIndexOf("<br>"), sb.length());
            return sb.toString();
        }
        return null;
    }

    @Override
    public Integer insertQuickCopy(String storeCd, String businessDate) {
        int i = 0,j =0;
        List<OrderItemDetailDTO> list = orderMapper.getMiddleListBy(storeCd);
        if(list.size() > 0){
            // 复制中间表数据
            i = orderMapper.insertItemByCopy(storeCd,businessDate);
            j = orderMapper.deleteByCopy(storeCd);
        }

        return i+j;
    }
}
