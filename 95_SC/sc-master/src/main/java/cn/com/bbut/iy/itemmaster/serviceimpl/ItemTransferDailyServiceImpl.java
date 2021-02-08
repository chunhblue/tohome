package cn.com.bbut.iy.itemmaster.serviceimpl;

import cn.com.bbut.iy.itemmaster.dao.ItemTransferDailyMapper;
import cn.com.bbut.iy.itemmaster.dto.inventory.Sk0020DTO;
import cn.com.bbut.iy.itemmaster.dto.vendorReceiptDaily.VendorReceiptDailyParamDTO;
import cn.com.bbut.iy.itemmaster.service.itemTransferDaily.ItemTransferDailyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ItemTransferDailyServiceImpl implements ItemTransferDailyService {

    @Autowired
    private ItemTransferDailyMapper itemTransferDailyMapper;

    @Override
    public Map<String, Object> search(VendorReceiptDailyParamDTO param) {
        // 获取总条数
        int count = itemTransferDailyMapper.searchCount(param);

        // 获取总页数
        int totalPage = (count % param.getRows() == 0) ? (count / param.getRows()) : (count / param.getRows()) + 1;

        List<Sk0020DTO> result = itemTransferDailyMapper.search(param);
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
    public List<Sk0020DTO> getPrintData(VendorReceiptDailyParamDTO param) {
        param.setFlg(false); // 不分页
        List<Sk0020DTO> result = itemTransferDailyMapper.search(param);
        for (Sk0020DTO item : result) {
            item.setVoucherDate(formatDate(item.getVoucherDate()));
        }
        return result;
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
