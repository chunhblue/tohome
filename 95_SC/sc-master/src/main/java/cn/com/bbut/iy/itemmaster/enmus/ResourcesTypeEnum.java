package cn.com.bbut.iy.itemmaster.enmus;

import cn.shiy.common.pmgr.typehandler.IntEnum;

/**
 * 角色资源类型枚举类
 *
 * @author shiy
 */
public enum ResourcesTypeEnum implements IntEnum<ResourcesTypeEnum> {
	GRAND_DIV(1, "事业部"), DEPARTMENT(2, "部"), DPT(3, "DPT"), STORE(4, "店铺");

	private int index;
	private String name;

	// ....get set
	ResourcesTypeEnum(int index, String name) {
		this.index = index;
		this.name = name;
	}

	public static String fromIndex(int index) {
		for (ResourcesTypeEnum p : ResourcesTypeEnum.values()) {
			if (index == p.getIntValue())
				return p.name;
		}
		return null;
	}

	@Override
	public int getIntValue() {
		return this.index;
	}

	@Override
	public String getName() {
		return name();
	}
}