package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dao.gen.MA4200GenMapper;
import cn.com.bbut.iy.itemmaster.dto.ma4200.MA4200GridDTO;
import cn.com.bbut.iy.itemmaster.dto.ma4200.MA4200ParamDTO;
import cn.com.bbut.iy.itemmaster.entity.MA4200;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MA4200Mapper extends MA4200GenMapper {
    /**
     * 用户信息查询一览
     * @param param
     * @return
     */
    List<MA4200GridDTO> selectList(MA4200ParamDTO param);

    long selectListCount(MA4200ParamDTO param);

    int updateUserInfo(MA4200 param);

    int deleteByEmpId(MA4200 param);

    int updatePassword(MA4200 param);

    List<MA4200GridDTO> getStoreInfoByuser(@Param("userCode")String userCode,@Param("businessDate")String businessDate
                                ,@Param("limitStartRow")int limitStartRow,@Param("rows")int rows);

    int countStoreByUser(@Param("userCode")String userCode);

    int insertUserToMa4210(MA4200 param);

    int updateMa4200ByUser(MA4200 param);
}