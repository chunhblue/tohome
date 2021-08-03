package cn.com.bbut.iy.itemmaster.serviceimpl;


import cn.com.bbut.iy.itemmaster.dao.Cm9060Mapper;
import cn.com.bbut.iy.itemmaster.dao.MA4320Mapper;
import cn.com.bbut.iy.itemmaster.dao.RealTimeInventoryQueryMapper;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.pi0100c.StocktakeItemDTOC;
import cn.com.bbut.iy.itemmaster.dto.promotion.PromotionParamDTO;
import cn.com.bbut.iy.itemmaster.dto.rtInventory.*;
import cn.com.bbut.iy.itemmaster.entity.base.Cm9060;
import cn.com.bbut.iy.itemmaster.service.CM9060Service;
import cn.com.bbut.iy.itemmaster.service.Ma4320Service;
import cn.com.bbut.iy.itemmaster.service.RealTimeInventoryQueryService;
import cn.com.bbut.iy.itemmaster.util.Utils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sun.net.www.protocol.http.HttpURLConnection;

import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service("RealTimeInventoryQueryServiceImpl")
public class RealTimeInventoryQueryServiceImpl implements RealTimeInventoryQueryService {

    @Autowired
    RealTimeInventoryQueryMapper realTimeInventoryQueryMapper;

    @Autowired
    private Cm9060Mapper cm9060Mapper;

    @Value("${esUrl.inventoryUrl}")
    private String inventoryUrl;

    @Autowired
    private CM9060Service cm9060Service;
    @Autowired
    private MA4320Mapper ma4320Mapper;
    @Autowired
    private Ma4320Service ma4320Service;
    @Override
    public GridDataDTO<RTInventoryQueryDTO> getInventoryList(RTInventoryQueryParamDTO rTParamDTO) {
        String inEsTime = cm9060Service.getValByKey("1206");
        String businessDate = getBusinessDate();
        rTParamDTO.setBusinessDate(businessDate);
        long count = realTimeInventoryQueryMapper.selectCountByCondition(rTParamDTO);
        if(count < 1){
            return new GridDataDTO<>();
        }
        GridDataDTO<RTInventoryQueryDTO> data = new GridDataDTO<>(null,
                rTParamDTO.getPage(), 0, rTParamDTO.getRows());
        List<String> articleIdList = new ArrayList<>();
        List<RTInventoryQueryDTO> _list = realTimeInventoryQueryMapper.InventoryQueryBy(rTParamDTO);
        if(_list.size()>0){
            for(RTInventoryQueryDTO rtDto:_list){
                articleIdList.add(rtDto.getItemCode());
            }
        }


        try {
            // List转jackJosn字符串
            String articleIdListJson = new ObjectMapper().writeValueAsString(articleIdList);

            String connUrl = inventoryUrl + "GetRelTimeInventory/"+"/"+rTParamDTO.getStoreCd()
                    +"/*/*/*/*/*/" + inEsTime+"/*/*";
           /** String urlData = RequestPost(articleIdListJson,connUrl);
            if(urlData == null || "".equals(urlData)){
                String message = "Failed to connect to live inventory data！";
                data.setMessage(message);
                return data;
            }
            Gson gson = new Gson();
            // 获取第一层的信息
            ArrayList<RtInvContent> rtInvContent2 = new ArrayList<>();
            String[] str = urlData.split("}");
            if(str.length<=1){
                RtInvContent param = gson.fromJson(urlData, RtInvContent.class);
                if("500".equals(param.getStatus()) || param.getContent() == null){
                    String message = "Failed to connect to live inventory data！";
                    data.setMessage(message);
                    return data;
                }
            }else {
                rtInvContent2 = gson.fromJson(urlData, new TypeToken<List<RtInvContent>>() {
                }.getType());
            }

            RtInvContent rtInvContent = rtInvContent2.get(0);
            if(rtInvContent == null){
                rtInvContent = new RtInvContent();
            }
            String content = rtInvContent.getContent();
            // 获取第二层的信息
            ArrayList<RealTimeDto> realTimeDto2 = gson.fromJson(content,new TypeToken<List<RealTimeDto>>() {}.getType());
            if(realTimeDto2.size()>0) {
                for (RTInventoryQueryDTO rtDto : _list) {
                    for (RealTimeDto realTimeDto : realTimeDto2) {
                        if (realTimeDto.getArticle_id().equals(rtDto.getItemCode())) {
                            rtDto.setAdjustQty(realTimeDto.getAdjustment_qty()); // 当日库存调整数量
                            rtDto.setTransferOutQty(realTimeDto.getTransfer_out_qty().add(realTimeDto.getTransfer_out_corr_qty()));//调拨--调出数量+调出修正
                            rtDto.setOnHandQty(realTimeDto.getOn_hand_qty());// 昨日库存数量
                            rtDto.setSaleQty(realTimeDto.getSale_qty());// 当日销售数量
                            rtDto.setScrapQty(realTimeDto.getWrite_off_qty());//报废数量
                            rtDto.setTransferInQty(realTimeDto.getTransfer_in_qty().add(realTimeDto.getTransfer_in_corr_qty()));//调拨--调入数量+调入修正
                            rtDto.setStoreReturnQty(realTimeDto.getReturn_qty().add(realTimeDto.getReturn_corr_qty()));//退货数量 + 退货更正数量
                            rtDto.setOnOrderQty(realTimeDto.getOn_order_qty());// 在途数量
                            rtDto.setReceiveQty(realTimeDto.getReceive_qty().add(realTimeDto.getReceive_corr_qty()));// 当日收货数量 + 收货更正数量
                            // 计算实时库存数量
                            BigDecimal rTimeQty = realTimeDto.getOn_hand_qty().add(realTimeDto.getReceive_qty().add(realTimeDto.getReceive_corr_qty()))
                                    .add(realTimeDto.getAdjustment_qty()).subtract(realTimeDto.getTransfer_out_qty().add(realTimeDto.getTransfer_out_corr_qty()))
                                    .subtract(realTimeDto.getSale_qty()).subtract(realTimeDto.getWrite_off_qty()).add(realTimeDto.getTransfer_in_qty().add(realTimeDto.getTransfer_in_corr_qty()))
                                    .subtract(realTimeDto.getReturn_qty().add(realTimeDto.getReturn_corr_qty()));
                            rtDto.setRealtimeQty(rTimeQty);
                        }
                    }
                }
            } */

         data = new GridDataDTO<>(_list,
                rTParamDTO.getPage(), count, rTParamDTO.getRows());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return data;
    }


    public GridDataDTO<RTInventoryQueryDTO> getList(RTInventoryQueryParamDTO rTParamDTO) {
        String inEsTime = cm9060Service.getValByKey("1206");
        String businessDate = getBusinessDate();
        rTParamDTO.setBusinessDate(businessDate);

        int page = rTParamDTO.getPage();
        int rows = rTParamDTO.getRows();
        String itemCode = rTParamDTO.getItemCode();
        String depId = rTParamDTO.getDepId();
        String pmaId = rTParamDTO.getPmaId();
        String categoryId = rTParamDTO.getCategoryId();
        String subCategoryId = rTParamDTO.getSubCategoryId();
        String barcode = rTParamDTO.getItemBarcode();
        if(barcode == null || "".equals(barcode)) {
            barcode = "*";
        }
        if(itemCode == null || "".equals(itemCode)){
            itemCode = "*";
        }
        if(depId == null || "".equals(depId)){
            depId = "*";
        }
        if(pmaId == null || "".equals(pmaId)){
            pmaId = "*";
        }
        if(categoryId == null || "".equals(categoryId)){
            categoryId = "*";
        }
        if(subCategoryId == null || "".equals(subCategoryId)){
            subCategoryId = "*";
        }

        GridDataDTO<RTInventoryQueryDTO> data = new GridDataDTO<>(null,
                page, 0, rows);

        // Area Manager、Operation Manager、Vendor Code/Name是否为空
        if((rTParamDTO.getOfcCode() != null && !rTParamDTO.getOfcCode().equals(""))
                || (rTParamDTO.getOcCode() != null && !rTParamDTO.getOcCode().equals(""))
                || (rTParamDTO.getVendorId() != null && !rTParamDTO.getVendorId().equals(""))
                || (rTParamDTO.getItemBarcode() != null && !rTParamDTO.getItemBarcode().equals(""))){
            long count = realTimeInventoryQueryMapper.selectCountByCondition(rTParamDTO);
            if(count < 1){
                return new GridDataDTO<>();
            }

            List<RTInventoryQueryDTO> _list = realTimeInventoryQueryMapper.InventoryQueryBy(rTParamDTO);

            data = new GridDataDTO<>(_list,
                    rTParamDTO.getPage(), count, rTParamDTO.getRows());

            for(RTInventoryQueryDTO rtDto :_list) {

                //拼接url，转义参数
//                String connUrl = inventoryUrl + "GetRelTimeInventory/" + rTParamDTO.getStoreCd() + "/"
//                        + rtDto.getItemCode() + "/*/*/*/*/" + inEsTime+"/*/*";
                String connUrl = inventoryUrl + "GetRelTimeInventory/"+rTParamDTO.getStoreCd()+"/"+ itemCode
                        +"/"+depId+"/"+ pmaId+"/"+categoryId+"/"+subCategoryId+"/"+ inEsTime+"/"+page+"/"+rows;
                String urlData = getConnUrlData(connUrl);
                Gson gson = new Gson();
                if(urlData == null || "".equals(urlData)){
                    String message = "Failed to connect to live inventory data！";
                    data.setMessage(message);
                    return data;
                }

                // 获取第一层的信息
                ArrayList<RtInvContent> rtInvContent2 = new ArrayList<>();
                String[] str = urlData.split("}");
                if(str.length<=1){
                    RtInvContent param = gson.fromJson(urlData, RtInvContent.class);
                    if("500".equals(param.getStatus()) || param.getContent() == null){
                        String message = "Failed to connect to live inventory data！";
                        data.setMessage(message);
                        return data;
                    }
                }else {
                    rtInvContent2 = gson.fromJson(urlData, new TypeToken<List<RtInvContent>>() {
                    }.getType());
                }

                RtInvContent rtInvContent = rtInvContent2.get(0);
                if(rtInvContent == null){
                    rtInvContent = new RtInvContent();
                }
                String content = rtInvContent.getContent();
                // 获取第二层的信息
                ArrayList<RealTimeDto> realTimeDto2 = gson.fromJson(content,new TypeToken<List<RealTimeDto>>() {}.getType());
                RealTimeDto realTimeDto = realTimeDto2.get(0);
                if(realTimeDto == null){
                    realTimeDto = new RealTimeDto();
                }
                rtDto.setAdjustQty(realTimeDto.getAdjustment_qty()); // 当日库存调整数量
                rtDto.setTransferOutQty(realTimeDto.getTransfer_out_qty().add(realTimeDto.getTransfer_out_corr_qty()));//调拨--调出数量+调出修正
                rtDto.setOnHandQty(realTimeDto.getOn_hand_qty());// 昨日库存数量
                rtDto.setSaleQty(realTimeDto.getSale_qty());// 当日销售数量
                rtDto.setScrapQty(realTimeDto.getWrite_off_qty());//报废数量
                rtDto.setTransferInQty(realTimeDto.getTransfer_in_qty().add(realTimeDto.getTransfer_in_corr_qty()));//调拨--调入数量+调入修正
                rtDto.setStoreReturnQty(realTimeDto.getReturn_qty().add(realTimeDto.getReturn_corr_qty()));//退货数量 + 退货更正数量
                rtDto.setOnOrderQty(realTimeDto.getOn_order_qty());// 在途数量
                rtDto.setReceiveQty(realTimeDto.getReceive_qty().add(realTimeDto.getReceive_corr_qty()));// 当日收货数量 + 收货更正数量
                // 计算实时库存数量
                BigDecimal rTimeQty = realTimeDto.getOn_hand_qty().add(realTimeDto.getReceive_qty().add(realTimeDto.getReceive_corr_qty()))
                        .add(realTimeDto.getAdjustment_qty()).subtract(realTimeDto.getTransfer_out_qty().add(realTimeDto.getTransfer_out_corr_qty()))
                        .subtract(realTimeDto.getSale_qty()).subtract(realTimeDto.getWrite_off_qty()).add(realTimeDto.getTransfer_in_qty().add(realTimeDto.getTransfer_in_corr_qty()))
                        .subtract(realTimeDto.getReturn_qty().add(realTimeDto.getReturn_corr_qty())).add(realTimeDto.getOn_order_qty());
                rtDto.setRealtimeQty(rTimeQty);
            }

            data = new GridDataDTO<>(_list,
                    rTParamDTO.getPage(), count, rTParamDTO.getRows());
        }else{

            //拼接url，转义参数
            String connUrl = inventoryUrl + "GetRelTimeInventory/"+rTParamDTO.getStoreCd()+"/"+ itemCode
                    +"/"+depId+"/"+ pmaId+"/"+categoryId+"/"+subCategoryId+"/"+ inEsTime+"/"+page+"/"+rows;
           String urlData = getConnUrlData(connUrl);
            if(urlData == null || "".equals(urlData)){
                String message = "Failed to connect to live inventory data！";
                data.setMessage(message);
                return data;
            }
            Gson gson = new Gson();
            // 获取第一层的信息
            ArrayList<RtInvContent> rtInvContent2 = new ArrayList<>();
            String[] str = urlData.split("}");
            if(str.length<=1){
                RtInvContent param = gson.fromJson(urlData, RtInvContent.class);
                if("500".equals(param.getStatus()) || param.getContent() == null){
                    String message = "Failed to connect to live inventory data！";
                    data.setMessage(message);
                    return data;
                }
            }else {
                rtInvContent2 = gson.fromJson(urlData, new TypeToken<List<RtInvContent>>() {
                }.getType());
            }

            RtInvContent rtInvContent = rtInvContent2.get(0);
            if(rtInvContent == null){
                rtInvContent = new RtInvContent();
            }
            String content = rtInvContent.getContent();
            // 获取第二层的信息（多条）
            ArrayList<RealTimeDto> realTimeDto2 = gson.fromJson(content,new TypeToken<List<RealTimeDto>>() {}.getType());
            ArrayList itemCodeList = new ArrayList();
            for(int i=0;i<realTimeDto2.size();i++){
                RealTimeDto realTimeDto = realTimeDto2.get(i);
                if(realTimeDto == null){
                    realTimeDto = new RealTimeDto();
                }
                itemCodeList.add(realTimeDto.getArticle_id());
            }
            Collection<String> articles = itemCodeList;
            if(articles.size() == 0){
                return data;
            }
            rTParamDTO.setArticles(articles);
            List<RTInventoryQueryDTO> _list = realTimeInventoryQueryMapper.InventoryEsQuery(rTParamDTO);
            for(RTInventoryQueryDTO rtDto : _list){
                String _itemCode = rtDto.getItemCode();
                for(RealTimeDto realTimeDto : realTimeDto2){
                    if(_itemCode.equals(realTimeDto.getArticle_id())){
                        rtDto.setAdjustQty(realTimeDto.getAdjustment_qty()); // 当日库存调整数量
                        rtDto.setTransferOutQty(realTimeDto.getTransfer_out_qty().add(realTimeDto.getTransfer_out_corr_qty()));//调拨--调出数量+调出修正
                        rtDto.setOnHandQty(realTimeDto.getOn_hand_qty());// 昨日库存数量
                        rtDto.setSaleQty(realTimeDto.getSale_qty());// 当日销售数量
                        rtDto.setScrapQty(realTimeDto.getWrite_off_qty());//报废数量
                        rtDto.setTransferInQty(realTimeDto.getTransfer_in_qty().add(realTimeDto.getTransfer_in_corr_qty()));//调拨--调入数量+调入修正
                        rtDto.setStoreReturnQty(realTimeDto.getReturn_qty().add(realTimeDto.getReturn_corr_qty()));//退货数量 + 退货更正数量
                        rtDto.setOnOrderQty(realTimeDto.getOn_order_qty());// 在途数量
                        rtDto.setReceiveQty(realTimeDto.getReceive_qty().add(realTimeDto.getReceive_corr_qty()));// 当日收货数量 + 收货更正数量
                        // 计算实时库存数量
                        BigDecimal rTimeQty = realTimeDto.getOn_hand_qty().add(realTimeDto.getReceive_qty().add(realTimeDto.getReceive_corr_qty()))
                                .add(realTimeDto.getAdjustment_qty()).subtract(realTimeDto.getTransfer_out_qty().add(realTimeDto.getTransfer_out_corr_qty()))
                                .subtract(realTimeDto.getSale_qty()).subtract(realTimeDto.getWrite_off_qty()).add(realTimeDto.getTransfer_in_qty().add(realTimeDto.getTransfer_in_corr_qty()))
                                .subtract(realTimeDto.getReturn_qty().add(realTimeDto.getReturn_corr_qty()));
                        rtDto.setRealtimeQty(rTimeQty);
                    }
                }
            }
            data = new GridDataDTO<>(_list,
                    rTParamDTO.getPage(), rtInvContent.getSize(), rTParamDTO.getRows());
        }

        return data;
    }

    /**
     * 获取当前业务日期
     */
    private String getBusinessDate() {
        Cm9060 dto =  cm9060Mapper.selectByPrimaryKey("0000");
        return dto.getSpValue();
    }

    /**
     * 获取接口数据,返回json格式字符串,方法2
     * @param connUrl 接口路径
     *  参数保存在 connUrl中
     * @return
     */
    @Override
    public String getConnUrlData(String connUrl){

        HttpURLConnection conn = null;
        BufferedReader reader = null;
        String rs = null;
        try {
            //创建连接
            URL url = new URL(connUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setUseCaches(false);
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);
            conn.setInstanceFollowRedirects(false);
            conn.connect();

            //获取并解析数据
            InputStream is = conn.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            StringBuffer sb = new StringBuffer();
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sb.append(strRead);
            }
            rs = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        return rs;
    }

    /**
     * 获取实时库存数量
     * lch.2021.01.07
     */
    @Override
    public RTInventoryQueryDTO getRtInventory(String connUrl){

        RTInventoryQueryDTO data = new RTInventoryQueryDTO();
        BigDecimal rTimeQty = BigDecimal.ZERO;


        /**String urlData = getConnUrlData(connUrl);
        if(urlData == null || "".equals(urlData)){
            String message = "Failed to connect to live inventory data！";
            data.setMessage(message);
            return data;
        }
        Gson gson = new Gson();
        // 获取第一层的信息
        ArrayList<RtInvContent> rtInvContent2 = new ArrayList<>();
        String[] str = urlData.split("}");
        if(str.length<=1){
            RtInvContent param = gson.fromJson(urlData, RtInvContent.class);
            if("500".equals(param.getStatus()) || param.getContent() == null){
                String message = "Failed to connect to live inventory data！";
                data.setMessage(message);
                return data;
            }
        }else {
            rtInvContent2 = gson.fromJson(urlData, new TypeToken<List<RtInvContent>>() {
            }.getType());
        }

        RtInvContent rtInvContent = rtInvContent2.get(0);
        if(rtInvContent == null){
            rtInvContent = new RtInvContent();
        }
        String content = rtInvContent.getContent();
        // 获取第二层的信息（多条）
        ArrayList<RealTimeDto> realTimeDto2 = gson.fromJson(content,new TypeToken<List<RealTimeDto>>() {}.getType());
        if(realTimeDto2.size() == 0){
            data.setRealtimeQty(BigDecimal.ZERO);
            data.setMessage("Inventory quantity not found!");
            return data;
        }
        for(RealTimeDto realTimeDto : realTimeDto2){
            // 计算实时库存数量
            BigDecimal rtQty = realTimeDto.getOn_hand_qty().add(realTimeDto.getReceive_qty().add(realTimeDto.getReceive_corr_qty()))
                    .add(realTimeDto.getAdjustment_qty()).subtract(realTimeDto.getTransfer_out_qty().add(realTimeDto.getTransfer_out_corr_qty()))
                    .subtract(realTimeDto.getSale_qty()).subtract(realTimeDto.getWrite_off_qty()).add(realTimeDto.getTransfer_in_qty().add(realTimeDto.getTransfer_in_corr_qty()))
                    .subtract(realTimeDto.getReturn_qty().add(realTimeDto.getReturn_corr_qty()));
            rTimeQty = rTimeQty.add(rtQty);
        }*/
        data.setRealtimeQty(rTimeQty);
        return data;
    }

    /**
     * 获取多条item实时库存
     * @param connUrl
     * @return
     */
    @Override
    public List<RTInventoryQueryDTO> getRtInventoryList(String connUrl){

        List<RTInventoryQueryDTO> rtList = new ArrayList<>();

        String urlData = getConnUrlData(connUrl);
        if(urlData == null || "".equals(urlData)){
            String message = "Failed to connect to live inventory data！";
            return rtList;
        }
        Gson gson = new Gson();

        // 获取第一层的信息
        ArrayList<RtInvContent> rtInvContent2 = new ArrayList<>();
        String[] str = urlData.split("}");
        if(str.length<=1){
            RtInvContent param = gson.fromJson(urlData, RtInvContent.class);
            if("500".equals(param.getStatus()) || param.getContent() == null){
                String message = "Failed to connect to live inventory data！";
                return rtList;
            }
        }else {
            rtInvContent2 = gson.fromJson(urlData, new TypeToken<List<RtInvContent>>() {
            }.getType());
        }
        RtInvContent rtInvContent = rtInvContent2.get(0);
        if(rtInvContent == null){
            rtInvContent = new RtInvContent();
        }
        String content = rtInvContent.getContent();
        // 获取第二层的信息（多条）
        ArrayList<RealTimeDto> realTimeDto2 = gson.fromJson(content,new TypeToken<List<RealTimeDto>>() {}.getType());
        if(realTimeDto2.size() == 0){
            return rtList;
        }

        for(RealTimeDto realTimeDto : realTimeDto2){
            BigDecimal rTimeQty = BigDecimal.ZERO;
            RTInventoryQueryDTO data = new RTInventoryQueryDTO();
            // 计算实时库存数量
            BigDecimal rtQty = realTimeDto.getOn_hand_qty().add(realTimeDto.getReceive_qty().add(realTimeDto.getReceive_corr_qty()))
                    .add(realTimeDto.getAdjustment_qty()).subtract(realTimeDto.getTransfer_out_qty().add(realTimeDto.getTransfer_out_corr_qty()))
                    .subtract(realTimeDto.getSale_qty()).subtract(realTimeDto.getWrite_off_qty()).add(realTimeDto.getTransfer_in_qty().add(realTimeDto.getTransfer_in_corr_qty()))
                    .subtract(realTimeDto.getReturn_qty().add(realTimeDto.getReturn_corr_qty()));
            rTimeQty = rTimeQty.add(rtQty);
            data.setItemCode(realTimeDto.getArticle_id());
            data.setStoreCd(realTimeDto.getStore_cd());
            data.setRealtimeQty(rTimeQty); // 库存数量
            data.setSaleQty(realTimeDto.getSale_qty()); // 当日销售数量
            rtList.add(data);
        }
        return rtList;
    }

    /**
     * 获取多条item实时库存
     * @param connUrl
     * @return
     */
    @Override
    public List<RTInventoryQueryDTO> getStockList(List<String> articles,String connUrl){

        List<RTInventoryQueryDTO> rtList = new ArrayList<>();
        if(articles.size() < 1){
            String message = "Failed to found to article ID！";
            return rtList;
        }
        try {
            // List转jackJosn字符串
            String articleIdListJson = new ObjectMapper().writeValueAsString(articles);
            String urlData = RealTimeInventoryQueryServiceImpl.RequestPost(articleIdListJson,connUrl);
            if(urlData == null || "".equals(urlData)){
                String message = "Failed to connect to live inventory data！";
                return rtList;
            }
            Gson gson = new Gson();
            ArrayList<RtInvContent> rtInvContent2 = new ArrayList<>();
            String[] str = urlData.split("}");
            if(str.length<=1){
                RtInvContent param = gson.fromJson(urlData, RtInvContent.class);
                if("500".equals(param.getStatus()) || param.getContent() == null){
                    String message = "Failed to connect to live inventory data！";
                    return rtList;
                }
            }else {
                rtInvContent2 = gson.fromJson(urlData, new TypeToken<List<RtInvContent>>() {
                }.getType());
            }
            RtInvContent rtInvContent = rtInvContent2.get(0);
            if(rtInvContent == null){
                rtInvContent = new RtInvContent();
            }
            String content = rtInvContent.getContent();
            // 获取第二层的信息（多条）
            ArrayList<RealTimeDto> realTimeDto2 = gson.fromJson(content,new TypeToken<List<RealTimeDto>>() {}.getType());
            if(realTimeDto2.size() == 0){
                return rtList;
            }
            for(RealTimeDto realTimeDto : realTimeDto2){
                BigDecimal rTimeQty = BigDecimal.ZERO;
                RTInventoryQueryDTO data = new RTInventoryQueryDTO();
                // 计算实时库存数量
                BigDecimal rtQty = realTimeDto.getOn_hand_qty().add(realTimeDto.getReceive_qty().add(realTimeDto.getReceive_corr_qty()))
                        .add(realTimeDto.getAdjustment_qty()).subtract(realTimeDto.getTransfer_out_qty().add(realTimeDto.getTransfer_out_corr_qty()))
                        .subtract(realTimeDto.getSale_qty()).subtract(realTimeDto.getWrite_off_qty()).add(realTimeDto.getTransfer_in_qty().add(realTimeDto.getTransfer_in_corr_qty()))
                        .subtract(realTimeDto.getReturn_qty().add(realTimeDto.getReturn_corr_qty()));
                rTimeQty = rTimeQty.add(rtQty);
                data.setItemCode(realTimeDto.getArticle_id());
                data.setStoreCd(realTimeDto.getStore_cd());
                data.setRealtimeQty(rTimeQty); // 库存数量
                data.setSaleQty(realTimeDto.getSale_qty()); // 当日销售数量
                rtList.add(data);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return rtList;
    }


    /**
     * 调用ES库存保存方法
     * @param saveRtQtyList
     * @return
     */
    @Override
    public RtInvContent saveRtQtyListToEs(List<SaveInventoryQty> saveRtQtyList){
        RtInvContent rtInvContent = new RtInvContent();
        List<String> itemList = new ArrayList<>();
        for(int i=0;i<saveRtQtyList.size();i++){
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//            saveRtQtyList.get(i).setAccDate(sdf.format(new Date()));
            String nowDate = ma4320Service.getNowDate();
            String ymd = nowDate.substring(0,8);
            saveRtQtyList.get(i).setAccDate(Utils.getFormateDateStr(ymd));
            String detailType = saveRtQtyList.get(i).getDetailType();
            float qty = saveRtQtyList.get(i).getInventoryQty();
            saveRtQtyList.get(i).setInEsTime(ma4320Mapper.getNowDate());
            switch (detailType) {
                case "tmp_receive":
                    saveRtQtyList.get(i).setReceiveQty(qty);
                    break;
                case "tmp_return":
                    saveRtQtyList.get(i).setReturnQty(qty);
                    break;
                case "tmp_receive_corr":
                    saveRtQtyList.get(i).setReceiveCorrQty(qty);
                    break;
                case "tmp_return_corr":
                    saveRtQtyList.get(i).setReturnCorrQty(qty);
                    break;
                case "tmp_adjustment":
                    saveRtQtyList.get(i).setAdjustmentQty(qty);
                    break;
                case "tmp_write_off":
                    saveRtQtyList.get(i).setWriteOffQty(qty);
                    break;
                case "tmp_transfer_out":
                    saveRtQtyList.get(i).setTransferOutQty(qty);
                    break;
                case "tmp_transfer_in":
                    saveRtQtyList.get(i).setTransferInQty(qty);
                    break;
                case "tmp_transfer_out_corr":
                    saveRtQtyList.get(i).setTransferOutCorrQty(qty);
                    break;
                case "tmp_transfer_in_corr":
                    saveRtQtyList.get(i).setTransferInCorrQty(qty);
                    break;
                case "tmp_on_order":
                    saveRtQtyList.get(i).setOnOrderQty(qty); // 在途量
                    break;
                case "tmp_ci_adjustment":
                    saveRtQtyList.get(i).setAdjustmentQty(qty);
                    break;
                case "tmp_rms_adjustment":
                    saveRtQtyList.get(i).setAdjustmentQty(qty);
                    break;
            }

            itemList.add( saveRtQtyList.get(i).getArticleId());
        }
        Collection<String> articles = itemList;
        if(articles.size() == 0){
            rtInvContent.setStatus("5");
            return rtInvContent;
        }
        // 获取商品的大中小分类
        String businessDate = getBusinessDate();
        List<SaveInventoryQty> categories = realTimeInventoryQueryMapper.getMoreInformation(articles,businessDate);
        for(int i=0;i<categories.size();i++){
            for(int n=0;n<saveRtQtyList.size();n++){
                if(categories.get(i).getArticleId().equals(saveRtQtyList.get(n).getArticleId())){
                    saveRtQtyList.get(n).setDepCd(categories.get(i).getDepCd());
                    saveRtQtyList.get(n).setPmaCd(categories.get(i).getPmaCd());
                    saveRtQtyList.get(n).setCategoryCd(categories.get(i).getCategoryCd());
                    saveRtQtyList.get(n).setSubCategoryCd(categories.get(i).getSubCategoryCd());
                }
            }
        }

        try {
            // List转jackJosn字符串
            String jsonStr = new ObjectMapper().writeValueAsString(saveRtQtyList);

            String url = inventoryUrl+"/SaveRelTimeInventory";
//            String urlData = RequestPost(jsonStr,url);
            String urlData = null;
            if(urlData != null){
                rtInvContent = new Gson().fromJson(urlData,RtInvContent.class);
            }
            if(urlData == null || "".equals(urlData) || !"0".equals(rtInvContent.getStatus())){
               int re = realTimeInventoryQueryMapper.addRtQtyListToEs(saveRtQtyList);
               if(re > 0){
                   String message = "Failed to connect to ES,but save successfully！";
                   rtInvContent.setStatus("0");
                   rtInvContent.setMessage(message);
               }else {
                   rtInvContent.setStatus("10"); // ES链接失败，DB保存失败
               }
                return rtInvContent;
            }

        } catch (JsonProcessingException e) {
            rtInvContent.setMessage("类对象转jackJson字符串异常");
            e.printStackTrace();
        }
        return rtInvContent;
    }

    /**
     * 对BI系统的实时库存进行操作
     * @return
     */
    @Override
    public String saveBIrtQty(List<SaveInventoryQty> oldList,String detailType,String storeCd){
        if(detailType == null || "".equals(detailType)){
            return null;
        }
        List<SaveInventoryQty> saveRtQtyList = new ArrayList<>();
        if(oldList.size()>0){
            for(SaveInventoryQty saveOld:oldList){
                boolean checkFlg = true;
                for(SaveInventoryQty saveQtyNew : saveRtQtyList){
                    if(saveOld.getArticleId().equals(saveQtyNew.getArticleId())){
                        saveQtyNew.setInventoryQty(saveQtyNew.getInventoryQty()+saveOld.getInventoryQty());
                        checkFlg = false;
                    }
                }
                if(checkFlg){
                    saveRtQtyList.add(saveOld);
                }
            }
        }

        String str = null;
        switch (detailType) {
            case "tmp_receive":
                str = updateBiQty(storeCd,saveRtQtyList);
                return str;
            case "tmp_return":
                // 存入符库存后，将数量还原
                if (saveRtQtyList.size() > 0) {
                    for (SaveInventoryQty rtQty : saveRtQtyList) {
                        rtQty.setInventoryQty(-rtQty.getInventoryQty());
                    }
                }
                str = updateBiQty(storeCd,saveRtQtyList);
                if (saveRtQtyList.size() > 0) {
                    for (SaveInventoryQty rtQty : saveRtQtyList) {
                        rtQty.setInventoryQty(-rtQty.getInventoryQty());
                    }
                }
                return str;
            case "tmp_receive_corr":
                str = updateBiQty(storeCd,saveRtQtyList);
                return str;
            case "tmp_return_corr":
                if (saveRtQtyList.size() > 0) {
                    for (SaveInventoryQty rtQty : saveRtQtyList) {
                        rtQty.setInventoryQty(-rtQty.getInventoryQty());
                    }
                }
                str = updateBiQty(storeCd,saveRtQtyList);
                if (saveRtQtyList.size() > 0) {
                    for (SaveInventoryQty rtQty : saveRtQtyList) {
                        rtQty.setInventoryQty(-rtQty.getInventoryQty());
                    }
                }
                return str;
            case "tmp_adjustment":
                str = updateBiQty(storeCd,saveRtQtyList);
                return str;
            case "tmp_write_off":
                if (saveRtQtyList.size() > 0) {
                    for (SaveInventoryQty rtQty : saveRtQtyList) {
                        rtQty.setInventoryQty(-rtQty.getInventoryQty());
                    }
                }
                str = updateBiQty(storeCd,saveRtQtyList);
                if (saveRtQtyList.size() > 0) {
                    for (SaveInventoryQty rtQty : saveRtQtyList) {
                        rtQty.setInventoryQty(-rtQty.getInventoryQty());
                    }
                }
                return str;
            case "tmp_transfer_out":
                if (saveRtQtyList.size() > 0) {
                    for (SaveInventoryQty rtQty : saveRtQtyList) {
                        rtQty.setInventoryQty(-rtQty.getInventoryQty());
                    }
                }
                str = updateBiQty(storeCd,saveRtQtyList);
                if (saveRtQtyList.size() > 0) {
                    for (SaveInventoryQty rtQty : saveRtQtyList) {
                        rtQty.setInventoryQty(-rtQty.getInventoryQty());
                    }
                }
                return str;
            case "tmp_transfer_in":
                str = updateBiQty(storeCd,saveRtQtyList);
                return str;
            case "tmp_transfer_out_corr":
                if (saveRtQtyList.size() > 0) {
                    for (SaveInventoryQty rtQty : saveRtQtyList) {
                        rtQty.setInventoryQty(-rtQty.getInventoryQty());
                    }
                }
                str = updateBiQty(storeCd,saveRtQtyList);
                if (saveRtQtyList.size() > 0) {
                    for (SaveInventoryQty rtQty : saveRtQtyList) {
                        rtQty.setInventoryQty(-rtQty.getInventoryQty());
                    }
                }
                return str;
            case "tmp_transfer_in_corr":
                str = updateBiQty(storeCd,saveRtQtyList);
                return str;
            case "tmp_ci_adjustment":
                str = updateBiQty(storeCd,saveRtQtyList);
                return str;
            case "tmp_rms_adjustment":
                str = updateBiQty(storeCd,saveRtQtyList);
                return str;
            default:
        }
        return str;
    }

    // 修正Bi实时库存
    public String updateBiQty(String storeCd,List<SaveInventoryQty> saveRtQtyList){
        String msg = null;
        Collection<String> articles = new ArrayList<>();
        if (saveRtQtyList.size() > 0) {
            for (int i = 0; i < saveRtQtyList.size(); i++) {
                if (saveRtQtyList.get(i).getInventoryQty()==0.0 || saveRtQtyList.get(i).getInventoryQty()==null) {
                    saveRtQtyList.remove(i);
                } else {
                    articles.add(saveRtQtyList.get(i).getArticleId());
                }
            }
        }
        if(articles.size() == 0){
            return "No need to change inventory!";
        }
        // 取得该商品的平均价格
        String businessDate = getBusinessDate();
        List<SaveInventoryQty> saveavgCostList = realTimeInventoryQueryMapper.selectAvgCost(storeCd, articles, businessDate);
        if (saveavgCostList.size() > 0) {
            for (int i = 0; i < saveavgCostList.size(); i++) {
                for (SaveInventoryQty saveQty : saveRtQtyList) {
                    if (saveQty.getArticleId().equals(saveavgCostList.get(i).getArticleId())) {
                        saveQty.setAvgCostNotax(saveavgCostList.get(i).getAvgCostNotax());
                    }
                }
            }
        }

            // 查看当前articleId是否在当天存在库存
        List<StoreItemWarehousCk> addAllList = new ArrayList<>();

//        Date date = new Date();
//        String yearMonthDay = new SimpleDateFormat("yyyyMMdd").format(date);
//        String yearMonth = yearMonthDay.substring(0,6);
//        String warehousDate = new SimpleDateFormat("yyyy-MM-dd").format(date);

        String nowDate = ma4320Service.getNowDate();
        String ymd = nowDate.substring(0,8);
        String yearMonthDay = Utils.getFormateDateStr(ymd);
        String yearMonth = yearMonthDay.substring(0,6);
        String warehousDate=Utils.getTimeStamp(ymd);
        for(SaveInventoryQty swh :saveRtQtyList) {
            // 计算修改Bi库存后的金额
            BigDecimal gapsAmt = swh.getAvgCostNotax().multiply(new BigDecimal(swh.getInventoryQty()));

            StoreItemWarehousCk itemWarehousCk = new StoreItemWarehousCk();
            itemWarehousCk.setWarehousDate(warehousDate);
            itemWarehousCk.setCompanyCd("01");
            itemWarehousCk.setStoreCd(swh.getStoreCd());
            itemWarehousCk.setArticleId(swh.getArticleId());
            itemWarehousCk.setStockQty(new BigDecimal(swh.getInventoryQty()));
            itemWarehousCk.setStockAmt(gapsAmt);
            itemWarehousCk.setWarehousDays("< 30 days");
            itemWarehousCk.setYearMonth(yearMonth);
            if(swh.getInventoryQty()>0){
                itemWarehousCk.setUsingFlag("");
            }else {
                itemWarehousCk.setUsingFlag("0");
            }

            addAllList.add(itemWarehousCk);
        }
        if(addAllList.size()>0){
            int count = realTimeInventoryQueryMapper.addBitemWarehousCk(addAllList);
            if(count > 0){
                msg = "Add successfully!";
            }
        }
        return msg;
    }

    // 减少Bi实时库存（未完善）
    public String reductionBiQty(String storeCd,List<SaveInventoryQty> saveRtQtyList) {
        String articleId = null;Integer qty = 0;
        String msg = null;
        Collection<String> articles = new ArrayList<>();
        if (saveRtQtyList.size() > 0) {
            for (int i = 0; i < saveRtQtyList.size(); i++) {
                if (saveRtQtyList.get(i).getInventoryQty().equals(BigDecimal.ZERO)) {
                    saveRtQtyList.remove(i);
                } else {
                    articles.add(saveRtQtyList.get(i).getArticleId());
                }
            }
        }

        // 取得该商品的平均价格
        BigDecimal avgCostNotax = BigDecimal.ZERO;
        String businessDate = getBusinessDate();
        List<SaveInventoryQty> saveavgCostList = realTimeInventoryQueryMapper.selectAvgCost(storeCd,articles,businessDate);
        if (saveavgCostList.size() > 0) {
            for (int i = 0; i < saveavgCostList.size(); i++) {
                for (SaveInventoryQty saveQty : saveRtQtyList) {
                    if (saveQty.getArticleId().equals(saveavgCostList.get(i).getArticleId())) {
                        saveQty.setAvgCostNotax(saveavgCostList.get(i).getAvgCostNotax());
                    }
                }
            }
        }

        List<SaveInventoryQty> warehousDateList = realTimeInventoryQueryMapper.getBIDate(storeCd, articles);
        if (warehousDateList.size() == 0) { // 该商品没有库存，存一条负数库存数据
            // 计算修改Bi库存后的金额
            BigDecimal gapsAmt = new BigDecimal(qty * (-1)).multiply(avgCostNotax);

//            Date date = new Date();
//            String yearMonthDay = new SimpleDateFormat("yyyyMMdd").format(date);
//            String yearMonth = yearMonthDay.substring(0, 6);
//            String warehousDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
            String nowDate = ma4320Service.getNowDate();
            String ymd = nowDate.substring(0,8);
            String yearMonthDay = Utils.getFormateDateStr(ymd);
            String yearMonth = yearMonthDay.substring(0,6);
            String warehousDate=Utils.getTimeStamp(ymd);
            StoreItemWarehousCk itemWarehousCk = new StoreItemWarehousCk();
            itemWarehousCk.setWarehousDate(warehousDate);
            itemWarehousCk.setCompanyCd("01");
            itemWarehousCk.setStoreCd(storeCd);
            itemWarehousCk.setArticleId(articleId);
            itemWarehousCk.setStockQty(new BigDecimal(qty * (-1)));
            itemWarehousCk.setStockAmt(gapsAmt);
            itemWarehousCk.setWarehousDays("< 30 days");
            itemWarehousCk.setYearMonth(yearMonth);

//            int count = realTimeInventoryQueryMapper.addBitemWarehousCk(itemWarehousCk);
//            if (count > 0) {
//                msg = "No BI stock found,inventory has gone negative！";
//            }
            return msg;
        }

        if (warehousDateList.size() > 0){
            float minStockQty = Float.parseFloat(warehousDateList.get(0).getBIStockQty().toString());
            if(minStockQty<0){
                // 若最小时间的库存量为负
//                Date date = new Date();
////                String yearMonthDay = new SimpleDateFormat("yyyyMMdd").format(date);
                String nowDate = ma4320Service.getNowDate();
                String ymd = nowDate.substring(0,8);
                String yearMonthDay = Utils.getFormateDateStr(ymd);
                int maxNum = warehousDateList.size()-1;
                qty = -qty;
                if(yearMonthDay.equals(warehousDateList.get(maxNum).getWarehousDate())){
//                    msg = addBiInventory(storeCd,articleId,qty,avgCostNotax);
                }else {
//                    msg = addBiInventory(storeCd, articleId, qty, avgCostNotax);
                }
                return msg;
            }
        }
        float biQty = 0;
        int num = 0;
        List<String> list = new ArrayList();
        // 消除之前时间的库存量.减少该时间下的库存量
        for (int j = 0; j < warehousDateList.size(); j++) {
            float bIStockQty = Float.parseFloat(warehousDateList.get(j).getBIStockQty().toString());
            biQty += bIStockQty;
            if (qty < biQty) {
                msg = "Eliminate," + "Reduction";
                list.add(warehousDateList.get(j).getWarehousDate());
                break;
            } else if (qty == biQty) {
                msg = "Eliminate";
                // 消除该时间下的库存量和之前时间下的库存量
                list.add(warehousDateList.get(j).getWarehousDate());
                break;
            }
            num++;
        }
        Collection<String> deleteList = list;
        if(("Eliminate," + "Reduction").equals(msg)){
            deleteList.remove(num);
//            int del = realTimeInventoryQueryMapper.deleteBiInventory(storeCd, articleId, deleteList);
//            if (del == 0) {
//                msg = null;
//            }
            // 最后warehousDate时间的库存差距数量
            float flqty = biQty - qty;
            BigDecimal gapsQty = new BigDecimal(Float.toString(flqty));
            // 计算修改Bi库存后的金额
            BigDecimal gapsAmt = gapsQty.multiply(avgCostNotax);
            int updateBi = realTimeInventoryQueryMapper.updateBiInventory(storeCd, articleId, warehousDateList.get(num).getWarehousDate(), gapsQty, gapsAmt);
            if (updateBi == 0) {
                msg = null;
            }
        }else if("Eliminate".equals(msg)){
//            int del = realTimeInventoryQueryMapper.deleteBiInventory(storeCd, articleId, deleteList);
//            if (del == 0) {
//                msg = null;
//            }
        }
        return msg;
    }

    // 增加Bi实时库存
    public String addBiQty(String storeCd,List<SaveInventoryQty> saveRtQtyList) {
        String msg = null;
        Collection<String> articles = new ArrayList<>();
        if (saveRtQtyList.size() > 0) {
            for (int i = 0; i < saveRtQtyList.size(); i++) {
                if (saveRtQtyList.get(i).getInventoryQty().equals(BigDecimal.ZERO)) {
                    saveRtQtyList.remove(i);
                } else {
                    articles.add(saveRtQtyList.get(i).getArticleId());
                }
            }
        }
        // 取得该商品的平均价格
        String businessDate = getBusinessDate();
        List<SaveInventoryQty> saveavgCostList = realTimeInventoryQueryMapper.selectAvgCost(storeCd, articles, businessDate);
        if (saveavgCostList.size() > 0) {
            for (int i = 0; i < saveavgCostList.size(); i++) {
                for (SaveInventoryQty saveQty : saveRtQtyList) {
                    if (saveQty.getArticleId().equals(saveavgCostList.get(i).getArticleId())) {
                        saveQty.setAvgCostNotax(saveavgCostList.get(i).getAvgCostNotax());
                    }
                }
            }
        }

        List<SaveInventoryQty> warehousDateList = realTimeInventoryQueryMapper.getBIDate(storeCd, articles);
        List<StoreItemWarehousCk> addAllList = new ArrayList<>();
        if (warehousDateList.size() > 0) {
            Collection<String> deleteAllList = new ArrayList<>();
            for (SaveInventoryQty saveRt : saveRtQtyList) {
                // 将多个Bi库存list分成多组，相同item为一组
                List<SaveInventoryQty> articleList = new ArrayList<>();
                for (SaveInventoryQty wareInfo : warehousDateList) {
                    if (saveRt.getArticleId().equals(wareInfo.getArticleId())) {
                        articleList.add(wareInfo);
                    }
                }
                if(articleList.size()>0){
                    // 异动数量参数
                    int qty = Integer.parseInt(saveRt.getInventoryQty().toString());
                    float minStockQty = Float.parseFloat(articleList.get(0).getBIStockQty().toString());
                    // 若最小时间的库存量为负，消除库存量为负数的数据.
                    if (minStockQty < 0) {
                        // 将要删除的数据的参数放入list
                        List<String> delList = new ArrayList();
                        float biQty = 0;
                        int num = 0;
                        for (int j = 0; j < articleList.size(); j++) {
                            float bIStockQty = Float.parseFloat(articleList.get(j).getBIStockQty().toString());
                            biQty += bIStockQty;
                            if (qty < Math.abs(biQty)) {
                                delList.add(articleList.get(j).getArticleId() + ":" + articleList.get(j).getWarehousDate());
                                // 最后warehousDate时间的库存差距数量（biQty为负数）
                                float flqty = biQty + qty;
                                BigDecimal gapsQty = new BigDecimal(Float.toString(flqty));
                                // 计算修改Bi库存后的金额
                                BigDecimal gapsAmt = gapsQty.multiply(saveRt.getAvgCostNotax());
                                String warehousDate = articleList.get(j).getWarehousDate();
                                String articleId = articleList.get(j).getArticleId();
                                int updateBi = realTimeInventoryQueryMapper.updateBiInventory(storeCd, articleId, warehousDate, gapsQty, gapsAmt);
                                if (updateBi == 0) {
                                    msg = null;
                                }
                                break;
                            } else if (qty == Math.abs(biQty)) {
                                // 消除该时间下的库存量和之前时间下的库存量
                                delList.add(articleList.get(j).getArticleId() + ":" + articleList.get(j).getWarehousDate());
                                break;
                            }
                            deleteAllList.addAll(delList);
                            num++;
                        }
                        // 首先判断增加的库存量是否大于所有的负库存量
                        if (qty > Math.abs(biQty)) {
                            msg = "Add," + "Eliminate";
                            StoreItemWarehousCk s1 = new StoreItemWarehousCk();
                            List<String> delAddList = new ArrayList();
                            for (SaveInventoryQty art : articleList) {
                                String delInfo = art.getArticleId() + ":" + art.getWarehousDate();
                                delAddList.add(delInfo);
                                s1.setArticleId(art.getArticleId());
                                s1.setStoreCd(storeCd);
                                s1.setAvgCostNotax(saveRt.getAvgCostNotax());
                            }
                            deleteAllList.addAll(delAddList);
                            Integer flqty = qty - Math.abs((int) biQty);
                            s1.setStockQty(new BigDecimal(flqty));
                            // 添加数据
                            addAllList.add(s1);
                        }
                    }else {
                        StoreItemWarehousCk s2 = new StoreItemWarehousCk();
                        s2.setStockQty(new BigDecimal(qty));
                        s2.setAvgCostNotax(saveRt.getAvgCostNotax());
                        s2.setArticleId(saveRt.getArticleId());
                        s2.setStoreCd(storeCd);
                        addAllList.add(s2);
                    }
                }else {
                    StoreItemWarehousCk s3 = new StoreItemWarehousCk();
                    s3.setStockQty(new BigDecimal(saveRt.getInventoryQty()));
                    s3.setAvgCostNotax(saveRt.getAvgCostNotax());
                    s3.setArticleId(saveRt.getArticleId());
                    s3.setStoreCd(storeCd);
                    addAllList.add(s3);
                }
            }
            if(deleteAllList.size()>0){
                int del = realTimeInventoryQueryMapper.deleteBiInventory(storeCd, deleteAllList);
            }
            msg = addBiInventory(addAllList);
        }else {
            for(int i=0;i<saveRtQtyList.size();i++){
                StoreItemWarehousCk s2 = new StoreItemWarehousCk();
                s2.setStockQty(new BigDecimal(saveRtQtyList.get(i).getInventoryQty()));
                s2.setAvgCostNotax(saveRtQtyList.get(i).getAvgCostNotax());
                s2.setArticleId(saveRtQtyList.get(i).getArticleId());
                s2.setStoreCd(storeCd);
                addAllList.add(s2);
            }
            msg = addBiInventory(addAllList);
        }
        return msg;
    }

    public String addBiInventory(List<StoreItemWarehousCk> wareCkList){
        String msg = null;
        Collection<StoreItemWarehousCk> addBIList = new ArrayList<>();
        if(wareCkList.size()>0){
//            Date date = new Date();
//            String yearMonthDay = new SimpleDateFormat("yyyyMMdd").format(date);
//            String yearMonth = yearMonthDay.substring(0,6);
//            String warehousDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
            String nowDate = ma4320Service.getNowDate();
            String ymd = nowDate.substring(0,8);
            String yearMonthDay = Utils.getFormateDateStr(ymd);
            String yearMonth = yearMonthDay.substring(0,6);
            String warehousDate=Utils.getTimeStamp(ymd);
            for(StoreItemWarehousCk swh :wareCkList){
                // 计算修改Bi库存后的金额
                BigDecimal gapsAmt = swh.getStockQty().multiply(swh.getAvgCostNotax());

                StoreItemWarehousCk itemWarehousCk = new StoreItemWarehousCk();
                itemWarehousCk.setWarehousDate(warehousDate);
                itemWarehousCk.setCompanyCd("01");
                itemWarehousCk.setStoreCd(swh.getStoreCd());
                itemWarehousCk.setArticleId(swh.getArticleId());
                itemWarehousCk.setStockQty(swh.getStockQty());
                itemWarehousCk.setStockAmt(gapsAmt);
                itemWarehousCk.setWarehousDays("< 30 days");
                itemWarehousCk.setYearMonth(yearMonth);

                SaveInventoryQty saveBiQty = realTimeInventoryQueryMapper.getBIDetailInfo(swh.getStoreCd(),swh.getArticleId(),yearMonthDay);
                if(saveBiQty != null){
                    itemWarehousCk.setStockQty(saveBiQty.getBIStockQty().add(swh.getStockQty()));
                    itemWarehousCk.setStockAmt(saveBiQty.getBIStockAmt().add(gapsAmt));
                    realTimeInventoryQueryMapper.updateBiInventory(swh.getStoreCd(),swh.getArticleId(),yearMonthDay
                            ,itemWarehousCk.getStockQty(),itemWarehousCk.getStockAmt());
                    msg = "Update successfully!";
                }else {
                    addBIList.add(itemWarehousCk);
                }
            }
        }

        if(addBIList.size()>0){
            int count = realTimeInventoryQueryMapper.addBitemWarehousCk(addBIList);
            if(count > 0){
                msg = "Add successfully!";
            }
        }

        return msg;
    }

    /**
     * post发送请求
     * @param param
     * @param url
     * @return
     */
    public static String RequestPost(String param, String url) {
        BufferedReader reader = null;
        String rs = null;
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(url);

        try {
            List<NameValuePair> list = new LinkedList<>();
            BasicNameValuePair param1 = new BasicNameValuePair("realTimeJson", param);
            list.add(param1);
            // 使用URL实体转换工具
            UrlEncodedFormEntity entityParam = new UrlEncodedFormEntity(list, "UTF-8");
            post.setEntity(entityParam);
            // 浏览器表示
            post.addHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.7.6)");
            // 传输的类型
            post.addHeader("Content-Type", "application/x-www-form-urlencoded");

            // 发送请求
            HttpResponse httpResponse = client.execute(post);

            //获取并解析数据
            InputStream is = httpResponse.getEntity().getContent();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            StringBuffer sb = new StringBuffer();
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sb.append(strRead);
            }
            rs = sb.toString();

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rs;
    }
}
