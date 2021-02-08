package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dto.ReconciliationMng.ReconciliationMngDto;
import cn.com.bbut.iy.itemmaster.dto.ReconciliationMng.ReconciliationMngParamDto;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.entity.Mb1200;
import cn.com.bbut.iy.itemmaster.entity.Mb1300;
import cn.com.bbut.iy.itemmaster.entity.Mb1700;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ReconciliationMngMapper {

    List<ReconciliationMngDto> selectListByCondition(String businessDate);

    /**
     * 获得下拉文件夹信息
     * @param v
     * @return
     */
    List<AutoCompleteDTO> getMb0010(@Param("v")String v);

    /**
     * 获取下拉文件信息
     * @return
     */
    List<AutoCompleteDTO> getMb0020(@Param("documentReconCd")String documentReconCd,@Param("v")String v);

    /**
     * 302 Viettel
     */
    int insertMb1700(Collection<Mb1700> list);

    List<Mb1700> getMb1700List(@Param("storeItems")Collection<String> storeItems);

    int deleteMb1700ByCondition(@Param("storeDateItems")Collection<String> storeDateItems);

    List<ReconciliationMngDto> selectMb1700List(ReconciliationMngParamDto reParam);

    int selectCountMb1700List(ReconciliationMngParamDto reParam);

    /**
     * 201 CK SAP (Paybill)
     */
    int insertExcelToMb1200(@Param("mb1200List") Collection<Mb1200> mb1200List);

    List<ReconciliationMngDto> selectListForMb1200(String businessDate,@Param("dbStores") Collection<String> dbStores);

    List<Mb1200> getMb1200List(@Param("stores")Collection<String> stores);

    int deleteMb1200ByCondition(@Param("storeDates")Collection<String> storeDates);

    List<ReconciliationMngDto> selectMb1200List(ReconciliationMngParamDto reParam);

    int selectCountMb1200List(ReconciliationMngParamDto reParam);

    /**
     * 202 CK SAP (Paycode)
     */
    List<Mb1300> getMb1300List(@Param("storeItems")Collection<String> storeItems);

    int insertExcelToMb1300(@Param("mb1300List") Collection<Mb1300> mb1300List);

    int deleteMb1300ByCondition(@Param("storeDateItems")Collection<String> storeDateItems);

    List<ReconciliationMngDto> selectListForMb1300(String businessDate,@Param("dbStores") Collection<String> dbStores);

    List<ReconciliationMngDto> selectMb1300List(ReconciliationMngParamDto reParam);

    int selectCountMb1300List(ReconciliationMngParamDto reParam);
}
