package cn.com.bbut.iy.itemmaster.serviceimpl.operationmanagement;

import cn.com.bbut.iy.itemmaster.dao.Cm9060Mapper;
import cn.com.bbut.iy.itemmaster.dao.CustEntryMapper;
import cn.com.bbut.iy.itemmaster.dao.CustEntryPlanMapper;
import cn.com.bbut.iy.itemmaster.dao.inventory.InventoryVouchersMapper;
import cn.com.bbut.iy.itemmaster.dto.cm9070.Cm9070ReturnDTO;
import cn.com.bbut.iy.itemmaster.dto.pi0100.StocktakeItemDTO;
import cn.com.bbut.iy.itemmaster.dto.pi0100c.PI0100DTOC;
import cn.com.bbut.iy.itemmaster.dto.pi0100c.StocktakeItemDTOC;
import cn.com.bbut.iy.itemmaster.dto.rtInventory.RTInventoryQueryDTO;
import cn.com.bbut.iy.itemmaster.entity.base.Cm9060;
import cn.com.bbut.iy.itemmaster.service.CM9060Service;
import cn.com.bbut.iy.itemmaster.service.RealTimeInventoryQueryService;
import cn.com.bbut.iy.itemmaster.service.SequenceService;
import cn.com.bbut.iy.itemmaster.service.cm9070.Cm9070Service;
import cn.com.bbut.iy.itemmaster.service.opreationmanagement.CustOfEntryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName CustOfEntryServiceImpl
 * @Description TODO
 * @Author Administrator
 * @Date 2020/3/19 13:05
 * @Version 1.0
 */
@Service
@Slf4j
public class CustOfEntryServiceImpl  implements CustOfEntryService {

    @Autowired
    private CustEntryMapper custEntryMapper;

    @Autowired
    private CustEntryPlanMapper custOfEntryPlanMapper;

    @Autowired
    private Cm9070Service cm9070ServiceImpl;

    @Autowired
    private SequenceService sequenceService;

    @Autowired
    private Cm9060Mapper cm9060Mapper;
    @Autowired
    private CM9060Service cm9060Service;
    @Value("${esUrl.inventoryUrl}")
    private String inventoryUrl;
    @Autowired
    private RealTimeInventoryQueryService rtInventoryService;

    /**
     * 查询商品
     */

    @Override
//    public StocktakeItemDTOC getItemInfo(String itemCode, String piCd, String piDate) {
    public StocktakeItemDTOC getItemInfo(String itemCode,String storeCd) {

        if (StringUtils.isEmpty(itemCode) || StringUtils.isEmpty(storeCd)) {
            return null;
        }
        String businessDate = getBusinessDate();

        return custEntryMapper.getItemInfo(itemCode,businessDate,storeCd);
    }

    /**
     * 保存    @Transactional   声明事务   对数据库增删改查的时候  一起成功或者失败   错误在抛出异常
     * 没用这个方法了
     */
    @Override
    @Transactional
    public int insert(String piCd, String piDate, List<StocktakeItemDTOC> stocktakeItemList) {

        try {
            int count = custEntryMapper.getCountByPicd(piCd);
            if (count > 0) {
                custEntryMapper.deleteByPicd(piCd);
            }
            custEntryMapper.save(stocktakeItemList);
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }

         return -1;
    }

    /**
     * 查询数据
     */
    @Override
    public PI0100DTOC getData(String piCd) {
        String inEsTime = cm9060Service.getValByKey("1206");
        if (StringUtils.isEmpty(piCd)) {
            return null;
        }
        // 获取主档信息
        PI0100DTOC pi0100c = custOfEntryPlanMapper.getPI0100ByPrimary(piCd);
        if (pi0100c==null) {
            return null;
        }
        List<String> articles = new ArrayList<>();
        List<StocktakeItemDTOC> list= custEntryMapper.getPI0130ByPrimary(piCd,pi0100c.getStoreCd());
        for(StocktakeItemDTOC item:list){
            articles.add(item.getArticleId());
        }
        //拼接url，转义参数
        String connUrl = inventoryUrl + "GetRelTimeInventory/" + pi0100c.getStoreCd() + "/"
                + "*" + "/*/*/*/*/" + inEsTime+"/*/*";
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
    @Transactional
    public PI0100DTOC insertData(List<StocktakeItemDTOC> stocktakeItemList, PI0100DTOC item, HttpServletRequest request, HttpSession session) {
        String dateStr = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());
        String ymd = dateStr.split("-")[0];
        String hms = dateStr.split("-")[1];
        // 费用金额
//        BigDecimal expensePrice = new BigDecimal("0");

        if (item.getPiCd()==null||StringUtils.isEmpty(item.getPiCd())) {
            // 新增
            //获取序列号
//            String piCd = sequenceService.getSequence("pi0135_pi_cd_seq");
            String piCd = sequenceService.getSequence("pi0135_cia_id_seq","CIA",item.getStoreCd());
            if(org.apache.commons.lang.StringUtils.isBlank(piCd)){
                //获取序列失败
                log.error("获取序列失败 getSequence: {}", "pi0135_cia_id_seq");
                RuntimeException e = new RuntimeException("获取序列失败[ pi0135_cia_id_seq ]");
                throw e;
            }
            item.setPiCd(piCd);
            item.setCreateYmd(ymd);
            item.setCreateHms(hms);
            // 补充明细数据
            for (StocktakeItemDTOC dto : stocktakeItemList) {
                String[] dateAndTime = formatLastUpdateTime(dto.getLastUpdateTime());
                dto.setPiCd(item.getPiCd());
                dto.setStoreCd(item.getStoreCd());
                dto.setUpdateYmd(dateAndTime[0]);
                dto.setUpdateHms(dateAndTime[1]);
//                expensePrice=expensePrice.add(dto.getExpensePrice());
            }
//            item.setExpenseAmt(expensePrice);

            try {
                // 保存数据
                // 保存头档
                custEntryMapper.saveItem(item);
                // 保存明细
                custEntryMapper.saveAllItem(stocktakeItemList);
                return item;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            // 修改
            // 补充明细数据
            for (StocktakeItemDTOC dto : stocktakeItemList) {
                String[] dateAndTime = formatLastUpdateTime(dto.getLastUpdateTime());
                dto.setPiCd(item.getPiCd());
                dto.setStoreCd(item.getStoreCd());
                dto.setUpdateYmd(dateAndTime[0]);
                dto.setUpdateHms(dateAndTime[1]);
//                expensePrice=expensePrice.add(dto.getExpensePrice());
            }
//            item.setExpenseAmt(expensePrice);
            item.setUpdateYmd(ymd);
            item.setUpdateHms(hms);

            try {
                // 修改头档
                custEntryMapper.updateItem(item);
                // 修改明细
                // 先删除, 再添加
                custEntryMapper.deleteByPicd(item.getPiCd());
                custEntryMapper.saveAllItem(stocktakeItemList);
                return item;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    private String[] formatLastUpdateTime(String dateStr) {
        String[] strArr = null;
        try {
            Date date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dateStr);
            strArr = new SimpleDateFormat("yyyyMMdd-HHmmss").format(date).split("-");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return strArr;
    }

    /**
     * 获取当前业务日期
     */
    private String getBusinessDate() {
        Cm9060 dto = cm9060Mapper.selectByPrimaryKey("0000");
        return dto.getSpValue();
    }
}