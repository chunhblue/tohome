package cn.com.bbut.iy.itemmaster.service.ma1105;

import cn.com.bbut.iy.itemmaster.dto.base.CommonDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.bm.BmListGridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.ma1105.Ma1105ParamDTO;
import cn.com.bbut.iy.itemmaster.entity.base.Ma1000;
import cn.com.bbut.iy.itemmaster.entity.base.Ma1105;
import cn.com.bbut.iy.itemmaster.entity.base.Ma1105Example;

import java.util.List;

public interface Ma1105Service {
    GridDataDTO<Ma1105> getData(Ma1105ParamDTO param);

    List<String> selectMa1105(List<Ma1105> list);

    int insertData(List<Ma1105Example> examples);

    // 保存文件名字等信息
    int insertFileData(List<Ma1105Example> examples);

    List<String> getShelf(String storeCd);

    List<String> getSubShelf(String storeCd,String shelf);

    Ma1105 getStoreInfo(String storeCd);

    int countPogName(String excelName,String storeCd);

    int isCovergePogNamePrefix(String storeCd,String excelNamePrefix);
}
