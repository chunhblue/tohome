package cn.com.bbut.iy.itemmaster.service;


import cn.com.bbut.iy.itemmaster.entity.od8030.OD8030;

import java.util.List;

/**
 * OD8030
 * 
 * @author lz
 */
public interface OD8030Service {

    Integer insert(OD8030 od8030);

    int delete(String return_id, String article_id);

    List<OD8030> isInItem(String storeCd, String returnId);

//    Integer updateByReturnId(String returnId, String vendorId);

    Integer updateByRA(OD8030 od8030);
}
