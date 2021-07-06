package cn.com.bbut.iy.itemmaster.service.ma4160refer;


import cn.com.bbut.iy.itemmaster.dto.ma0020.MA0020DTO;
import cn.com.bbut.iy.itemmaster.dto.store.MA4160DTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface Ma4160referService {

    List<MA4160DTO> getLJobTye(boolean flg);


    List<MA0020DTO> getStructName();
}
