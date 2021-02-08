package cn.com.bbut.iy.itemmaster.serviceimpl;

import cn.com.bbut.iy.itemmaster.dao.SequenceMapper;
import cn.com.bbut.iy.itemmaster.service.SequenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class SequenceServiceImpl implements SequenceService {
    @Autowired
    private SequenceMapper sequenceMapper;

    @Override
    public String getSequence(String sequenceName) {
        return sequenceMapper.getSequence(sequenceName);
    }

    /**
     * 生成code
     * @param sequenceName 序列名称
     * @param prefix id生成规则前缀
     * @param storeCd id生成结尾店铺id
     */
    public String getSequence(String sequenceName,String prefix,String storeCd) {
        StringBuilder sb = new StringBuilder(prefix);
        String seq = sequenceMapper.getSequence(sequenceName);
        seq = String.format("%03d", Integer.parseInt(seq));
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
        sb.append(date).append(seq).append(storeCd);
        return sb.toString();
    }

    /**
     * 重置序列
     * 只能用于可以循环的序列, 更改后 值为 999, 下一次获取就为 1
     * @param arr
     */
    @Override
    public void updateSeq(String[] arr) {

        // 取出序列名称重置为0
        for (int i = 0; i < arr.length; i++) {
            String seqName = arr[i];
            String val = sequenceMapper.getSequence(seqName);
            Integer seq = Integer.parseInt(val)*(-1);
            this.updateSequence(seqName,seq);
        }
    }

    /**
     * 修改序列得状态
     * @param seqName
     * @param seq
     */
    private void updateSequence(String seqName, Integer seq) {
        // 修改序列当前值
        sequenceMapper.updateSeqNum(seqName,seq);
        // 再查一遍，走一下,重置为1了
        sequenceMapper.getSequence(seqName);
        // 还原
        sequenceMapper.resetSeq(seqName);
    }
}
