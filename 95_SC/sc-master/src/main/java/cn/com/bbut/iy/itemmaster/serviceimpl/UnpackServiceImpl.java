package cn.com.bbut.iy.itemmaster.serviceimpl;
 
import cn.com.bbut.iy.itemmaster.dao.Cm9060Mapper;
import cn.com.bbut.iy.itemmaster.dao.UnpackMapper;
import cn.com.bbut.iy.itemmaster.dto.base.CommonDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.cm9070.Cm9070ReturnDTO;
import cn.com.bbut.iy.itemmaster.dto.unpack.UnpackDTO;
import cn.com.bbut.iy.itemmaster.dto.unpack.UnpackDetailsDTO;
import cn.com.bbut.iy.itemmaster.dto.unpack.UnpackParamDTO;
import cn.com.bbut.iy.itemmaster.entity.base.Cm9060;
import cn.com.bbut.iy.itemmaster.service.UnpackService;
import cn.com.bbut.iy.itemmaster.service.cm9070.Cm9070Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
 
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;
 
/**
 * @author mxy
 */
@Service
@Slf4j
public class UnpackServiceImpl implements UnpackService {
 
    @Autowired
    private UnpackMapper unpackMapper;
    @Autowired
    private Cm9070Service cm9070ServiceImpl;
    @Autowired
    private Cm9060Mapper cm9060Mapper;
 
 
    /**
     * 获取当前业务日期
     */
    public String getBusinessDate() {
        Cm9060 dto =  cm9060Mapper.selectByPrimaryKey("0000");
        return dto.getSpValue();
    }
 
    /**
     * 条件查询头档
     *
     * @param dto
     * @return
     */
    @Override
    public GridDataDTO<UnpackDTO> getList(UnpackParamDTO dto) {
        // 获取业务日期
        dto.setBusinessDate(getBusinessDate());
        int count = unpackMapper.selectCountByCondition(dto);
        if(count == 0){
            return new GridDataDTO<UnpackDTO>();
        }
        List<UnpackDTO> _list = unpackMapper.selectListByCondition(dto);
        GridDataDTO<UnpackDTO> data = new GridDataDTO<UnpackDTO>(_list,
                dto.getPage(), count, dto.getRows());
        return data;
    }
 
    /**
     * 主键查询头档
     *
     * @param dto
     * @return
     */
    @Override
    public UnpackDTO getByKey(UnpackParamDTO dto, String flag) {
        // 获取业务日期
        dto.setBusinessDate(getBusinessDate());
        if("add".equals(flag)){
            return unpackMapper.selectPackByKey(dto);
        }else{
            return unpackMapper.selectByKey(dto);
        }
    }
 
    /**
     * 查询明细
     *
     * @param dto
     * @return
     */
    @Override
    public GridDataDTO<UnpackDetailsDTO> getDetails(UnpackParamDTO dto, String flag) {
        // 获取业务日期
        dto.setBusinessDate(getBusinessDate());
        GridDataDTO<UnpackDetailsDTO> data = new GridDataDTO<UnpackDetailsDTO>();
        List<UnpackDetailsDTO> _list = null;
        if("add".equals(flag)){
            _list = unpackMapper.selectPackDetailsByKey(dto);
        }else{
            _list = unpackMapper.selectDetailsByKey(dto);
        }
        data.setRows(_list);
        return data;
    }
 
    /**
     * 新增记录
     *
     * @param dto
     * @param list
     * @return
     */
    @Override
    @Transactional
    public String insert(HttpSession session, HttpServletRequest request, UnpackDTO dto, List<UnpackDetailsDTO> list) {
        /* 自动采番开始 */
        Cm9070ReturnDTO _cm9070ReturnDto = new Cm9070ReturnDTO();
        try {
            _cm9070ReturnDto = cm9070ServiceImpl.Get(session, request,
                    "MA1220", "ma1220_id", new Date(), null);
        } catch (UnknownHostException e) {
            log.error("采番异常-----"+e.getMessage());
            return null;
        }
        if (0 != _cm9070ReturnDto.getCode()) {
            log.error("采番失败", _cm9070ReturnDto.getMsg());
            return null;
        }
        /* 自动采番结束 */
        String unpackId = _cm9070ReturnDto.getaSreturnNumber();
        // 执行保存
        dto.setUnpackId(unpackId);
        try{
            unpackMapper.insertUnpack(dto);
            String storeCd = dto.getStoreCd();
            String parentArticleId = dto.getParentArticleId();
            CommonDTO commonDTO = dto.getCommonDTO();
            for(UnpackDetailsDTO bean : list){
                bean.setUnpackId(unpackId);
                bean.setStoreCd(storeCd);
                bean.setParentArticleId(parentArticleId);
                bean.setCommonDTO(commonDTO);
                unpackMapper.insertDetails(bean);
            }
        }catch (RuntimeException e){
            log.error("执行保存异常-----"+e.getMessage());
            return null;
        }
        return unpackId;
    }
 
    /**
     * 更新记录
     *
     * @param dto
     * @param list
     * @return
     */
    @Override
    @Transactional
    public String update(UnpackDTO dto, List<UnpackDetailsDTO> list) {
        String unpackId = dto.getUnpackId();
        try{
            unpackMapper.updateUnpack(dto);
            String storeCd = dto.getStoreCd();
            String parentArticleId = dto.getParentArticleId();
            CommonDTO commonDTO = dto.getCommonDTO();
            for(UnpackDetailsDTO bean : list){
                bean.setUnpackId(unpackId);
                bean.setStoreCd(storeCd);
                bean.setParentArticleId(parentArticleId);
                bean.setCommonDTO(commonDTO);
                unpackMapper.updateDetails(bean);
            }
        }catch (RuntimeException e){
            log.error("执行更新异常-----"+e.getMessage());
            return null;
        }
        return unpackId;
    }
 
    /**
     * 删除记录
     *
     * @param list
     * @return
     */
    @Override
    @Transactional
    public int delete(List<UnpackParamDTO> list) {
        try{
            for(UnpackParamDTO bean : list){
                unpackMapper.deleteUnpack(bean);
                unpackMapper.deleteDetails(bean);
            }
        }catch (RuntimeException e){
            log.error("执行删除异常-----"+e.getMessage());
            return 0;
        }
        return 1;
    }
}