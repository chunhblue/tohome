
package cn.com.bbut.iy.itemmaster.dto.operatorLog;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;
import lombok.Data;

import java.util.Collection;

@Data
public class OperatorLogParamDTO extends GridParamDTO {
	//	用户账号
	private String userId;
	
	//	用户账号
	private String userName;
	
	//	页面名称
	private String menuName;
	
	//	查询开始日期
	private String startYmd;
	
	//	查询结束日期
	private String endYmd;
	
	//	查询开始时间
	private String startHms;
	
	//	查询结束时间
	private String endHms;

	private int limitStart;

	private boolean flg = true;

}
