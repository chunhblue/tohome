package cn.com.bbut.iy.itemmaster.dao.remoteUpdate;

import cn.com.bbut.iy.itemmaster.dto.ma0020.MA0020DTO;
import cn.com.bbut.iy.itemmaster.dto.remoteUpdate.RemoteUpdateCheckStoresDto;
import cn.com.bbut.iy.itemmaster.dto.remoteUpdate.RemoteUpdateDto;
import cn.com.bbut.iy.itemmaster.dto.remoteUpdate.RemoteUpdateSaveDto;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
public interface RemoteUpdateMapper {
    List<RemoteUpdateDto> getList(RemoteUpdateDto remoteUpdateEntry);

    Integer getListCount(RemoteUpdateDto remoteUpdateEntry);

    Collection<MA0020DTO> getAllStores(Integer id);

    List<RemoteUpdateDto> checkStoresCanAdd(RemoteUpdateCheckStoresDto updateCheckStoresDto);

    Integer selectMaxId();

    int insertAll(RemoteUpdateSaveDto remoteUpdateSaveDto);

    RemoteUpdateSaveDto selectRemoteUpdate(RemoteUpdateSaveDto select);

    int deleteById(Integer id);
}
