package cn.com.bbut.iy.itemmaster.serviceimpl.electronicReceipt;

import cn.com.bbut.iy.itemmaster.dao.SA0060Mapper;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.electronicReceipt.ElectronicReceiptParam;
import cn.com.bbut.iy.itemmaster.entity.SA0060;
import cn.com.bbut.iy.itemmaster.service.electronicReceipt.ElectronicReceiptService;
import cn.com.bbut.iy.itemmaster.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ElectronicReceiptServiceImpl implements ElectronicReceiptService {

    @Autowired
    private SA0060Mapper mapper;


    @Override
    public List<SA0060> getList(ElectronicReceiptParam dto) {
        dto.setSaleDate(Utils.getTimeStamp(dto.getSaleDate()));
        List<SA0060> _list = null;
        if(dto.getBillSaleNo() != null && !"".equals(dto.getBillSaleNo())){
            if(dto.getBillSaleNo().contains("NRISV")){
                dto.setBillFlg(1);
            }else {
                dto.setBillFlg(0);
            }
        }
        _list = mapper.selectByDtoJe(dto);
        return _list;
    }
    @Override
    public List<AutoCompleteDTO> getReceiptType(String v, String storeCd, String posId,String saleDate,String endDate) {

        List<AutoCompleteDTO> receiptType = mapper.getReceiptType(v, storeCd, posId,saleDate,endDate);
        return  receiptType;
    }

    @Override
    public List<AutoCompleteDTO> getItemInfo(String v, String storeCd, String posId, String salesDate) {
        salesDate = Utils.getTimeStamp(salesDate);
        List<AutoCompleteDTO> item = mapper.getItem(v, storeCd, posId, salesDate);
        return item;

    }
}
