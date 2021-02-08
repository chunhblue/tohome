package cn.com.bbut.iy.itemmaster.dao.gen;

import cn.com.bbut.iy.itemmaster.entity.SK0010;
import cn.com.bbut.iy.itemmaster.entity.SK0010Example;
import cn.com.bbut.iy.itemmaster.entity.SK0010Key;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SK0010GenMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sk0010
     *
     * @mbggenerated
     */
    int countByExample(SK0010Example example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sk0010
     *
     * @mbggenerated
     */
    int deleteByExample(SK0010Example example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sk0010
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(SK0010Key key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sk0010
     *
     * @mbggenerated
     */
    int insert(SK0010 record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sk0010
     *
     * @mbggenerated
     */
    int insertSelective(SK0010 record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sk0010
     *
     * @mbggenerated
     */
    List<SK0010> selectByExample(SK0010Example example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sk0010
     *
     * @mbggenerated
     */
    SK0010 selectByPrimaryKey(SK0010Key key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sk0010
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") SK0010 record, @Param("example") SK0010Example example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sk0010
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") SK0010 record, @Param("example") SK0010Example example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sk0010
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(SK0010 record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sk0010
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(SK0010 record);

    /**
     * 注意：必须在example执行过<code>setNeedFoundRows(true);</code>才可使用此方法
     * 此方法必须与select语句在同一session内(可理解为同一service方法内)执行
     */
    int countLastSelect();
}