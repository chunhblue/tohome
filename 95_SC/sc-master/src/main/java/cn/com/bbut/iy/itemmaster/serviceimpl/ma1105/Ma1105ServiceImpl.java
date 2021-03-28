package cn.com.bbut.iy.itemmaster.serviceimpl.ma1105;

import cn.com.bbut.iy.itemmaster.dao.Cm9060Mapper;
import cn.com.bbut.iy.itemmaster.dao.Ma1105Mapper;
import cn.com.bbut.iy.itemmaster.dto.base.CommonDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.ma1105.Ma1104Dto;
import cn.com.bbut.iy.itemmaster.dto.ma1105.Ma1105ParamDTO;
import cn.com.bbut.iy.itemmaster.entity.base.Cm9060;
import cn.com.bbut.iy.itemmaster.entity.base.Ma1105;
import cn.com.bbut.iy.itemmaster.entity.base.Ma1105Example;
import cn.com.bbut.iy.itemmaster.service.SequenceService;
import cn.com.bbut.iy.itemmaster.service.ma1105.Ma1105Service;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class Ma1105ServiceImpl implements Ma1105Service {
    @Autowired
    private Ma1105Mapper ma1105Mapper;
    @Autowired
    private Cm9060Mapper cm9060Mapper;
    @Autowired
    private SequenceService sequenceService;

    @Override
    public GridDataDTO<Ma1105> getData(Ma1105ParamDTO param) {
        Gson gson = new Gson();
        Ma1105 dto = gson.fromJson(param.getSearchJson(), Ma1105.class);
        Cm9060 cm9060 = cm9060Mapper.selectByPrimaryKey("0000");
        dto.setBusinessDate(cm9060.getSpValue());
        dto.setLimitStart(param.getLimitStart());
        dto.setLimitEnd(param.getLimitEnd());
        dto.setOrderByClause(param.getOrderByClause());
        dto.setResources(param.getResources());
        dto.setStores(param.getStores());
        long count = ma1105Mapper.selectListCount(dto);
        if(count == 0){
            return new GridDataDTO<Ma1105>();
        }
        List<Ma1105> list = ma1105Mapper.selectList(dto);
        return new GridDataDTO<>(list, param.getPage(), count,
                param.getRows());
    }

    @Override
    public List<String> selectMa1105(List<Ma1105> list) {
        List<String> shelf = ma1105Mapper.selectMa1105(list);
        return shelf;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertData(List<Ma1105> ma1105s,String storeCd) {
        //获取shelf集合
//        List<String> shelfs = ma1105s.stream().map(shelf->shelf.getShelf()).collect(Collectors.toList());
//        Ma1105Example example = new Ma1105Example();
        //删除货架的数据
//        example.or().andStoreCdEqualTo(storeCd).andShelfIn(shelfs);
//        ma1105Mapper.deleteByExample(example);
        //导入数据
//        ma1105s.forEach(ma1105 -> {
//            ma1105Mapper.insertSelective(ma1105);
//        });

        ma1105Mapper.insertMa1105tSelective(ma1105s);
        //添加审核信息
        ma1105Mapper.insertAudit(storeCd);
        return 1;
    }

    @Override
    public int updateShelfToMa1105(String storeCd) {
        // 获取上传POG店铺的审核状态
        int status = ma1105Mapper.getStatusByStoreCd(storeCd);
         // 审核通过
        if(status == 10){
            List<Ma1105> getExamples = ma1105Mapper.getTempShelf(storeCd);
            Collection<Ma1105> ma1105s = getExamples;
            if(getExamples.size()>0){
                //删除已有货架的数据
                ma1105Mapper.deleteMa1105Byshelf(getExamples);
            }
            // 导入数据
            int num = ma1105Mapper.insertShelfToMall05(storeCd);
        }
        ma1105Mapper.deleteShelf(storeCd);

        // 获取POG的信息
        Ma1104Dto pogInfo = ma1105Mapper.getPOGInformation(storeCd);
        pogInfo.setReviewStatus(new BigDecimal(status));
        if(status == 10){
            pogInfo.setIsExpired("0");
        }else {
            pogInfo.setIsExpired("1");
        }

        // 更新ma1104的状态
        ma1105Mapper.updatePOGAuditStatus(pogInfo);
        if(status == 10){
            // 使之前上传货架的状态失效
            ma1105Mapper.updatePOGIsExpiredStatus(pogInfo);
        }
        return status;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertFileData(List<Ma1105> ma1105s, String excelName, String storeCd, CommonDTO dto) {
        String pogCd = "";
        pogCd = sequenceService.getSequence("ma1107_pog_cd_seq","",storeCd);
        ma1105Mapper.deleteMa1107(excelName,storeCd);
//        ma1105Mapper.deleteMa1104(pogCd,storeCd);
        ma1105Mapper.insertMa1107(pogCd,excelName,storeCd,dto);
        ma1105Mapper.insertMa1104(pogCd,excelName,storeCd,dto);
        for(Ma1105 ma1105 :ma1105s){
            ma1105.setPogCd(pogCd);
            ma1105.setExcelName(excelName);
            ma1105.setStoreCd(storeCd);
        }


        // 过滤掉相同的shelf,subShelf
        List<Ma1105> newShelfList = new ArrayList<>();
        List<Ma1105> newSubShelfList = new ArrayList<>();
        for(Ma1105 ma1105 : ma1105s){
            boolean checkFlg = true;
            boolean checkFlg1 = true;
            for(Ma1105 newShelf : newShelfList){
                if(newShelf.getPogCd().equals(ma1105.getPogCd())
                   && newShelf.getShelf().equals(ma1105.getShelf())
                   && newShelf.getStoreCd().equals(ma1105.getStoreCd())){
                    checkFlg = false;
                }
            }
            if(checkFlg){
                newShelfList.add(ma1105);
            }

            for(Ma1105 newSubShelf : newSubShelfList){
                if(newSubShelf.getPogCd().equals(ma1105.getPogCd())
                        && newSubShelf.getShelf().equals(ma1105.getShelf())
                        && newSubShelf.getSubShelf().equals(ma1105.getSubShelf())
                        && newSubShelf.getStoreCd().equals(ma1105.getStoreCd())){
                    checkFlg1 = false;
                }
            }
            if(checkFlg1){
                newSubShelfList.add(ma1105);
            }
        }

        ma1105Mapper.insertMa1108(newShelfList);
        ma1105Mapper.insertMa1109(newSubShelfList);
        return 1;
    }

    @Override
    public List<String> getShelf(String storeCd) {
        return ma1105Mapper.getShelf(storeCd);
    }

    @Override
    public List<String> getSubShelf(String storeCd, String shelf) {
        return ma1105Mapper.getSubShelf(storeCd,shelf);
    }

    @Override
    public Ma1105 getStoreInfo(String storeCd) {
        Cm9060 cm9060 = cm9060Mapper.selectByPrimaryKey("0000");
        return ma1105Mapper.getStoreInfo(storeCd,cm9060.getSpValue());
    }

    @Override
    public int countPogName(String excelName, String storeCd) {
        return ma1105Mapper.countPogName(excelName,storeCd);
    }
}
