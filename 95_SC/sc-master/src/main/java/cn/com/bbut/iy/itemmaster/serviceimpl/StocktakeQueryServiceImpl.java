package cn.com.bbut.iy.itemmaster.serviceimpl;


import cn.com.bbut.iy.itemmaster.dao.PI0010Mapper;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.inventory.Sk0010DTO;
import cn.com.bbut.iy.itemmaster.dto.inventory.Sk0020DTO;
import cn.com.bbut.iy.itemmaster.dto.stocktakeQuery.*;
import cn.com.bbut.iy.itemmaster.service.CM9060Service;
import cn.com.bbut.iy.itemmaster.service.StocktakeQueryService;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lz
 */
@Service
public class StocktakeQueryServiceImpl implements StocktakeQueryService {

    @Autowired
    private PI0010Mapper pi0010Mapper;

    @Autowired
    private CM9060Service cm9060Service;

    @Override
    public GridDataDTO<StocktakeQueryListDTO> getStocktakeQueryList(StocktakeQueryParamDTO param) {
        StocktakeQueryParam stocktakeQueryParam = new Gson().fromJson(param.getSearchJson(), StocktakeQueryParam.class);
        if (StringUtils.isBlank(stocktakeQueryParam.getAccDate())){
            stocktakeQueryParam.setAccDate(cm9060Service.getValByKey("0000"));
        }
        stocktakeQueryParam.setStoreCd("100011");
        stocktakeQueryParam.setLimitStart(param.getLimitStart());
        stocktakeQueryParam.setLimitEnd(param.getLimitEnd());
        stocktakeQueryParam.setOrderByClause(param.getOrderByClause());
        List<StocktakeQueryListDTO> list= pi0010Mapper.selectStocktakeBy(stocktakeQueryParam);
        long count = pi0010Mapper.selectStocktakeByCount(stocktakeQueryParam);
        return new GridDataDTO<>(list, param.getPage(), count,param.getRows());
    }

    @Override
    public GridDataDTO<StocktakeQueryItemsDTO> getDetailsList(StocktakeQueryParamDTO param) {
        //前端jsonList解析为json
        String param1 = param.getSearchJson();
        param1 = param1.replace("{","[").replace("}","]");
        param1 = param1.replace("(","{").replace(")","}");
        param1 = param1.replace("StocktakeQueryItemParam","");
        String strReg="(\\w+)\\s*=\\s*(\\w+)";
        String strResult=param1.replaceAll(strReg,"\"$1\":\"$2\"");
        strResult = strResult.substring(1, strResult.length()-1);
        List<StocktakeQueryItemParam> paramList = new Gson().fromJson(strResult,new TypeToken<List<StocktakeQueryItemParam>>() {}.getType());

        List<StocktakeQueryItemsDTO> itemsList= new ArrayList<>();//返回页面的list
        long count = 0;
        for (StocktakeQueryItemParam item: paramList) {
            item.setStoreCd("100011");
            if (StringUtils.isBlank(item.getAccDate())){
                item.setAccDate(cm9060Service.getValByKey("0000"));
            }
            //判断货位编号
            if("null".equals(item.getAreaCd()) || null == item.getAreaCd()){
                List<StocktakeQueryItemsDTO> list= pi0010Mapper.selectItemsBy(item);
                for (StocktakeQueryItemsDTO itemDTO:list) {
                    itemDTO.setPartCd(itemDTO.getDepCd()+"-"+itemDTO.getPmaCd()+"-"+itemDTO.getCategoryCd()+"-"+itemDTO.getSubCategoryCd());
                    itemDTO.setPartName(itemDTO.getDepName()+"-"+itemDTO.getSubCategoryName());
                    itemsList.add(itemDTO);
                }
                count += pi0010Mapper.selectItemsByCount(item);
            }else{
                List<StocktakeQueryItemsDTO> list= pi0010Mapper.selectItemsByAreaCd(item);
                for (StocktakeQueryItemsDTO itemDTO:list) {
                    itemDTO.setPartCd(itemDTO.getDepCd()+"-"+itemDTO.getPmaCd()+"-"+itemDTO.getCategoryCd()+"-"+itemDTO.getSubCategoryCd());
                    itemDTO.setPartName(itemDTO.getDepName()+"-"+itemDTO.getSubCategoryName());
                    itemsList.add(itemDTO);
                }
                count += pi0010Mapper.selectItemsByAreaCdCount(item);
            }
        }
        return new GridDataDTO<>(itemsList, param.getPage(), count,param.getRows());
    }

    /**
     * 查询一览数据
     * @param param
     */
    @Override
    public GridDataDTO<StocktakeQueryListDTO> search(StocktakeQueryParam param) {
        int count = 0;
        count = pi0010Mapper.searchCount(param);
        List<StocktakeQueryListDTO> result = pi0010Mapper.search(param);
        for (StocktakeQueryListDTO stock : result) {
            stock.setAccDate(formatDate(stock.getAccDate()));
        }
        return new GridDataDTO<>(result,param.getPage() , count,param.getRows());
    }

    /**
     * 格式化日期
     * @param piDate
     * @return
     */
    private String formatDate(String piDate) {
        if (org.springframework.util.StringUtils.isEmpty(piDate)) {
            return "";
        }
        String year = piDate.substring(0, 4);
        String month = piDate.substring(4, 6);
        String day = piDate.substring(6, 8);
        return day+"/"+month+"/"+year;
    }

    /**
     * 查询盘点商品数据
     */
    @Override
    public GridDataDTO<StocktakeQueryItemsDTO> queryItems(String searchJson, int page, int rows) {

        if (searchJson==null||StringUtils.isEmpty(searchJson)) {
            return null;
        }
        Gson gson = new Gson();
        StocktakeQueryParam stockParam = gson.fromJson(searchJson, StocktakeQueryParam.class);
        stockParam.setPage(page);
        stockParam.setRows(rows);
        stockParam.setLimitStart((page - 1)*rows);
        int count = 0;
        count = pi0010Mapper.queryItemCount(stockParam);
        List<StocktakeQueryItemsDTO> result = pi0010Mapper.queryItems(stockParam);
        return new GridDataDTO<>(result,stockParam.getPage() , count,stockParam.getRows());
    }

    // 保存商品盘点数
    @Override
    @Transactional
    public int update(String record) {
        if (record==null||StringUtils.isEmpty(record)) {
            return -1;
        }
        try {
            Gson gson = new Gson();
            List<StocktakeQueryItemsDTO> stockList = gson.fromJson(record, new TypeToken<List<StocktakeQueryItemsDTO>>() {}.getType());
            for (StocktakeQueryItemsDTO stock : stockList) {
                pi0010Mapper.update(stock);
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return -1;
        }
        return 1;
    }

    // 设置初盘
    @Override
    @Transactional
    public int updatePiFirstFinish(String jsonStr) {

        if (jsonStr==null||StringUtils.isEmpty(jsonStr)) {
            return -1;
        }
        try {
            Gson gson = new Gson();
            List<StocktakeQueryItemsDTO> stockList = gson.fromJson(jsonStr, new TypeToken<List<StocktakeQueryItemsDTO>>() {}.getType());

            for (StocktakeQueryItemsDTO stock : stockList) {
                pi0010Mapper.setPiFirstFinish(stock);
                pi0010Mapper.setPiQtyStatus(stock);
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return -1;
        }

        return 1;
    }

    // 盘点差异认列
    @Override
    @Transactional
    public int updatePiCommit(String jsonStr) {
        if (jsonStr==null||StringUtils.isEmpty(jsonStr)) {
            return -1;
        }
        try {
            Gson gson = new Gson();
            List<StocktakeQueryItemsDTO> stockList = gson.fromJson(jsonStr, new TypeToken<List<StocktakeQueryItemsDTO>>() {}.getType());

            for (StocktakeQueryItemsDTO stock : stockList) {
                // 更改计划的是否差异认列的状态
                pi0010Mapper.setPiCommitFlag(stock.getPiCd(),stock.getAccDate());
                // 查询出计划下商品的详细数据
                List<Sk0020DTO> itemList=pi0010Mapper.getItemInfoList(stock.getPiCd(),stock.getAccDate());
                int i = 1;
                BigDecimal voucharAmtNotax = new BigDecimal("0");
                for (Sk0020DTO sk0020 : itemList) {
                    sk0020.setVoucherNo(stock.getPiCd());
                    sk0020.setVoucherDate(stock.getAccDate());
                    sk0020.setVoucherType("605");
                    sk0020.setStoreCd1(sk0020.getStoreCd());
                    sk0020.setAdjustReason("55");
//                    sk0020.setDisplaySeq(new BigDecimal(i++));
                    sk0020.setDisplaySeq(new BigDecimal(i+""));
                    voucharAmtNotax=voucharAmtNotax.add(sk0020.getAmtNoTax());
                    i++;
                }
                // 添加库存异动传票明细数据
                pi0010Mapper.insertSK0020(itemList);
                // 添加库存异动传票头档数据
                Sk0010DTO sk0010 = new Sk0010DTO();
                sk0010.setStoreCd(itemList.get(0).getStoreCd());
                sk0010.setVoucherNo(stock.getPiCd());
                sk0010.setVoucherDate(stock.getAccDate());
                sk0010.setVoucherType("605");
                sk0010.setStoreCd1(itemList.get(0).getStoreCd());
                sk0010.setVoucherSts("10");
                sk0010.setRemark("盘点差异-盘点日"+stock.getAccDate()+"-店铺发起");
                sk0010.setVoucherAmtNoTax(voucharAmtNotax);
                sk0010.setUploadFlg("0");
                pi0010Mapper.insertSK0010(sk0010);
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return -1;
        }

        return 1;
    }

}
