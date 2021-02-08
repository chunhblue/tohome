package cn.com.bbut.iy.itemmaster.serviceimpl.sa0000;

import cn.com.bbut.iy.itemmaster.dao.*;
import cn.com.bbut.iy.itemmaster.dto.AjaxResultDto;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.paymentMode.SA0000DetailGridDto;
import cn.com.bbut.iy.itemmaster.dto.paymentMode.SA0000DetailParamDto;
import cn.com.bbut.iy.itemmaster.dto.paymentMode.SA0005DetailGridDto;
import cn.com.bbut.iy.itemmaster.dto.sa0005.SA0005ParamDTO;
import cn.com.bbut.iy.itemmaster.entity.SA0000;
import cn.com.bbut.iy.itemmaster.entity.SA0000Example;
import cn.com.bbut.iy.itemmaster.entity.SA0005;
import cn.com.bbut.iy.itemmaster.entity.SA0005Example;
import cn.com.bbut.iy.itemmaster.entity.sa0160.SA0160;
import cn.com.bbut.iy.itemmaster.entity.sa0160.SA0160Example;
import cn.com.bbut.iy.itemmaster.exception.SystemRuntimeException;
import cn.com.bbut.iy.itemmaster.service.CM9060Service;
import cn.com.bbut.iy.itemmaster.service.sa0000.SA0000Service;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Service
public class SA0000ServiceImpl implements SA0000Service {

    @Autowired
    private SA0000Mapper sa0000Mapper;
    @Autowired
    private SA0005Mapper sa0005Mapper;
    @Autowired
    private SA0160Mapper sa0160Mapper;
    @Autowired
    private CM9060Service cm9060Service;

    /**
     * SA0000查询
     */
    @Override
    public List<SA0000> getList() {
        SA0000Example example = new SA0000Example();
        example.setOrderByClause("pay_cd");
        SA0000Example.Criteria criteria = example.createCriteria();
        criteria.andEffectiveStsEqualTo("10");
        example.or(criteria);
        return sa0000Mapper.selectByExample(example);
    }

    @Override
    public GridDataDTO<SA0000DetailGridDto> getList(SA0000DetailParamDto param) {
        List<SA0000DetailGridDto> list = sa0000Mapper.getList(param);
        long count = sa0000Mapper.getListCount(param);
        return new GridDataDTO<>(list, param.getPage(), count,
                param.getRows());
    }

    @Override
    public List<SA0160> getPosId(String storeCd) {
        SA0160Example ex = new SA0160Example();
        ex.or().andStoreCdEqualTo(storeCd);
        ex.setOrderByClause("pos_id desc");
        return sa0160Mapper.selectByExample(ex);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResultDto insertPaymentMode(SA0000 param, String paymentModeDetailJson) {
        AjaxResultDto resultDto = new AjaxResultDto();
        SA0000 sa0000 = sa0000Mapper.selectByPrimaryKey(param.getPayCd());
        if(sa0000!=null){
            resultDto.setSuccess(false);
            resultDto.setMessage("Payment No. already exists!");
            return  resultDto;
        }
        if(StringUtils.isNotBlank(param.getPayParameter())){
            String ca = param.getPayParameter().substring(0,1);
            if("1".equals(ca)&&this.getChargeByPayment(param.getPayCd())){
                resultDto.setSuccess(false);
                resultDto.setMessage("\"Change Available\" option is aleardy set in existing payment methods! ");
                return  resultDto;
            }
        }
        param.setEffectiveSts("10");
        //添加pos支付方式头档信息
        sa0000Mapper.insertSelective(param);
        //添加pos支付方式明细信息
        List<SA0005> sa0005List = new Gson().fromJson(paymentModeDetailJson, new TypeToken<List<SA0005>>(){}.getType());
        if(sa0005List!=null){
            for (int i = 0; i < sa0005List.size(); i++) {
                SA0005 sa0005 = sa0005List.get(i);
                sa0005.setPayCd(param.getPayCd());
                sa0005.setCreateUserId(param.getCreateUserId());
                sa0005.setCreateYmd(param.getCreateYmd());
                sa0005.setCreateHms(param.getCreateHms());
                sa0005Mapper.insertSelective(sa0005);
            }
        }
        resultDto.setSuccess(true);
        resultDto.setMessage("Saved Successfully!");
        return  resultDto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResultDto updatePaymentMode(SA0000 param, String paymentModeDetailJson) {
        AjaxResultDto resultDto = new AjaxResultDto();
        if(StringUtils.isNotBlank(param.getPayParameter())){
            String ca = param.getPayParameter().substring(0,1);
            if("1".equals(ca)&&this.getChargeByPayment(param.getPayCd())){
                resultDto.setSuccess(false);
                resultDto.setMessage("\"Change Available\" option is aleardy set in existing payment methods! ");
                return  resultDto;
            }
        }
        //修改pos支付方式头档信息
        sa0000Mapper.updateByPrimaryKeySelective(param);
        //添加pos支付方式明细信息
        List<SA0005> sa0005List = new Gson().fromJson(paymentModeDetailJson, new TypeToken<List<SA0005>>(){}.getType());
        if(sa0005List!=null){
            //删除pos支付方式明细信息
            SA0005Example example = new SA0005Example();
            example.or().andPayCdEqualTo(param.getPayCd());
            sa0005Mapper.deleteByExample(example);
            for (int i = 0; i < sa0005List.size(); i++) {
                SA0005 sa0005 = sa0005List.get(i);
                if (!StringUtils.isNotBlank(sa0005.getRecordStatus())) {
                    sa0005.setPayCd(param.getPayCd());
                    sa0005.setCreateUserId(param.getCreateUserId());
                    sa0005.setCreateYmd(param.getCreateYmd());
                    sa0005.setCreateHms(param.getCreateHms());
                    sa0005Mapper.insertSelective(sa0005);
                }
            }
        }else{
            throw new SystemRuntimeException("支付明细为空!");
        }
        resultDto.setSuccess(true);
        resultDto.setMessage("Saved Successfully!");
        return  resultDto;
    }
    @Override
    public int insertSa0005(SA0005ParamDTO sa0005) {
       return sa0005Mapper.insertTosa0005Del(sa0005);

    }

    @Override
    public int insertoSa0005(String paymentModeDetailJson) {
        List<SA0005ParamDTO> sa0005List = new Gson().fromJson(paymentModeDetailJson, new TypeToken<List<SA0005ParamDTO>>(){}.getType());
         Integer count=0;
        for (int i = 0; i < sa0005List.size(); i++) {
            if (StringUtils.isNotBlank(sa0005List.get(i).getRecordStatus())) {
                this.sa0005Mapper.insertTosa0005Del(sa0005List.get(i));
                count++;
            }
        }
        return  count;
    }

    @Override
    public int deleteSa005(SA0005ParamDTO sa0005) {
        return sa0005Mapper.deleteSa005(sa0005);
    }


    @Override
    public List<SA0005DetailGridDto> getDeldetailList(String payCd) {
        String businessDate =  cm9060Service.getValByKey("0000");
        List<SA0005DetailGridDto> delDetailList = sa0000Mapper.getDelDetailList(payCd, businessDate);
        return delDetailList;
    }


    @Override
    public List<SA0005ParamDTO> getdeldetailList(String payCd) {
        String businessDate =  cm9060Service.getValByKey("0000");
        return sa0000Mapper.getdetailList(payCd,businessDate);
    }

    @Override
    public int inserttoSa0005(SA0005ParamDTO sa0005) {
        return sa0005Mapper.inserttoSa0005(sa0005);
    }


    @Override
    public List<SA0005DetailGridDto> getDetailList(String payCd) {
        String businessDate =  cm9060Service.getValByKey("0000");
        return sa0000Mapper.getDetailList(payCd,businessDate);
    }

    @Override
    public SA0000 getPaymentInfo(String payCd) {
        return sa0000Mapper.selectByPrimaryKey(payCd);
    }

    @Override
    public List<AutoCompleteDTO> getStoreList(String v) {
        return sa0160Mapper.selectStoreList(v);
    }


    /**
     * 判断其他支付方式是否有找零
     * @param payCd
     * @return true 已存在找零
     */
    private boolean getChargeByPayment(String payCd){
        if(sa0000Mapper.getChargeByPayment(payCd)>0){
            return true;
        }
        return false;
    }
}
