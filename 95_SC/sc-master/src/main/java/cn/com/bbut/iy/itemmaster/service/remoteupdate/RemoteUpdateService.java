package cn.com.bbut.iy.itemmaster.service.remoteupdate;


import cn.com.bbut.iy.itemmaster.dto.AjaxResultDto;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.base.Node;
import cn.com.bbut.iy.itemmaster.dto.remoteUpdate.RemoteUpdateCheckStoresDto;
import cn.com.bbut.iy.itemmaster.dto.remoteUpdate.RemoteUpdateDto;
import cn.com.bbut.iy.itemmaster.dto.remoteUpdate.RemoteUpdateSaveDto;

import java.util.List;

public interface RemoteUpdateService {

    GridDataDTO<RemoteUpdateDto> getList(RemoteUpdateDto remoteUpdateEntry);

    List<Node> getStoreNodeList(Integer id);

    List<RemoteUpdateDto> checkStoresCanAdd(RemoteUpdateCheckStoresDto updateCheckStoresDto);

    AjaxResultDto insertRemodeUpdate(RemoteUpdateSaveDto remoteUpdateSaveDto);

    AjaxResultDto getRemodeUpdate(Integer id);

    AjaxResultDto updateRemodeUpdate(RemoteUpdateSaveDto remoteUpdateSaveDto);
}
