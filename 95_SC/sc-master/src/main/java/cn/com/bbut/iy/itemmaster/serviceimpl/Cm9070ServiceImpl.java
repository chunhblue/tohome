package cn.com.bbut.iy.itemmaster.serviceimpl;

import cn.com.bbut.iy.itemmaster.dao.CM9070Mapper;
import cn.com.bbut.iy.itemmaster.dto.cm9070.Cm9070DTO;
import cn.com.bbut.iy.itemmaster.dto.cm9070.Cm9070ReturnDTO;
import cn.com.bbut.iy.itemmaster.service.cm9070.Cm9070Interface;
import cn.com.bbut.iy.itemmaster.service.cm9070.Cm9070Service;
import cn.com.bbut.iy.itemmaster.service.ExclusiveService;
import cn.com.bbut.iy.itemmaster.util.DateConvert;
import cn.com.bbut.iy.itemmaster.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Date;

@Service
@Slf4j
public class Cm9070ServiceImpl implements Cm9070Service {
    /**
     * 采番DAO
     */
    @Autowired
    public CM9070Mapper cm9070Mapper;

    /**
     * 排他共通
     */
    @Autowired
    public ExclusiveService exclusiveServiceImpl;

    @Override
    public Cm9070ReturnDTO Get(HttpSession session,HttpServletRequest request,
                               String businessCd,String asType,Date adtNrDate, String asCustomerFix) throws UnknownHostException {
        Cm9070ReturnDTO _returnDto = new  Cm9070ReturnDTO();
        //前缀
        String _lsPrefix=Cm9070Interface.EMPTY;
        //后缀
        String _lsPostfix=Cm9070Interface.EMPTY;
        //日期格式
        String _sngDateFormat=Cm9070Interface.EMPTY;
        //当前日期(旧)
        String _sngCurrentDateOld=Cm9070Interface.EMPTY;
        //当前日期
        String _ldtCurrentDate=Cm9070Interface.EMPTY;
        //当前最大流水号
        long _sngCurrentMaxNumber=0;
        //采番流水号
        String _aSreturnNumber=Cm9070Interface.EMPTY;

        //单号类型检查
        if(StringUtil.IsBlank(asType)){
            _returnDto.setCode(11);
            _returnDto.setMsg("Bill type cannot be empty!");  //单号类型为空
            return _returnDto;
        }
        //日结日期检查
        if(adtNrDate==null){
            _returnDto.setCode(12);
            _returnDto.setMsg("Daily payment cannot be empty!");  //日结日期为空
            return _returnDto;
        }
        //取得并检查序列号表数据
        Cm9070DTO cm9070DTO = cm9070Mapper.Select(asType);
        if(cm9070DTO==null){
            _returnDto.setCode(21);
            _returnDto.setMsg("Serial No. does not exist!");  //序列号表数据不存在
            return _returnDto;
        }
        cm9070DTO.setRowId(cm9070DTO.getSngWhichCode());
        //数据排他
        boolean _flg =exclusiveServiceImpl.LockData(session, request, Cm9070Interface.TATBL_CM9070, Cm9070Interface.TATBL_CM9070_SNG_WHICH_CODE,Cm9070Interface.TATBL_CM9070,cm9070DTO.getRowId());
        //数据锁住
        if(!_flg){
            _returnDto.setCode(10);
            _returnDto.setMsg("Data is locked!");
            return _returnDto;
        }
        //当前最大流水号未定义
        if("null".equals(String.valueOf(cm9070DTO.getSngCurrentMaxNumber()))){
            _returnDto.setCode(23);
            _returnDto.setMsg("Existing maximum serial No. cannot be empty!");  //当前最大流水号未定义
            unLockData(session, request, Cm9070Interface.TATBL_CM9070,cm9070DTO.getRowId());
            return _returnDto;
        }
        //起始流水号未定义
        if("null".equals(String.valueOf(cm9070DTO.getSngMinNumber()))){
            _returnDto.setCode(24);
            _returnDto.setMsg("Initial serial No. cannot be empty!");  //其实流水号未定义
            unLockData(session, request, Cm9070Interface.TATBL_CM9070,cm9070DTO.getRowId());
            return _returnDto;
        }
        //最大可用流水号未定义
        if("null".equals(String.valueOf(cm9070DTO.getSngMaxNumber()))){
            _returnDto.setCode(25);
            _returnDto.setMsg("Maximum serial No. cannot be empty!"); //最大可用流水号未定义.
            unLockData(session, request, Cm9070Interface.TATBL_CM9070,cm9070DTO.getRowId());
            return _returnDto;
        }
        //流水号长度未定义
        if(Integer.valueOf(cm9070DTO.getSngNumberWidth())==null){
            _returnDto.setCode(26);
            _returnDto.setMsg("Length of serial No. cannot be empty!");  //流水号长度未定义
            unLockData(session, request, Cm9070Interface.TATBL_CM9070,cm9070DTO.getRowId());
            return _returnDto;
        }
        //当前日期未定义
        if(StringUtil.IsBlank(cm9070DTO.getSngCurrentDate())){
            _returnDto.setCode(27);
            _returnDto.setMsg("Current business date cannot be empty! "); //当前日期未定义
            unLockData(session, request, Cm9070Interface.TATBL_CM9070,cm9070DTO.getRowId());
            return _returnDto;
        }
        //流水号长度和最大可用流水号长度不符
        if(!cm9070DTO.getSngNumberWidth().equals((short)String.valueOf(cm9070DTO.getSngMaxNumber()).length())){
            _returnDto.setCode(28);
            _returnDto.setMsg("Serial No. length exceeded maximum length of serial No.");  //流水号长度和最大可用流水号长度不符.
            unLockData(session, request, Cm9070Interface.TATBL_CM9070,cm9070DTO.getRowId());
            return _returnDto;
        }
        //用户定制字符串未指定
        if(!StringUtil.IsBlank(cm9070DTO.getSngCustomerFixFlag())){
            if(Cm9070Interface.CUSTOMER_FIX_FLAG_1.equals(cm9070DTO.getSngCustomerFixFlag())&&StringUtil.IsBlank(asCustomerFix)){
                _returnDto.setCode(32);
                _returnDto.setMsg("User-defined string cannot be empty!");  //用户定制字符串未指定
                unLockData(session, request, Cm9070Interface.TATBL_CM9070,cm9070DTO.getRowId());
                return _returnDto;
            }
        }
        //用户定制字符串位置不正确
        if(!Cm9070Interface.CUSTOMER_FIX_SITE_0.equals(cm9070DTO.getSngCustomerFixSite())&&!Cm9070Interface.CUSTOMER_FIX_SITE_1.equals(cm9070DTO.getSngCustomerFixSite())
                &&!Cm9070Interface.CUSTOMER_FIX_SITE_2.equals(cm9070DTO.getSngCustomerFixSite())&&!Cm9070Interface.CUSTOMER_FIX_SITE_3.equals(cm9070DTO.getSngCustomerFixSite())&&
                !Cm9070Interface.CUSTOMER_FIX_SITE_4.equals(cm9070DTO.getSngCustomerFixSite())){
            _returnDto.setCode(29);
            _returnDto.setMsg("User-defined string is filled in wrong field!");  //用户定制字符串位置不正确
            unLockData(session, request, Cm9070Interface.TATBL_CM9070,cm9070DTO.getRowId());
            return _returnDto;
        }
        //当前日期(旧)
        _sngCurrentDateOld = cm9070DTO.getSngCurrentDate();
        //2-4. 判断日结日期是否为采番表中的当前日期,如果不相等以日结日期为准
        if(!DateConvert.ToString(adtNrDate,Cm9070Interface.DATE_YYYYMMDD).equals(cm9070DTO.getSngCurrentDate())){
            _ldtCurrentDate = DateConvert.ToString(adtNrDate,Cm9070Interface.DATE_YYYYMMDD);
        } else {
            _ldtCurrentDate = cm9070DTO.getSngCurrentDate();
        }
        Date _CurrentDate=DateConvert.FromString(_ldtCurrentDate,Cm9070Interface.DATE_YYYYMMDD);

        //2-5. 日期格式转换
        long _liDay = getDay(_CurrentDate);
        String _lsDay = Cm9070Interface.STRING_000+_liDay;
        _lsDay=_lsDay.substring(_lsDay.length()-3,_lsDay.length());

        String _lsDate =Cm9070Interface.EMPTY;
        _sngDateFormat = cm9070DTO.getSngDateFormat();
        if(!StringUtil.IsBlank(_sngDateFormat)){
            if(Cm9070Interface.DATE_FORMAT_YYYYMMDD.equals(_sngDateFormat)){
                _lsDate = DateConvert.ToString(_CurrentDate,Cm9070Interface.DATE_YYYYMMDD);
            }else if(Cm9070Interface.DATE_FORMAT_YYMMDD.equals(_sngDateFormat)){
                _lsDate = DateConvert.ToString(_CurrentDate,Cm9070Interface.DATE_YYMMDD);
            }else if(Cm9070Interface.DATE_FORMAT_YYYYDDD.equals(_sngDateFormat)){
                _lsDate = DateConvert.ToString(_CurrentDate,Cm9070Interface.DATE_YYYYMMDD).substring(0,4);
            }else if(Cm9070Interface.DATE_FORMAT_YYDDD.equals(_sngDateFormat)){
                _lsDate = DateConvert.ToString(_CurrentDate,Cm9070Interface.DATE_YYMMDD).substring(0,2);
            }else if(Cm9070Interface.DATE_FORMAT_MMDD.equals(_sngDateFormat)){
                _lsDate = DateConvert.ToString(_CurrentDate,Cm9070Interface.DATE_YYMMDD).substring(2,4);
            }else if(Cm9070Interface.DATE_FORMAT_DDD.equals(_sngDateFormat)){
                _lsDate = _lsDay;
            }
        }

        //2-6. 当前最大流水号的设置
        _sngCurrentMaxNumber=cm9070DTO.getSngCurrentMaxNumber();
        if(!DateConvert.ToString(_CurrentDate,Cm9070Interface.DATE_YYYYMMDD).equals(_sngCurrentDateOld)){
            String yy = DateConvert.ToString(_CurrentDate,Cm9070Interface.DATE_YYYYMMDD).substring(2, 4);
            String mm = DateConvert.ToString(_CurrentDate,Cm9070Interface.DATE_YYYYMMDD).substring(4, 6);
            String yyOld = _sngCurrentDateOld.substring(2, 4);
            String mmOld = _sngCurrentDateOld.substring(4, 6);
            if(Cm9070Interface.NUMBER_GENERATE_TYPE_Y.equals(cm9070DTO.getSngNumberGenerateType())&&!yy.equals(yyOld)){
                _sngCurrentMaxNumber=cm9070DTO.getSngMinNumber();
            }else if(Cm9070Interface.NUMBER_GENERATE_TYPE_M.equals(cm9070DTO.getSngNumberGenerateType())&&!mm.equals(mmOld)){
                _sngCurrentMaxNumber=cm9070DTO.getSngMinNumber();
            } else if(Cm9070Interface.NUMBER_GENERATE_TYPE_D.equals(cm9070DTO.getSngNumberGenerateType())){
                _sngCurrentMaxNumber=cm9070DTO.getSngMinNumber();
            }
        }
        _sngCurrentMaxNumber=_sngCurrentMaxNumber+1;
        //当前流水号已到上限
        if(_sngCurrentMaxNumber>cm9070DTO.getSngMaxNumber()){
            _returnDto.setCode(30);
            _returnDto.setMsg("Current serial No. reaches the maximum serial No.!");  //当前流水号已到上限
            unLockData(session, request, Cm9070Interface.TATBL_CM9070,cm9070DTO.getRowId());
            return _returnDto;
        }
        //2-7. 把当前流水号转换为字符串，并补足长度
        long length = cm9070DTO.getSngNumberWidth()-String.valueOf(_sngCurrentMaxNumber).length();
        //转换后流水号
        String lsNumber=Cm9070Interface.EMPTY;
        String str = Cm9070Interface.EMPTY;
        if(length>0){
            if(!StringUtil.IsBlank(cm9070DTO.getSngNumberLeftstuff())){
                for(int i=0;i<length;i++){
                    str+=cm9070DTO.getSngNumberLeftstuff();
                }
                lsNumber= str+_sngCurrentMaxNumber;
            }else {
                for(int i=0;i<length;i++){
                    str+=Cm9070Interface.STRING_0;
                }
                lsNumber= str+_sngCurrentMaxNumber;
            }
        }else {
            lsNumber  = String.valueOf(_sngCurrentMaxNumber);
        }


        //单号生成
        //3-1. 判断是否添加用户定制字符串
        if(Cm9070Interface.CUSTOMER_FIX_FLAG_1.equals(cm9070DTO.getSngCustomerFixFlag())){
            asCustomerFix = asCustomerFix.trim();
        }else {
            asCustomerFix=Cm9070Interface.EMPTY;
        }
        //3-2.判断用户定制字符串位置并拼接单号
        if(!StringUtil.IsBlank(cm9070DTO.getSngPrefix())){
            _lsPrefix=cm9070DTO.getSngPrefix().trim();
        }
        if(!StringUtil.IsBlank(cm9070DTO.getSngPostfix())){
            _lsPostfix =cm9070DTO.getSngPostfix().trim();
        }

        _aSreturnNumber=Cm9070Interface.EMPTY;
        if(Cm9070Interface.CUSTOMER_FIX_SITE_0.equals(cm9070DTO.getSngCustomerFixSite())){
            _aSreturnNumber = asCustomerFix+_lsPrefix+_lsDate+lsNumber+_lsPostfix;
        }else if(Cm9070Interface.CUSTOMER_FIX_SITE_1.equals(cm9070DTO.getSngCustomerFixSite())){
            _aSreturnNumber = _lsPrefix+asCustomerFix+_lsDate+lsNumber+_lsPostfix;
        }else if(Cm9070Interface.CUSTOMER_FIX_SITE_2.equals(cm9070DTO.getSngCustomerFixSite())){
            _aSreturnNumber = _lsPrefix+_lsDate+asCustomerFix+lsNumber+_lsPostfix;
        }else if(Cm9070Interface.CUSTOMER_FIX_SITE_3.equals(cm9070DTO.getSngCustomerFixSite())){
            _aSreturnNumber = _lsPrefix+_lsDate+lsNumber+asCustomerFix+_lsPostfix;
        }else if(Cm9070Interface.CUSTOMER_FIX_SITE_4.equals(cm9070DTO.getSngCustomerFixSite())){
            _aSreturnNumber = _lsPrefix+_lsDate+lsNumber+_lsPostfix+asCustomerFix;
        }

        //3-3.判断是否添加校验码
        if(Cm9070Interface.PARITY_BIT_FLAG_1.equals(cm9070DTO.getSngParityBitFlag())){
            String bit =createValidateNumber(_aSreturnNumber);
            if(StringUtil.IsBlank(bit)){
                _returnDto.setCode(31);
                _returnDto.setMsg("Failed in generating check code!");  //校验码生成失败
                unLockData(session, request, Cm9070Interface.TATBL_CM9070,cm9070DTO.getRowId());
                return _returnDto;
            }
            _aSreturnNumber=_aSreturnNumber+bit;
        }

        //当前最大流水号
        cm9070DTO.setSngCurrentMaxNumber(_sngCurrentMaxNumber);
        //当前日期
        cm9070DTO.setSngCurrentDate(_ldtCurrentDate);
        //更新序列号表单
        int flag = cm9070Mapper.Update(cm9070DTO);
        log.debug("更新序列号是"+_sngCurrentMaxNumber);
        log.debug("采番的流水号是"+_aSreturnNumber);
        if(flag>0){
            //数据解锁
            _flg =unLockData(session, request, Cm9070Interface.TATBL_CM9070,cm9070DTO.getRowId());
            if(_flg){
                _returnDto.setCode(0);
                _returnDto.setaSreturnNumber(_aSreturnNumber);
                return _returnDto;
            }
        }else {
            _returnDto.setCode(41);
            _returnDto.setMsg("Failed in updating serial No.");  //更新序列号失败
            unLockData(session, request, Cm9070Interface.TATBL_CM9070,cm9070DTO.getRowId());
            return _returnDto;
        }
        return _returnDto;
    }


    /**
     * @Description:生成验证码
     * @param aSreturnNumber
     * @return String 验证码
     */
    public String createValidateNumber(String aSreturnNumber){
        if(StringUtil.IsBlank(aSreturnNumber)){
            return Cm9070Interface.EMPTY;
        }
        //生成验证位
        int _bitLen = aSreturnNumber.length();
        //循环计数用变量
        int _bitI=1;
        //当前位的值累加
        int _bitSum=0;
        //当前位的值
        int _bitB=0;
        while(_bitI<=_bitLen){
            _bitB = Integer.parseInt(aSreturnNumber.substring(_bitLen-_bitI,_bitLen+1-_bitI));
            //如果是奇数次循环的话取当前位的值乘以3
            if(_bitI%2==1){
                _bitB = _bitB*3;
            }
            //把循环取得所有位的值累加
            _bitSum+=_bitB;
            _bitI+=1;
        }
        //取累加结果的个位数
        _bitSum = _bitSum%10;
        //10减去前面所得个位数
        _bitSum = 10-_bitSum;
        //取前一步骤所得的个位数
        _bitSum = _bitSum%10;
        log.debug("生成验证码是"+_bitSum);
        return String.valueOf(_bitSum);
    }

    /**
     * 将当前时间所在当年的天数
     * @param date
     * @return int 天数
     */
    private int getDay(Date date) {
        Calendar _cal = Calendar.getInstance();
        _cal.setTime(date);
        return _cal.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * 数据解锁
     * @param session
     * @param request
     * @param tableId
     * @param tableId
     * @param dataId
     * @return boolean
     */
    private boolean unLockData(HttpSession session,HttpServletRequest request,
                               String tableId,String dataId) {
        return exclusiveServiceImpl.UnLockData(session, request, Cm9070Interface.TATBL_CM9070, Cm9070Interface.TATBL_CM9070_SNG_WHICH_CODE,Cm9070Interface.TATBL_CM9070,dataId);
    }
}
