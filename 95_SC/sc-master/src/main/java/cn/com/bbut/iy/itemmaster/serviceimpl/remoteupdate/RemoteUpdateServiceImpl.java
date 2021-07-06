package cn.com.bbut.iy.itemmaster.serviceimpl.remoteupdate;

import cn.com.bbut.iy.itemmaster.dao.MA4320Mapper;
import cn.com.bbut.iy.itemmaster.dao.remoteUpdate.RemoteUpdateMapper;
import cn.com.bbut.iy.itemmaster.dto.AjaxResultDto;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.base.Node;
import cn.com.bbut.iy.itemmaster.dto.ma0020.MA0020DTO;
import cn.com.bbut.iy.itemmaster.dto.remoteUpdate.RemoteUpdateCheckStoresDto;
import cn.com.bbut.iy.itemmaster.dto.remoteUpdate.RemoteUpdateDto;
import cn.com.bbut.iy.itemmaster.dto.remoteUpdate.RemoteUpdateSaveDto;
import cn.com.bbut.iy.itemmaster.entity.MA4320;
import cn.com.bbut.iy.itemmaster.entity.MA4320Example;
import cn.com.bbut.iy.itemmaster.service.remoteupdate.RemoteUpdateService;
import cn.com.bbut.iy.itemmaster.util.FileUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
public class RemoteUpdateServiceImpl implements RemoteUpdateService {
    @Autowired
    private RemoteUpdateMapper remoteUpdateMapper;
    @Autowired
    private MA4320Mapper ma4320Mapper;
    @Value("${file.fileDir}")
    private String fileDir;//上传文件路径

    @Override
    public GridDataDTO<RemoteUpdateDto> getList(RemoteUpdateDto param) {
        List<RemoteUpdateDto> list = remoteUpdateMapper.getList(param);
        Integer count = remoteUpdateMapper.getListCount(param);
        return new GridDataDTO<>(list, param.getPage(), count,param.getRows());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResultDto insertRemodeUpdate(RemoteUpdateSaveDto remoteUpdateSaveDto) {
        AjaxResultDto rest = new AjaxResultDto();
        //获取id
        Integer id = remoteUpdateMapper.selectMaxId();
        if (id == null){
            id = 1;
        }
        remoteUpdateSaveDto.setId(id);
        remoteUpdateSaveDto.setGroupCode("group"+remoteUpdateSaveDto.getId());
        //当前项目下路径
        File file = new File("");

        // 获取文件信息
        List<MA4320> ma4320List = new Gson().fromJson(remoteUpdateSaveDto.getFileDetailJson(), new TypeToken<List<MA4320>>(){}.getType());
        List<String> resourcesFiles = new ArrayList<String>();
        for (int j = 0; j < ma4320List.size(); j++) {
            MA4320 ma4320 = ma4320List.get(j);
            ma4320.setCreateUserId(remoteUpdateSaveDto.getCreateUserId());
            ma4320.setCreateYmd(remoteUpdateSaveDto.getCreateYmd());
            ma4320.setCreateHms(remoteUpdateSaveDto.getCreateHms());
            resourcesFiles.add(fileDir + File.separator + ma4320.getFilePath()+":"+ma4320.getFileName());
        }
        StringBuilder targetPath = new StringBuilder(fileDir+ File.separator);
        targetPath.append("update_files");
        targetPath.append(File.separator);
        targetPath.append(remoteUpdateSaveDto.getStartDate());
        targetPath.append(File.separator);
        targetPath.append(remoteUpdateSaveDto.getUpdateType());
        targetPath.append(File.separator);
        targetPath.append(remoteUpdateSaveDto.getGroupCode()+".zip");
        //创建压缩文件以及删除旧文件
        try{
            FileUtil.compressedFile(resourcesFiles,targetPath.toString()
                    ,fileDir + File.separator+remoteUpdateSaveDto.getOldFtpFilePath());
        }catch (Exception ex){
            log.error("RemoteUpdateServiceImpl.saveRemodeUpdate Create Zip file failed!error:"+ex.getMessage());
            rest.setSuccess(false);
            rest.setMessage("Create Zip file failed!");
            return rest;
        }
        String uploadFtpPath = File.separator+"update_files"+File.separator+remoteUpdateSaveDto.getStartDate()
                +File.separator+remoteUpdateSaveDto.getUpdateType()+File.separator+"group"+remoteUpdateSaveDto.getId()+".zip";

        //lyz 20210607 ftp 相关改为python使用ssh获取
        /*try{
            FileUtil.uploadFileToFtp(targetPath.toString(),uploadFtpPath,remoteUpdateSaveDto.getOldFtpFilePath());
            remoteUpdateSaveDto.setFtpFilePath(uploadFtpPath);
        }catch (Exception ex){
            log.error("RemoteUpdateServiceImpl.saveRemodeUpdate Upload Ftp failed!error:"+ex.getMessage());
            rest.setSuccess(false);
            rest.setMessage("Upload Ftp failed!");
            return rest;
        }*/

        //删除1个月前的文件删除旧文件
        try{
            FileUtil.delOneMonthAgoDir(fileDir + File.separator+"update_files");
        }catch (Exception ex){
            log.error("RemoteUpdateServiceImpl.saveRemodeUpdate Delete Old Update File failed!error:"+ex.getMessage());
            rest.setSuccess(false);
            rest.setMessage("Delete Old Update File failed!");
            return rest;
        }
        remoteUpdateSaveDto.setFtpFilePath(uploadFtpPath);

        //保存文件信息
        ma4320Mapper.insertMA4320All(ma4320List);
        //保存上传信息
        remoteUpdateMapper.insertAll(remoteUpdateSaveDto);

        rest.setData(remoteUpdateSaveDto);
        rest.setSuccess(true);
        return rest;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResultDto updateRemodeUpdate(RemoteUpdateSaveDto remoteUpdateSaveDto) {
        AjaxResultDto rest = new AjaxResultDto();
        //获取id
        Integer id = remoteUpdateSaveDto.getId();
        remoteUpdateSaveDto.setId(id);
        remoteUpdateSaveDto.setGroupCode("group"+remoteUpdateSaveDto.getId());
        //当前项目下路径
        File file = new File("");
        // 获取文件信息
        List<MA4320> ma4320List = new Gson().fromJson(remoteUpdateSaveDto.getFileDetailJson(), new TypeToken<List<MA4320>>(){}.getType());
        List<String> resourcesFiles = new ArrayList<String>();
        for (int j = 0; j < ma4320List.size(); j++) {
            MA4320 ma4320 = ma4320List.get(j);
            ma4320.setCreateUserId(remoteUpdateSaveDto.getCreateUserId());
            ma4320.setCreateYmd(remoteUpdateSaveDto.getCreateYmd());
            ma4320.setCreateHms(remoteUpdateSaveDto.getCreateHms());
            resourcesFiles.add(fileDir + File.separator + ma4320.getFilePath()+":"+ma4320.getFileName());
        }
        StringBuilder targetPath = new StringBuilder(fileDir+ File.separator);
        targetPath.append("update_files");
        targetPath.append(File.separator);
        targetPath.append(remoteUpdateSaveDto.getStartDate());
        targetPath.append(File.separator);
        targetPath.append(remoteUpdateSaveDto.getUpdateType());
        targetPath.append(File.separator);
        targetPath.append(remoteUpdateSaveDto.getGroupCode()+".zip");
        //创建压缩文件以及删除旧文件
        try{
            FileUtil.compressedFile(resourcesFiles,targetPath.toString()
                    ,fileDir + remoteUpdateSaveDto.getOldFtpFilePath());
        }catch (Exception ex){
            log.error("RemoteUpdateServiceImpl.saveRemodeUpdate Create Zip file failed!error:"+ex.getMessage());
            rest.setSuccess(false);
            rest.setMessage("Create Zip file failed!");
            return rest;
        }
        String uploadFtpPath = File.separator+"update_files"+File.separator+remoteUpdateSaveDto.getStartDate()
                +File.separator+remoteUpdateSaveDto.getUpdateType()+File.separator+"group"+remoteUpdateSaveDto.getId()+".zip";

        //lyz 20210607 ftp 相关改为python使用ssh获取
        /*try{
            FileUtil.uploadFileToFtp(targetPath.toString(),uploadFtpPath,remoteUpdateSaveDto.getOldFtpFilePath());
            remoteUpdateSaveDto.setFtpFilePath(uploadFtpPath);
        }catch (Exception ex){
            log.error("RemoteUpdateServiceImpl.saveRemodeUpdate Upload Ftp failed!error:"+ex.getMessage());
            rest.setSuccess(false);
            rest.setMessage("Upload Ftp failed!");
            return rest;
        }*/
        //删除1个月前的文件
        try{
            FileUtil.delOneMonthAgoDir(fileDir + File.separator+"update_files");
        }catch (Exception ex){
            log.error("RemoteUpdateServiceImpl.saveRemodeUpdate Delete Old Update File failed!error:"+ex.getMessage());
            rest.setSuccess(false);
            rest.setMessage("Delete Old Update File failed!");
            return rest;
        }
        remoteUpdateSaveDto.setFtpFilePath(uploadFtpPath);

        //删除旧文件信息
        MA4320Example example = new MA4320Example();
        example.or().andInformCdEqualTo(remoteUpdateSaveDto.getInformCd()).andFileTypeEqualTo("09");
        ma4320Mapper.deleteByExample(example);
        //保存文件信息
        ma4320Mapper.insertMA4320All(ma4320List);
        //删除旧上传信息
        remoteUpdateMapper.deleteById(remoteUpdateSaveDto.getId());
        //保存上传信息
        remoteUpdateMapper.insertAll(remoteUpdateSaveDto);

        rest.setData(remoteUpdateSaveDto);
        rest.setSuccess(true);
        return rest;
    }

    @Override
    public AjaxResultDto getRemodeUpdate(Integer id) {
        AjaxResultDto rest = new AjaxResultDto();
        try{
            RemoteUpdateSaveDto select = new RemoteUpdateSaveDto();
            select.setId(id);
            RemoteUpdateSaveDto remoteUpdateSaveDto = remoteUpdateMapper.selectRemoteUpdate(select);
            if(remoteUpdateSaveDto != null){
                rest.setMsg("Success!");
                rest.setSuccess(true);
                rest.setO(remoteUpdateSaveDto);
            }else{
                rest.setMsg("Failed to get Remode Update InFo!");
                rest.setSuccess(false);
            }
        }catch (Exception ex){
            log.error("RemoteUpdateServiceImpl.getRemodeUpdate failed!error:"+ex.getMessage());
            rest.setSuccess(false);
            rest.setMsg("getRemodeUpdate failed!");
        }
        return rest;
    }

    @Override
    public List<Node> getStoreNodeList(Integer id) {
        Collection<MA0020DTO> ma0020DTOS = remoteUpdateMapper.getAllStores(id);
        List<Node> nodes = new ArrayList<Node>();//把所有资源转换成树模型的节点集合，此容器用于保存所有节点
        final int[] i = {0};
        ma0020DTOS.forEach(ma0020DTO -> {
            Node node = new Node();
            node.setNodeId(String.valueOf(ma0020DTO.getStructureCd()));
            node.setPid(String.valueOf(ma0020DTO.getAdminStructureCd()));
            if(ma0020DTO.getStructureLevel().equals("4")){
                Map<String,Object> state = new HashMap<String,Object>();
                state.put("checked",ma0020DTO.getIsCheck());
                node.setState(state);
                node.setIcon("glyphicon");
                node.setText(ma0020DTO.getStructureCd()+"&nbsp;&nbsp;&nbsp;&nbsp;"+ma0020DTO.getStructureName());
                node.setStructureCd(ma0020DTO.getStructureCd());
            }else {
                if(i[0] ==0){
                    node.setPid("-1");
                    i[0]++;
                }
                Map<String,Object> state = new HashMap<String,Object>();
                state.put("checked",ma0020DTO.getIsCheck());
                state.put("expanded",ma0020DTO.getExpanded());
                node.setState(state);
                node.setText(ma0020DTO.getStructureName());
                node.setStructureCd(ma0020DTO.getStructureCd());
            }
            nodes.add(node);
        });

        return nodes;
    }

    @Override
    public List<RemoteUpdateDto> checkStoresCanAdd(RemoteUpdateCheckStoresDto updateCheckStoresDto) {
        return remoteUpdateMapper.checkStoresCanAdd(updateCheckStoresDto);
    }

}
