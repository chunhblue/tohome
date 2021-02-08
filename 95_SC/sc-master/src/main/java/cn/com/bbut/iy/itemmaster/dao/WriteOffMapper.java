package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dto.writeOff.WriteOffDTO;
import cn.com.bbut.iy.itemmaster.dto.writeOff.WriteOffParamDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WriteOffMapper {

    /**
     * 条件查询记录
     * @param dto
     */
    List<WriteOffDTO> selectListByCondition(WriteOffParamDTO dto);

    int selectCountByCondition(WriteOffParamDTO dto);
    int getCountItemSku(WriteOffParamDTO dto);
}
