/**
 * @Package cn.com.nri.beijing.Logistics.BIZ.APP.CM9070.DTO
 * @Description 采番返回DTO
 */
package cn.com.bbut.iy.itemmaster.dto.cm9070;

/**
 * @Description:采番返回DTO
 */
public class Cm9070ReturnDTO {
	//流水号
	public String aSreturnNumber;
	//返回类型
	public int code;
	//返回消息
	public String msg;
	/**
	 * @return aSreturnNumber 获取属性aSreturnNumber的值
	 */
	public String getaSreturnNumber() {
		return aSreturnNumber;
	}
	/**
	 * @param aSreturnNumber 将参数 aSreturnNumber的值赋给属性 aSreturnNumber
	 */
	public void setaSreturnNumber(String aSreturnNumber) {
		this.aSreturnNumber = aSreturnNumber;
	}
	/**
	 * @return code 获取属性code的值
	 */
	public int getCode() {
		return code;
	}
	/**
	 * @param code 将参数 code的值赋给属性 code
	 */
	public void setCode(int code) {
		this.code = code;
	}
	/**
	 * @return msg 获取属性msg的值
	 */
	public String getMsg() {
		return msg;
	}
	/**
	 * @param msg 将参数 msg的值赋给属性 msg
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}
