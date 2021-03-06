package cn.com.bbut.iy.itemmaster.dao.gen;

import cn.com.bbut.iy.itemmaster.entity.od8030.OD8030;
import cn.com.bbut.iy.itemmaster.entity.od8030.OD8030Example;
import cn.com.bbut.iy.itemmaster.entity.od8030.OD8030Key;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OD8030GenMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table od8030
     *
     * @mbggenerated
     */
    int countByExample(OD8030Example example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table od8030
     *
     * @mbggenerated
     */
    int deleteByExample(OD8030Example example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table od8030
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(OD8030Key key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table od8030
     *
     * @mbggenerated
     */
    int insert(OD8030 record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table od8030
     *
     * @mbggenerated
     */
    int insertSelective(OD8030 record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table od8030
     *
     * @mbggenerated
     */
    List<OD8030> selectByExample(OD8030Example example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table od8030
     *
     * @mbggenerated
     */
    OD8030 selectByPrimaryKey(OD8030Key key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table od8030
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") OD8030 record, @Param("example") OD8030Example example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table od8030
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") OD8030 record, @Param("example") OD8030Example example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table od8030
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(OD8030 record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table od8030
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(OD8030 record);

    /**
     * 注意：必须在example执行过<code>setNeedFoundRows(true);</code>才可使用此方法
     * 此方法必须与select语句在同一session内(可理解为同一service方法内)执行
     */
    int countLastSelect();
}