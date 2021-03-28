package cn.com.bbut.iy.itemmaster.service.cm9010;


import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.entity.AuditTypeExample;
import cn.com.bbut.iy.itemmaster.entity.cm9010.Cm9010;

import java.util.List;

public interface Cm9010Service {
    /**
     * cm9010共通查询
     *
     */
    List<Cm9010> selectByCodeType(String codeType);

    /**
     * cm9010共通查询
     *
     */
    List<Cm9010> getList(String codeValue);
    List<AutoCompleteDTO> getReason(String v);
    List<AutoCompleteDTO> getWriteOffReasonValue(String v);

    List<AutoCompleteDTO> getDiffReason(String v);

    List<AutoCompleteDTO> getReturnDifferReason(String v);

    List<AutoCompleteDTO> getReceiptDifferReason(String v);


    List<AutoCompleteDTO> getAllReason(String v);
}
