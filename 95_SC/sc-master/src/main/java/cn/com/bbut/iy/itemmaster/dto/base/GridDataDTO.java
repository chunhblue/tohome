/**
 * ClassName  GridDataDTO
 *
 * History
 * Create User: shiy
 * Create Date: 2011-11-11
 * Update User:
 * Update Date:
 */
package cn.com.bbut.iy.itemmaster.dto.base;

import java.io.Serializable;
import java.util.List;

import cn.com.bbut.iy.itemmaster.dto.cash.CashDetail;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * jqGrid返回数据用DTO
 * 
 * @author shiy
 */
@Data
@NoArgsConstructor
public class GridDataDTO<T extends Serializable> implements Serializable {

	private static final long serialVersionUID = 2011L;

	public GridDataDTO(List<T> rows, long page, long records, long rowPerPage) {
		this.setRows(rows);
		this.setPage(page);
		this.setRecords(records);
		this.setRowPerPage(rowPerPage);
	}

	/**
	 * 返回的数据
	 */
	private List<T> rows;

	/** 当前页 */
	private long page;

	/** 记录数 */
	private long records;

	/** 每页记录数 */
	private long rowPerPage;

	/**
	 * 总页数
	 */
	private long total;

	/**
	 * 消息提示
	 */
	private String message;

	/**
	 * 设置每页记录数
	 */
	public void setRowPerPage(long rowPerPage) {
		this.rowPerPage = rowPerPage;
		setTotalValue();
	}

	/**
	 * 设置总记录数
	 * 
	 * @param records
	 */
	public void setRecords(long records) {
		this.records = records;
		setTotalValue();
	}

	/**
	 * 设置页数
	 * 
	 * @return
	 */
	private void setTotalValue() {
		if (rowPerPage != 0) {
			total = (long) Math.ceil((double) records / rowPerPage);
		}
	}

}
