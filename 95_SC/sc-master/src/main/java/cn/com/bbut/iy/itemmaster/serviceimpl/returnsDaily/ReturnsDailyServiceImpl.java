package cn.com.bbut.iy.itemmaster.serviceimpl.returnsDaily;

import cn.com.bbut.iy.itemmaster.dao.ReturnsDailyMapper;
import cn.com.bbut.iy.itemmaster.dto.returnsDaily.ReturnsDailyDTO;
import cn.com.bbut.iy.itemmaster.dto.returnsDaily.ReturnsDailyParamDTO;
import cn.com.bbut.iy.itemmaster.entity.base.Ma1000;
import cn.com.bbut.iy.itemmaster.service.CM9060Service;
import cn.com.bbut.iy.itemmaster.service.returnsDaily.ReturnsDailyService;
import cn.com.bbut.iy.itemmaster.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReturnsDailyServiceImpl implements ReturnsDailyService {

    @Autowired
    private ReturnsDailyMapper returnsDailyMapper;
    
    @Autowired
    private CM9060Service cm9060Service;

    // 查询退货数据
    @Override
    public  Map<String,Object> search(ReturnsDailyParamDTO param) {
        param.setReturnDate(Utils.getTimeStamp(param.getReturnDate()));
//        List<ReturnsDailyDTO> result = returnsDailyMapper.search(param);
        int count = returnsDailyMapper.searchNoSaleCount(param);
        // 获取总页数
        int totalPage = (count % param.getRows() == 0) ? (count / param.getRows()) : (count / param.getRows()) + 1;

        List<ReturnsDailyDTO> result1 = returnsDailyMapper.searchNoSale(param);
        List<ReturnsDailyDTO> tranTime = returnsDailyMapper.getAllTranTime(param);
        List<ReturnsDailyDTO> timePeriodsByStore = new ArrayList<>();
        for(int i=0;i<tranTime.size();i+=2){
            ReturnsDailyDTO re = new ReturnsDailyDTO();
            re.setStoreCd(tranTime.get(i).getStoreCd());
            re.setPosId(tranTime.get(i).getPosId());
            re.setTranStartDate(tranTime.get(i).getTranDate());
            // 如果训练模式只有开始时间，没有结束时间，以下一条数据的时间为结束时间
            if(i==tranTime.size()-1 || !"train_end".equals(tranTime.get(i+1).getTranType())){
                ReturnsDailyDTO nextTranTime = returnsDailyMapper.getNextTranTime(tranTime.get(i).getStoreCd(),tranTime.get(i).getPosId()
                        ,tranTime.get(i).getTranDate());
                re.setTranEndDate(nextTranTime.getTranDate());
                i = i-1;
            }else {
                re.setTranEndDate(tranTime.get(i+1).getTranDate());
            }
            timePeriodsByStore.add(re);
        }
        // 判断删除商品是否在训练时间段内
        for(ReturnsDailyDTO dto:result1){
            boolean checkFlg = true;
            for(ReturnsDailyDTO periodDto:timePeriodsByStore){
                if(dto.getStoreCd().equals(periodDto.getStoreCd())&&dto.getPosId().equals(periodDto.getPosId())){
                    if(dto.getTranDate().compareTo(periodDto.getTranStartDate())>0
                            && dto.getTranDate().compareTo(periodDto.getTranEndDate())<0 ){
                        checkFlg = false;
                    }
                }
            }
            if(checkFlg){
                dto.setMode("Sale Mode");
            }else {
                dto.setMode("Train Mode");
            }
        }
        BigDecimal totalQty = BigDecimal.ZERO;
        BigDecimal totalAmout = BigDecimal.ZERO;
        param.setFlg(false);
        ReturnsDailyDTO getItemTotal = returnsDailyMapper.getItemTotal(param);
        if(getItemTotal!=null){
            totalQty = getItemTotal.getTotalQty();
            totalAmout = getItemTotal.getTotalAmt();
        }

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("totalPage",totalPage);
        map.put("count",count);
        map.put("data",result1);
        map.put("totalQty",totalQty);
        map.put("totalAmount",totalAmout);

        return map;
    }

    @Override
    public Map<String,Object> getPrintDate(ReturnsDailyParamDTO param) {
        param.setFlg(false);
        param.setReturnDate(Utils.getTimeStamp(param.getReturnDate()));
        List<ReturnsDailyDTO> result = returnsDailyMapper.searchNoSale(param);
        List<ReturnsDailyDTO> tranTime = returnsDailyMapper.getAllTranTime(param);
        List<ReturnsDailyDTO> timePeriodsByStore = new ArrayList<>();
        for(int i=0;i<tranTime.size();i+=2){
            ReturnsDailyDTO re = new ReturnsDailyDTO();
            re.setStoreCd(tranTime.get(i).getStoreCd());
            re.setPosId(tranTime.get(i).getPosId());
            re.setTranStartDate(tranTime.get(i).getTranDate());
            // 如果训练模式只有开始时间，没有结束时间，以下一条数据的时间为结束时间
            if(i==tranTime.size()-1 || !"train_end".equals(tranTime.get(i+1).getTranType())){
                ReturnsDailyDTO nextTranTime = returnsDailyMapper.getNextTranTime(tranTime.get(i).getStoreCd(),tranTime.get(i).getPosId()
                        ,tranTime.get(i).getTranDate());
                re.setTranEndDate(nextTranTime.getTranDate());
                i = i-1;
            }else {
                re.setTranEndDate(tranTime.get(i+1).getTranDate());
            }
            timePeriodsByStore.add(re);
        }
        // 判断删除商品是否在训练时间段内
        for(ReturnsDailyDTO dto:result){
            boolean checkFlg = true;
            for(ReturnsDailyDTO periodDto:timePeriodsByStore){
                if(dto.getStoreCd().equals(periodDto.getStoreCd())&&dto.getPosId().equals(periodDto.getPosId())){
                    if(dto.getTranDate().compareTo(periodDto.getTranStartDate())>0
                            && dto.getTranDate().compareTo(periodDto.getTranEndDate())<0 ){
                        checkFlg = false;
                    }
                }
            }
            if(checkFlg){
                dto.setMode("Sale Mode");
            }else {
                dto.setMode("Train Mode");
            }
        }
        BigDecimal totalQty = BigDecimal.ZERO;
        BigDecimal totalAmout = BigDecimal.ZERO;
        ReturnsDailyDTO getItemTotal = returnsDailyMapper.getItemTotal(param);
        if(getItemTotal!=null){
            totalQty = getItemTotal.getTotalQty();
            totalAmout = getItemTotal.getTotalAmt();
        }
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("data",result);
        map.put("totalQty",totalQty);
        map.put("totalAmount",totalAmout);
        return map;
    }
    // 查询店铺信息
    @Override
    public Ma1000 selectByStoreCd(String storeCd) {
        String businessDate = cm9060Service.getValByKey("0000");
        return returnsDailyMapper.selectByStoreCd(storeCd,businessDate);
    }
}
