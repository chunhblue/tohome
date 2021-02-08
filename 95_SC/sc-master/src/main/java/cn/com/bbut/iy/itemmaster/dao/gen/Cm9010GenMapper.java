package cn.com.bbut.iy.itemmaster.dao.gen;

import cn.com.bbut.iy.itemmaster.entity.cm9010.Cm9010;
import cn.com.bbut.iy.itemmaster.entity.cm9010.Cm9010Example;
import cn.com.bbut.iy.itemmaster.entity.cm9010.Cm9010Key;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface Cm9010GenMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cm9010
     *
     * @mbg.generated
     */
    long countByExample(Cm9010Example example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cm9010
     *
     * @mbg.generated
     */
    int deleteByExample(Cm9010Example example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cm9010
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Cm9010Key key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cm9010
     *
     * @mbg.generated
     */
    int insert(Cm9010 record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cm9010
     *
     * @mbg.generated
     */
    int insertSelective(Cm9010 record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cm9010
     *
     * @mbg.generated
     */
    List<Cm9010> selectByExample(Cm9010Example example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cm9010
     *
     * @mbg.generated
     */
    Cm9010 selectByPrimaryKey(Cm9010Key key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cm9010
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") Cm9010 record, @Param("example") Cm9010Example example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cm9010
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") Cm9010 record, @Param("example") Cm9010Example example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cm9010
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(Cm9010 record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cm9010
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(Cm9010 record);

    /**
     * 注意：必须在example执行过<code>setNeedFoundRows(true);</code>才可使用此方法
     * 此方法必须与select语句在同一session内(可理解为同一service方法内)执行
     */
    int countLastSelect();
}