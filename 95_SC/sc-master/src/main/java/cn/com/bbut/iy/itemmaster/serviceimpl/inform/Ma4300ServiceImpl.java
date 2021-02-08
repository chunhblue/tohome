package cn.com.bbut.iy.itemmaster.serviceimpl.inform;

import cn.com.bbut.iy.itemmaster.dao.*;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.base.role.RoleStoreDTO;
import cn.com.bbut.iy.itemmaster.dto.cm9070.Cm9070ReturnDTO;
import cn.com.bbut.iy.itemmaster.dto.inform.Ma4300DetailGridDto;
import cn.com.bbut.iy.itemmaster.dto.inform.Ma4300DetailParamDto;
import cn.com.bbut.iy.itemmaster.dto.inform.Ma4305DetailGridDto;
import cn.com.bbut.iy.itemmaster.dto.inform.Ma4310DetailGridDto;
import cn.com.bbut.iy.itemmaster.entity.*;
import cn.com.bbut.iy.itemmaster.entity.ma4300.MA4300;
import cn.com.bbut.iy.itemmaster.entity.ma4310.MA4310;
import cn.com.bbut.iy.itemmaster.entity.ma4310.MA4310Example;
import cn.com.bbut.iy.itemmaster.entity.ma4330.MA4330;
import cn.com.bbut.iy.itemmaster.service.CM9060Service;
import cn.com.bbut.iy.itemmaster.service.SequenceService;
import cn.com.bbut.iy.itemmaster.service.cm9070.Cm9070Service;
import cn.com.bbut.iy.itemmaster.service.inform.Ma4300Service;
import cn.com.bbut.iy.itemmaster.service.inform.Ma4311Service;
import cn.com.bbut.iy.itemmaster.service.inform_log.Ma4330Service;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class Ma4300ServiceImpl implements Ma4300Service {

    @Autowired
    private MA4300Mapper ma4300Mapper;
    @Autowired
    private MA4310Mapper ma4310Mapper;
    @Autowired
    private MA4311Mapper ma4311Mapper;
    @Autowired
    private MA4305Mapper ma4305Mapper;
    @Autowired
    private MA4320Mapper ma4320Mapper;
    @Autowired
    private Ma4311Service ma4311Service;
    @Autowired
    private Ma4330Service ma4330Service;
    @Autowired
    private Cm9070Service cm9070ServiceImpl;
    @Autowired
    private CM9060Service cm9060Service;
    @Autowired
    private SequenceService sequenceService;

    @Override
    public GridDataDTO<Ma4300DetailGridDto> getList(Ma4300DetailParamDto param) {
        List<Ma4300DetailGridDto> list = ma4300Mapper.getList(param);
        long count = ma4300Mapper.getListCount(param);
        return new GridDataDTO<>(list, param.getPage(), count,
                param.getRows());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String insertInform(HttpSession session, HttpServletRequest request, MA4300 param) {
        if(StringUtils.isNotBlank(param.getStoreDetailJson())){
            //获取序列号
            String informCd = sequenceService.getSequence("ma4300_inform_cd_seq");
            if(StringUtils.isBlank(informCd)){
                //获取序列失败
                log.error("获取序列失败 getSequence: {}", "ma4300_inform_cd_seq");
                RuntimeException e = new RuntimeException("获取序列失败[ ma4300_inform_cd_seq ]");
                throw e;
            }
            //设置通报编号
            param.setInformCd(informCd);
            //添加通报消息头档信息
            ma4300Mapper.insertSelective(param);
            //添加通报日志信息
            MA4330 ma4330 = MA4330.builder()
                    .informCd(param.getInformCd())
                    .createUserId(param.getCreateUserId())
                    .createYmd(param.getCreateYmd())
                    .createHms(param.getCreateHms())
                    .logType("01")
                    .build();
            ma4330Service.insertInformLog(ma4330);
            if(StringUtils.isNotBlank(param.getFileDetailJson())){
                List<MA4320> ma4320List = new Gson().fromJson(param.getFileDetailJson(), new TypeToken<List<MA4320>>(){}.getType());
                for (int i = 0; i < ma4320List.size(); i++) {
                    //添加通报消息附件信息
                    MA4320 ma4320 = ma4320List.get(i);
                    ma4320.setInformCd(param.getInformCd());
                    ma4320.setCreateUserId(param.getCreateUserId());
                    ma4320.setCreateYmd(param.getCreateYmd());
                    ma4320.setCreateHms(param.getCreateHms());
                    ma4320Mapper.insertSelective(ma4320);
                }
            }

            List<MA4311> ma4311List = new Gson().fromJson(param.getStoreDetailJson(), new TypeToken<List<MA4311>>(){}.getType());
            if(ma4311List!=null && ma4311List.size()>0){
                for (int i = 0; i < ma4311List.size(); i++) {
                    //添加通报消息门店范围显示信息
                    MA4311 ma4311 = ma4311List.get(i);
                    ma4311.setInformCd(param.getInformCd());
                    ma4311.setCreateUserId(param.getCreateUserId());
                    ma4311.setCreateYmd(param.getCreateYmd());
                    ma4311.setCreateHms(param.getCreateHms());
                    ma4311Mapper.insertSelective(ma4311);
                }
                Collection<String> stores = ma4311Service.getAllStore(ma4311List);
                if(stores!=null && !stores.isEmpty()){
                    for (String storeCd :stores) {
                        //添加通报消息门店范围信息
                        MA4310 ma4310 = new MA4310();
                        ma4310.setInformCd(param.getInformCd());
                        ma4310.setStoreCd(storeCd);
                        ma4310.setCreateUserId(param.getCreateUserId());
                        ma4310.setCreateYmd(param.getCreateYmd());
                        ma4310.setCreateHms(param.getCreateHms());
                        ma4310Mapper.insertSelective(ma4310);
                    }
                }
            }


            if(StringUtils.isNotBlank(param.getRoleDetailJson())){
                List<MA4305> ma4305List = new Gson().fromJson(param.getRoleDetailJson(), new TypeToken<List<MA4305>>(){}.getType());
                if(ma4305List!=null && ma4305List.size()>0){
                    for (int i = 0; i < ma4305List.size(); i++) {
                        //添加通报消息角色范围信息
                        MA4305 ma4305 = ma4305List.get(i);
                        ma4305.setInformCd(param.getInformCd());
                        ma4305.setCreateUserId(param.getCreateUserId());
                        ma4305.setCreateYmd(param.getCreateYmd());
                        ma4305.setCreateHms(param.getCreateHms());
                        ma4305Mapper.insertSelective(ma4305);
                    }
                }
            }
            return param.getInformCd();
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateInform(MA4300 param) {
        if(StringUtils.isNotBlank(param.getStoreDetailJson())){
            //修改通报消息头档信息
            ma4300Mapper.updateByPrimaryKeySelective(param);
            //删除通报消息附件信息
            MA4320Example ma4320Example = new MA4320Example();
            ma4320Example.or().andInformCdEqualTo(param.getInformCd());
            ma4320Mapper.deleteByExample(ma4320Example);
            if(StringUtils.isNotBlank(param.getFileDetailJson())){
                List<MA4320> ma4320List = new Gson().fromJson(param.getFileDetailJson(), new TypeToken<List<MA4320>>(){}.getType());
                for (int i = 0; i < ma4320List.size(); i++) {
                    //添加通报消息附件信息
                    MA4320 ma4320 = ma4320List.get(i);
                    ma4320.setInformCd(param.getInformCd());
                    ma4320.setUpdateUserId(param.getUpdateUserId());
                    ma4320.setUpdateYmd(param.getUpdateYmd());
                    ma4320.setUpdateHms(param.getUpdateHms());
                    ma4320Mapper.insertSelective(ma4320);
                }
            }

            List<MA4311> ma4311List = new Gson().fromJson(param.getStoreDetailJson(), new TypeToken<List<MA4311>>(){}.getType());
            if(ma4311List!=null && ma4311List.size()>0){
                //删除通报消息门店范围信息
                MA4311Example ma4311Example = new MA4311Example();
                ma4311Example.or().andInformCdEqualTo(param.getInformCd());
                ma4311Mapper.deleteByExample(ma4311Example);
                MA4310Example ma4310Example = new MA4310Example();
                ma4310Example.or().andInformCdEqualTo(param.getInformCd());
                ma4310Mapper.deleteByExample(ma4310Example);
                for (int i = 0; i < ma4311List.size(); i++) {
                    //添加通报消息门店范围显示信息
                    MA4311 ma4311 = ma4311List.get(i);
                    ma4311.setInformCd(param.getInformCd());
                    ma4311.setUpdateUserId(param.getCreateUserId());
                    ma4311.setUpdateYmd(param.getCreateYmd());
                    ma4311.setUpdateHms(param.getCreateHms());
                    ma4311Mapper.insertSelective(ma4311);
                }
                Collection<String> stores = ma4311Service.getAllStore(ma4311List);
                if(stores!=null && !stores.isEmpty()){
                    for (String storeCd :stores) {
                        //添加通报消息门店范围信息
                        MA4310 ma4310 = new MA4310();
                        ma4310.setInformCd(param.getInformCd());
                        ma4310.setStoreCd(storeCd);
                        ma4310.setUpdateUserId(param.getCreateUserId());
                        ma4310.setUpdateYmd(param.getCreateYmd());
                        ma4310.setUpdateHms(param.getCreateHms());
                        ma4310Mapper.insertSelective(ma4310);
                    }
                }
            }

            if(StringUtils.isNotBlank(param.getRoleDetailJson())){
                List<MA4305> ma4305List = new Gson().fromJson(param.getRoleDetailJson(), new TypeToken<List<MA4305>>(){}.getType());
                if(ma4305List!=null){
                    //删除通报消息角色范围信息
                    MA4305Example ma4305Example = new MA4305Example();
                    ma4305Example.or().andInformCdEqualTo(param.getInformCd());
                    ma4305Mapper.deleteByExample(ma4305Example);
                    for (int i = 0; i < ma4305List.size(); i++) {
                        //添加通报消息角色范围信息
                        MA4305 ma4305 = ma4305List.get(i);
                        ma4305.setInformCd(param.getInformCd());
                        ma4305.setUpdateUserId(param.getCreateUserId());
                        ma4305.setUpdateYmd(param.getCreateYmd());
                        ma4305.setUpdateHms(param.getCreateHms());
                        ma4305Mapper.insertSelective(ma4305);
                    }
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public MA4300 getMa4300(String informCd) {
        return ma4300Mapper.selectByPrimaryKey(informCd);
    }

    @Override
    public List<Ma4305DetailGridDto> getMa4305List(String informCd) {
        return ma4305Mapper.getList(informCd);
    }

    @Override
    public List<Ma4310DetailGridDto> getMa4310List(String informCd) {
        String businessDate =  cm9060Service.getValByKey("0000");
        return ma4310Mapper.getList(informCd,businessDate);
    }

    @Override
    public List<AutoCompleteDTO> getRoleListAll(String v) {
        return ma4300Mapper.getRoleListAll(v);
    }

}
