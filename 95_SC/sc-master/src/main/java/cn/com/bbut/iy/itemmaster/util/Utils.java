/**
 * ClassName  Utils
 *
 * History
 * Create User: Shiy
 * Create Date: 2013-2-18
 * Update User:
 * Update Date:
 */
package cn.com.bbut.iy.itemmaster.util;

import java.io.File;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;
import cn.com.bbut.iy.itemmaster.config.ExcelConfig;
import cn.com.bbut.iy.itemmaster.exception.SystemRuntimeException;
import cn.shiy.common.baseutil.Container;

/**
 * 
 * @author Shiy
 */
@Slf4j
public class Utils {

    /**
     * 获取完整的随机文件名
     * 
     * @return
     */
    public static String getFullRandomFileName() {
        ExcelConfig config = Container.getBean(ExcelConfig.class);
        File path = new File(config.getFileDir());
        if (path.isDirectory()) {
            return path.getAbsoluteFile() + File.separator + getRandomFileName();
        } else {
            throw new SystemRuntimeException("excel file path is not exist");
        }
    }

    /**
     * 生成随机文件名，规则：当前时间毫秒数+三位随机数
     * 
     * @return
     */
    public static String getRandomFileName() {
        StringBuffer sb = new StringBuffer();
        sb.append("IM-");
        sb.append(UUID.randomUUID());
        return sb.toString();
    }

    /**
     * 取下一startnum 如果curStartNum为0返回1，其他情况返回curStartNum + pageSize
     * 
     * @param curStartNum
     *            当前startnum
     * @param pageSize
     *            每页数据数量
     * @return
     */
    public static int getNextStartNum(int curStartNum, int pageSize) {
        return curStartNum == 0 ? 1 : curStartNum + pageSize;
    }

    /**
     * 取下一endnum
     * 
     * @param curEndNum
     *            当前endnum
     * @param pageSize
     *            每页数据量
     * @return 返回curEndNum + pageSize
     */
    public static int getNextEndNum(int curEndNum, int pageSize) {
        return curEndNum + pageSize;
    }
}
