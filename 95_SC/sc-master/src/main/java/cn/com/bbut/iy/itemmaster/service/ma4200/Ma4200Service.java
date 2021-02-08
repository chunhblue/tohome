package cn.com.bbut.iy.itemmaster.service.ma4200;

import cn.com.bbut.iy.itemmaster.dto.AjaxResultDto;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.ma4200.MA4200GridDTO;
import cn.com.bbut.iy.itemmaster.dto.ma4200.MA4200ParamDTO;
import cn.com.bbut.iy.itemmaster.entity.MA4200;

public interface Ma4200Service {
    /**
     * 获取用户一览
     * @param ma4200ParamDTO
     * @return
     */
    GridDataDTO<MA4200GridDTO> search(MA4200ParamDTO ma4200ParamDTO);

    /**
     * 查看用户id是否已经存在
     * @param userId
     * @return
     */
    boolean getUserIdCount(String userId);

    /**
     * 查看用户密码是否正确
     * @param password
     * @return
     */
    boolean confirmPassword(String userId,String password);

    /**
     * 修改密码
     * @param param
     * @return
     */
    AjaxResultDto updatePassword(MA4200 param);

    /**
     * 添加修改用户信息
     * @param param
     * @param operateFlg
     * @return
     */
    AjaxResultDto insertOrUpdate(MA4200 param,String operateFlg);

    /**
     * 删除用户
     * @param empNumId
     * @return
     */
    AjaxResultDto delete(String empNumId);
}
