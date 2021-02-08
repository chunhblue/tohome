package cn.com.bbut.iy.itemmaster.service.bmhis;

import java.util.Collection;
import java.util.List;

import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.bm.BmViewMainDTO;
import cn.com.bbut.iy.itemmaster.dto.bmhis.BmHisInitDTO;
import cn.com.bbut.iy.itemmaster.dto.bmhis.BmHisJsonParamDTO;
import cn.com.bbut.iy.itemmaster.dto.bmhis.BmHisListGridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.bmhis.BmHisParamDTO;
import cn.com.bbut.iy.itemmaster.dto.bmhis.BmHisResuleDataDTO;
import cn.com.bbut.iy.itemmaster.entity.IyBmCk;
import cn.com.bbut.iy.itemmaster.entity.IyBmItemCk;
import cn.com.bbut.iy.itemmaster.entity.User;

/**
 * BM历史查询
 * 
 * @author songxz
 */
public interface BmHisService {

    /**
     * 得到BM历史查询画面初始化所使用的基本数据
     * 
     * @param roleIds
     * @return
     */
    BmHisInitDTO getInitData(Collection<Integer> roleIds, User u);

    /**
     * 根据权限+事业部，得到最终的部门list
     * 
     * @param pCodeBmHisView
     * @param division
     * @return
     */
    List<AutoCompleteDTO> getdepartments(String pCodeBmHisView, String division);

    /**
     * 得到检索数据
     * 
     * @param param
     * @return
     */
    GridDataDTO<BmHisListGridDataDTO> getData(BmHisParamDTO param);

    /**
     * 得到实际的检索数据，包含数据总量
     * 
     * @param bmJsonParam
     * @return
     */
    BmHisResuleDataDTO getBmHisList(BmHisJsonParamDTO bmJsonParam);

    /**
     * 根据 newNo和newNoSub 得到明细数据
     * 
     * @param bmType
     * @param newNo
     * @param newNoSub
     * @return
     */
    BmViewMainDTO getBmHisViewData(String bmType, String newNo, String newNoSub);

    /**
     * 根据key得到bmhis主挡数据<br>
     * key=newNo+newNoSub<br>
     * 使用bm管理的查看处理方式和对象，所以这里返回的对象是IyBmCk，2个表字段除了缺少newNo、newNoSub外都一致
     * 
     * @param newNo
     * @param newNoSub
     * @return
     */
    IyBmCk getBmCkBaseByKey(String newNo, String newNoSub);

    /**
     * 根据key得到bmhis 明细档<br>
     * key=newNo+newNoSub<br>
     * 使用bm管理的查看处理方式和对象，所以这里返回的对象是IyBmItemCk，2个表字段除了缺少newNo、newNoSub外都一致
     * 
     * @param newNo
     * @param newNoSub
     * @return
     */
    List<IyBmItemCk> getBmCkItemsByKey(String newNo, String newNoSub);
}
