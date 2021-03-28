package cn.com.bbut.iy.itemmaster.service.yearPromotionService;

import cn.com.bbut.iy.itemmaster.dto.yearendpromotion.yearEndPromotionParamDto;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface yearEndPromotionService {

    Map<String, Object> search(@Param("param") yearEndPromotionParamDto param);
}
