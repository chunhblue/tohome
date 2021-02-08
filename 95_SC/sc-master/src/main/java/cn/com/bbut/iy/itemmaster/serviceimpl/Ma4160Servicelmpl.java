package cn.com.bbut.iy.itemmaster.serviceimpl;

import cn.com.bbut.iy.itemmaster.dao.MA4160DTOMapper;
import cn.com.bbut.iy.itemmaster.dao.MA4160Mapper;
import cn.com.bbut.iy.itemmaster.dto.article.ArticleDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.store.MA4160DTO;
import cn.com.bbut.iy.itemmaster.dto.store.MA4160ParamDTO;
import cn.com.bbut.iy.itemmaster.entity.MA4160;
import cn.com.bbut.iy.itemmaster.entity.MA4160Example;
import cn.com.bbut.iy.itemmaster.service.Ma4160Service;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName Ma4160Servicelmpl
 * @Description TODO
 * @Author Administrator
 * @Date 2020/3/4 14:42
 * @Version 1.0
 */

@Service
public class Ma4160Servicelmpl  implements Ma4160Service {

      @Autowired
     private MA4160DTOMapper ma4160DTOMapper ;


    @Override
    public GridDataDTO<MA4160DTO> selectAll(MA4160ParamDTO ma4160ParamDTO) {
        int count = ma4160DTOMapper.selectCountByCondition(ma4160ParamDTO);
        if (count==0) {
            return new GridDataDTO<MA4160DTO>();
        }
        List<MA4160DTO> ma4160DTOSList = ma4160DTOMapper.selectListByCondition(ma4160ParamDTO);
        GridDataDTO<MA4160DTO> data = new GridDataDTO<MA4160DTO>( ma4160DTOSList,
                ma4160ParamDTO.getPage(), count, ma4160ParamDTO.getRows());
            return data;
    }

    @Override
    public GridDataDTO<MA4160DTO> search(MA4160ParamDTO ma4160ParamDTO) {
        int count = ma4160DTOMapper.selectCountByCondition(ma4160ParamDTO);
        if (count==0) {
            return new GridDataDTO<MA4160DTO>();
        }
        List<MA4160DTO> list=ma4160DTOMapper.search(ma4160ParamDTO);
        GridDataDTO<MA4160DTO> datadto = new GridDataDTO<MA4160DTO>( list, ma4160ParamDTO.getPage(),
                count, ma4160ParamDTO.getRows());
        return datadto;
    }

}
