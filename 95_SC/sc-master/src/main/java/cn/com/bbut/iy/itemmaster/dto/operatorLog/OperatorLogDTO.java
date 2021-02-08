
package cn.com.bbut.iy.itemmaster.dto.operatorLog;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import lombok.Data;

@Data
public class OperatorLogDTO extends GridDataDTO {
	// 行号
	private String rowNum;

	//	用户账号
	private String userId;
	
	//	用户账号
	private String userName;
	
	//	页面编号
	private Integer menuId;
	
	//	页面名称
	private String menuName;
	
	//	操作日期
	private String operateYmd;
	
	//	操作时间
	private String operateHms;
	
	//	操作
	private String action;
	
	//	查询开始日期
	private String selectStartYmd;
	
	//	查询结束日期
	private String selectEndYmd;
	
	//	查询开始时间
	private String selectStartHms;
	
	//	查询结束时间
	private String selectEndHms;
}
