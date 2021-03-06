package cn.com.bbut.iy.itemmaster.dao.gen;

import cn.com.bbut.iy.itemmaster.entity.od0000.OD0000;
import cn.com.bbut.iy.itemmaster.entity.od0000.OD0000Example;
import cn.com.bbut.iy.itemmaster.entity.od0000.OD0000Key;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OD0000GenMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table od0000
     *
     * @mbggenerated
     */
    int countByExample(OD0000Example example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table od0000
     *
     * @mbggenerated
     */
    int deleteByExample(OD0000Example example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table od0000
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(OD0000Key key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table od0000
     *
     * @mbggenerated
     */
    int insert(OD0000 record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table od0000
     *
     * @mbggenerated
     */
    int insertSelective(OD0000 record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table od0000
     *
     * @mbggenerated
     */
    List<OD0000> selectByExample(OD0000Example example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table od0000
     *
     * @mbggenerated
     */
    OD0000 selectByPrimaryKey(OD0000Key key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table od0000
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") OD0000 record, @Param("example") OD0000Example example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table od0000
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") OD0000 record, @Param("example") OD0000Example example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table od0000
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(OD0000 record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table od0000
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(OD0000 record);

    /**
     * 注意：必须在example执行过<code>setNeedFoundRows(true);</code>才可使用此方法
     * 此方法必须与select语句在同一session内(可理解为同一service方法内)执行
     */
    int countLastSelect();
}