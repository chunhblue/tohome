package cn.com.bbut.iy.itemmaster.serviceimpl.expenditure;

import cn.com.bbut.iy.itemmaster.dao.Cm9060Mapper;
import cn.com.bbut.iy.itemmaster.dao.ExpenditureMapper;
import cn.com.bbut.iy.itemmaster.dao.MA4320Mapper;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.expenditure.ExpenditureDTO;
import cn.com.bbut.iy.itemmaster.dto.expenditure.ExpenditureParamDTO;
import cn.com.bbut.iy.itemmaster.entity.MA4320;
import cn.com.bbut.iy.itemmaster.entity.MA4320Example;
import cn.com.bbut.iy.itemmaster.entity.base.Cm9060;
import cn.com.bbut.iy.itemmaster.service.expenditure.ExpenditureService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
public class ExpenditureServiceImpl implements ExpenditureService {

    @Autowired
    private ExpenditureMapper mapper;
    @Autowired
    private Cm9060Mapper cm9060Mapper;
    @Autowired
    private MA4320Mapper ma4320Mapper;

    /**
     * 获取当前业务日期
     */
    public String getBusinessDate() {
        Cm9060 dto =  cm9060Mapper.selectByPrimaryKey("0000");
        return dto.getSpValue();
    }

    /**
     * 条件查询记录
     *
     * @param dto
     */
    @Override
    public GridDataDTO<ExpenditureDTO> getList(ExpenditureParamDTO dto) {
        int count = mapper.selectCountByCondition(dto);
        if(count < 1){
            return new GridDataDTO<ExpenditureDTO>();
        }
        List<ExpenditureDTO> _list = mapper.selectListByCondition(dto);
        GridDataDTO<ExpenditureDTO> data = new GridDataDTO<ExpenditureDTO>(_list,
                dto.getPage(), count, dto.getRows());
        return data;
    }

    /**
     * 主键查询记录
     *
     * @param dto
     */
    @Override
    public ExpenditureDTO getByKey(ExpenditureParamDTO dto) {
        String businessDate = getBusinessDate();
        dto.setBusinessStartDate(businessDate);
        return mapper.selectByKey(dto);
    }

    /**
     * 判断编号是否存在
     *
     * @param voucherNo
     */
    @Override
    public int getByVoucherNo(String voucherNo) {
        return mapper.selectCountByVoucherNo(voucherNo);
    }

    /**
     * 新增记录
     *
     * @param dto
     */
    @Override
    @Transactional
    public int insert(ExpenditureDTO dto) {
        mapper.insertRecord(dto);
        //添加附件信息
        if(StringUtils.isNotBlank(dto.getFileDetailJson())){
            List<MA4320> ma4320List = new Gson().fromJson(dto.getFileDetailJson(), new TypeToken<List<MA4320>>(){}.getType());
            for (int j = 0; j < ma4320List.size(); j++) {
                MA4320 ma4320 = ma4320List.get(j);
                ma4320.setInformCd(dto.getVoucherNo());
                ma4320.setCreateUserId(dto.getCreateUser());
                ma4320.setCreateYmd(dto.getCreateYmd());
                ma4320.setCreateHms(dto.getCreateHms());
                ma4320Mapper.insertSelective(ma4320);
            }
        }
        return 1;
    }

    /**
     * 更新记录
     *
     * @param dto
     */
    @Override
    public int update(ExpenditureDTO dto) {
        mapper.updateRecord(dto);
        //删除附件信息
        MA4320Example example = new MA4320Example();
        example.or().andInformCdEqualTo(dto.getVoucherNo()).andFileTypeEqualTo("06");
        ma4320Mapper.deleteByExample(example);
        //添加附件信息
        if(StringUtils.isNotBlank(dto.getFileDetailJson())){
            List<MA4320> ma4320List = new Gson().fromJson(dto.getFileDetailJson(), new TypeToken<List<MA4320>>(){}.getType());
            for (int j = 0; j < ma4320List.size(); j++) {
                MA4320 ma4320 = ma4320List.get(j);
                ma4320.setInformCd(dto.getVoucherNo());
                ma4320.setCreateUserId(dto.getCreateUser());
                ma4320.setCreateYmd(dto.getCreateYmd());
                ma4320.setCreateHms(dto.getCreateHms());
                ma4320Mapper.insertSelective(ma4320);
            }
        }
        return 1;
    }

    /**
     * 删除记录
     *
     * @param list
     */
    @Override
    @Transactional
    public int delete(List<ExpenditureParamDTO> list) {
        try{
            for(ExpenditureParamDTO bean : list){
                mapper.deleteRecord(bean);
                //删除附件信息
                MA4320Example example = new MA4320Example();
                example.or().andInformCdEqualTo(bean.getVoucherNo()).andFileTypeEqualTo("06");
                ma4320Mapper.deleteByExample(example);
            }
        }catch (RuntimeException e){
            log.error("Delete Error----"+e.getMessage());
            return 0;
        }
        return 1;
    }

    @Override
    public List<AutoCompleteDTO> getExpenditureList(String v, String accDate, String storeCd) {
        return mapper.getExpenditureList(v,accDate,storeCd);
    }

    @Override
    public ExpenditureDTO getFundEntryData(String voucherNo, String storeCd, String accDate) {
        ExpenditureDTO list = mapper.getFundEntryData(voucherNo,storeCd,accDate);
        if(list.getExpenseAmt() == null){
            list.setExpenseAmt(BigDecimal.ZERO);
        }
        return list;
    }

    @Override
    public ExpenditureDTO getDescription(String voucherNo, String storeCd, String accDate) {
        return mapper.getDescription(voucherNo,storeCd,accDate);
    }

    @Override
    public List<AutoCompleteDTO> getOperator(String v,String storeCd) {
        return mapper.getOperator(v,storeCd);

    }
}
