package cn.com.bbut.iy.itemmaster.serviceimpl.writeOff;

import cn.com.bbut.iy.itemmaster.dao.Cm9060Mapper;
import cn.com.bbut.iy.itemmaster.dao.WriteOffMapper;
import cn.com.bbut.iy.itemmaster.dto.cashierDetail.SaleDetail;
import cn.com.bbut.iy.itemmaster.dto.writeOff.WriteOffDTO;
import cn.com.bbut.iy.itemmaster.dto.writeOff.WriteOffParamDTO;
import cn.com.bbut.iy.itemmaster.entity.base.Cm9060;
import cn.com.bbut.iy.itemmaster.service.writeOff.WriteOffService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 门店报废日报
 *
 * @author mxy
 */
@Slf4j
@Service
public class WriteOffServiceImpl implements WriteOffService {

    @Autowired
    private WriteOffMapper writeOffMapper;
    @Autowired
    private Cm9060Mapper cm9060Mapper;


    /**
     * 获取当前业务日期
     */
    public String getBusinessDate() {
        Cm9060 dto =  cm9060Mapper.selectByPrimaryKey("0000");
        return dto.getSpValue();
    }

    /**
     * 条件查询数据
     *
     * @param dto
     * @return
     */
    @Override
    public Map<String,Object> deleteGetList(WriteOffParamDTO dto) {
        WriteOffDTO offDTO = null;
        BigDecimal saleqty=BigDecimal.ZERO;
        // 占时注释掉sale_qty
        //BigDecimal  newwriteOffQty=BigDecimal.ZERO;
         BigDecimal  writeOffQty=BigDecimal.ZERO;
         Integer records=0;
        dto.setBusinessDate(getBusinessDate());
        // 获取总条数
        int count = writeOffMapper.selectCountByCondition(dto);


        // 获取总页数
        int totalPage = (count % dto.getRows() == 0) ? (count / dto.getRows()) : (count / dto.getRows()) + 1;
        if (totalPage==dto.getPage()){
//            offDTO = this.deleteGetOffQty(dto);
//            saleqty=offDTO.getSaleQty();
//           writeOffQty = offDTO.getWriteOffQty();
//            records= Math.toIntExact(offDTO.getRecords());
        }
        dto.setFlg(true);

        List<WriteOffDTO> result = writeOffMapper.selectListByCondition(dto);

        // 占时注释掉sale_qty
//        List<String> storePosTranNoList = new ArrayList<>();
//        for (WriteOffDTO dto1 : result) {
//            String storePosTranNo = dto1.getStoreCd()+"_"+dto1.getArticleId()+"_"+dto1.getWriteOffDate();
//            storePosTranNoList.add(storePosTranNo);
//        }
//        if (result.size()!=0){
//            List<WriteOffDTO> exSaleDetail = writeOffMapper.getExSaleDetail(storePosTranNoList);
//            for (WriteOffDTO dto2:result) {
//                for (WriteOffDTO dto3: exSaleDetail) {
//                    if (dto2.getArticleId().equals(dto3.getArticleId())){
//                        dto2.setSaleQty(dto3.getSaleQty());
//                    }
//                }
//            }
//        }

        Map<String,Object> map = new HashMap<String,Object>();

        map.put("totalPage",totalPage);
        map.put("count",count);
        map.put("data",result);
        map.put("ItemQty",writeOffQty);
        map.put("itemSaleQty", saleqty);
        map.put("totalItem",records);
        return map;
    }

    @Override
    public List<WriteOffDTO> deleteGetList1(WriteOffParamDTO dto) {
        dto.setBusinessDate(getBusinessDate());
        return writeOffMapper.selectListByCondition(dto);

    }

    @Override
    public WriteOffDTO deleteGetOffQty(WriteOffParamDTO dto){
//        dto.setFlg(false);
//        List<WriteOffDTO> result = writeOffMapper.selectListByCondition(dto);
//        List<String> storePosTranNoList = new ArrayList<>();
//        for (WriteOffDTO dto1 : result) {
//            String storePosTranNo = dto1.getStoreCd()+"_"+dto1.getArticleId()+"_"+dto1.getWriteOffDate();
//            storePosTranNoList.add(storePosTranNo);
//        }
            dto.setBusinessDate(getBusinessDate());
            Integer ItemQty=0;
            Integer saleQty=0;
            dto.setFlg(false);
            int countItemSku = writeOffMapper.getCountItemSku(dto);
            List<WriteOffDTO> ItemAllQty = writeOffMapper.selectSaleQty(dto);
            for (WriteOffDTO item:ItemAllQty) {
                BigDecimal bigDecimal =item.getWriteOffQty();
                BigDecimal bigSaleQty =item.getSaleQty();
                ItemQty+=bigDecimal.intValue();
                saleQty+=bigSaleQty.intValue();
            }
            WriteOffDTO offDTO = new WriteOffDTO();
            offDTO.setWriteOffQty(BigDecimal.valueOf(ItemQty));
            offDTO.setSaleQty(BigDecimal.valueOf(saleQty));
            offDTO.setRecords(countItemSku);
            return offDTO;
    }

    @Override
    public Map<String, Object> deleteGetListPrint(WriteOffParamDTO param) {
        param.setBusinessDate(getBusinessDate());
        WriteOffDTO offDTO;
        BigDecimal  writeOffQty;
        BigDecimal  itemSaleQty;
        Integer records=0;
        param.setFlg(false);
        List<WriteOffDTO> result = writeOffMapper.selectListByCondition(param);
        Map<String,Object> map = new HashMap<String,Object>();
        offDTO = this.deleteGetOffQty(param);
        writeOffQty = offDTO.getWriteOffQty();
        itemSaleQty = offDTO.getSaleQty();
        records= Math.toIntExact(offDTO.getRecords());
        map.put("data",result);
        map.put("ItemQty",writeOffQty);
        map.put("totalItem",records);
        map.put("itemSaleQty",itemSaleQty);
        return map;
    }

}

