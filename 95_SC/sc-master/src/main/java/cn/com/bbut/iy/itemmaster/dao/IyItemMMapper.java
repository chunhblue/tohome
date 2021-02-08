package cn.com.bbut.iy.itemmaster.dao;

import java.util.Map;

import cn.com.bbut.iy.itemmaster.dao.gen.IyItemMGenMapper;
import cn.com.bbut.iy.itemmaster.dto.base.ItemStoreDTO;

public interface IyItemMMapper extends IyItemMGenMapper {

    /**
     * 取得c表的单品数据，包含单价售价
     * 
     * @param map
     * @return
     */
    ItemStoreDTO selectItemInfoFromC(Map<String, String> map);
}