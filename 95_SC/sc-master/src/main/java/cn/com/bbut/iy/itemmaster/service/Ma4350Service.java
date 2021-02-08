package cn.com.bbut.iy.itemmaster.service;


import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.ma4350.Ma4350DetailGridDto;
import cn.com.bbut.iy.itemmaster.dto.ma4350.Ma4350DetailParamDto;

public interface Ma4350Service {
    /**
     * 配方销售头档一览
     * @param param
     * @return
     */
    GridDataDTO<Ma4350DetailGridDto> getList(Ma4350DetailParamDto param);
    /**
     * 获取配方销售头档信息
     * @param voucherCd
     * @return
     */
    Ma4350DetailGridDto getMa4350ByVoucherCd(String voucherCd);
}
