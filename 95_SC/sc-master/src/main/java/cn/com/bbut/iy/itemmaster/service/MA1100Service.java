package cn.com.bbut.iy.itemmaster.service;

import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.entity.ma1100.MA1100;

import java.util.List;

/**
 * MA1100
 * 
 * @author lz
 */
public interface MA1100Service {
    List<MA1100> selectById(String articleId);

    String selectByarticleId(String articleId);

    List<AutoCompleteDTO> getItemAll(String storeCd,String v);
    List<MA1100> getItemNo();
}
