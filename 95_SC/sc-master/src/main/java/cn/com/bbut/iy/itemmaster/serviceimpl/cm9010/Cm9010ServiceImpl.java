package cn.com.bbut.iy.itemmaster.serviceimpl.cm9010;

import cn.com.bbut.iy.itemmaster.dao.Cm9000Mapper;
import cn.com.bbut.iy.itemmaster.dao.Cm9010Mapper;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.entity.AuditTypeExample;
import cn.com.bbut.iy.itemmaster.entity.cm9000.Cm9000;
import cn.com.bbut.iy.itemmaster.entity.cm9010.Cm9010;
import cn.com.bbut.iy.itemmaster.entity.cm9010.Cm9010Example;
import cn.com.bbut.iy.itemmaster.service.cm9010.Cm9010Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Cm9010ServiceImpl implements Cm9010Service {

    @Autowired
    private Cm9000Mapper cm9000Mapper;

    @Autowired
    private Cm9010Mapper cm9010Mapper;

    /**
     * cm9010共通查询
     *
     * @param codeValue
     */
    @Override
    public List<Cm9010> getList(String codeValue) {
        // 判断是否有效
        Cm9000 bean = cm9000Mapper.selectByPrimaryKey(codeValue);
        if(bean == null || !"10".equals(bean.getEffectiveSts())){
            return null;
        }

        Cm9010Example example = new Cm9010Example();
        example.setOrderByClause("code_value");
        example.or().andCodeTypeEqualTo(codeValue).andEffectiveStsEqualTo("10");

        return cm9010Mapper.selectByExample(example);
    }

    @Override
    public List<AutoCompleteDTO> getReason(String v) {
        List<AutoCompleteDTO> reasonValue = cm9010Mapper.getReasonValue(v);
        return reasonValue;
    }


    @Override
    public List<AutoCompleteDTO> getWriteOffReasonValue(String v) {
        List<AutoCompleteDTO> reasonValue = cm9010Mapper.getWriteOffReasonValue(v);
        return reasonValue;
    }

    @Override
    public List<AutoCompleteDTO> getDiffReason(String v) {
        return cm9010Mapper.getDifferenceReason(v);
    }

    @Override
    public List<AutoCompleteDTO> getReturnDifferReason(String v) {
        return cm9010Mapper.getReturnDifferReason(v);
    }

    @Override
    public List<AutoCompleteDTO> getReceiptDifferReason(String v) {
        return cm9010Mapper.getReceiptDifferReason(v);
    }

    /**
     * cm9010共通查询
     *
     * @param CodeType
     */
    @Override
    public List<Cm9010> selectByCodeType(String CodeType) {
        Cm9010Example cm9010Example = new Cm9010Example();
        cm9010Example.setOrderByClause("code_value");
        Cm9010Example.Criteria criteria = cm9010Example.createCriteria();
        //添加查询条件
        criteria.andCodeTypeEqualTo(CodeType).andEffectiveStsEqualTo("10");
        return cm9010Mapper.selectByExample(cm9010Example);
    }
}
