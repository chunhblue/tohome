package cn.com.bbut.iy.itemmaster.serviceimpl;

import cn.com.bbut.iy.itemmaster.dao.MA0020DTOMapper;
import cn.com.bbut.iy.itemmaster.dao.MA4160DTOMapper;
//import cn.com.bbut.iy.itemmaster.dto.initVendorReceiptDaily.MA0020DTO;
import cn.com.bbut.iy.itemmaster.dto.ma0020.MA0020DTO;
import cn.com.bbut.iy.itemmaster.dto.store.MA4160DTO;
import cn.com.bbut.iy.itemmaster.entity.ma0020.MA0020CExample;
import cn.com.bbut.iy.itemmaster.service.ma4160refer.Ma4160referService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName Ma4160referServiceImpl
 * @Description TODO
 * @Author Administrator
 * @Date 2020/3/11 15:01
 * @Version 1.0
 */

@Service
public class Ma4160referServiceImpl implements Ma4160referService {
 @Autowired
 private MA4160DTOMapper ma4160DTOMapper;
 @Autowired
 private MA0020DTOMapper ma0020DTOMapper;

    @Override
    public List<MA4160DTO> getLJobTye() {

        return  ma4160DTOMapper.selectAllma4160();

    }

    @Override
    public List<MA0020DTO> getStructName() {
        MA0020CExample ma0020CExample = new MA0020CExample();

        List<MA0020DTO> ma0020DTOS = ma0020DTOMapper.selectByExample(ma0020CExample);
        ArrayList<MA0020DTO> ma0020DtoDetail = new ArrayList<MA0020DTO>();
        for (MA0020DTO  ma0020:ma0020DTOS) {
            if (ma0020.getStructureName().equals("Nationwide") || ma0020.getStructureName().equals("VN North")||ma0020.getStructureName().equals("VN South")) {
                ma0020DtoDetail.add(ma0020);
            }
        }
        return ma0020DtoDetail;
    }


}
