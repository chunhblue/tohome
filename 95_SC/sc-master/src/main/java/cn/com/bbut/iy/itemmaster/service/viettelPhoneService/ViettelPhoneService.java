package cn.com.bbut.iy.itemmaster.service.viettelPhoneService;

import cn.com.bbut.iy.itemmaster.dto.VendorReturnedDaily.VendorReturnedDailyParamDTO;
import cn.com.bbut.iy.itemmaster.dto.viettelParamPhone.ma8407Paramdto;
import org.springframework.stereotype.Service;

import java.util.Map;
@Service
public interface ViettelPhoneService {
    Map<String, Object> searchData(ma8407Paramdto param);
}
