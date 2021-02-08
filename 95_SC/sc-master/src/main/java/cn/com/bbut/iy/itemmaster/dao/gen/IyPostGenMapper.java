package cn.com.bbut.iy.itemmaster.dao.gen;

import cn.com.bbut.iy.itemmaster.entity.base.IyPost;
import cn.com.bbut.iy.itemmaster.entity.base.IyPostExample;
import cn.com.bbut.iy.itemmaster.entity.base.IyPostKey;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface IyPostGenMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table IY_POST
     *
     * @mbg.generated
     */
    long countByExample(IyPostExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table IY_POST
     *
     * @mbg.generated
     */
    int deleteByExample(IyPostExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table IY_POST
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(IyPostKey key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table IY_POST
     *
     * @mbg.generated
     */
    int insert(IyPost record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table IY_POST
     *
     * @mbg.generated
     */
    int insertSelective(IyPost record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table IY_POST
     *
     * @mbg.generated
     */
    List<IyPost> selectByExample(IyPostExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table IY_POST
     *
     * @mbg.generated
     */
    IyPost selectByPrimaryKey(IyPostKey key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table IY_POST
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") IyPost record, @Param("example") IyPostExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table IY_POST
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") IyPost record, @Param("example") IyPostExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table IY_POST
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(IyPost record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table IY_POST
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(IyPost record);

    /**
     * 注意：必须在example执行过<code>setNeedFoundRows(true);</code>才可使用此方法
     * 此方法必须与select语句在同一session内(可理解为同一service方法内)执行
     */
    int countLastSelect();
}