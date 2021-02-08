package cn.com.bbut.iy.itemmaster.service;

import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;

import java.util.List;

/**
 * MA5321
 * 
 */
public interface MA5321Service {

    /**
     * 查询待选
     * @param v
     */
    List<AutoCompleteDTO> getWarehouse(String v);

}
