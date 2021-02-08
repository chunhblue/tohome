package cn.com.bbut.iy.itemmaster.dto;

import java.io.Serializable;

import cn.com.bbut.iy.itemmaster.dto.base.ReturnDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Ajax返回结果
 * 
 * @author HanHaiyun
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class AjaxResultDto extends ReturnDTO implements Serializable {

    private static final long serialVersionUID = 2012L;

    /** 状态, 继承的父类 ReturnDTO 里面有 success 所以这里的先注释 */
    // private boolean success;
    /** 消息 */
    private String message;
    /** 状态码 **/
    private String toKen;
    /** 重复提交 */
    private boolean repeat;
    /** 返回数据 */
    private Object data;
    /* b标志*/
    private String flag;
    /** 来源：默认null，sys：系统 **/
    private String source;
    /** 跳转url */
    private String s;
}
