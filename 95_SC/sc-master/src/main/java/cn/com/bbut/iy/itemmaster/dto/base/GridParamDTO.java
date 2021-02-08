/**
 * ClassName  GridParamDTO
 *
 * History
 * Create User: shiy
 * Create Date: 2011-11-11
 * Update User:
 * Update Date:
 */
package cn.com.bbut.iy.itemmaster.dto.base;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 
 * @author shiy
 */
@Data
public class GridParamDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	/** 排序的列 */
	private String sidx;
	/** 升序、降序 */
	private String sord;
	/** 每页条数 */
	private int rows;
	/** 当前页 */
	private int page;
	/** 报表导出主要的起始时间 */
	private Date primaDateFrom;
	/** 报表导出主要的结束时间 */
	private Date primaDateTo;
	/** 是否为批处理 默认false */
	private boolean isbatch = false;
	/** 导出报表用户名 */
	private String reportUserId;


	/**
	 * 取得LimitStart值，即开始记录数
	 * 
	 * @return
	 */
	public int getLimitStart() {
		return (page - 1) * rows;
	}

	/**
	 * 取得LimitEnd值，即取得的记录数
	 * 
	 * @return
	 */
	public int getLimitEnd() {
		return rows;
	}

	public String getOrderByClause() {
		return sidx + " " + sord;
	}

}
