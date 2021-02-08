package cn.com.bbut.iy.itemmaster.service.base;

import java.util.List;

import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.DptResourceDTO;
import cn.com.bbut.iy.itemmaster.dto.base.LabelDTO;
import cn.com.bbut.iy.itemmaster.entity.base.IyDpt;
import cn.com.bbut.iy.itemmaster.entity.ma0070.MA0070;
import cn.com.bbut.iy.itemmaster.entity.ma0080.MA0080;
import cn.com.bbut.iy.itemmaster.entity.ma0090.MA0090;
import cn.com.bbut.iy.itemmaster.entity.ma0100.MA0100;

/**
 * dep
 * 
 * @author baixg
 */
public interface DepService {

    public final static String ALL_DEP = "999";
    public final static String ALL_DEP_NAME = "All Top Department";
    public final static String ALL_PMA_NAME = "All Department";
    public final static String ALL_CATEGORY_NAME = "All Category";
    public final static String ALL_SUB_CATEGORY_NAME = "All Sub Category";

    /**
     * 输入dptcode或名称，得到对应dpt集合（没有上下级关系，不适用大部分检索项）<br>
     * 适用于类似默认角色授权模块
     * 
     * @param param
     * @return
     */
    List<AutoCompleteDTO> getDptAutoCompleteByParam(String param);

    /**
     * 根据dptcode得到名称等内容 <br>
     * 默认授权角色使用
     * 
     * @param dpt
     * @return
     */
    LabelDTO getDptName(String dpt);

    /**
     * 检索DPT信息
     *
     * @param depId
     *            上级id
     * @param flg
     *            检索层级标识 0：事业部，1：部，2：DPT
     * @param id
     *            ID
     * @param name
     *            名称
     * @param allFlg
     *            是否包含全事业部 或 全部 或 全dpt，1：包含，2：不包含
     * @return
     */
    List<DptResourceDTO> getDpts(String depId,String pmaId,String categoryId, Integer flg, String id, String name,
            Integer allFlg);

    MA0070 getDepById(String depId);

    MA0080 getPmaById(String depId,String pmaId);

    MA0090 getCategoryById(String depId,String pmaId,String categoryId);

    MA0100 getSubCategoryById(String depId,String pmaId,String categoryId,String subCategoryId);

    /**
     * 检索DPT信息
     *
     * @param id
     *            ID
     * @param name
     *            名称
     * @param pCode
     *            权限
     * @return
     */
    List<AutoCompleteDTO> getDpts(String id, String name, String pCode);

    /**
     * 事业部集合（非模糊查询）<br>
     * 根据角色集合和权限 得到当前操作人所在画面的事业部数据集合
     * 
     * @param pCode
     *            权限
     * @return
     */
    List<AutoCompleteDTO> getDivisionByPrmission(String pCode);

    /**
     * 部<br>
     * 取得事业部下的部，有资源权限的限定
     * 
     * @param division
     *            事业部code
     * @param pCode
     *            权限
     * @return
     */
    List<AutoCompleteDTO> getDepartmentByPrmission(String division, String pCode);

    /**
     * 部<br>
     * 取得事业部下的部，有资源权限的限定
     * 
     * @param division
     *            事业部code
     * @param department
     *            部code
     * @param pCode
     *            权限
     * @return
     */
    List<AutoCompleteDTO> getDptByPrmission(String division, String department, String pCode);

}
