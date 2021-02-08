package cn.com.bbut.iy.itemmaster.dao;


import org.apache.ibatis.annotations.Param;

public interface SequenceMapper{
    /**
     * 获取最新序列
     * @param sequenceName 序列名称
     * @return
     */
    String getSequence(@Param("sequenceName") String sequenceName);

    /**
     * 修改序列当前的值
     * @param seqName
     * @param seq
     */
    void updateSeqNum(@Param("sequenceName")String seqName, @Param("seq")Integer seq);

    void resetSeq(@Param("sequenceName")String seqName);
}