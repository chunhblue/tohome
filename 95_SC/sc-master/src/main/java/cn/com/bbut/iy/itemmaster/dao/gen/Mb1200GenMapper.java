package cn.com.bbut.iy.itemmaster.dao.gen;

import cn.com.bbut.iy.itemmaster.entity.Mb1200;
import cn.com.bbut.iy.itemmaster.entity.Mb1200Example;
import cn.com.bbut.iy.itemmaster.entity.Mb1200Key;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface Mb1200GenMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mb1200
     *
     * @mbg.generated
     */
    long countByExample(Mb1200Example example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mb1200
     *
     * @mbg.generated
     */
    int deleteByExample(Mb1200Example example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mb1200
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Mb1200Key key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mb1200
     *
     * @mbg.generated
     */
    int insert(Mb1200 record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mb1200
     *
     * @mbg.generated
     */
    int insertSelective(Mb1200 record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mb1200
     *
     * @mbg.generated
     */
    List<Mb1200> selectByExample(Mb1200Example example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mb1200
     *
     * @mbg.generated
     */
    Mb1200 selectByPrimaryKey(Mb1200Key key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mb1200
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") Mb1200 record, @Param("example") Mb1200Example example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mb1200
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") Mb1200 record, @Param("example") Mb1200Example example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mb1200
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(Mb1200 record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mb1200
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(Mb1200 record);

    /**
     * 注意：必须在example执行过<code>setNeedFoundRows(true);</code>才可使用此方法
     * 此方法必须与select语句在同一session内(可理解为同一service方法内)执行
     */
    int countLastSelect();
}