package cn.com.bbut.iy.itemmaster.service;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.store.MA4160DTO;
import cn.com.bbut.iy.itemmaster.dto.store.MA4160ParamDTO;
import cn.com.bbut.iy.itemmaster.entity.MA4160;
import org.springframework.stereotype.Service;
import java.util.List;


public interface Ma4160Service {
   /**
    *查询所有职务类型
    */

   GridDataDTO<MA4160DTO> selectAll(MA4160ParamDTO ma4160ParamDTO);
   GridDataDTO<MA4160DTO> search(MA4160ParamDTO ma4160ParamDTO);

   int getPositionByStoreCd(String storeCd,String userId);

   int checkAuditByStoreCdAndUserId(String storeCd,String userId);

   List<Integer> getPositionList(String storeCd,String userId);
}
