package cn.com.bbut.iy.itemmaster.dao.gen;

import cn.com.bbut.iy.itemmaster.entity.MySubmissions;
import cn.com.bbut.iy.itemmaster.entity.MySubmissionsExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MySubmissionsGenMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table my_submissions
     *
     * @mbggenerated
     */
    int countByExample(MySubmissionsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table my_submissions
     *
     * @mbggenerated
     */
    int deleteByExample(MySubmissionsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table my_submissions
     *
     * @mbggenerated
     */
    int insert(MySubmissions record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table my_submissions
     *
     * @mbggenerated
     */
    int insertSelective(MySubmissions record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table my_submissions
     *
     * @mbggenerated
     */
    List<MySubmissions> selectByExample(MySubmissionsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table my_submissions
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") MySubmissions record, @Param("example") MySubmissionsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table my_submissions
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") MySubmissions record, @Param("example") MySubmissionsExample example);

    /**
     * 注意：必须在example执行过<code>setNeedFoundRows(true);</code>才可使用此方法
     * 此方法必须与select语句在同一session内(可理解为同一service方法内)执行
     */
    int countLastSelect();
}