package cn.com.bbut.iy.itemmaster.dao.gen;

import cn.com.bbut.iy.itemmaster.entity.Mb1700;
import cn.com.bbut.iy.itemmaster.entity.Mb1700Example;
import cn.com.bbut.iy.itemmaster.entity.Mb1700Key;
import java.util.List;
import org.apache.ibatis.annotations.Param;


public interface Mb1700GenMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mb1700
     *
     * @mbg.generated
     */
    long countByExample(Mb1700Example example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mb1700
     *
     * @mbg.generated
     */
    int deleteByExample(Mb1700Example example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mb1700
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Mb1700Key key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mb1700
     *
     * @mbg.generated
     */
    int insert(Mb1700 record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mb1700
     *
     * @mbg.generated
     */
    int insertSelective(Mb1700 record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mb1700
     *
     * @mbg.generated
     */
    List<Mb1700> selectByExample(Mb1700Example example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mb1700
     *
     * @mbg.generated
     */
    Mb1700 selectByPrimaryKey(Mb1700Key key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mb1700
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") Mb1700 record, @Param("example") Mb1700Example example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mb1700
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") Mb1700 record, @Param("example") Mb1700Example example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mb1700
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(Mb1700 record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mb1700
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(Mb1700 record);

    /**
     * 注意：必须在example执行过<code>setNeedFoundRows(true);</code>才可使用此方法
     * 此方法必须与select语句在同一session内(可理解为同一service方法内)执行
     */
    int countLastSelect();
}