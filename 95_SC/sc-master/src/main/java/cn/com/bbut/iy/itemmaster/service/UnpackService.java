package cn.com.bbut.iy.itemmaster.service;
 
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.unpack.*;
 
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
 
/**
 * @author mxy
 */
public interface UnpackService {
 
    /**
     * 条件查询头档
     * 
     * @param dto
     * @return
     */
    GridDataDTO<UnpackDTO> getList(UnpackParamDTO dto);
 
    /**
     * 主键查询头档
     *
     * @param dto
     * @return
     */
    UnpackDTO getByKey(UnpackParamDTO dto, String flag);
 
    /**
     * 查询明细
     *
     * @param dto
     * @return
     */
    GridDataDTO<UnpackDetailsDTO> getDetails(UnpackParamDTO dto, String flag);
 
    /**
     * 新增记录
     *
     * @param dto
     * @param list
     * @return
     */
    String insert(HttpSession session, HttpServletRequest request,
               UnpackDTO dto, List<UnpackDetailsDTO> list);
 
    /**
     * 更新记录
     *
     * @param dto
     * @param list
     * @return
     */
    String update(UnpackDTO dto, List<UnpackDetailsDTO> list);
 
    /**
     * 删除记录
     *
     * @param list
     * @return
     */
    int delete(List<UnpackParamDTO> list);
 
 
}