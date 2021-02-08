package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dto.mySubmissions.MySubmissionsGridDTO;
import cn.com.bbut.iy.itemmaster.dto.mySubmissions.MySubmissionsParamDTO;

import java.util.List;

public interface MySubmissionsMapper{

    List<MySubmissionsGridDTO> getShortcutList(MySubmissionsParamDTO subParam);

    int countSubmissions(MySubmissionsParamDTO subParam);
}