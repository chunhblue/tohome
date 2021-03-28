package cn.com.bbut.iy.itemmaster.dao.viettelPhoneMapper;

        import cn.com.bbut.iy.itemmaster.dto.viettelParamPhone.ma8407Paramdto;
        import cn.com.bbut.iy.itemmaster.dto.viettelPhone.ma8407dto;
        import org.apache.ibatis.annotations.Mapper;
        import org.apache.ibatis.annotations.Param;
        import org.springframework.stereotype.Component;

        import java.util.List;

@Mapper
@Component
public interface ViettelPhoneMapper {
    List<ma8407dto> searchData(@Param("param") ma8407Paramdto param);

    int searchCount(@Param("param")ma8407Paramdto param);

    List<ma8407dto> searchDataExport(@Param("param") ma8407Paramdto param);
}
