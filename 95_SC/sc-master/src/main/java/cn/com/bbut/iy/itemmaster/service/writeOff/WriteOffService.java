package cn.com.bbut.iy.itemmaster.service.writeOff;

import java.util.List;
import java.util.Map;

import cn.com.bbut.iy.itemmaster.dto.writeOff.WriteOffDTO;
import cn.com.bbut.iy.itemmaster.dto.writeOff.WriteOffParamDTO;

/**
 * @author mxy
 */
public interface WriteOffService {

    /**
     * 条件查询数据
     * @param dto
     * @return
     */
    Map<String,Object> deleteGetList(WriteOffParamDTO dto);

    List<WriteOffDTO> deleteGetList1(WriteOffParamDTO dto);


//    WriteOffDTO getOffQty(WriteOffParamDTO dto);

    WriteOffDTO deleteGetOffQty(WriteOffParamDTO jsonParam);
//
//    List<WriteOffDTO> deleteGetList1(WriteOffParamDTO param);
}
