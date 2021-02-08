package cn.com.bbut.iy.itemmaster.service.reconciliationMng;

import cn.com.bbut.iy.itemmaster.dto.ReconciliationMng.ReconciliationMngDto;
import cn.com.bbut.iy.itemmaster.dto.ReconciliationMng.ReconciliationMngParamDto;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.entity.Mb0030;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.List;

public interface ReconciliationMngService {

    GridDataDTO<ReconciliationMngDto> getByTypeCondition(ReconciliationMngParamDto reParam);

    String insert(ReconciliationMngDto recon);

    /**
     *
     * @param v
     * @return
     */
    List<AutoCompleteDTO> getMb0010(String v);

    /**
     * 获取Store Group信息
     * @return
     */
    List<AutoCompleteDTO> getMb0020(String documentReconCd,String v);

    List<Mb0030> getMb0030List(String recordCd);

    Mb0030 selectFile(String excelGroupCd);

    int countExcelGroupCd(String excelGroupCd);

    int delFile(String filePath);

    int insertMb1700(MultipartFile file,String userId);

    int insertExcelToMb1200(String userId, MultipartFile file);

    int insertExcelTomb1300(MultipartFile file,String userId);
}
