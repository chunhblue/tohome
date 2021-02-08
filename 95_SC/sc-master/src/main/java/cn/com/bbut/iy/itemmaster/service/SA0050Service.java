package cn.com.bbut.iy.itemmaster.service;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.sa0050.SA0050GridDto;
import cn.com.bbut.iy.itemmaster.dto.sa0050.SA0050ParamDto;
import cn.com.bbut.iy.itemmaster.entity.sa0050.SA0050;

/**
 * SA0050
 *
 * @author zcz
 */
public interface SA0050Service {
    /**
     * 获取收银员一览
     * @param param
     * @return
     */
    GridDataDTO<SA0050GridDto> getList(SA0050ParamDto param);

    /**
     * 获取收银员信息
     * @param cashierId
     * @return
     */
    SA0050 getCashier(String cashierId);

    /**
     * 获取收银员密码
     * @param cashierId
     * @return
     */
    String getCashierPwd(String cashierId);

    /**
     * 添加收银员
     * @param sa0050
     * @return
     */
    int insertCashier(SA0050 sa0050);

    /**
     * 修改收银员权限
     * @param sa0050
     * @return
     */
    int updateCashier(SA0050 sa0050);

    /**
     * 修改收营员状态
     * @param sa0050
     * @return
     */
    int updateCashierSts(SA0050 sa0050);

    /**
     * 修改收银员密码
     * @param sa0050
     * @param isInit 是否初始化密码
     * @return
     */
    int updateCashierPwd(SA0050 sa0050,boolean isInit);

    /**
     * 删除数据
     * @param cashierId
     * @return
     */
    int delete(String cashierId,String storeCd);


    Boolean getCashierIdCount(String cashierId);


    Boolean getCashierPwd1(String cashierId,String oldPassword,String storeCd);

    Boolean getCashierIdCount1(String cashierId, String storeCd);

    SA0050 getCashier1(String cashierId, String storeCd);
}
