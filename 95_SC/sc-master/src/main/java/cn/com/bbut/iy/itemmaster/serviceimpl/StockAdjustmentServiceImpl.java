package cn.com.bbut.iy.itemmaster.serviceimpl;

        import cn.com.bbut.iy.itemmaster.dao.ExpenditureMapper;
        import cn.com.bbut.iy.itemmaster.dao.inventory.InventoryVouchersMapper;
        import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
        import cn.com.bbut.iy.itemmaster.dto.expenditure.ExpenditureDTO;
        import cn.com.bbut.iy.itemmaster.dto.inventory.Sk0020DTO;
        import cn.com.bbut.iy.itemmaster.dto.inventory.Sk0020ParamDTO;
        import cn.com.bbut.iy.itemmaster.dto.rtInventory.RTInventoryQueryDTO;
        import cn.com.bbut.iy.itemmaster.service.CM9060Service;
        import cn.com.bbut.iy.itemmaster.service.RealTimeInventoryQueryService;
        import cn.com.bbut.iy.itemmaster.service.StockAdjustmentService;
        import lombok.extern.slf4j.Slf4j;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.beans.factory.annotation.Value;
        import org.springframework.stereotype.Service;

        import java.math.BigDecimal;
        import java.util.HashMap;
        import java.util.List;
        import java.util.Map;

@Slf4j
@Service
public class StockAdjustmentServiceImpl implements StockAdjustmentService {

    @Autowired
    private InventoryVouchersMapper inventoryVouchersMapper;
    @Autowired
    private ExpenditureMapper expenditureMapper;
    @Autowired
    private RealTimeInventoryQueryService rtInventoryService;
    @Value("${esUrl.inventoryUrl}")
    private String inventoryUrl;
    @Autowired
    private CM9060Service cm9060Service;

    @Override
    public GridDataDTO<Sk0020DTO> getSk0020(Sk0020ParamDTO sk0020) {
        String inEsTime = cm9060Service.getValByKey("1206");
        GridDataDTO<Sk0020DTO> data = new GridDataDTO<Sk0020DTO>();
        List<Sk0020DTO> _list = inventoryVouchersMapper.adjustDetailsList(sk0020);
        if(_list == null || _list.equals("")){
            log.info("<<<<<<<<<<_list is null");
        }else {
            for (Sk0020DTO sk02: _list) {
                // 获取实时库存
                //拼接url，转义参数
                String connUrl = inventoryUrl + "GetRelTimeInventory/" + sk02.getStoreCd() + "/"
                        + sk02.getArticleId() + "/*/*/*/*/" + inEsTime+"/*/*";
                RTInventoryQueryDTO rTInventoryQueryDTO = rtInventoryService.getRtInventory(connUrl);
                BigDecimal inventoryQty = rTInventoryQueryDTO.getRealtimeQty();
                if(inventoryQty == null){
                    sk02.setInventoryQty("0");
                }else {
                    sk02.setInventoryQty(inventoryQty.toString());
                }
                // 获取variance Qty
                if(sk02.getQty2() == null){
                    sk02.setQty2(BigDecimal.ZERO);
                }else if(sk02.getQty1() == null){
                    sk02.setQty1(BigDecimal.ZERO);
                }
                sk02.setDifferenQty(sk02.getQty2().subtract(sk02.getQty1()));
                // 获取费用管理描述
                ExpenditureDTO expenditureDTO = expenditureMapper.getDescription(sk02.getExpenditureNo(),sk02.getStoreCd(),sk02.getVoucherDate());
                if(expenditureDTO != null){
                    String description = expenditureDTO.getDescription();
                    sk02.setDescription(description);
                }
            }
        }
        data.setRows(_list);
        return data;
    }

    @Override
    public Map<String,Object> Total(Sk0020ParamDTO sk0020) {
       Integer sumQty1=0;
        Integer integer = inventoryVouchersMapper.adjustItemSKU(sk0020);
        List<Sk0020DTO> _list = inventoryVouchersMapper.adjustDetailsList(sk0020);
        for (Sk0020DTO S1:_list) {
            sumQty1+=S1.getQty1().intValue();
        }
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("totalQty",sumQty1);
        map.put("totalItemSKU",integer);
        return map;
    }
}
