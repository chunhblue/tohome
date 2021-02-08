package cn.com.bbut.iy.itemmaster.serviceimpl.VendorReturnedDaily;

import cn.com.bbut.iy.itemmaster.dao.VendorReturnedDailyMapper;
import cn.com.bbut.iy.itemmaster.dto.VendorReturnedDaily.VendorReturnedDailyDTO;
import cn.com.bbut.iy.itemmaster.dto.VendorReturnedDaily.VendorReturnedDailyParamDTO;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.service.CM9060Service;
import cn.com.bbut.iy.itemmaster.service.VendorReturnedDaily.VendorReturnedDailyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class VendorReturnedDailyServiceImpl implements VendorReturnedDailyService {
    @Autowired
    private CM9060Service cm9060Service;
    @Autowired
    private VendorReturnedDailyMapper vendorReturnedDailyMapper;

    @Override
    public Map<String, Object> deleteSearch(VendorReturnedDailyParamDTO param) {

        String date = cm9060Service.getValByKey("0000");
        param.setBusinessDate(date);
        // 总条数
        int count = 0;
        // 获取总页数
        int totalPage = 0;
        if (param.isFlg()) {
            count = vendorReturnedDailyMapper.searchCount(param);
            // 获取总页数
            totalPage = (count % param.getRows() == 0) ? (count / param.getRows()) : (count / param.getRows()) + 1;
        }

        List<VendorReturnedDailyDTO> list = vendorReturnedDailyMapper.search(param);
        for(VendorReturnedDailyDTO item : list){
            item.setAccDate(formatDate(item.getAccDate()));
        }
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("totalPage",totalPage);
        map.put("count",count);
        map.put("data",list);
        return map;
    }

    @Override
    public List<VendorReturnedDailyDTO> deleteVendorSearchPrint(VendorReturnedDailyParamDTO param) {
        String date = cm9060Service.getValByKey("0000");
        param.setBusinessDate(date);
        param.setFlg(false);
        List<VendorReturnedDailyDTO> list = vendorReturnedDailyMapper.search(param);
        for(VendorReturnedDailyDTO item : list){
            item.setAccDate(formatDate(item.getAccDate()));
        }
        return list;
    }

    @Override
    public List<AutoCompleteDTO> getAMList(String flag, String v) {
        return vendorReturnedDailyMapper.getAMList(flag,v);
    }

    @Override
    public List<AutoCompleteDTO> getOMList(String flag, String v) {
        return vendorReturnedDailyMapper.getOMList(flag,v);
    }


    @Override
    public Map<String, Object> deleteDcDailySearch(VendorReturnedDailyParamDTO param) {

        String date = cm9060Service.getValByKey("0000");
        param.setBusinessDate(date);
        // 总条数
        int count = 0;
        // 获取总页数
        int totalPage = 0;
        if (param.isFlg()) {
            count = vendorReturnedDailyMapper.dcDailySearchCount(param);
            // 获取总页数
            totalPage = (count % param.getRows() == 0) ? (count / param.getRows()) : (count / param.getRows()) + 1;
        }
        List<VendorReturnedDailyDTO> list = vendorReturnedDailyMapper.dcDailySearch(param);
        for(VendorReturnedDailyDTO item : list){
            item.setAccDate(formatDate(item.getAccDate()));
        }
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("totalPage",totalPage);
        map.put("count",count);
        map.put("data",list);
        return map;
    }

    @Override
    public List<VendorReturnedDailyDTO> deleteDcDailySearchPrint(VendorReturnedDailyParamDTO param) {
        String date = cm9060Service.getValByKey("0000");
        param.setBusinessDate(date);
        param.setFlg(false);
        List<VendorReturnedDailyDTO> list = vendorReturnedDailyMapper.dcDailySearch(param);
        for(VendorReturnedDailyDTO item : list){
            item.setAccDate(formatDate(item.getAccDate()));
        }
        return list;
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
