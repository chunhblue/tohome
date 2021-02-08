package cn.com.bbut.iy.itemmaster.service;


import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.ma1200.MA1200DTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Ma1200
 * 
 */
public interface Ma1200Service {

    /**
     * get value by key
     *
     * key
     */
    MA1200DTO getByParentId(String parentId);

    /**
     * 遍历出母货号
     * @param items
     */
    List<String> checkList(List<String> items);

    /**
     * 获取母货号待选List
     *
     */
    List<AutoCompleteDTO> getList(String v);

    List<Map<String,String>> getChildDetail(List<String> parentArticleId);

    /**
     * 遍历BOM配方母货号
     * @param items
     * @return
     */
    List<String> checkBOMList(List<String> items);

    List<Map<String,String>> getBOMChildDetail(List<String> parentArticleId);
}
