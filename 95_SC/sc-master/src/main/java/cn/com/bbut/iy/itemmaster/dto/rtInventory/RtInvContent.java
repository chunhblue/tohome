package cn.com.bbut.iy.itemmaster.dto.rtInventory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class RtInvContent {

    // 提示
    private String message;

    // 数据
    private String content;

    // 状态 0：成功
    private String status;
    // 总条数
    private Integer size;

}
