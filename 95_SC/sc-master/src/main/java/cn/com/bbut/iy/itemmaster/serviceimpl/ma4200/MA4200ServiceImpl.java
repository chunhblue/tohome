package cn.com.bbut.iy.itemmaster.serviceimpl.ma4200;

import cn.com.bbut.iy.itemmaster.dao.MA4200Mapper;
import cn.com.bbut.iy.itemmaster.dto.AjaxResultDto;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.ma4200.MA4200GridDTO;
import cn.com.bbut.iy.itemmaster.dto.ma4200.MA4200ParamDTO;
import cn.com.bbut.iy.itemmaster.entity.MA4200;
import cn.com.bbut.iy.itemmaster.service.ma4200.Ma4200Service;
import cn.com.bbut.iy.itemmaster.serviceimpl.CM9060ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class MA4200ServiceImpl implements Ma4200Service {
    @Autowired
    private MA4200Mapper ma4200Mapper;
    @Autowired
    private CM9060ServiceImpl cm9060Service;

    public final static String DEFAULT_PASSWORD = "123456";

    @Override
    public GridDataDTO<MA4200GridDTO> search(MA4200ParamDTO ma4200ParamDTO) {
        // 获取业务日期
        ma4200ParamDTO.setBusinessDate(cm9060Service.getValByKey("0000"));
        List<MA4200GridDTO> list = ma4200Mapper.selectList(ma4200ParamDTO);
        long count = ma4200Mapper.selectListCount(ma4200ParamDTO);
        return new GridDataDTO<>(list, ma4200ParamDTO.getPage(), count,
                ma4200ParamDTO.getRows());
    }

    @Override
    public boolean getUserIdCount(String userId) {
        MA4200 ma4200 = ma4200Mapper.selectByPrimaryKey(userId);
        if(ma4200==null)
        return true;
        else return false;
    }

    @Override
    public boolean confirmPassword(String userId,String password) {
        String inputPassword = DigestUtils.md5DigestAsHex(password.getBytes());
        MA4200 ma4200 = ma4200Mapper.selectByPrimaryKey(userId);
        String dbPassword = ma4200.getEmpPassword();
        if(inputPassword.equalsIgnoreCase(dbPassword)){
            return true;
        }
        return false;
    }

    @Override
    public AjaxResultDto updatePassword(MA4200 param) {
        AjaxResultDto rest = new AjaxResultDto();
        int flg = 0;
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HHmmss");
        // 当前时间年月日
        String ymd = dateFormat.format(now);
        String hms = timeFormat.format(now);
        param.setUpdateYmd(ymd);
        param.setUpdateHms(hms);
        param.setEmpPassword(DigestUtils.md5DigestAsHex(param.getEmpOldPassword().getBytes()));
        param.setEmpNewPassword(DigestUtils.md5DigestAsHex(param.getEmpNewPassword().getBytes()));
        flg = ma4200Mapper.updatePassword(param);
        if(flg>0){
            rest.setSuccess(true);
            rest.setMessage("Modify Successfully!");
        }else{
            rest.setSuccess(false);
            rest.setMessage("Modify Failure!");
        }
        return rest;
    }

    @Override
    public AjaxResultDto insertOrUpdate(MA4200 param, String operateFlg) {
        AjaxResultDto rest = new AjaxResultDto();
        int flg = 0;
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HHmmss");
        // 当前时间年月日
        String ymd = dateFormat.format(now);
        String hms = timeFormat.format(now);
        if("add".equalsIgnoreCase(operateFlg)){
            param.setCreateYmd(ymd);
            param.setCreateHms(hms);
            param.setUpdateUserId(null);
            param.setEmpId(0);
            //密码为空使用默认密码
            if(StringUtils.isBlank(param.getEmpPassword())){
                param.setEmpPassword(DEFAULT_PASSWORD);
            }
            //添加默认使用序列自增
//            param.setEmpId(null);
            param.setEmpId(0);
            //MD5加密
            String md5Password = DigestUtils.md5DigestAsHex(param.getEmpPassword().getBytes());
            param.setEffectiveStatus("10");
            param.setEmpPassword(md5Password);
            flg = ma4200Mapper.insertSelective(param);
        }else if ("edit".equalsIgnoreCase(operateFlg)){
            param.setUpdateYmd(ymd);
            param.setUpdateHms(hms);
            flg = ma4200Mapper.updateUserInfo(param);
        }
        if(flg>0){
            rest.setSuccess(true);
            rest.setMessage("Data Saved Successfully!");
        }else{
            rest.setSuccess(false);
            rest.setMessage("Data Saved Failure!");
        }
        return rest;
    }

    @Override
    public AjaxResultDto delete(String empNumId) {
        AjaxResultDto rest = new AjaxResultDto();
        int flg = 0;
        flg = ma4200Mapper.deleteByPrimaryKey(empNumId);
        if(flg>0){
            rest.setSuccess(true);
            rest.setMessage("Delete Successfully!");
        }else{
            rest.setSuccess(false);
            rest.setMessage("Delete Failure!");
        }
        return rest;
    }
}
