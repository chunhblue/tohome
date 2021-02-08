package cn.com.bbut.iy.itemmaster.serviceimpl.electronicReceipt;

import cn.com.bbut.iy.itemmaster.dao.Cm9060Mapper;
import cn.com.bbut.iy.itemmaster.dao.ExpenditureMapper;
import cn.com.bbut.iy.itemmaster.dao.SA0060Mapper;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.electronicReceipt.ElectronicReceiptParam;
import cn.com.bbut.iy.itemmaster.dto.electronicReceipt.ReceiptTypeDto;
import cn.com.bbut.iy.itemmaster.dto.expenditure.ExpenditureDTO;
import cn.com.bbut.iy.itemmaster.dto.expenditure.ExpenditureParamDTO;
import cn.com.bbut.iy.itemmaster.entity.SA0060;
import cn.com.bbut.iy.itemmaster.entity.SA0060Example;
import cn.com.bbut.iy.itemmaster.entity.base.Cm9060;
import cn.com.bbut.iy.itemmaster.service.electronicReceipt.ElectronicReceiptService;
import cn.com.bbut.iy.itemmaster.service.expenditure.ExpenditureService;
import com.google.gson.Gson;
import com.sun.corba.se.pept.transport.ListenerThread;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ElectronicReceiptServiceImpl implements ElectronicReceiptService {

    @Autowired
    private SA0060Mapper mapper;


    /**
     * 条件查询记录
     *
     * @param dto
     */
//    @Override
//    public List<SA0060> getList(ElectronicReceiptParam dto) {
//        SA0060Example example = new SA0060Example();
//        // 设置排序
//        example.setOrderByClause("acc_date,pos_id,sale_serial_no ASC");
//        // 拼接条件参数
//        SA0060Example.Criteria criteria = example.createCriteria();
//        List<String> stores = new ArrayList<>(dto.getStores());
//        criteria.andStoreCdIn(stores);
//        criteria.andAccDateBetween(dto.getSaleStartDate(),dto.getSaleEndDate());
//        if(StringUtils.isNotBlank(dto.getPosNo())){
//            criteria.andPosIdEqualTo(dto.getPosNo());
//        }
//        if(dto.getSaleNo()!=0){
//            criteria.andSaleSerialNoEqualTo(dto.getSaleNo());
//        }
//        example.or(criteria);
//
//
//        List<SA0060> sa0060s = mapper.selectByExample(example);
//
//
//        return sa0060s;
//    }
    @Override
    public List<SA0060> getList(ElectronicReceiptParam dto) {
        return mapper.selectByDto(dto);
    }
    @Override
    public List<AutoCompleteDTO> getReceiptType(String v, String storeCd, String posId,String saleDate,String endDate) {

        List<AutoCompleteDTO> receiptType = mapper.getReceiptType(v, storeCd, posId,saleDate,endDate);
        return  receiptType;
    }

    @Override
    public List<AutoCompleteDTO> getItemInfo(String v, String storeCd, String posId, String startDate, String endDate) {
        List<AutoCompleteDTO> item = mapper.getItem(v, storeCd, posId, startDate, endDate);
        System.out.println(item);
        return item;

    }

    @Override
    public List<SA0060> getContentList(ElectronicReceiptParam dto) {
        List<SA0060> _list = null;
        return mapper.selectByDto(dto);
    }


}
