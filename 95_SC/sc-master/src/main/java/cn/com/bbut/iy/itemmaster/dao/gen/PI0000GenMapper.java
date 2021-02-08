package cn.com.bbut.iy.itemmaster.dao.gen;

import cn.com.bbut.iy.itemmaster.entity.pi0000.PI0000;
import cn.com.bbut.iy.itemmaster.entity.pi0000.PI0000Example;
import cn.com.bbut.iy.itemmaster.entity.pi0000.PI0000Key;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PI0000GenMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pi0000
     *
     * @mbggenerated
     */
    int countByExample(PI0000Example example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pi0000
     *
     * @mbggenerated
     */
    int deleteByExample(PI0000Example example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pi0000
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(PI0000Key key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pi0000
     *
     * @mbggenerated
     */
    int insert(PI0000 record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pi0000
     *
     * @mbggenerated
     */
    int insertSelective(PI0000 record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pi0000
     *
     * @mbggenerated
     */
    List<PI0000> selectByExample(PI0000Example example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pi0000
     *
     * @mbggenerated
     */
    PI0000 selectByPrimaryKey(PI0000Key key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pi0000
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") PI0000 record, @Param("example") PI0000Example example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pi0000
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") PI0000 record, @Param("example") PI0000Example example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pi0000
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(PI0000 record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pi0000
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(PI0000 record);

    /**
     * 注意：必须在example执行过<code>setNeedFoundRows(true);</code>才可使用此方法
     * 此方法必须与select语句在同一session内(可理解为同一service方法内)执行
     */
    int countLastSelect();
}