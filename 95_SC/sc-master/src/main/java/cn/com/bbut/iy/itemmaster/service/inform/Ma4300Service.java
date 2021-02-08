package cn.com.bbut.iy.itemmaster.service.inform;


import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.inform.Ma4300DetailGridDto;
import cn.com.bbut.iy.itemmaster.dto.inform.Ma4300DetailParamDto;
import cn.com.bbut.iy.itemmaster.dto.inform.Ma4305DetailGridDto;
import cn.com.bbut.iy.itemmaster.dto.inform.Ma4310DetailGridDto;
import cn.com.bbut.iy.itemmaster.entity.ma4300.MA4300;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

public interface Ma4300Service {
    /**
     * 通报消息一览
     * @param param
     * @return
     */
    GridDataDTO<Ma4300DetailGridDto> getList(Ma4300DetailParamDto param);
    /**
     *  通报消息保存
     * @param param
     * @return
     */
    String insertInform(HttpSession session, HttpServletRequest request, MA4300 param);
    /**
     *  通报消息修改
     * @param param
     * @return
     */
    boolean updateInform(MA4300 param);

    /**
     * 通报消息头档
     * @param informCd
     * @return
     */
    MA4300 getMa4300(String informCd);

    /**
     * 通报消息角色范围一览
     * @param informCd
     * @return
     */
    List<Ma4305DetailGridDto> getMa4305List(String informCd);

    /**
     * 通报消息门店范围一览
     * @param informCd
     * @return
     */
    List<Ma4310DetailGridDto> getMa4310List(String informCd);

    /**
     * 获取角色下拉
     * @param v
     * @return
     */
    List<AutoCompleteDTO> getRoleListAll(String v);
}
