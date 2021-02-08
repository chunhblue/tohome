        package cn.com.bbut.iy.itemmaster.dao;
        import cn.com.bbut.iy.itemmaster.dao.gen.OD0010CGenMapper;
        import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
        import cn.com.bbut.iy.itemmaster.dto.od0010.od0010DTO;
        import cn.com.bbut.iy.itemmaster.dto.od0010.od0010ParamDTO;
        import cn.com.bbut.iy.itemmaster.entity.OD0010C;
        import org.apache.ibatis.annotations.Param;
        import org.springframework.stereotype.Component;

        import java.util.List;
@Component
public interface OD0010CMapper extends OD0010CGenMapper {
    // 查询在条件区间的od0010
    List<od0010DTO> selectOrderByCondition(@Param("startDate") String startDate, @Param("endDate") String endDate, od0010DTO od0010dTO);

    List<od0010DTO> selectOrderByConditionAll(@Param("startDate") String startDate, @Param("endDate") String endDate, od0010DTO od0010dTO, @Param("optionTime") String optionTime);

    List<od0010DTO> selectAll();
    List<od0010DTO> getdirectOrderInformation(@Param("param")od0010ParamDTO param);
    int getdirectOrderInformationCount(@Param("param")od0010ParamDTO param);
    int getcdOrderInformationCount(@Param("param")od0010ParamDTO param);
    List<od0010DTO> getOrderInformation(@Param("param")od0010ParamDTO param);
    List<od0010DTO> getDcOrderInformation(@Param("param")od0010ParamDTO param);

    int getOrderInformationCount(@Param("param")od0010ParamDTO param);

    List<od0010DTO> getOrderInformation1(@Param("param")od0010ParamDTO param);

    List<od0010DTO> getSupplierOrderPrintInformation(@Param("param")od0010ParamDTO param);

    List<od0010DTO> getDCOrderPrintInformation(@Param("param")od0010ParamDTO param);


    // 在使用
    List<od0010DTO> getcdOrderInformation(@Param("param")od0010ParamDTO param);
    List<OD0010C> getOrderDetailList(@Param("param")od0010ParamDTO param);
       int getOrderDetailListCount(@Param("param")od0010ParamDTO param);

    /**
     * 检索供应商
     * @param v
     */
    List<AutoCompleteDTO> selectList(@Param("businessDate") String businessDate, @Param("v") String v);
}