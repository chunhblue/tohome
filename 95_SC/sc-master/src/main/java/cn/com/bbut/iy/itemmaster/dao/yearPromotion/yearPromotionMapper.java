package cn.com.bbut.iy.itemmaster.dao.yearPromotion;


        import cn.com.bbut.iy.itemmaster.dto.yearendpromotion.yearEndPromotionDto;
        import cn.com.bbut.iy.itemmaster.dto.yearendpromotion.yearEndPromotionParamDto;
        import org.apache.ibatis.annotations.Mapper;
        import org.apache.ibatis.annotations.Param;
        import org.springframework.stereotype.Component;

        import java.util.List;

@Mapper
@Component
public interface yearPromotionMapper {

    List<yearEndPromotionDto> search(@Param("paramDto") yearEndPromotionParamDto paramDto);


    Integer searchDataCount(@Param("paramDto") yearEndPromotionParamDto param);
}
