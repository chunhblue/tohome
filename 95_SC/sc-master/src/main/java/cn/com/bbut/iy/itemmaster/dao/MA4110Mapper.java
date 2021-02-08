package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.ma4110.Ma4110DetailGridDto;
import cn.com.bbut.iy.itemmaster.dto.ma4110.Ma4110GridDto;
import cn.com.bbut.iy.itemmaster.dto.ma4110.Ma4110ParamDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MA4110Mapper{
    /**
     * 获取区域信息
     * @return
     */
    List<AutoCompleteDTO> getRegionList();
    /**
     * 促销消息一览
     * @param param
     * @return
     */
    List<Ma4110GridDto> getList(Ma4110ParamDto param);

    long getListCount(Ma4110ParamDto param);
    /**
     * 促销详细消息一览
     * @param param
     * @return
     */
    List<Ma4110DetailGridDto> getDetailList(Ma4110ParamDto param);

    long getDetailListCount(Ma4110ParamDto param);
}