package cn.com.bbut.iy.itemmaster.serviceimpl.storeTransferDaily;

import cn.com.bbut.iy.itemmaster.dao.Cm9060Mapper;
import cn.com.bbut.iy.itemmaster.dao.StoreTransferDailyMapper;
import cn.com.bbut.iy.itemmaster.dto.inventory.Sk0020DTO;
import cn.com.bbut.iy.itemmaster.dto.vendorReceiptDaily.VendorReceiptDailyParamDTO;
import cn.com.bbut.iy.itemmaster.entity.base.Cm9060;
import cn.com.bbut.iy.itemmaster.service.storeTransferDaily.StoreTransferDailyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StoreTransferDailyServiceImpl implements StoreTransferDailyService {

    @Autowired
    private StoreTransferDailyMapper storeTransferDailyMapper;

    @Autowired
    private Cm9060Mapper cm9060Mapper;

    @Override
    public Map<String,Object> search(VendorReceiptDailyParamDTO param) {

        // 获取总条数
        int count = storeTransferDailyMapper.searchCount(param);

        // 获取总页数
        int totalPage = (count % param.getRows() == 0) ? (count / param.getRows()) : (count / param.getRows()) + 1;

        List<Sk0020DTO> result = storeTransferDailyMapper.search(param);
        for (Sk0020DTO item : result) {
            item.setVoucherDate(formatDate(item.getVoucherDate()));
            item.setTranInDate(formatDate(item.getTranInDate()));
        }

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("totalPage",totalPage);
        map.put("count",count);
        map.put("data",result);
        return map;
    }

    @Override
    public Map<String,Object> getList(VendorReceiptDailyParamDTO param) {
        if (param==null) {
            param=new VendorReceiptDailyParamDTO();
        }
        param.setBusinessDate(getBusinessDate());
        int count = storeTransferDailyMapper.getListCount(param);

        // 获取总页数
        int totalPage = (count % param.getRows() == 0) ? (count / param.getRows()) : (count / param.getRows()) + 1;

        List<Sk0020DTO> result = storeTransferDailyMapper.getList(param);
        for (Sk0020DTO item : result) {
            item.setVoucherDate(formatDate(item.getVoucherDate()));
        }
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("totalPage",totalPage);
        map.put("count",count);
        map.put("data",result);
        return map;
    }

    @Override
    public List<Sk0020DTO> getListPrintData(VendorReceiptDailyParamDTO param) {
        if (param==null) {
            param=new VendorReceiptDailyParamDTO();
        }
        param.setBusinessDate(getBusinessDate());
        param.setFlg(false);

        List<Sk0020DTO> result = storeTransferDailyMapper.getList(param);
        for (Sk0020DTO item : result) {
            item.setVoucherDate(formatDate(item.getVoucherDate()));
        }
        return result;
    }

    @Override
    public List<Sk0020DTO> getPrintData(VendorReceiptDailyParamDTO param) {
        param.setFlg(false);
        List<Sk0020DTO> result = storeTransferDailyMapper.search(param);
        for (Sk0020DTO item : result) {
            item.setVoucherDate(formatDate(item.getVoucherDate()));
            item.setTranInDate(formatDate(item.getTranInDate()));
        }
        return result;
    }

    @Override
    public Sk0020DTO getTranferQty(VendorReceiptDailyParamDTO param) {
        param.setFlg(false);
        Sk0020DTO sk0020DTO = new Sk0020DTO();
        int count = storeTransferDailyMapper.searchCount(param);
        sk0020DTO.setRecords(count);

        List<Sk0020DTO> list = storeTransferDailyMapper.search(param);
        if(list.size() == 0)
            return sk0020DTO;
        if(list.size() > 1){
            for(int i = 0;i < list.size()-1;i++){
                if(list.get(i).getQty2() == null)
                    list.get(i).setQty2(BigDecimal.ZERO);
                if(list.get(i+1).getQty2() == null)
                    list.get(i+1).setQty2(BigDecimal.ZERO);

                if(list.get(i).getQty1() == null)
                    list.get(i).setQty1(BigDecimal.ZERO);
                if(list.get(i+1).getQty1() == null)
                    list.get(i+1).setQty1(BigDecimal.ZERO);
                list.get(i+1).setQty2(list.get(i+1).getQty2().add(list.get(i).getQty2()));
                list.get(i+1).setQty1(list.get(i+1).getQty1().add(list.get(i).getQty1()));
                sk0020DTO.setQty2(list.get(i+1).getQty2());
                sk0020DTO.setQty1(list.get(i+1).getQty1());
            }
        }else {
            if(list.get(0).getQty2() == null)
                list.get(0).setQty2(BigDecimal.ZERO);
            if(list.get(0).getQty1() == null)
                list.get(0).setQty1(BigDecimal.ZERO);
            sk0020DTO.setQty2(list.get(0).getQty2());
            sk0020DTO.setQty1(list.get(0).getQty1());
        }
        return sk0020DTO;
    }

    /**
     * 获取当前业务日期
     */
    private String getBusinessDate() {
        Cm9060 dto =  cm9060Mapper.selectByPrimaryKey("0000");
        return dto.getSpValue();
    }


    /**
     * 格式化日期
     * @param piDate
     * @return
     */
    private String formatDate(String piDate) {
        if (StringUtils.isEmpty(piDate)) {
            return "";
        }
        String year = piDate.substring(0, 4);
        String month = piDate.substring(4, 6);
        String day = piDate.substring(6, 8);
        return day+"/"+month+"/"+year;
    }
}
