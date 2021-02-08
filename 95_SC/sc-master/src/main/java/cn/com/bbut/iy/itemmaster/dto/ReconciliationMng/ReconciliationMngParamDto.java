package cn.com.bbut.iy.itemmaster.dto.ReconciliationMng;

import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;
import cn.com.bbut.iy.itemmaster.dto.base.role.ResourceViewDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class ReconciliationMngParamDto  extends GridParamDTO {
    /**
     * 检索参数json格式，前端传过来后，使用 RTInventoryQueryParamDTO 进行反射解析
     */
    private String searchJson;

    private String businessDate;

    private String transStrartDate;

    private String transEndDate;

    // 上传文件夹的类型编号
    private String documentReconCd;

    private String documentReconName;

    // 上传文件的类型编号
    private String excelGroupCd;

    private String excelGroupName;
    // 文件路径
    private String filePath;

    // 分页
    private int limitStart;

    // 是否分页
    private boolean flg = true;

    /**
     * 角色资源组
     */
    private List<ResourceViewDTO> resources;

    /** 大区CD */
    private String regionCd;
    /** 城市CD */
    private String cityCd;
    /** 区域CD */
    private String districtCd;
    /** 店铺CD */
    private String storeCd;
    /** 权限包含的所有店铺编号 */
    private Collection<String> stores;
}
