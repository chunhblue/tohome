package cn.com.bbut.iy.itemmaster.dao.gen;

import cn.com.bbut.iy.itemmaster.entity.IyBm;
import cn.com.bbut.iy.itemmaster.entity.IyBmExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface IyBmGenMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table IY_BM
     *
     * @mbg.generated
     */
    long countByExample(IyBmExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table IY_BM
     *
     * @mbg.generated
     */
    int deleteByExample(IyBmExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table IY_BM
     *
     * @mbg.generated
     */
    int insert(IyBm record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table IY_BM
     *
     * @mbg.generated
     */
    int insertSelective(IyBm record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table IY_BM
     *
     * @mbg.generated
     */
    List<IyBm> selectByExample(IyBmExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table IY_BM
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") IyBm record, @Param("example") IyBmExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table IY_BM
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") IyBm record, @Param("example") IyBmExample example);

    /**
     * 注意：必须在example执行过<code>setNeedFoundRows(true);</code>才可使用此方法
     * 此方法必须与select语句在同一session内(可理解为同一service方法内)执行
     */
    int countLastSelect();
}