package cn.com.bbut.iy.itemmaster.dao.gen;

import cn.com.bbut.iy.itemmaster.entity.MA8250;
import cn.com.bbut.iy.itemmaster.entity.MA8250Example;
import cn.com.bbut.iy.itemmaster.entity.MA8250Key;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MA8250GenMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ma8250
     *
     * @mbg.generated
     */
    long countByExample(MA8250Example example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ma8250
     *
     * @mbg.generated
     */
    int deleteByExample(MA8250Example example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ma8250
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(MA8250Key key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ma8250
     *
     * @mbg.generated
     */
    int insert(MA8250 record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ma8250
     *
     * @mbg.generated
     */
    int insertSelective(MA8250 record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ma8250
     *
     * @mbg.generated
     */
    List<MA8250> selectByExample(MA8250Example example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ma8250
     *
     * @mbg.generated
     */
    MA8250 selectByPrimaryKey(MA8250Key key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ma8250
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") MA8250 record, @Param("example") MA8250Example example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ma8250
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") MA8250 record, @Param("example") MA8250Example example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ma8250
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(MA8250 record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ma8250
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(MA8250 record);

    /**
     * 注意：必须在example执行过<code>setNeedFoundRows(true);</code>才可使用此方法
     * 此方法必须与select语句在同一session内(可理解为同一service方法内)执行
     */
    int countLastSelect();
}