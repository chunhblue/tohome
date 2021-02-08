package cn.com.bbut.iy.itemmaster.dao;

import java.util.List;
import java.util.Map;

import cn.com.bbut.iy.itemmaster.dao.gen.IyDptGenMapper;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.LabelDTO;

public interface IyDptMapper extends IyDptGenMapper {
    /** 向店铺表取得对应的dpt 默认角色授权需要使用 **/
    List<AutoCompleteDTO> selectDptByStore(String param);

    /** 向店铺表取得对应的dpt 的名字 **/
    LabelDTO getDptNameByStore(String dpt);

    /** 向MMCBA52_STAFF表取得对应的dpt 默认角色授权需要使用 **/
    List<AutoCompleteDTO> selectDptByStaff(String param);

    /** 向MMCBA52_STAFF表取得对应的dpt 的名字 **/
    LabelDTO getDptNameByStaff(String dpt);

    /** 向iy_dpt_ten表取得对应的dpt 默认角色授权需要使用 **/
    List<AutoCompleteDTO> selectDptByTen(String param);

    /** 向iy_dpt_ten表取得对应的dpt 的名字 **/
    LabelDTO getDptNameByTen(String dpt);

    /** 向iy_dpt表取得对应的dpt 的名字 **/
    List<AutoCompleteDTO> getDptsInResource(Map<String, Object> map);

    /** 向iy_dpt表取得事业部集合，关系到该操作人的资源 **/
    List<AutoCompleteDTO> getDivisionByPrmission(List<String> list);

    /** 向iy_dpt表取得部集合，关系到该操作人的资源 **/
    List<AutoCompleteDTO> getDepartmentByPrmission(Map<String, Object> selectMap);

    /** 向iy_dpt表取得DPT集合，关系到该操作人的资源 **/
    List<AutoCompleteDTO> getDptByPrmission(Map<String, Object> selectMap);

}