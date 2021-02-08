package cn.com.bbut.iy.itemmaster.serviceimpl;

import cn.com.bbut.iy.itemmaster.dao.Cm9060Mapper;
import cn.com.bbut.iy.itemmaster.dao.Ma1200Mapper;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.ma1200.MA1200DTO;
import cn.com.bbut.iy.itemmaster.entity.base.Cm9060;
import cn.com.bbut.iy.itemmaster.service.CM9060Service;
import cn.com.bbut.iy.itemmaster.service.Ma1200Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

@Service
public class Ma1200ServiceImpl implements Ma1200Service {

    @Autowired
    private Ma1200Mapper ma1200Mapper;
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
     * get value by key
     * <p>
     * key
     *
     * @param parentId
     */
    @Override
    public MA1200DTO getByParentId(String parentId) {
        MA1200DTO param = new MA1200DTO();
        param.setParentArticleId(parentId);
        param.setBusinessDate(getBusinessDate());
        return ma1200Mapper.selectByParentId(param);
    }

    /**
     * 遍历出母货号
     *
     * @param items
     */
    @Override
    public List<String> checkList(List<String> items) {
        List<String> parents = ma1200Mapper.selectAllParentId();
        if(parents.size() == 0){
            return new ArrayList<>();
        }
        parents.retainAll(items);
        return parents;
    }

    /**
     * 获取母货号待选List
     *
     * @param v
     */
    @Override
    public List<AutoCompleteDTO> getList(String v) {
        return ma1200Mapper.selectList(v);
    }

    @Override
    public List<Map<String, String>> getChildDetail(List<String> parentArticleId) {
        Collection<String> groupParentIdList = parentArticleId;
        List<MA1200DTO> list = ma1200Mapper.getChildInfo(groupParentIdList);
        List<Map<String, String>> _result = new ArrayList<>();
        if(list.size() > 0){
            for(int i=0;i<list.size();i++){
                MA1200DTO ma1200DTO = list.get(i);
                Map<String, String> map = new HashMap<>();
                map.put("parentArticleId",ma1200DTO.getParentArticleId());
                map.put("childArticleId",ma1200DTO.getChildArticleId());
                //  stripTrailingZeros() 去除小数点  toPlainString() 转换字符串
                map.put("childQty",ma1200DTO.getChildQty().stripTrailingZeros().toPlainString());
                _result.add(map);
            }
        }
        return _result;
    }

    /**
     * 遍历BOM配方母货号
     * @param items
     * @return
     */
    @Override
    public List<String> checkBOMList(List<String> items) {
        List<String> parents = ma1200Mapper.selectBOMAllParentId();
        if(parents.size() == 0){
            return new ArrayList<>();
        }
        parents.retainAll(items);
        return parents;
    }

    @Override
    public List<Map<String, String>> getBOMChildDetail(List<String> parentArticleId) {

        Collection<String> bomParentIdList = parentArticleId;
        List<MA1200DTO> list = ma1200Mapper.getBOMChildInfo(bomParentIdList);

        List<Map<String, String>> _result = new ArrayList<>();
        DecimalFormat df2 =new DecimalFormat("#.0000");
        if(list.size() > 0){
            for(MA1200DTO ma1200DTO :list){
                Map<String, String> map = new HashMap<>();
                map.put("parentArticleId",ma1200DTO.getParentArticleId());
                map.put("childArticleId",ma1200DTO.getChildArticleId());
                map.put("childQty",df2.format(ma1200DTO.getChildQty()));
                _result.add(map);
            }
        }
        return _result;
    }
}
