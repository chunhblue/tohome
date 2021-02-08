package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dao.gen.MA4310GenMapper;
import cn.com.bbut.iy.itemmaster.dto.inform.Ma4300DetailGridDto;
import cn.com.bbut.iy.itemmaster.dto.inform.Ma4310DetailGridDto;
import cn.com.bbut.iy.itemmaster.dto.inform.Ma4310DetailParamDto;
import cn.com.bbut.iy.itemmaster.dto.inform.Ma4310ResultDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MA4310Mapper extends MA4310GenMapper {
    /**
     * 通报消息门店范围一览
     * @param informCd
     * @param businessDate
     * @return
     */
    List<Ma4310DetailGridDto>  getList(@Param("informCd") String informCd, @Param("businessDate") String businessDate);


    /**
     * 通报消息 回复一览
     * @param param
     * @return
     */
    List<Ma4310DetailGridDto> getReplyList(Ma4310DetailParamDto param);

    long getReplyListCount(Ma4310DetailParamDto param);

    /**
     * 通报消息 回复详细信息
     * @param informCd
     * @param storeCd
     * @return
     */
    Ma4310ResultDto getInformReply(@Param("informCd") String informCd, @Param("storeCd") String storeCd ,@Param("businessDate") String businessDate);
}