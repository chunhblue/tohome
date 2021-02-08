package cn.com.bbut.iy.itemmaster.dao.gen;

import java.util.List;

import cn.com.bbut.iy.itemmaster.dto.ma0020.MA0020DTO;
import cn.com.bbut.iy.itemmaster.entity.ma0020.MA0020C;
import cn.com.bbut.iy.itemmaster.entity.ma0020.MA0020CExample;
import cn.com.bbut.iy.itemmaster.entity.ma0020.MA0020CKey;
import org.apache.ibatis.annotations.Param;

public interface MA0020GenMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ma0020
     *
     * @mbggenerated
     */
    int countByExample(MA0020CExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ma0020
     *
     * @mbggenerated
     */
    int deleteByExample(MA0020CExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ma0020
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(MA0020CKey key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ma0020
     *
     * @mbggenerated
     */
    int insert(MA0020C record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ma0020
     *
     * @mbggenerated
     */
    int insertSelective(MA0020C record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ma0020
     *
     * @mbggenerated
     */
    List<MA0020DTO> selectByExample(MA0020CExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ma0020
     *
     * @mbggenerated
     */
    MA0020C selectByPrimaryKey(MA0020CKey key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ma0020
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") MA0020C record, @Param("example") MA0020CExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ma0020
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") MA0020C record, @Param("example") MA0020CExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ma0020
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(MA0020C record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ma0020
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(MA0020C record);

    /**
     * 注意：必须在example执行过<code>setNeedFoundRows(true);</code>才可使用此方法
     * 此方法必须与select语句在同一session内(可理解为同一service方法内)执行
     */
    int countLastSelect();
}