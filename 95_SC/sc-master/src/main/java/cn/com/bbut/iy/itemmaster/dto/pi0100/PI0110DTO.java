package cn.com.bbut.iy.itemmaster.dto.pi0100;

import lombok.Data;

import java.io.Serializable;

@Data
public class PI0110DTO implements Serializable {

    private String piCd;
    private String piDate;
//    private String piTypeCode;
//    private String piTypeName;
    private String pmaCd;
    private String pmaName;
    private String createYmd;
    private String createHms;
    private String createUserId;
    private int num;
}
