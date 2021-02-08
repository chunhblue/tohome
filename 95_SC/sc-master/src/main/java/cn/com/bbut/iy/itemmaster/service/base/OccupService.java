package cn.com.bbut.iy.itemmaster.service.base;

import java.util.List;

import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.LabelDTO;
import cn.com.bbut.iy.itemmaster.entity.base.IyOccup;

/**
 * 职务
 * 
 * @author songxz
 */
public interface OccupService {

    /**
     * 根据职种名称或code 模糊查询得到相关职种集合
     * 
     * @param param
     *            职种名称或者职种code
     * @return
     */
    List<IyOccup> getOccupByParam(String param);

    /**
     * 根据职种名称或code 模糊查询得到相关职种集合(返回Automatic结构)
     * 
     * @param param
     *            职种名称或者职务code
     * @return
     */
    List<AutoCompleteDTO> getOccupAutoByParam(String param);

    /**
     * 根据编号得到职种名称等相关内容
     * 
     * @param occupCode
     * @return
     */
    LabelDTO getName(String occupCode);
}
