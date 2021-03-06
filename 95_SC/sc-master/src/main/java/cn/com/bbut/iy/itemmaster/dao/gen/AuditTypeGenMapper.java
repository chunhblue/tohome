package cn.com.bbut.iy.itemmaster.dao.gen;

import cn.com.bbut.iy.itemmaster.entity.AuditType;
import cn.com.bbut.iy.itemmaster.entity.AuditTypeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AuditTypeGenMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_audit_type
     *
     * @mbg.generated
     */
    long countByExample(AuditTypeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_audit_type
     *
     * @mbg.generated
     */
    int deleteByExample(AuditTypeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_audit_type
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer nTypeid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_audit_type
     *
     * @mbg.generated
     */
    int insert(AuditType record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_audit_type
     *
     * @mbg.generated
     */
    int insertSelective(AuditType record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_audit_type
     *
     * @mbg.generated
     */
    List<AuditType> selectByExample(AuditTypeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_audit_type
     *
     * @mbg.generated
     */
    AuditType selectByPrimaryKey(Integer nTypeid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_audit_type
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") AuditType record, @Param("example") AuditTypeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_audit_type
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") AuditType record, @Param("example") AuditTypeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_audit_type
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(AuditType record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_audit_type
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(AuditType record);

    /**
     * 注意：必须在example执行过<code>setNeedFoundRows(true);</code>才可使用此方法
     * 此方法必须与select语句在同一session内(可理解为同一service方法内)执行
     */
    int countLastSelect();
}