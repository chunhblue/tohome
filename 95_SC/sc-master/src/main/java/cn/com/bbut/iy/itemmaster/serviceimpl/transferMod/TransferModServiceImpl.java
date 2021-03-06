package cn.com.bbut.iy.itemmaster.serviceimpl.transferMod;

import cn.com.bbut.iy.itemmaster.dao.Cm9060Mapper;
import cn.com.bbut.iy.itemmaster.dao.SK0010Mapper;
import cn.com.bbut.iy.itemmaster.dao.SK0020Mapper;
import cn.com.bbut.iy.itemmaster.dao.TransferModMapper;
import cn.com.bbut.iy.itemmaster.dao.inventory.InventoryVouchersMapper;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.inventory.Sk0010DTO;
import cn.com.bbut.iy.itemmaster.dto.inventory.Sk0020DTO;
import cn.com.bbut.iy.itemmaster.dto.inventory.Sk0020ParamDTO;
import cn.com.bbut.iy.itemmaster.entity.SK0010;
import cn.com.bbut.iy.itemmaster.entity.SK0010Example;
import cn.com.bbut.iy.itemmaster.entity.SK0010Key;
import cn.com.bbut.iy.itemmaster.entity.SK0020Example;
import cn.com.bbut.iy.itemmaster.entity.base.Cm9060;
import cn.com.bbut.iy.itemmaster.entity.od0000.OD0000;
import cn.com.bbut.iy.itemmaster.service.SequenceService;
import cn.com.bbut.iy.itemmaster.service.transferMod.TransferModService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 店间调拨修正
 *
 * @author
 */
@Service
@Slf4j
public class TransferModServiceImpl implements TransferModService {

    @Autowired
    private Cm9060Mapper cm9060Mapper;
    @Autowired
    private TransferModMapper mapper;
    @Autowired
    private InventoryVouchersMapper inventoryMapper;
    @Autowired
    private SK0020Mapper sk0020Mapper;
    @Autowired
    private SequenceService sequenceService;


    /**
     * 获取当前业务日期
     */
    public String getBusinessDate() {
        Cm9060 dto =  cm9060Mapper.selectByPrimaryKey("0000");
        return dto.getSpValue();
    }

    /**
     * 查询头档数据
     *
     * @param sk0010
     */
    @Override
    public SK0010 getSk0010(SK0010Key sk0010) {
        return mapper.selectSk0010(sk0010);
    }

    /**
     * 查询详情数据
     *
     * @param sk0020
     */
    @Override
    public GridDataDTO<Sk0020DTO> getSk0020(Sk0020ParamDTO sk0020) {
        List<Sk0020DTO> _list = mapper.selectSk0020(sk0020);
        GridDataDTO<Sk0020DTO> data = new GridDataDTO<Sk0020DTO>();
        data.setRows(_list);
        return data;
    }

    /**
     * 查询传票下拉
     *
     * @param storeCd
     * @param type
     * @param v
     */
    @Override
    public List<AutoCompleteDTO> getOrgOrderList(String storeCd, String type, String v) {
        String businessDate = getBusinessDate();
        List<AutoCompleteDTO> _list =  mapper.selectOrgOrder(storeCd,businessDate, type, v);
        if(_list == null || _list.size() == 0){
            return _list;
        }
        Collection<String> voucherNos = new ArrayList<>();
        for(AutoCompleteDTO  dto : _list){
            voucherNos.add(dto.getV());
        }
        List<SK0010> corrList = mapper.getCorrList(voucherNos,type);
        List<SK0010> newList = new ArrayList<>();
        for(SK0010 oldItem : corrList){
            boolean checkFlg = true;
            for(SK0010 newItem : newList){
                if(newItem.getVoucherNo1().equals(oldItem.getVoucherNo1())){
                    if(oldItem.getCreateYmd()!=null && oldItem.getCreateHms()!=null
                            && newItem.getCreateYmd()!=null&& newItem.getCreateHms()!=null){
                        if(Long.parseLong(oldItem.getCreateYmd()+oldItem.getCreateHms())
                                > Long.parseLong(newItem.getCreateYmd()+newItem.getCreateHms())){
                            newItem.setVoucherNo(oldItem.getVoucherNo());
                            newItem.setVoucherNo1(oldItem.getVoucherNo1());
                            newItem.setReviewSts(oldItem.getReviewSts());
                            newItem.setCreateYmd(oldItem.getCreateYmd());
                            newItem.setCreateHms(oldItem.getCreateHms());
                            newItem.setStoreCd(oldItem.getStoreCd());
                        }
                    }
                    checkFlg = false;
                }
            }
            if(checkFlg){
                newList.add(oldItem);
            }
        }
        if(newList.size()>0){
            for(int i =0;i<_list.size();i++){
                AutoCompleteDTO dto = _list.get(i);
                for(SK0010 newItem : newList){
                    if(dto.getV().equals(newItem.getVoucherNo1()) && !newItem.getReviewSts().equals("10")){
                        _list.remove(dto);
                        i--; // 索引减1，保证索引正常，不然报java.util.ConcurrentModificationException
                    }
                }
            }
        }
        return _list;
    }

    /**
     * 保存数据
     *  @param sk0010
     * @param list
     * @return
     */
    @Override
    @Transactional
    public String insert(Sk0010DTO sk0010, List<Sk0020DTO> list) {
        String _id = null;
        try{
            // 生成传票编号
            String upFlg = "";
            if(StringUtils.isBlank(sk0010.getVoucherNo())){
                _id = sequenceService.getSequence("sk0010_cst_id_seq","CST",sk0010.getStoreCd());
                upFlg = "add";
            }else{
                upFlg = "edit";
                _id = sk0010.getVoucherNo();
            }
            if(StringUtils.isBlank(_id)){
                throw new RuntimeException("Failed to generate document No., please try again!");
            }

            List<Sk0020DTO> lastCorrList;
            String lastVoucherNo =  inventoryMapper.getLastCorrTransfer(sk0010.getVoucherNo1(),sk0010.getVoucherType());
            if(lastVoucherNo == null || "".equals(lastVoucherNo)){
                // 如果是第一次修正，将原转移数量放入qty2
                lastCorrList = inventoryMapper.getLastCorrQty(sk0010.getVoucherNo1());
                for(Sk0020DTO bean : list){
                    for(Sk0020DTO lastCorr : lastCorrList){
                        if(bean.getArticleId().equals(lastCorr.getArticleId())
                                && bean.getBarcode().equals(lastCorr.getBarcode())){
                            if("504".equals(bean.getVoucherType())){
                                bean.setQty2(lastCorr.getQty1());
                            }else {
                                bean.setQty2(lastCorr.getQty2());
                            }
                        }
                    }
                }
            }else {
                // 若是多次修正，将上一次的修正数量放入qty2
                lastCorrList = inventoryMapper.getLastCorrQty(lastVoucherNo);
                for(Sk0020DTO bean : list){
                    for(Sk0020DTO lastCorr : lastCorrList){
                        if(bean.getArticleId().equals(lastCorr.getArticleId())
                                && bean.getBarcode().equals(lastCorr.getBarcode())){
                            bean.setQty2(lastCorr.getActualQty());
                        }
                    }
                }
            }


            // 执行保存
            if("add".equals(upFlg)){
                sk0010.setVoucherNo(_id);
                inventoryMapper.insertSk0010(sk0010);
                log.error("voucherType:"+sk0010.getVoucherType()+"<br>"+"sk0010:"+sk0010);
            }else if ("edit".equals(upFlg)){
                inventoryMapper.updateSk0010(sk0010);
                //删除明细数据
                SK0020Example example = new SK0020Example();
                example.or().andVoucherNoEqualTo(_id);
                sk0020Mapper.deleteByExample(example);
            }
            for(Sk0020DTO bean : list){
                bean.setVoucherNo(sk0010.getVoucherNo());
                bean.setCommonDTO(sk0010.getCommonDTO());
                inventoryMapper.insertSk0020(bean);
                inventoryMapper.updateActualQty(sk0010.getVoucherNo1(),sk0010.getStoreCd(),bean.getActualQty(),bean.getArticleId());
            }
        }catch (Exception e){
            log.error("save >>>>>> "+e.getMessage());
            return null;
        }
        return _id;
    }
}
