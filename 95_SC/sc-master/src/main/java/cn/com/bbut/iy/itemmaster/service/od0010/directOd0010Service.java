package cn.com.bbut.iy.itemmaster.service.od0010;

        import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
        import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
        import cn.com.bbut.iy.itemmaster.dto.od0010.od0010DTO;
        import cn.com.bbut.iy.itemmaster.dto.od0010.od0010ParamDTO;
        import cn.com.bbut.iy.itemmaster.dto.od0010.od0010viewDTO;
        import cn.com.bbut.iy.itemmaster.entity.OD0010C;

        import java.util.List;

public interface directOd0010Service {
    //GridDataDTO<od0010DTO> getListByCondition(od0010ParamDTO od0010paramdto);

    //GridDataDTO<od0010DTO> getAllOrderInfo(od0010ParamDTO od0010paramdto);

    // 订货查询
    GridDataDTO<od0010DTO> getOrderInformation(od0010ParamDTO param);

    // 供应商订货查询打印
    List<od0010DTO> getSupplierPrintData(od0010ParamDTO param);

    // 仓库订货查询打印
    List<od0010DTO> getDCPrintData(od0010ParamDTO param);

    GridDataDTO<od0010DTO> getdirectOrderInformation(od0010ParamDTO param);

    GridDataDTO<od0010DTO> getCdOrderInformation(od0010ParamDTO param);

    List<OD0010C> getItemsByOrder(od0010ParamDTO param);

    int getOrderDetailListCount(od0010ParamDTO param);

    // 检索供应商
    List<AutoCompleteDTO> getList(String v);
}
