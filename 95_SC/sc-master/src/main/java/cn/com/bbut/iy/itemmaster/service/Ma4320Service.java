package cn.com.bbut.iy.itemmaster.service;



import cn.com.bbut.iy.itemmaster.entity.MA4320;

import java.util.List;

public interface Ma4320Service {
    /**
     * 附件一览
     * @param recordCd
     * @param fileType 附件类型
     * @return
     */
    List<MA4320> getMa4320List(String recordCd, String fileType);

    String getNowDate();
}
