package cn.com.bbut.iy.itemmaster.serviceimpl;

import cn.com.bbut.iy.itemmaster.dao.MA0020DTOMapper;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.ma0020.MA0020DTO;
import cn.com.bbut.iy.itemmaster.dto.ma0020.Ma0020ParamDTO;
import cn.com.bbut.iy.itemmaster.service.ma0020.Ma0020Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * @ClassName ma0020ServiceImpl
 * @Description TODO
 * @Author Administrator
 * @Date 2020/3/7 18:54
 * @Version 1.0
 */
@Service
public class ma0020ServiceImpl  implements Ma0020Service {
    //树状菜单
    @Autowired
    private MA0020DTOMapper ma0020DTOMapper;

    @Override
    public Collection<MA0020DTO> getAllMenus() {
        Ma0020ParamDTO dto = new Ma0020ParamDTO();
        List<MA0020DTO> ma0020DTOS = ma0020DTOMapper.selectListByCondition(dto);
        return ma0020DTOS;
    }

    // 显示单元格
//    @Override
//    public GridDataDTO<MA0020DTO> selectInformation(String structureName) {
//        //根据前台传出的名字 查找一 MA0020DTO
//       MA0020DTO ma0020DTO = ma0020DTOMapper.selectAMa0020C(structureName);
//
//        Integer count =ma0020DTOMapper.selectCountByCondition(ma0020DTO);
//        if (count==0) {
//            return  new GridDataDTO<MA0020DTO>();
//        }
//        List<MA0020DTO> ma0020DTOSList = ma0020DTOMapper.selectListByCondition(ma0020DTO);
//        GridDataDTO<MA0020DTO> data = new GridDataDTO<>(ma0020DTOSList,1,1,1);
//
//
//             return data;
//    }

    @Override
    public GridDataDTO<MA0020DTO> selectAMa0020C(Ma0020ParamDTO ma0020ParamDTO) {
        GridDataDTO<MA0020DTO> data = new GridDataDTO<MA0020DTO>();
        List<MA0020DTO> ma0020DTOS = ma0020DTOMapper.selectListByCondition(ma0020ParamDTO);
        data.setRows(ma0020DTOS);
        return data;
    }

    @Override
    public MA0020DTO selectDetailMa0020(String structureCd) {
       return ma0020DTOMapper.selectDetailMa0020(structureCd);
    }

    @Override
    public List<AutoCompleteDTO> getListStructName(String v) {
        List<AutoCompleteDTO> dtos= ma0020DTOMapper.getListStructName(v);
        return dtos;
    }

    /**
     * 角色权限设置时，根据组织结构等级查询
     *
     * @param level
     * @param v
     */
    @Override
    public List<AutoCompleteDTO> getListByLevel(String level, String p, String v) {
        return ma0020DTOMapper.selectListByLevel(level, p, v);
    }

    /**
     * 直接获取角色全部店铺
     *
     * @param stores
     * @param v
     */
    @Override
    public List<AutoCompleteDTO> getAllStore(Collection<String> stores, String v) {
        return ma0020DTOMapper.selectAllStore(stores, v);
    }

    @Override
    public GridDataDTO<MA0020DTO> getData(Ma0020ParamDTO param) {
        List<MA0020DTO> list = ma0020DTOMapper.getData(param);
        int count =ma0020DTOMapper.getDataCount(param);
        return new GridDataDTO<MA0020DTO>(list, param.getPage(), count,
                param.getRows());
    }
}
