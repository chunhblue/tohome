package cn.com.bbut.iy.itemmaster.service.base;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import cn.com.bbut.iy.itemmaster.Application;
import cn.com.bbut.iy.itemmaster.dao.MenuMapper;
import cn.com.bbut.iy.itemmaster.entity.base.Menu;
import cn.com.bbut.iy.itemmaster.entity.base.MenuExample;
import cn.com.bbut.iy.itemmaster.service.base.MenuService;

@SpringBootTest(classes = Application.class)
public class MenuServiceTest extends AbstractTestNGSpringContextTests {

	@Autowired
	private MenuService menuService;
	@Autowired
	private MenuMapper menuMapper;

	@Test
	public void testGetAllMenus() throws Exception {
		Collection<Menu> menus = menuService.getAllMenus();
		Long count = menuMapper.countByExample(new MenuExample());
		assertEquals(menus.size(), count.intValue());
	}
}