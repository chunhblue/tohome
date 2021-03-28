package cn.com.bbut.iy.itemmaster.serviceimpl.materialentry;

import cn.com.bbut.iy.itemmaster.dao.Cm9060Mapper;
import cn.com.bbut.iy.itemmaster.dao.inventory.InventoryVouchersMapper;
import cn.com.bbut.iy.itemmaster.dao.materialEntryMapper;
import cn.com.bbut.iy.itemmaster.dao.materialPlanMapper;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.cm9070.Cm9070ReturnDTO;
import cn.com.bbut.iy.itemmaster.dto.fsInventory.StocktakeItemDTOD;
import cn.com.bbut.iy.itemmaster.dto.materialentry.PI0100DTOE;
import cn.com.bbut.iy.itemmaster.dto.materialentry.StocktakeItemDTOE;
import cn.com.bbut.iy.itemmaster.dto.pi0100.ItemInStoreDto;
import cn.com.bbut.iy.itemmaster.dto.pi0100.PI0100DTO;
import cn.com.bbut.iy.itemmaster.dto.pi0100.StocktakeItemDTO;
import cn.com.bbut.iy.itemmaster.dto.pi0100c.MaterialDTO;
import cn.com.bbut.iy.itemmaster.dto.pi0100c.PI0100DTOC;
import cn.com.bbut.iy.itemmaster.dto.pi0100c.PI0100ParamDTOC;
import cn.com.bbut.iy.itemmaster.dto.pi0100c.StocktakeItemDTOC;
import cn.com.bbut.iy.itemmaster.dto.rtInventory.RTInventoryQueryDTO;
import cn.com.bbut.iy.itemmaster.dto.rtInventory.RealTimeDto;
import cn.com.bbut.iy.itemmaster.dto.rtInventory.RtInvContent;
import cn.com.bbut.iy.itemmaster.dto.rtInventory.SaveInventoryQty;
import cn.com.bbut.iy.itemmaster.entity.base.Cm9060;
import cn.com.bbut.iy.itemmaster.service.CM9060Service;
import cn.com.bbut.iy.itemmaster.service.Ma1200Service;
import cn.com.bbut.iy.itemmaster.service.RealTimeInventoryQueryService;
import cn.com.bbut.iy.itemmaster.service.SequenceService;
import cn.com.bbut.iy.itemmaster.service.cm9070.Cm9070Service;
import cn.com.bbut.iy.itemmaster.service.materialentry.materialEntryService;
import cn.com.bbut.iy.itemmaster.service.stocktake.StocktakePlanService;
import cn.com.bbut.iy.itemmaster.serviceimpl.RealTimeInventoryQueryServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @ClassName materialEntryServiceImpl
 * @Description TODO
 * @Author Administrator
 * @Date 2020/3/24 11:20
 * @Version 1.0
 */

@Service
@Slf4j
public class materialEntryServiceImpl implements materialEntryService {

    @Autowired
    private materialEntryMapper materialentryMapper;

    @Autowired
    private materialPlanMapper materialplanMapper;

    @Autowired
    private SequenceService sequenceService;


    @Autowired
    private Cm9060Mapper cm9060Mapper;

    @Autowired
    private StocktakePlanService planService;
    @Value("${esUrl.inventoryUrl}")
    private String inventoryUrl;
    @Autowired
    private RealTimeInventoryQueryService rtInventoryService;
    @Autowired
    private CM9060Service cm9060Service;
    @Autowired
    private RealTimeInventoryQueryService rtService;
    @Autowired
    private Ma1200Service ma1200Service;

    @Override
    public GridDataDTO<PI0100DTOC> search(PI0100ParamDTOC pi0100Param) {
        int count = materialplanMapper.selectCountByParam(pi0100Param);

        if (count < 1) {
            return new GridDataDTO<PI0100DTOC>();
        }
        List<PI0100DTOC> _list = materialplanMapper.search(pi0100Param);

        GridDataDTO<PI0100DTOC> data = new GridDataDTO<PI0100DTOC>(_list,pi0100Param.getPage(), count, pi0100Param.getRows());
        return data;
    }

    @Override
    public GridDataDTO<MaterialDTO> storeAllItem(ItemInStoreDto param) {
        List<String> articles = new ArrayList<>();
        List<MaterialDTO> _list= materialplanMapper.selectAllItem(param);
//        Integer count= materialplanMapper.selectCountItem(param);
        for (MaterialDTO dto:_list) {
            articles.add(dto.getArticleId());
        }
        String inEsTime = cm9060Service.getValByKey("1206");
        //拼接url，转义参数
        String connUrl = inventoryUrl + "GetRelTimeInventory/" +  param.getStoreCd()
                + "/*/*/*/*/*/" + inEsTime+"/*/*";
        List<RTInventoryQueryDTO> rTdTOList = rtInventoryService.getStockList(articles,connUrl);
        if(rTdTOList.size()>0){
            for(RTInventoryQueryDTO rtDto:rTdTOList){
                for(MaterialDTO articlelist:_list){
                    if(rtDto.getItemCode().equals(articlelist.getArticleId())){
                        articlelist.setStockQty(rtDto.getRealtimeQty().toString()); // 实时库存
                    }
                }
            }
        }
        GridDataDTO<MaterialDTO> data = new GridDataDTO<MaterialDTO>();
        data.setRows(_list);

        return  data ;
    }

    @Override
    public PI0100DTOC getInvenDataPio(PI0100DTOC pi0100c) {
        if (StringUtils.isEmpty(pi0100c.getPiCd())) {
            return null;
        } // 获取主档信息
        List<String> articles = new ArrayList<>();
        List<StocktakeItemDTOC> list= materialentryMapper.getPI0140ByPrimary(pi0100c.getPiCd());
        for(StocktakeItemDTOC item:list){
            articles.add(item.getArticleId());
        }
        String inEsTime = cm9060Service.getValByKey("1206");
        //拼接url，转义参数
        String connUrl = inventoryUrl + "GetRelTimeInventory/" +  pi0100c.getStoreCd()
                + "/*/*/*/*/*/" + inEsTime+"/*/*";
        List<RTInventoryQueryDTO> rTdTOList = rtInventoryService.getStockList(articles,connUrl);
        if(rTdTOList.size()>0){
            for(RTInventoryQueryDTO rtDto:rTdTOList){
                for(StocktakeItemDTOC stockDto:list){
                    if(rtDto.getItemCode().equals(stockDto.getArticleId())){
                        stockDto.setStockQty(rtDto.getRealtimeQty().toString()); // 实时库存
                    }
                }
            }
        }

        pi0100c.setItemList(list);
        return pi0100c;


    }

    @Override
    public PI0100DTOC getData(String piCd) {
        if (StringUtils.isEmpty(piCd)) {
            return null;
        }

        // 获取主档信息
        PI0100DTOC pi0100c = materialplanMapper.getPI0100ByPrimary(piCd);
        if (pi0100c==null) {
            return null;
        }
        // 格式化日期
        String date = fomatData(pi0100c.getPiDate());
        pi0100c.setPiDate(date);
        List<StocktakeItemDTOC> list= materialentryMapper.getPI0140ByPrimary(piCd);
        pi0100c.setItemList(list);
        return pi0100c;
    }

//    @Override
//    public PI0100DTOC getInvenData(String piCd) {
////        Cm9060 cm9060 = cm9060Mapper.selectByPrimaryKey("0000");
//        if (StringUtils.isEmpty(piCd)) {
//            return null;
//        } // 获取主档信息
//        PI0100DTOC pi0100c = materialplanMapper.getPI0100ByPrimary(piCd);
//        if (pi0100c==null) {
//            return null;
//        }
//        // 格式化日期
//        String date = fomatData(pi0100c.getPiDate());
//        pi0100c.setPiDate(date);
//        List<String> articles = new ArrayList<>();
//        List<StocktakeItemDTOC> list= materialentryMapper.getPI0140ByPrimary(piCd);
//        for(StocktakeItemDTOC item:list){
//            articles.add(item.getArticleId());
//        }
//        String inEsTime = cm9060Service.getValByKey("1206");
//        //拼接url，转义参数
//        String connUrl = inventoryUrl + "GetRelTimeInventory/" +  pi0100c.getStoreCd()
//                + "/*/*/*/*/*/" + inEsTime+"/*/*";
//        List<RTInventoryQueryDTO> rTdTOList = rtInventoryService.getStockList(articles,connUrl);
//        if(rTdTOList.size()>0){
//            for(RTInventoryQueryDTO rtDto:rTdTOList){
//                for(StocktakeItemDTOC stockDto:list){
//                    if(rtDto.getItemCode().equals(stockDto.getArticleId())){
//                        stockDto.setStockQty(rtDto.getRealtimeQty().toString()); // 实时库存
//                    }
//                }
//            }
//        }
//
//        pi0100c.setItemList(list);
//        return pi0100c;
//
//
//    }


    @Override
    public StocktakeItemDTOC getItemInfo(String itemCode,String storeCd) {

        if (StringUtils.isEmpty(itemCode) || StringUtils.isEmpty(storeCd)) {
            return null;
        }
        String businessDate = getBusinessDate();
        return materialentryMapper.getItemInfo(itemCode,businessDate,storeCd);
    }


    @Override
    @Transactional
    public PI0100DTOC insertData(List<StocktakeItemDTOC> stocktakeItemList, PI0100DTOC item, HttpServletRequest request, HttpSession session,String detailType) {
        String dateStr = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());
        String ymd = dateStr.split("-")[0];
        String hms = dateStr.split("-")[1];
          Integer count=0;
        if (item.getPiCd()==null||StringUtils.isEmpty(item.getPiCd())) {
            count++;
            // 新增
            //获取序列号
            // String piCd = sequenceService.getSequence("pi0155_pi_cd_seq");
            String piCd = sequenceService.getSequence("pi0155_irm_id_seq","IRM",item.getStoreCd());
            if(org.apache.commons.lang.StringUtils.isBlank(piCd)){

                //获取序列失败
                log.error("获取序列失败 getSequence: {}", "pi0155_irm_id_seq");
                RuntimeException e = new RuntimeException("获取序列失败[ pi0155_irm_id_seq ]");
                throw e;
            }
            item.setPiCd(piCd);
            item.setCreateYmd(ymd);
            item.setCreateHms(hms);
            // 补充明细数据
            for (StocktakeItemDTOC dto : stocktakeItemList) {
                dto.setPiCd(item.getPiCd());
                dto.setStoreCd(item.getStoreCd());
                dto.setPiCd(item.getPiCd());
            }

            try {
                // 保存数据
                // 保存头档
                materialentryMapper.saveItem(item);
                // 保存明细
                if (count>0){
                    materialentryMapper.saveAllItemIn(stocktakeItemList);
                }else {
                    materialentryMapper.saveAllItem(stocktakeItemList);
                }
                Boolean checkFlg = checkRawMaterialItemStock(detailType,item.getStoreCd(),stocktakeItemList);
                if(!checkFlg){
                    item.setPiCd(null);
                }
                return item;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            // 修改
            // 补充明细数据
            for (StocktakeItemDTOC dto : stocktakeItemList) {
                dto.setPiCd(item.getPiCd());
                dto.setPiCd(item.getPiCd());
            }
            item.setUpdateYmd(ymd);
            item.setUpdateHms(hms);

            try {
                // 修改头档
                materialentryMapper.updateItem(item);
                // 修改明细
                // 先删除, 再添加
                materialentryMapper.deleteByPicd(item.getPiCd());
                materialentryMapper.saveAllItem(stocktakeItemList);
                Boolean checkFlg = checkRawMaterialItemStock(detailType,item.getStoreCd(),stocktakeItemList);
                if(!checkFlg){
                    item.setPiCd(null);
                }
                return item;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    /**
     *格式化日期
     */
    private String fomatData(String piDate){
        if (StringUtils.isEmpty(piDate)) {
            return "";
        }
        String year = piDate.substring(0, 4);
        String month = piDate.substring(4, 6);
        String day = piDate.substring(6, 8);
        return day+"/"+month+"/"+year;
    }

    /**
     * 获取当前业务日期
     */
    private String getBusinessDate() {
        Cm9060 dto = cm9060Mapper.selectByPrimaryKey("0000");
        return dto.getSpValue();
    }

    /**
     * Cost Item Adjustment Management 费用商品
     * @param detailType
     * @param storeCd
     * @param stockItemList
     * @return
     */
    public boolean checkRawMaterialItemStock(String detailType,String storeCd,List<StocktakeItemDTOC> stockItemList){
        boolean checkFlg = true;

        List<String> parentList = new ArrayList<>();
        List<SaveInventoryQty> saveRtQtyList = new ArrayList<>();
        if(stockItemList.size()>0){
            for(int i=0;i<stockItemList.size();i++){
                StocktakeItemDTOC stockTake = stockItemList.get(i);
                SaveInventoryQty saveDetail = new SaveInventoryQty();
                saveDetail.setDetailType(detailType);
                saveDetail.setStoreCd(storeCd);
                saveDetail.setArticleId(stockTake.getArticleId());

                saveDetail.setInventoryQty(Float.parseFloat(stockTake.getQty().toString()));
                saveRtQtyList.add(saveDetail);
                parentList.add(stockTake.getArticleId());
            }

            // List转jackJosn字符串
            String articleIdListJson;
            boolean checkData = true;
            try {
                if(parentList.size() == 0){
                    return false;
                }
                String inEsTime = cm9060Service.getValByKey("1206");
                articleIdListJson = new ObjectMapper().writeValueAsString(parentList);

                String connUrl = inventoryUrl + "GetRelTimeInventory/"+"/"+storeCd
                        +"/*/*/*/*/*/" + inEsTime+"/*/*";
                String urlData = RealTimeInventoryQueryServiceImpl.RequestPost(articleIdListJson,connUrl);
                if(urlData == null || "".equals(urlData)){
                    String message = "Failed to connect to live inventory data！";
                    checkData = false;
                }

                if(checkData){
                    Gson gson = new Gson();
                    // 获取第一层的信息
                    ArrayList<RtInvContent> rtInvContent2 = gson.fromJson(urlData,new TypeToken<List<RtInvContent>>() {}.getType());

                    RtInvContent rtInvContent = rtInvContent2.get(0);
                    if(rtInvContent == null){
                        rtInvContent = new RtInvContent();
                    }
                    String content = rtInvContent.getContent();
                    // 获取第二层的信息
                    ArrayList<RealTimeDto> realTimeDto2 = gson.fromJson(content,new TypeToken<List<RealTimeDto>>() {}.getType());
                    if(realTimeDto2.size()>0) {
                        for(SaveInventoryQty saveQty : saveRtQtyList){
                            for (RealTimeDto realTimeDto : realTimeDto2) {
                                if (realTimeDto.getArticle_id().equals(saveQty.getArticleId())) {
                                    // 计算实时库存数量
                                    BigDecimal rTimeQty = realTimeDto.getOn_hand_qty().add(realTimeDto.getReceive_qty().add(realTimeDto.getReceive_corr_qty()))
                                            .add(realTimeDto.getAdjustment_qty()).subtract(realTimeDto.getTransfer_out_qty().add(realTimeDto.getTransfer_out_corr_qty()))
                                            .subtract(realTimeDto.getSale_qty()).subtract(realTimeDto.getWrite_off_qty()).add(realTimeDto.getTransfer_in_qty().add(realTimeDto.getTransfer_in_corr_qty()))
                                            .subtract(realTimeDto.getReturn_qty().add(realTimeDto.getReturn_corr_qty()));
                                    saveQty.setRealtimeQty(Float.parseFloat(rTimeQty.toString()));
                                    saveQty.setInventoryQty(saveQty.getInventoryQty()-saveQty.getRealtimeQty());
                                }
                            }
                        }
                    }
                }

                // 修正商品实时库存
                RtInvContent rtInvContent = rtService.saveRtQtyListToEs(saveRtQtyList);
                if(!rtInvContent.getStatus().equals("0")){
                    checkFlg = false;
                }

                // 修正bi库存
                String rtBi = rtService.saveBIrtQty(saveRtQtyList,detailType,storeCd);
                if(rtBi == null){
                    checkFlg = false;
                }

                // 判断是否存在group商品母货号
                List<SaveInventoryQty> groupSavertList = new ArrayList<>();
                List<String> result = ma1200Service.checkList(parentList);
                log.info("<<<result:"+result.size());
                if(result.size()>0){
                    // 取得子商品明细
                    List<Map<String, String>> _result = ma1200Service.getChildDetail(result);
                    if(_result.size()>0){
                        for (Map<String, String> map : _result) {
                            for (SaveInventoryQty saveQty:saveRtQtyList) {
                                if(saveQty.getArticleId().equals(map.get("parentArticleId"))){
                                    float qty1 = saveQty.getInventoryQty();
                                    // 对子商品进行库存操作
                                    float articleQty = qty1*Integer.parseInt(map.get("childQty"));
                                    String childeArticleId = map.get("childArticleId");
                                    SaveInventoryQty saveGroupDetail = new SaveInventoryQty();
                                    saveGroupDetail.setArticleId(childeArticleId);
                                    saveGroupDetail.setInventoryQty(articleQty);
                                    saveGroupDetail.setDetailType(detailType);
                                    saveGroupDetail.setStoreCd(storeCd);
                                    groupSavertList.add(saveGroupDetail);
                                }
                            }
                        }
                    }
                    // 修正子商品实时库存
                    RtInvContent rtChildContent = rtService.saveRtQtyListToEs(groupSavertList);
                    if (!rtChildContent.getStatus().equals("0")) {
                        checkFlg = false;
                    }
                    // 修正bi库存
                    String groupRtBi = rtService.saveBIrtQty(groupSavertList,detailType,storeCd);
                    if(groupRtBi == null){
                        checkFlg = false;
                    }
                }

                // 判断是否存在BOM商品母货号
                List<SaveInventoryQty> bomSavertList = new ArrayList<>();
                List<String> bomResult = ma1200Service.checkBOMList(parentList);
                if(bomResult.size()>0){
                    // 取得子商品明细
                    List<Map<String, String>> _result = ma1200Service.getBOMChildDetail(bomResult);
                    if (_result.size() > 0) {
                        for (Map<String, String> map : _result) {
                            for (SaveInventoryQty saveQty : saveRtQtyList) {
                                if (saveQty.getArticleId().equals(map.get("parentArticleId"))) {
                                    float qty1 = saveQty.getInventoryQty();
                                    // 对子商品进行库存操作
                                    float articleQty = qty1*Float.parseFloat(map.get("childQty"));
                                    String childeArticleId = map.get("childArticleId");
                                    SaveInventoryQty saveBomDetail = new SaveInventoryQty();
                                    saveBomDetail.setArticleId(childeArticleId);
                                    saveBomDetail.setInventoryQty(articleQty);
                                    saveBomDetail.setDetailType(detailType);
                                    saveBomDetail.setStoreCd(storeCd);
                                    bomSavertList.add(saveBomDetail);
                                }
                            }
                        }
                        // 修正子商品实时库存
                        RtInvContent rtChildContent = rtService.saveRtQtyListToEs(bomSavertList);
                        if (!rtChildContent.getStatus().equals("0")) {
                            checkFlg = false;
                        }
                        // 修正bi库存
                        String groupRtBi = rtService.saveBIrtQty(bomSavertList,detailType,storeCd);
                        if(groupRtBi == null){
                            checkFlg = false;
                        }
                    }
                }
                return checkFlg;

            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

        }

        return checkFlg;
    }
}
