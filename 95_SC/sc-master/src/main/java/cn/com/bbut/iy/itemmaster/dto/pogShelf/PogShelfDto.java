package cn.com.bbut.iy.itemmaster.dto.pogShelf;

import cn.com.bbut.iy.itemmaster.dto.base.CommonDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class PogShelfDto  extends GridDataDTO {

    private String pogCd;

    private String pogName;

    private String storeCd;

    private String storeName;

    private String createTime;

    private String createUserName;

    private String shelf;

    private String subShelf;

    private CommonDTO commonDTO;
}
