package cn.com.bbut.iy.itemmaster.serviceimpl.inventory;

import cn.com.bbut.iy.itemmaster.dao.*;
import cn.com.bbut.iy.itemmaster.dao.inventory.InventoryVouchersMapper;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.CommonDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.expenditure.ExpenditureDTO;
import cn.com.bbut.iy.itemmaster.dto.inventory.*;
import cn.com.bbut.iy.itemmaster.dto.ma100Ld.Ma1000DTO;
import cn.com.bbut.iy.itemmaster.dto.ma8350.MA8350dto;
import cn.com.bbut.iy.itemmaster.dto.rtInventory.RTInventoryQueryDTO;
import cn.com.bbut.iy.itemmaster.entity.*;
import cn.com.bbut.iy.itemmaster.entity.base.Cm9060;
import cn.com.bbut.iy.itemmaster.service.CM9060Service;
import cn.com.bbut.iy.itemmaster.service.RealTimeInventoryQueryService;
import cn.com.bbut.iy.itemmaster.service.SequenceService;
import cn.com.bbut.iy.itemmaster.service.inventory.InventoryVouchersService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * InventoryVouchers
 *
 * @author
 */
@Service
@Slf4j
public class InventoryVouchersServiceImpl implements InventoryVouchersService {

    @Autowired
    private InventoryVouchersMapper inventoryVouchersMapper;
    @Autowired
    private Cm9060Mapper cm9060Mapper;
    @Autowired
    private SK0010Mapper sK0010Mapper;
    @Autowired
    private SK0020Mapper sK0020Mapper;
    @Autowired
    private MA4320Mapper ma4320Mapper;
    @Autowired
    private SequenceService sequenceService;
    @Autowired
    private ExpenditureMapper expenditureMapper;
    @Value("${esUrl.inventoryUrl}")
    private String inventoryUrl;
    @Autowired
    private CM9060Service cm9060Service;
    @Autowired
    private RealTimeInventoryQueryService rtInventoryService;


    /*
     * 条件查询一览数据
     *
     */
    @Override
    public GridDataDTO<InventoryVouchersGridDTO> getListByCondition(InventoryVouchersParamDTO param) {
        String businessDate = getBusinessDate();
        param.setBusinessDate(businessDate);
        int count = inventoryVouchersMapper.selectCountByCondition(param);
        if(count < 1){
            return new GridDataDTO<>();
        }
        List<InventoryVouchersGridDTO> _list = inventoryVouchersMapper.selectListByCondition(param);
        GridDataDTO<InventoryVouchersGridDTO> data = new GridDataDTO<>(_list,
          param.getPage(), count, param.getRows());
        return data;
    }

    /*
     * 类型&条件查询数据
     *
     */
    @Override
    public GridDataDTO<InventoryVouchersGridDTO> getByTypeCondition(InventoryVouchersParamDTO param) {

        String businessDate = getBusinessDate();
        param.setBusinessDate(businessDate);

        int count = inventoryVouchersMapper.selectCountByTypeCondition(param);
        if(count < 1){
            return new GridDataDTO<InventoryVouchersGridDTO>();
        }

        List<InventoryVouchersGridDTO> _list = inventoryVouchersMapper.selectByTypeCondition(param);

        GridDataDTO<InventoryVouchersGridDTO> data = new GridDataDTO<InventoryVouchersGridDTO>(_list,
                param.getPage(), count, param.getRows());

        return data;
    }

    /*
     * 店间转入查询数据
     *
     */
    @Override
    public GridDataDTO<InventoryVouchersGridDTO> getByTypeInCondition(InventoryVouchersParamDTO param) {

        String businessDate = getBusinessDate();
        param.setBusinessDate(businessDate);

        int count = inventoryVouchersMapper.selectCountByTypeInCondition(param);
        if(count < 1){
            return new GridDataDTO<InventoryVouchersGridDTO>();
        }

        List<InventoryVouchersGridDTO> _list = inventoryVouchersMapper.selectByTypeInCondition(param);

        GridDataDTO<InventoryVouchersGridDTO> data = new GridDataDTO<InventoryVouchersGridDTO>(_list,
                param.getPage(), count, param.getRows());

        return data;
    }

    /*
     * 查询商品信息
     *
     */
    @Override
    public ItemInfoDTO getItemInfoByCode(String storeCd, String itemCode) {
        String businessDate = getBusinessDate();
        return inventoryVouchersMapper.selectItemInfoByCode(storeCd, itemCode, businessDate);
    }

    /*
     * 查询店铺信息
     *
     */
    @Override
    public StoreInfoDTO getStoreByCode(String storeCd) {
        String businessDate = getBusinessDate();
        return inventoryVouchersMapper.selectStoreByCode(storeCd, businessDate);
    }

    /**
     * 保存传票
     *  @param sk0010
     * @param sk0020List
     * @return
     */
    @Override
    @Transactional
    public String insert(Sk0010DTO sk0010, List<Sk0020DTO> sk0020List) {
        int i = 0;
        String _id = "";
        try {
            /**
             * 生成传票编号
             * 604: 库存调整
             * 603: 库存报废
             * 502: 转出
             * 501: 转入
             * 601:店内转入
             * 602:店内转出
             */
            if ("604".equals(sk0010.getVoucherType())) {
                _id = sequenceService.getSequence("sk0010_iad_id_seq","IAD",sk0010.getStoreCd());
            } else if ("603".equals(sk0010.getVoucherType())) {
                _id = sequenceService.getSequence("sk0010_iwo_id_seq","IWO",sk0010.getStoreCd());
            } else if ("502".equals(sk0010.getVoucherType())) {
                _id = sequenceService.getSequence("sk0010_iso_id_seq","ISO",sk0010.getStoreCd());
            } else if ("501".equals(sk0010.getVoucherType())) {
                _id = sequenceService.getSequence("sk0010_isi_id_seq","ISI",sk0010.getStoreCd());
            } else if ("602".equals(sk0010.getVoucherType())) {
                _id = sequenceService.getSequence("sk0010_ist_id_seq","IST",sk0010.getStoreCd());
            } else if ("601".equals(sk0010.getVoucherType())) {
                _id = sequenceService.getSequence("sk0010_ist_id_seq","IST",sk0010.getStoreCd());
            }

            if(StringUtils.isBlank(_id)){
                throw new RuntimeException("生成传票编号失败");
            }
            // 执行保存
            sk0010.setVoucherNo(_id);
            // 是否上传初始为No
            sk0010.setUploadFlg("0");
            // 保存头档
            i = inventoryVouchersMapper.insertSk0010(sk0010);
            log.error("voucherType:"+sk0010.getVoucherType()+"<br>"+"sk0010:"+sk0010);
            // 保存明细
            for(Sk0020DTO bean : sk0020List){
                bean.setVoucherNo(sk0010.getVoucherNo());
                bean.setUploadFlg("0");
                bean.setCommonDTO(sk0010.getCommonDTO());
                inventoryVouchersMapper.insertSk0020(bean);
            }

            //添加附件信息
            if(StringUtils.isNotBlank(sk0010.getFileDetailJson())){
                List<MA4320> ma4320List = new Gson().fromJson(sk0010.getFileDetailJson(), new TypeToken<List<MA4320>>(){}.getType());
                for (int j = 0; j < ma4320List.size(); j++) {
                    MA4320 ma4320 = ma4320List.get(j);
                    ma4320.setInformCd(sk0010.getVoucherNo());
                    ma4320.setCreateUserId(sk0010.getCommonDTO().getCreateUserId());
                    ma4320.setCreateYmd(sk0010.getCommonDTO().getCreateYmd());
                    ma4320.setCreateHms(sk0010.getCommonDTO().getCreateHms());
                    ma4320Mapper.insertSelective(ma4320);
                }
            }
        }catch (RuntimeException e){
            log.error(e.getMessage());
            return null;
        }
        return _id;
    }

    /**
     * 修改传票
     *
     * @param sk0010
     * @param sk0020List
     */
    @Override
    @Transactional
    public int update(SK0010 sk0010, List<Sk0020DTO> sk0020List) {
        int i = 0;
        try {
            // 保存头档
            i = sK0010Mapper.updateByPrimaryKeySelective(sk0010);
            // 保存明细
            inventoryVouchersMapper.deleteSk0020ByKey(sk0020List.get(0));
            for(Sk0020DTO bean : sk0020List){
                bean.setUploadFlg("0");
                inventoryVouchersMapper.insertSk0020(bean);
            }
            inventoryVouchersMapper.updateSk0010Amt(sk0020List.get(0));

            //添加附件信息  "04" : 调拨  "10" : 报废
            MA4320Example example = new MA4320Example();
            example.or().andInformCdEqualTo(sk0010.getVoucherNo()).andFileTypeEqualTo(sk0010.getFileType());
            ma4320Mapper.deleteByExample(example);
            if(StringUtils.isNotBlank(sk0010.getFileDetailJson())){
                List<MA4320> ma4320List = new Gson().fromJson(sk0010.getFileDetailJson(), new TypeToken<List<MA4320>>(){}.getType());
                for (int j = 0; j < ma4320List.size(); j++) {
                    MA4320 ma4320 = ma4320List.get(j);
                    ma4320.setInformCd(sk0010.getVoucherNo());
                    ma4320.setCreateUserId(sk0010.getCreateUserId());
                    ma4320.setCreateYmd(sk0010.getCreateYmd());
                    ma4320.setCreateHms(sk0010.getCreateHms());
                    ma4320Mapper.insertSelective(ma4320);
                }
            }
        }catch (RuntimeException e){
            log.error(e.getMessage());
            return 0;
        }
        return i;
    }

    @Override
    public int updateQty1(Sk0020DTO sk0020DTO) {
        return inventoryVouchersMapper.updateQty1(sk0020DTO);
    }

    @Override
    public int updateDiffReasons(Sk0020DTO sk0020DTO) {
        return inventoryVouchersMapper.updateDifferenceReasons(sk0020DTO);
    }

    /**
     * 修改传票商品详情
     *
     * @param dto
     * @param sk0020List
     */
    @Override
    @Transactional
    public int updateSk0020(CommonDTO dto, List<Sk0020DTO> sk0020List) {
        int i = 0;
        try {
            // 保存明细
            inventoryVouchersMapper.deleteSk0020ByKey(sk0020List.get(0));
            for(Sk0020DTO bean : sk0020List){
                bean.setCommonDTO(dto);
                inventoryVouchersMapper.insertSk0020(bean);
            }
            i = 1;
        }catch (RuntimeException e){
            log.error(e.getMessage());
            return 0;
        }
        return i;
    }

    /**
     * 查询头档数据
     *
     * @param sk0010
     */
    @Override
    public SK0010 getSk0010(SK0010Key sk0010) {
        return sK0010Mapper.selectByPrimaryKey(sk0010);
    }

    /**
     * 查询详情数据
     *
     * @param sk0020
     */
    @Override
    public GridDataDTO<Sk0020DTO> getSk0020(Sk0020ParamDTO sk0020) {
        String inEsTime = cm9060Service.getValByKey("1206");
        if(sk0020.getVoucherType().equals("603")){
          sk0020.setCodeType("00236");
        }else if(sk0020.getVoucherType().equals("501")||sk0020.getVoucherType().equals("502")){
            sk0020.setCodeType("00235");
        }
        GridDataDTO<Sk0020DTO> data = new GridDataDTO<Sk0020DTO>();
        List<Sk0020DTO> _list = inventoryVouchersMapper.selectListSk0020(sk0020);

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
    public GridDataDTO<Sk0020DTO> selectDetailSk0020(Sk0020ParamDTO sk0020) {
        GridDataDTO<Sk0020DTO> data = new GridDataDTO<Sk0020DTO>();
        List<Sk0020DTO> _list = inventoryVouchersMapper.selectDetailSk0020(sk0020);
        if(_list == null || _list.equals("")){
            log.info("<<<<<<<<<<_list is null");
        }else {
            for (Sk0020DTO sk02: _list) {
                if(sk02.getQty2() == null){
                    sk02.setQty2(BigDecimal.ZERO);
                }else if(sk02.getQty1() == null){
                    sk02.setQty1(BigDecimal.ZERO);
                }
                sk02.setDifferenQty(sk02.getQty2().subtract(sk02.getQty1()));
            }
        }
        data.setRows(_list);
        return data;
    }

    /**
     * 查询传票编号
     *
     * @param voucherNo
     */
    @Override
    public int getSk0010ByVoucherNo(String voucherNo) {
        SK0010Example example = new SK0010Example();
        example.or().andVoucherNoEqualTo(voucherNo);
        List<SK0010> _list = sK0010Mapper.selectByExample(example);
        return _list == null ? 0 : _list.size();
    }

    /**
     * 自动加载下拉
     * @param v
     * @return
     */
    @Override
    public List<AutoCompleteDTO> getStoreList(String v) {
        String businessDate = getBusinessDate();
        return inventoryVouchersMapper.getStoreList(v, businessDate);
    }
    @Override
    public List<AutoCompleteDTO> getOutStoreList(String v, String zoCd) {
        String businessDate = getBusinessDate();
        return inventoryVouchersMapper.getOutStoreList(v,zoCd, businessDate);
    }

    /**
     * 获取审核通过的转出门店by InStore
     * @param v
     * @param vstore
     * @return
     */
    @Override
    public List<AutoCompleteDTO> getStoreListByInStore(String v, String vstore) {
        String businessDate = getBusinessDate();
        return inventoryVouchersMapper.getStoreListByInStore(v,vstore, businessDate);
    }

    @Override
    public List<AutoCompleteDTO> getMa1172OutItemList(String storeCd, String v) {
        String businessDate = getBusinessDate();
        return inventoryVouchersMapper.getMa1172OutItemList(storeCd, v, businessDate);
    }

    @Override
    public List<AutoCompleteDTO> getMa1172InItemList(String outArticleId, String storeCd, String v) {
        String businessDate = getBusinessDate();
        return inventoryVouchersMapper.getMa1172InItemList(outArticleId,storeCd, v, businessDate);
    }

    /**
     * 商品自动下拉
     */
    @Override
    public List<AutoCompleteDTO> getItemList(String storeCd, String v) {
        String businessDate = getBusinessDate();
        return inventoryVouchersMapper.getItemList(storeCd, v, businessDate);
    }

    @Override
    public List<AutoCompleteDTO> getInventoryItemList(String storeCd, String v) {
        String businessDate = getBusinessDate();
        return inventoryVouchersMapper.getInventoryItemList(storeCd, v, businessDate);
    }

    @Override
    public List<AutoCompleteDTO> getDOCList(String storeCd,String storeCd1,String v) {
        return inventoryVouchersMapper.getDOCList(storeCd, storeCd1, v);
    }

    @Override
    public List<InventoryVouchersGridDTO> existsDOC(String storeCd, String storeCd1) {
        return inventoryVouchersMapper.existsDOC(storeCd,storeCd1);
    }

    @Override
    public List<InventoryVouchersGridDTO> getApprovedList(String storeCd, String storeCd1) {
        return inventoryVouchersMapper.getApprovedList(storeCd,storeCd1);
    }

    /**
     * 获取实时库存数量
     *
     * @param storeCd
     * @param itemId
     */
    @Override
    public String getStock(String storeCd, String itemId) {
        return inventoryVouchersMapper.selectStock(storeCd, itemId);
    }

    @Override
    public List<AutoCompleteDTO> generalReason(String v) {
        return inventoryVouchersMapper.getMa8350(v);
    }

    @Override
    public List<AutoCompleteDTO> detailReason( String v,int num) {
        return inventoryVouchersMapper.getMa8360(v,num);
    }
    @Override
    public List<AutoCompleteDTO> Reasondetail(String v) {
       return inventoryVouchersMapper.getReasondetailAndGeneralReason(v);

    }

    @Override
    public MA8350dto getGeneralReason(String generalLevelCd) {
        MA8350dto ma8350dto = inventoryVouchersMapper.getGeneralReasonDetail(generalLevelCd);
        if (ma8350dto==null){
            String detailedLevelCd=generalLevelCd;
            MA8350dto reasonDetail = inventoryVouchersMapper.getNewGeneralReasonDetail(detailedLevelCd);;
            return  reasonDetail;
        }
        return ma8350dto;
    }

    @Override
    public MA8350dto getGeneralReasonDetail(String detailedLevelCd) {
        MA8350dto ma8350dto = inventoryVouchersMapper.getNewGeneralReasonDetail(detailedLevelCd);
        if (ma8350dto==null){
            String generalLevelCd=detailedLevelCd;
            MA8350dto reasonDetail = inventoryVouchersMapper.getGeneralReasonDetail(generalLevelCd);
            return  reasonDetail;
        }
        return ma8350dto;
    }


        @Override
        public GridDataDTO<Sk0020DTO> getSk0020Out(Sk0020ParamDTO sk0020) {
            String inEsTime = cm9060Service.getValByKey("1206");
            GridDataDTO<Sk0020DTO> data = new GridDataDTO<Sk0020DTO>();
            List<Sk0020DTO> _list = inventoryVouchersMapper.selectListSk0020OutStore(sk0020);
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
    public Ma1000DTO getSouthOrNouth(String storeCd) {
        return inventoryVouchersMapper.getSouthOrNouth(storeCd);


    }


    @Override
    public Map<String, Object> Total(Sk0020ParamDTO sk0020) {
        Integer sumQty1=0;
        if(sk0020.getVoucherType().equals("603")){
            sk0020.setCodeType("00236");
        }else if(sk0020.getVoucherType().equals("501")||sk0020.getVoucherType().equals("502")){
            sk0020.setCodeType("00235");
        }
        Cm9060 dto = cm9060Mapper.selectByPrimaryKey("0000");
        sk0020.setBusinessDate(dto.getSpValue());
        int totalArticle = inventoryVouchersMapper.selectSumArticle(sk0020);
        List<Sk0020DTO> _list = inventoryVouchersMapper.selectListSk0020(sk0020);
        for (Sk0020DTO S1:_list) {
            sumQty1+=S1.getQty1().intValue();
        }
        System.out.println(sumQty1);
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("totalQty",sumQty1);
        map.put("totalItemSKU",totalArticle);
        return map;
    }

    @Override
    public SK0010 getStoreSeries(String storeCd){
        return sK0010Mapper.getStoreSeries(storeCd);
    }

    @Override
    public List<SK0010> getApprovedInfo(String voucherNo){
        return sK0010Mapper.getApprovedInfo(voucherNo);
    }


    /**
     * 获取当前业务日期
     */
    public String getBusinessDate() {
        Cm9060 dto =  cm9060Mapper.selectByPrimaryKey("0000");
        return dto.getSpValue();
    }
}
