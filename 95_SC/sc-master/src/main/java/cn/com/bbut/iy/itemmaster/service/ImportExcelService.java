package cn.com.bbut.iy.itemmaster.service;

import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.entity.base.Ma1105;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface  ImportExcelService {
    /**
     * 获取导入的Excel表中数据
     *
     * @param file 文件
     * @param req
     * @param resp
     * @return 返回集合
     */
    List<Ma1105> importExcelWithSimple(MultipartFile file, String storeCd, User user, HttpServletRequest req, HttpServletResponse resp);

    }
