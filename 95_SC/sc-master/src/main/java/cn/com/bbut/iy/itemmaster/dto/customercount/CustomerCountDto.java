package cn.com.bbut.iy.itemmaster.dto.customercount;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName CusterCountDto
 * @Description TODO
 * @Author Ldd
 * @Date 2021/2/20 17:11
 * @Version 1.0
 * @Description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerCountDto {
    private String storeCd;
    private String storeName;
    private String dateTime;
    private String k1bill19k;
    private String k19bill29k;
    private String k29bill39k;
    private String k39bill49k;
    private String bill49k;
    private String total;
}
