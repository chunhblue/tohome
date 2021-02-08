/**
 * ClassName  PermissionResource
 * History
 * Create User: shiy
 * Create Date: 2015年3月26日
 * Update User:
 * Update Date:
 */
package cn.com.bbut.iy.itemmaster.dto;

import java.io.Serializable;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import cn.com.bbut.iy.itemmaster.exception.SystemRuntimeException;

/**
 * 权限资源数据对象 构造函数设定值后不可改变
 * 
 * @author shiy
 *
 */
@EqualsAndHashCode
@ToString
public class PermissionResource implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 权限级别 事业部>部>DPT */
    public enum LEVEL {
        /** 无权限 */
        NO_PERMISSION,
        /** 全部 */
        ALL,
        /** 事业部 */
        GRANDDIV,
        /** 部 */
        DEPARTMENT,
        /** DPT */
        DPT
    }

    /** 权限级别 */
    private LEVEL level;

    /** 事业部 */
    private String grandDIV;

    /** 部 */
    private String department;

    /** dpt */
    private String dpt;

    /** 是否为全部店铺 */
    private boolean isAllStore;

    /** 店铺 */
    private String store;

    /**
     * 
     * @param grandDIV
     *            不可为空
     * @param department
     *            此值与dpt同为空时level为事业部
     * @param dpt
     *            只有此值为空时level为部，全部有值时level为dpt
     * @param store
     *            如果store不为null且首字母为'0'则认为是单店，isAllStore设为false
     */
    public PermissionResource(LEVEL level, String grandDIV, String department, String dpt,
            String store) {
        this.level = level;
        switch (level) {
        // 无权限和全部权限时，不设定
        case NO_PERMISSION:
            this.isAllStore = false;
            this.store = null;
            break;
        case ALL:
            this.isAllStore = true;
            return;
        case GRANDDIV:
            this.grandDIV = grandDIV;
            break;
        case DEPARTMENT:
            this.grandDIV = grandDIV;
            this.department = department;
            break;
        case DPT:
            this.grandDIV = grandDIV;
            this.department = department;
            this.dpt = dpt;
            break;
        default:
            throw new SystemRuntimeException("unknown level " + level);
        }

        this.store = store;
        if (store == null) {
            this.isAllStore = false;
        } else if ("99999".equals(store)) {
            this.isAllStore = true;
            this.store = null;
        } else {
            this.isAllStore = false;
        }
    }

    /**
     * 
     * @param level
     * @param grandDIV
     * @param department
     * @param dpt
     * @param isAllStore
     * @param store
     */
    public PermissionResource(LEVEL level, String grandDIV, String department, String dpt,
            boolean isAllStore, String store) {
        this.grandDIV = grandDIV;
        this.department = department;
        this.dpt = dpt;
        this.level = level;
        this.isAllStore = isAllStore;
        this.store = store;
    }

    /**
     * 事业部
     * 
     * @return the grandDIV
     */
    public String getGrandDIV() {
        return grandDIV;
    }

    /**
     * 部
     * 
     * @return the department
     */
    public String getDepartment() {
        return department;
    }

    /**
     * DPT
     * 
     * @return the dpt
     */
    public String getDpt() {
        return dpt;
    }

    /**
     * 店铺
     * 
     * @return the store
     */
    public String getStore() {
        return store;
    }

    /**
     * 级别
     * 
     * @return the level
     */
    public LEVEL getLevel() {
        return level;
    }

    /**
     * 是否全店
     * 
     * @return the isAllStore
     */
    public boolean isAllStore() {
        return isAllStore;
    }

}
