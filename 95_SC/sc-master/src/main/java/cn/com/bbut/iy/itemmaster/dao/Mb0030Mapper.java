package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dao.gen.Mb0030GenMapper;
import cn.com.bbut.iy.itemmaster.entity.Mb0030;
import org.springframework.stereotype.Component;

@Component
public interface Mb0030Mapper extends Mb0030GenMapper {

    Mb0030 selectFile(String excelGroupCd);

    int countExcelGroupCd(String excelGroupCd);

    int delFile(String filePath);
}