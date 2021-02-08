package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dto.unpack.UnpackDTO;
import cn.com.bbut.iy.itemmaster.dto.unpack.UnpackDetailsDTO;
import cn.com.bbut.iy.itemmaster.dto.unpack.UnpackParamDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
 
@Repository
public interface UnpackMapper {
 
    /**
     * 条件查询头档总数
     * @param dto
     */
    int selectCountByCondition(UnpackParamDTO dto);
 
    /**
     * 条件查询头档
     * @param dto
     */
    List<UnpackDTO> selectListByCondition(UnpackParamDTO dto);
 
    /**
     * 主键查询组合头档
     * @param dto
     */
    UnpackDTO selectPackByKey(UnpackParamDTO dto);
 
    /**
     * 主键查询拆包头档
     * @param dto
     */
    UnpackDTO selectByKey(UnpackParamDTO dto);
 
    /**
     * 查询组合明细
     * @param dto
     */
    List<UnpackDetailsDTO> selectPackDetailsByKey(UnpackParamDTO dto);
 
    /**
     * 查询拆包明细
     * @param dto
     */
    List<UnpackDetailsDTO> selectDetailsByKey(UnpackParamDTO dto);
 
    /**
     * 新增头档
     * @param dto
     */
    void insertUnpack(UnpackDTO dto);
 
    /**
     * 新增明细
     * @param dto
     */
    void insertDetails(UnpackDetailsDTO dto);
 
    /**
     * 更新头档
     * @param dto
     */
    void updateUnpack(UnpackDTO dto);
 
    /**
     * 更新明细
     * @param dto
     */
    void updateDetails(UnpackDetailsDTO dto);
 
    /**
     * 删除头档
     * @param dto
     */
    void deleteUnpack(UnpackParamDTO dto);
 
    /**
     * 删除明细
     * @param dto
     */
    void deleteDetails(UnpackParamDTO dto);
}