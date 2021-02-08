/**
 * ClassName  ExportStatus
 *
 * History
 * Create User: Shiy
 * Create Date: 2013-9-27
 * Update User:
 * Update Date:
 */
package cn.com.bbut.iy.itemmaster.excel;

import java.io.Serializable;

import lombok.Data;

/**
 * 
 * @author Shiy
 */
@Data
public class ExportStatus implements Serializable {

    private static final long serialVersionUID = 1L;

    private short status;

    private String filename;
}
