package cn.com.bbut.iy.itemmaster.dao.gen;

import cn.com.bbut.iy.itemmaster.entity.MA8260;
import cn.com.bbut.iy.itemmaster.entity.MA8260Example;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MA8260GenMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ma8260
     *
     * @mbg.generated
     */
    long countByExample(MA8260Example example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ma8260
     *
     * @mbg.generated
     */
    int deleteByExample(MA8260Example example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ma8260
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(String accDate);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ma8260
     *
     * @mbg.generated
     */
    int insert(MA8260 record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ma8260
     *
     * @mbg.generated
     */
    int insertSelective(MA8260 record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ma8260
     *
     * @mbg.generated
     */
    List<MA8260> selectByExample(MA8260Example example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ma8260
     *
     * @mbg.generated
     */
    MA8260 selectByPrimaryKey(String accDate);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ma8260
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") MA8260 record, @Param("example") MA8260Example example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ma8260
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") MA8260 record, @Param("example") MA8260Example example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ma8260
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(MA8260 record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ma8260
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(MA8260 record);

    /**
     * 注意：必须在example执行过<code>setNeedFoundRows(true);</code>才可使用此方法
     * 此方法必须与select语句在同一session内(可理解为同一service方法内)执行
     */
    int countLastSelect();
}