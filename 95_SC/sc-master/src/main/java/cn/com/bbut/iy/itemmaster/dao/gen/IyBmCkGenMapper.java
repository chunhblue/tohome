package cn.com.bbut.iy.itemmaster.dao.gen;

import cn.com.bbut.iy.itemmaster.entity.IyBmCk;
import cn.com.bbut.iy.itemmaster.entity.IyBmCkExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface IyBmCkGenMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table IY_BM_CK
     *
     * @mbg.generated
     */
    long countByExample(IyBmCkExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table IY_BM_CK
     *
     * @mbg.generated
     */
    int deleteByExample(IyBmCkExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table IY_BM_CK
     *
     * @mbg.generated
     */
    int insert(IyBmCk record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table IY_BM_CK
     *
     * @mbg.generated
     */
    int insertSelective(IyBmCk record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table IY_BM_CK
     *
     * @mbg.generated
     */
    List<IyBmCk> selectByExample(IyBmCkExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table IY_BM_CK
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") IyBmCk record, @Param("example") IyBmCkExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table IY_BM_CK
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") IyBmCk record, @Param("example") IyBmCkExample example);

    /**
     * 注意：必须在example执行过<code>setNeedFoundRows(true);</code>才可使用此方法
     * 此方法必须与select语句在同一session内(可理解为同一service方法内)执行
     */
    int countLastSelect();
}