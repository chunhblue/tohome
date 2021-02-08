/**
 * ClassName  AutoCompleteDTO
 * History
 * Create User: lilw
 * Create Date: 2017年8月11日
 * Update User:
 * Update Date:
 */
package cn.com.bbut.iy.itemmaster.dto.base;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author lilw
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AutoCompleteDTO implements Serializable {

    private static final long serialVersionUID = 3706371142162014026L;

    private String k;

    private String v;

    private String hidek;

}
