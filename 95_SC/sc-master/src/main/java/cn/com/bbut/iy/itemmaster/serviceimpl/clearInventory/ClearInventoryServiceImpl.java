package cn.com.bbut.iy.itemmaster.serviceimpl.clearInventory;

import cn.com.bbut.iy.itemmaster.dao.StocktakeEntryMapper;
import cn.com.bbut.iy.itemmaster.dao.clearInventory.ClearInventoryMapper;
import cn.com.bbut.iy.itemmaster.dto.AjaxResultDto;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.base.ReturnDTO;
import cn.com.bbut.iy.itemmaster.dto.clearInventory.ClearInventoryDTO;
import cn.com.bbut.iy.itemmaster.dto.clearInventory.ClearInventoryParamDTO;
import cn.com.bbut.iy.itemmaster.dto.pi0100.PI0100DTO;
import cn.com.bbut.iy.itemmaster.dto.pi0100.PI0100ParamDTO;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.service.CM9060Service;
import cn.com.bbut.iy.itemmaster.service.clearInventory.ClearInventoryService;
import cn.com.bbut.iy.itemmaster.util.ExcelBaseUtil;
import com.google.gson.Gson;
import freemarker.template.utility.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class ClearInventoryServiceImpl implements ClearInventoryService {

    @Autowired
    private CM9060Service cm9060Service;
    @Autowired
    private StocktakeEntryMapper stocktakeEntryMapper;
    @Autowired
    private ClearInventoryMapper clearInventoryMapper;

    /**
     * 上传商品
     */
    @Override
    public String insertImportExcel(User user, MultipartFile file, HttpServletRequest request, HttpSession session) {
        Gson gson = new Gson();
        // 获取文件路径
        AjaxResultDto _extjsFormResult = new AjaxResultDto();

        if (user == null) {
            _extjsFormResult.setMessage("Session time out!");
            _extjsFormResult.setSuccess(false);
            return gson.toJson(_extjsFormResult);
        }

        // 上传失败, 文件为空
        if (file.getSize() < 0) {
            _extjsFormResult.setMessage("No data found!");
            _extjsFormResult.setSuccess(false);
            return gson.toJson(_extjsFormResult);
        }

        // 上传文件大小不能超过5M！
        if (file.getSize() > 5242880) {
            _extjsFormResult.setMessage("Uploaded file size cannot exceed 5M!");
            _extjsFormResult.setSuccess(false);
            return gson.toJson(_extjsFormResult);
        }

        // 上传文件格式错误！
        if (!ExcelBaseUtil.isExcel(file)) {
            _extjsFormResult.setMessage("Upload file format error!");
            _extjsFormResult.setSuccess(false);
            return gson.toJson(_extjsFormResult);
        }

        String businessDate = cm9060Service.getValByKey("0000");

        try {

            // 读取文件里的商品数据, 并补充数量
            List<ClearInventoryDTO> itemList = importExcel(file,businessDate);

            if (itemList == null || itemList.size() < 1) {
                _extjsFormResult.setMessage("No data found!");
                _extjsFormResult.setSuccess(false);
                return gson.toJson(_extjsFormResult);
            }
            String format = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());
            itemList.stream().forEach(item->{
                item.setCreateUserId(user.getUserId());
                item.setCreateYmd(format.split("-")[0]);
                item.setCreateHms(format.split("-")[1]);
                item.setUpdateUserId(user.getUserId());
                item.setUpdateYmd(format.split("-")[0]);
                item.setUpdateHms(format.split("-")[1]);
            });

            // 删除冲突得数据
            clearInventoryMapper.deleteByKey(itemList,businessDate);
            // 将数据保存进DB
            clearInventoryMapper.insertToDB(itemList);

            _extjsFormResult.setMessage("success");
            _extjsFormResult.setSuccess(true);
            _extjsFormResult.setData(itemList);
            return gson.toJson(_extjsFormResult);
        } catch (Exception e) {
            e.printStackTrace();
            // 上传失败
            _extjsFormResult.setMessage("Upload failed!");
            _extjsFormResult.setSuccess(false);
            return gson.toJson(_extjsFormResult);
        }
    }

    /**
     * 查询记录
     */
    @Override
    public GridDataDTO<ClearInventoryDTO> inquire(User user, String searchJson, int page, int rows, HttpServletRequest request, HttpSession session) {
        String businessDate = cm9060Service.getValByKey("0000");

        Gson gson = new Gson();
        ClearInventoryParamDTO param = new ClearInventoryParamDTO();

        if (!StringUtils.isEmpty(searchJson)) {
            param = gson.fromJson(searchJson, ClearInventoryParamDTO.class);
        }

        param.setPage(page);
        param.setRows(rows);
        param.setLimitStart((page - 1)*rows);

        int count = clearInventoryMapper.searchCount(param,businessDate);

        if (count < 1) {
            return new GridDataDTO<ClearInventoryDTO>();
        }

        List<ClearInventoryDTO> _list = clearInventoryMapper.search(param, businessDate);

        GridDataDTO<ClearInventoryDTO> data = new GridDataDTO<ClearInventoryDTO>(_list, param.getPage(), count, param.getRows());

        return data;
    }

    /**
     * 获取商品List
     */
    @Override
    public List<AutoCompleteDTO> getItemList(String v) {
        String bsDate = cm9060Service.getValByKey("0000");
        return clearInventoryMapper.getItemList(v,bsDate);
    }

    /**
     * 获得商品详细信息
     */
    @Override
    public ReturnDTO getItemInfo(String articleId, HttpServletRequest request, HttpSession session) {
        if (StringUtils.isEmpty(articleId)) {
            return new ReturnDTO(false,"Parameter exception!");
        }

        String bsDate = cm9060Service.getValByKey("0000");

        ClearInventoryDTO dto = clearInventoryMapper.getArticle(articleId,bsDate);

        if (dto == null) {
            return new ReturnDTO(false,"No data found!");
        }

        return new ReturnDTO(true,"success",dto);
    }

    /**
     * 添加商品
     */
    @Override
    public ReturnDTO insert(ClearInventoryDTO dto, User user, HttpServletRequest request, HttpSession session) {
        if (user == null) {
            return new ReturnDTO(false,"Session time out!");
        }
        if (dto == null) {
            return new ReturnDTO(false,"Parameter exception!");
        }
        String accDate = cm9060Service.getValByKey("0000");
        String format = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());
        // 补充业务日期, 创建人,...
        dto.setCreateUserId(user.getUserId());
        dto.setAccDate(accDate);
        dto.setCreateYmd(format.split("-")[0]);
        dto.setCreateHms(format.split("-")[1]);

        // 设置主键
        ClearInventoryParamDTO param = new ClearInventoryParamDTO();
        param.setAccDate(accDate);
        param.setArticleId(dto.getArticleId());
        // 如果存在, 不可添加
        int count = clearInventoryMapper.searchCount(param,accDate);

        if (count > 0) {
            return new ReturnDTO(false,"The selected item( "+dto.getArticleId()+" ) already exists, please do not add it repeatedly!");
        }
        // 执行添加
        clearInventoryMapper.insert(dto);

        return new ReturnDTO(true,"Data saved successfully!");
    }


    /**
     * 导入文件中的商品
     */
    public List<ClearInventoryDTO> importExcel(MultipartFile file,String businessDate) {
        List<ClearInventoryDTO> list = new ArrayList<ClearInventoryDTO>();
        Workbook workbook = ExcelBaseUtil.createWorkbook(file);

        if (workbook == null) {
            return list;
        }
        // 获取工作表
        Sheet sheet = workbook.getSheetAt(0);
        // 获取sheet中第一行行号
        int firstRowNum = sheet.getFirstRowNum();
        // 获取sheet中最后一行行号
        int lastRowNum = sheet.getLastRowNum();
        // 设置起始行
        int curRow = 2;

        ClearInventoryDTO item = null;
        // 遍历每一行
        one:
        for (int i = curRow; i < lastRowNum; i++) {
            // 取得每一行
            Row row = sheet.getRow(curRow++);
            int curCol = 0;

            // 封装返回对象
            item = new ClearInventoryDTO();

            // 商品id
            item.setArticleId(ExcelBaseUtil.getVal(row.getCell(curCol++)));
            // 跳过重复的商品id
            for (ClearInventoryDTO newItem : list)
                if (item.getArticleId().equals(newItem.getArticleId()))
                    continue one;

            list.add(item);
        }
        // 将数据存入临时表, 补齐信息
        list = this.insertTempTable(list, businessDate);

        return list;
    }

    /**
     * 利用临时表, 补全商品信息
     */
    @Transactional
    public List<ClearInventoryDTO> insertTempTable(List<ClearInventoryDTO> oldList, String businessDate) {

        if (oldList == null || oldList.size() < 1)
            return new ArrayList<ClearInventoryDTO>();

        String tempTableName = "temp_clear_inventory_table";
        // 创建前先删除
        stocktakeEntryMapper.deleteTempTable(tempTableName);
        // 批量保存数据到临时表
        int totalSize = oldList.size(); //总记录数
        int pageSize = 1000; //每页N条
        int totalPage = totalSize / pageSize; //共N页

        if (totalSize % pageSize != 0) {
            totalPage += 1;
            if (totalSize < pageSize) {
                pageSize = oldList.size();
            }
        }

        for (int pageNum = 1; pageNum < totalPage + 1; pageNum++) {
            int starNum = (pageNum - 1) * pageSize;
            int endNum = pageNum * pageSize > totalSize ? (totalSize) : pageNum * pageSize;

            // 用来存入新的集合
            List<ClearInventoryDTO> newList = new ArrayList<ClearInventoryDTO>();
            for (int i = starNum; i < endNum; i++) {
                newList.add(oldList.get(i));
            }
            // 添加数据进临时表
            clearInventoryMapper.insertTempTable(tempTableName, newList);
        }

        // 根据临时表, 补充商品信息
        List<ClearInventoryDTO> newList = clearInventoryMapper.getItemInfo(tempTableName, businessDate);

        if (newList == null) {
            newList = new ArrayList<ClearInventoryDTO>();
        }
        return newList;
    }
}
