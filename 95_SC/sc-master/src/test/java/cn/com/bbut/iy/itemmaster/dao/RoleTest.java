package cn.com.bbut.iy.itemmaster.dao;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import cn.com.bbut.iy.itemmaster.Application;
import cn.shiy.common.baseutil.Container;
import cn.shiy.common.pmgr.dao.RoleMapper;
import cn.shiy.common.pmgr.entity.Role;
import cn.shiy.common.pmgr.entity.RoleExample;
import cn.shiy.common.pmgr.enums.RoleGrantEnum;

@SpringBootTest(classes = Application.class)
public class RoleTest extends AbstractTestNGSpringContextTests {

	@Autowired
	private RoleMapper roleMapper;

	@Test(groups = "dao")
	public void testSelectByExample() throws Exception {
		RoleMapper roleMapper = Container.getBean(RoleMapper.class);
		System.out.println(roleMapper.hashCode());
		Role role1 = new Role(null, "SY测试角色一", 0, RoleGrantEnum.ENABLED, 2, "3");
		Integer roleId1 = roleMapper.insert(role1);
		Role role2 = new Role(null, "SY测试角色二", 0, RoleGrantEnum.DISABLED, 3, "4");
		Integer roleId2 = roleMapper.insert(role2);

		System.out.println(role1);
		System.out.println(role2);

		RoleExample ex = new RoleExample();
		ex.or().andNameLike("SY测试%");
		ex.setOrderByClause("id desc");
		List<Role> roles = roleMapper.selectByExample(ex);
		if (roles != null && roles.size() > 0) {
			assertEquals(roles.get(0), role2);
			assertEquals(roles.get(1), role1);
		}
		roleMapper.deleteByPrimaryKey(role1.getId());
		roleMapper.deleteByPrimaryKey(role2.getId());

	}
}
