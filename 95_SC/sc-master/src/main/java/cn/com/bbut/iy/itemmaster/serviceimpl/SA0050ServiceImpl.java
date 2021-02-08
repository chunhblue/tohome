package cn.com.bbut.iy.itemmaster.serviceimpl;

import cn.com.bbut.iy.itemmaster.dao.SA0050Mapper;
import cn.com.bbut.iy.itemmaster.dao.SA0055Mapper;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.sa0050.SA0050GridDto;
import cn.com.bbut.iy.itemmaster.dto.sa0050.SA0050ParamDto;
import cn.com.bbut.iy.itemmaster.entity.sa0050.SA0050;
import cn.com.bbut.iy.itemmaster.entity.sa0055.SA0055;
import cn.com.bbut.iy.itemmaster.service.CM9060Service;
import cn.com.bbut.iy.itemmaster.service.SA0050Service;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;


@Service
public class SA0050ServiceImpl implements SA0050Service {
    @Autowired
    private SA0050Mapper sa0050Mapper;
    @Autowired
    private SA0055Mapper sa0055Mapper;
    @Autowired
    private CM9060Service cm9060Service;
    //初始密码
    private String firstPwd = "000";
    //字符串加密flag
    private String flagLock = "lock";
    private String flagUnlock = "unlock";

    @Override
    public GridDataDTO<SA0050GridDto> getList(SA0050ParamDto param) {
        String str="null";
        System.out.println(param);
       // String businessDate = cm9060Service.getValByKey("0000");
    //    param.setBusinessDate(businessDate);
        if (param.getStoreCd().equals(str)) {
            param.setStoreCd("");
        }
        List<SA0050GridDto> list = sa0050Mapper.getCashierList(param);


        long count = sa0050Mapper.getCashierListCount(param);
       return new GridDataDTO<>(list, param.getPage(), count,
                param.getRows());
    }

    @Override
    public SA0050 getCashier(String cashierId) {
        return sa0050Mapper.selectByPrimaryKey(cashierId);
    }

    @Override
    public String getCashierPwd(String cashierId) {
        SA0050 sa0050 = sa0050Mapper.selectByPrimaryKey(cashierId);
        if(sa0050!=null&& StringUtils.isNotBlank(sa0050.getCashierPassword())){
            return this.pwd(sa0050.getCashierPassword(),flagUnlock);
        }
        return null;
    }

    @Override
    public int insertCashier(SA0050 sa0050) {
        //获取初始密码
        String password = this.pwd(firstPwd,flagLock);
        sa0050.setCashierPassword(password);
        //获取权限参数
        SA0055 sa0055 = sa0055Mapper.selectByPrimaryKey(sa0050.getCashierLevel());
        if(sa0055!=null){
            sa0050.setCashierParameter(sa0055.getCashierParameter());
        }
        return sa0050Mapper.insertSelective(sa0050);
    }

    @Override
    public int updateCashier(SA0050 sa0050) {
        //获取权限参数
        SA0055 sa0055 = sa0055Mapper.selectByPrimaryKey(sa0050.getCashierLevel());
        if(sa0055!=null){
            sa0050.setCashierParameter(sa0055.getCashierParameter());
        }
        return sa0050Mapper.updateByPrimaryKeySelective1(sa0050);
    }

    @Override
    public int updateCashierSts(SA0050 sa0050) {
        return sa0050Mapper.updateByPrimaryKeySelective1(sa0050);
    }

    @Override
    public int updateCashierPwd(SA0050 sa0050,boolean isInit) {
        String password = null;
        if(isInit){//为初始化
            //获取初始密码
            password = this.pwd(firstPwd,flagLock);
        }else{
            password = this.pwd(sa0050.getCashierPassword(),flagLock);
        }
        sa0050.setCashierPassword(password);
        return sa0050Mapper.updateByPrimaryKeySelective1(sa0050);
    }

    /**
     * 删除数据
     *
     * @param cashierId
     * @return
     */
    @Override
    public int delete(String cashierId,String storeCd) {
        return sa0050Mapper.deleteByPrimaryKey(cashierId,storeCd);
    }

    @Override
    public Boolean getCashierIdCount(String cashierId) {
        SA0050 sa0050 = sa0050Mapper.selectByPrimaryKey(cashierId);
        if (sa0050 == null)
            return true;
        else return false;
    }


    @Override
    public Boolean getCashierPwd1(String cashierId,String oldPassword,String storeCd) {
        String pwd=null;
        SA0050 sa0050 = sa0050Mapper.selectByPrimaryKey1(cashierId,storeCd);
        if(sa0050!=null&& StringUtils.isNotBlank(sa0050.getCashierPassword())){
           pwd = this.pwd(sa0050.getCashierPassword(), flagUnlock);
        }
        if (pwd!=null&&pwd.equalsIgnoreCase(oldPassword)) {
            return true;
        }
            return  false;
    }

    @Override
    public Boolean getCashierIdCount1(String cashierId, String storeCd) {
        SA0050 sa0050 = sa0050Mapper.selectByPrimaryKey1(cashierId,storeCd);
        if (sa0050 == null)
            return true;
        else return false;
    }

    @Override
    public SA0050 getCashier1(String cashierId, String storeCd) {

            return sa0050Mapper.selectByPrimaryKey1(cashierId,storeCd);
    }


    /**
     * 字符串加密方法
     * @param code
     * @param flag
     * @return
     */
    private String pwd(String code, String flag){
        String result = "";
        Random rd = new Random();
        if (flag.equals("lock"))
        {
            for (int i = 0; i < code.length(); i++)
            {
                result = result + (9 - Integer.parseInt(code.substring(i, i+1)));
                if (i < code.length())
                {
                    result = result + (char)(rd.nextInt(62) + 48);
                }
            }
        }
        else if (flag.equals("unlock"))
        {
            if (code.length() == 0)
                result = "0";
            else
            {
                for (int i = 0; i < code.length(); i++)
                {
                    result = result + (9 - Integer.parseInt(code.substring(i,i+1)));
                    i++;
                }
            }
        }
        return result;
    }

}
