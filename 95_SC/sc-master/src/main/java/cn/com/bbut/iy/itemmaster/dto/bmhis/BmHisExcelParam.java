package cn.com.bbut.iy.itemmaster.dto.bmhis;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import cn.com.bbut.iy.itemmaster.excel.BaseExcelParam;

/**
 * BM excel传参用DTO
 * 
 * @author songxz
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class BmHisExcelParam extends BaseExcelParam {
    // 检索参数
    private BmHisParamDTO param;
    // 权限
    private String pCode;
    // 当前登录人角色is集合
    private List<Integer> roleIds;
}
