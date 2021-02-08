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
import cn.com.bbut.iy.itemmaster.service.SequenceService;
import cn.com.bbut.iy.itemmaster.service.transferMod.TransferModService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return mapper.selectOrgOrder(storeCd,businessDate, type, v);
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
            // 执行保存
            if("add".equals(upFlg)){
                sk0010.setVoucherNo(_id);
                inventoryMapper.insertSk0010(sk0010);
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
