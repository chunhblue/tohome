package cn.com.bbut.iy.itemmaster.dao.audit;

import cn.com.bbut.iy.itemmaster.dto.audit.AuditBean;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public interface AuditBeanMapper{

    /**
     * 根据ID查询
     * @param nRecordid
     * @return
     */
    AuditBean selectAuditById(@Param("nRecordid") long nRecordid);

    /**
     * 根据主档ID和适用开始日查询
     * @param audit
     * @return
     */
    List<AuditBean> selectAuditByRecordIdAndTime(@Param("selectBean") AuditBean audit);

    /**
     * 根据主档ID、适用开始日和操作序列查询
     * @param nTypeid：主档类型ID
     * @return
     */
    AuditBean selectAuditByRecordIdAndTimeAndSubNo(@Param("typeId") int nTypeid,
                                                   @Param("recordCd") String recordCd, @Param("startDate") String startDate);

    /**
     * 根据主档ID和操作序列查询记录
     * @param nTypeid：主档类型ID
     * @return
     */
    AuditBean selectByRecordId(@Param("typeId") int nTypeid,
                               @Param("recordCd") String recordCd);

    /**
     * 查询主键最大值
     * @param
     * @return
     */
    int selectMaxId();

    /**
     * 查询审核操作序列
     * @param audit
     * @return
     */
    int selectMaxSubNo(@Param("_AuditSubNoBean") AuditBean audit);

    /**
     * 添加记录
     * @param audit
     * @return
     */
    int insertAudit(@Param("insertBean") AuditBean audit);

    /**
     * 更新流程的状态值
     * @param audit
     * @return
     */
    int updateAuditStatus(AuditBean audit);

    /**
     * 更新审核信息
     * @param audit
     * @return
     */
    int updateAuditInfo(@Param("_AuditInfo") AuditBean audit);

    /**
     * 记录设为无效
     * @param audit
     * @return
     */
    int invalidAudit(@Param("invalidBean") AuditBean audit);

    /**
     * 根据编号查询是否有进行审核
     * @param distinguishId
     * @param recordCd
     * @param effectiveStartDate
     * @return
     */
    int selectInAuditById(@Param("distinguishId") String distinguishId, @Param("recordCd") String recordCd,@Param("effectiveStartDate")String effectiveStartDate);

    /**
     * 根据类型编号查询类别编号
     * @param typeId
     * @return
     */
    String selectDistinguishById(@Param("typeId") int typeId);

    /**
     * 更新审核数据表审核状态值
     */
    int modifyRecordStatus(@Param("table")String table,
                           @Param("key")String key,
                           @Param("recordCd")String recordCd,
                           @Param("status")int status,
                           @Param("reviewId") long reviewId,
                           @Param("updateUserId") String userId,
                           @Param("updateYmd") String date,
                           @Param("updateHms") String time);

    /**
     * 获取数据审核状态
     */
    Integer getAuditStatus(@Param("table")String table,@Param("key")String key,@Param("recordCd")String recordCd);

    /**
     * 检查用户是否拥有审核权限
     * @param storeCd 审核记录id
     * @param userId 审核状态 1：通过 0：不通过
     * @return
     */
    int checkAuditByUserId(@Param("storeCd")String storeCd,@Param("userId") String userId);

    /**
     * 检查用户是否拥有审核权限
     * @param storeCd 审核记录id
     * @param userId 审核状态 1：通过 0：不通过
     * @param position 职位
     * @return
     */
    int checkAuditByUserIdAndPosition(@Param("storeCd")String storeCd,@Param("userId") String userId, @Param("position")long position);

    /**
     * 检查用户是否拥有审核权限
     * @param storeCd 审核记录id
     * @param userId 审核状态 1：通过 0：不通过
     * @param position 职位
     * @return
     */
    int checkAuditByPosition(@Param("storeCd")String storeCd,@Param("userId") String userId, @Param("position")long position);

    String getCorrType(@Param("tableName")String reTableName, @Param("recordCd")String recordCd,@Param("typeId")Integer typeId);
}