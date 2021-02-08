package cn.com.bbut.iy.itemmaster.service.ma4170;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.base.ReturnDTO;
import cn.com.bbut.iy.itemmaster.dto.ma4170.Ma4170DetailGridDto;
import cn.com.bbut.iy.itemmaster.dto.ma4170.Ma4170DetailParamDto;
import cn.com.bbut.iy.itemmaster.entity.ma4170.MA4170;

public interface Ma4170Service {
    /**
     * 门店原因一览
     * @param param
     * @return
     */
    GridDataDTO<Ma4170DetailGridDto> getList(Ma4170DetailParamDto param);

    /**
     * 修改门店原因
     */
    int updateMa4170(MA4170 ma4170);

    /**
     * 添加门店原因
     */
    int insertMa4170(MA4170 ma4170);

    /**
     * 查询门店原因
     */
    MA4170 selectMa4170(MA4170 ma4170);

    /**
     * 删除门店原因
     */
    int deleteMa4170(String reasonCd);

    /**
     * 校验唯一
     */
    ReturnDTO checkUnique(String reasonCd);
}
