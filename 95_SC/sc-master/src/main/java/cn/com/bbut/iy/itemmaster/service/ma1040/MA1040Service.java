package cn.com.bbut.iy.itemmaster.service.ma1040;


import cn.com.bbut.iy.itemmaster.entity.MA1040;

import java.util.List;

public interface MA1040Service {

    /**
     * MA1040查询
     *
     */
    List<MA1040> getList(String storeCd);

}
