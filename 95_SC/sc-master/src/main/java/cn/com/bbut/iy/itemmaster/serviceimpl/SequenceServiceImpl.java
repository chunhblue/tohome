package cn.com.bbut.iy.itemmaster.serviceimpl;

import cn.com.bbut.iy.itemmaster.dao.SequenceMapper;
import cn.com.bbut.iy.itemmaster.service.Ma4320Service;
import cn.com.bbut.iy.itemmaster.service.SequenceService;
import cn.com.bbut.iy.itemmaster.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class SequenceServiceImpl implements SequenceService {
    @Autowired
    private SequenceMapper sequenceMapper;
    @Autowired
    private Ma4320Service ma4320Service;
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
        String nowDate = ma4320Service.getNowDate();
        String ymd = nowDate.substring(0,8);
        sb.append(ymd).append(seq).append(storeCd);
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
            sequenceMapper.resetSeq(seqName);
        }
    }

    /**
     * 重置序列
     * @param seqName
     */
    @Override
    public void updateResetSeqSeq(String seqName) {
        sequenceMapper.resetSeq(seqName);
    }
}
