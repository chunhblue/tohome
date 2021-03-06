package cn.com.bbut.iy.itemmaster.dao.gen;

import cn.com.bbut.iy.itemmaster.entity.MA4340;
import cn.com.bbut.iy.itemmaster.entity.MA4340Example;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MA4340GenMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ma4340
     *
     * @mbg.generated
     */
    long countByExample(MA4340Example example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ma4340
     *
     * @mbg.generated
     */
    int deleteByExample(MA4340Example example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ma4340
     *
     * @mbg.generated
     */
    int insert(MA4340 record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ma4340
     *
     * @mbg.generated
     */
    int insertSelective(MA4340 record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ma4340
     *
     * @mbg.generated
     */
    List<MA4340> selectByExample(MA4340Example example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ma4340
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") MA4340 record, @Param("example") MA4340Example example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ma4340
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") MA4340 record, @Param("example") MA4340Example example);

    /**
     * 注意：必须在example执行过<code>setNeedFoundRows(true);</code>才可使用此方法
     * 此方法必须与select语句在同一session内(可理解为同一service方法内)执行
     */
    int countLastSelect();
}