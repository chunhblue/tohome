package cn.com.bbut.iy.itemmaster.dao.gen;

import cn.com.bbut.iy.itemmaster.entity.MA4320;
import cn.com.bbut.iy.itemmaster.entity.MA4320Example;
import cn.com.bbut.iy.itemmaster.entity.MA4320Key;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MA4320GenMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ma4320
     *
     * @mbg.generated
     */
    long countByExample(MA4320Example example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ma4320
     *
     * @mbg.generated
     */
    int deleteByExample(MA4320Example example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ma4320
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(MA4320Key key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ma4320
     *
     * @mbg.generated
     */
    int insert(MA4320 record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ma4320
     *
     * @mbg.generated
     */
    int insertSelective(MA4320 record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ma4320
     *
     * @mbg.generated
     */
    List<MA4320> selectByExample(MA4320Example example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ma4320
     *
     * @mbg.generated
     */
    MA4320 selectByPrimaryKey(MA4320Key key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ma4320
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") MA4320 record, @Param("example") MA4320Example example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ma4320
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") MA4320 record, @Param("example") MA4320Example example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ma4320
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(MA4320 record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ma4320
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(MA4320 record);

    /**
     * 注意：必须在example执行过<code>setNeedFoundRows(true);</code>才可使用此方法
     * 此方法必须与select语句在同一session内(可理解为同一service方法内)执行
     */
    int countLastSelect();
}