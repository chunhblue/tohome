package cn.com.bbut.iy.itemmaster.service.base;

import java.util.List;

import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.LabelDTO;
import cn.com.bbut.iy.itemmaster.entity.base.IyPost;

/**
 * 职务
 * 
 * @author songxz
 */
public interface PostService {

    /**
     * 根据职务名称或code 模糊查询得到相关职务集合
     * 
     * @param param
     *            职务名称或者职务code
     * @return
     */
    List<IyPost> getPostByParam(String param);

    /**
     * 根据职务名称或code 模糊查询得到相关职务集合(返回Automatic结构)
     * 
     * @param param
     *            职务名称或者职务code
     * @return
     */
    List<AutoCompleteDTO> getPostAutoByParam(String param);

    /**
     * 根据编号取得职务相关名称编号等信息
     * 
     * @param postCode
     * @return
     */
    LabelDTO getName(String postCode);
}
