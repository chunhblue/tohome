package cn.com.bbut.iy.itemmaster.service;


public interface SequenceService {

    /**
     * 获取最新序列
     * @param sequenceName 序列名称
     * @return
     */
    String getSequence(String sequenceName);

    /**
     * 获取最新序列
     * @param sequenceName 序列名称
     * @param prefix id前缀
     * @param storeCd 店铺编号
     * @return
     */
    String getSequence(String sequenceName,String prefix,String storeCd);

    /**
     * 重置序列
     * @param arr
     */
    void updateSeq(String[] arr);
}
