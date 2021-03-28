package cn.com.bbut.iy.itemmaster.service.audit;

import cn.com.bbut.iy.itemmaster.dto.audit.AuditBean;

import java.util.List;

public interface IAuditService {

    /**
     * 根据ID获取记录
     * @return
     */
    AuditBean getAuditById(long nRecordid);

    /**
     * 根据主档ID和适用开始日获取记录
     * @return
     */
    List<AuditBean> getAuditByRecordIdAndTime(AuditBean audit);

    /**
     * 根据主档ID、适用开始日和操作序列获取记录
     * @return
     */
    AuditBean getAuditByRecordIdAndTimeAndSubNo(int nTypeid, String recordCd, String startDate);

    /**
     * 根据主档ID和操作序列查询记录
     * @param nTypeid：主档类型ID
     * @return
     */
    AuditBean getIdByRecordId(int nTypeid, String recordCd);

    /**
     * 查询主键最大值
     * @return
     */
    int getMaxId();

    /**
     * 根据主档ID和适用开始日获取操作序列
     * @return
     */
    int getMaxSubNo(AuditBean audit);

    /**
     * 添加记录
     * @return
     */
    int addRecord(AuditBean audit);

    /**
     * 更新审核信息
     * @return
     */
    int updateAuditInfo(AuditBean audit);

    /**
     * 根据主档ID和适用开始日设置记录无效
     * @return
     */
    int updateVoidAudit(AuditBean audit);

    /**
     * 根据编号查询是否有进行审核
     * @param distinguishId
     * @param recordCd
     * @param effectiveStartDate
     * @return
     */
    int getInAuditById(String distinguishId, String recordCd,String effectiveStartDate);

    /**
     * 根据类型编号查询类别编号
     * @param typeId
     * @return
     */
    String getDistinguishById(int typeId);

    /**
     * 更新流程第一条步骤的状态值
     * @param audit
     * @return
     */
    int updateAuditStatus(AuditBean audit);

    /**
     * 更新审核数据表审核状态值
     * @param table 表名
     * @param key 数据字段名
     * @param recordCd 数据cd
     * @param status 审核状态值
     * @param reviewId 审核流程id
     * @param userId 审核用户id
     * @return
     */
    int updateRecordStatus(String table,String key,String recordCd,int status,long reviewId,String userId);

    /**
     * 获取数据审核状态
     * @param table 表名
     * @param key 数据字段名
     * @param recordCd 数据cd
     * @return
     */
    Integer getAuditStatus(String table,String key,String recordCd);

    /**
     * 修改主档审核状态
     * @param auditStepId 审核记录id
     * @param auditStatus 审核状态 1：通过 0：不通过
     * @param userId 审核用户id
     * @return
     */
    int updateRecordStatus(long auditStepId, String auditStatus,String userId);

    /**
     * 检查用户是否拥有审核权限
     * @param storeCd 审核记录id
     * @param userId 审核状态 1：通过 0：不通过
     * @param position 职位
     * @return
     */
    int checkAuditByUserId(String storeCd, String userId , long position);


    String getCorrType(String reTableName,String recordCd,Integer typeId);
}
