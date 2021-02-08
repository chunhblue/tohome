package cn.com.bbut.iy.itemmaster.dao.gen;

import cn.com.bbut.iy.itemmaster.entity.MB1000;
import cn.com.bbut.iy.itemmaster.entity.MB1000Example;
import cn.com.bbut.iy.itemmaster.entity.MB1000Key;
import java.util.List;
import org.apache.ibatis.annotations.Param;


public interface MB1000GenMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mb1000
     *
     * @mbg.generated
     */
    long countByExample(MB1000Example example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mb1000
     *
     * @mbg.generated
     */
    int deleteByExample(MB1000Example example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mb1000
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(MB1000Key key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mb1000
     *
     * @mbg.generated
     */
    int insert(MB1000 record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mb1000
     *
     * @mbg.generated
     */
    int insertSelective(MB1000 record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mb1000
     *
     * @mbg.generated
     */
    List<MB1000> selectByExample(MB1000Example example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mb1000
     *
     * @mbg.generated
     */
    MB1000 selectByPrimaryKey(MB1000Key key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mb1000
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") MB1000 record, @Param("example") MB1000Example example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mb1000
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") MB1000 record, @Param("example") MB1000Example example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mb1000
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(MB1000 record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mb1000
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(MB1000 record);

    /**
     * 注意：必须在example执行过<code>setNeedFoundRows(true);</code>才可使用此方法
     * 此方法必须与select语句在同一session内(可理解为同一service方法内)执行
     */
    int countLastSelect();
}