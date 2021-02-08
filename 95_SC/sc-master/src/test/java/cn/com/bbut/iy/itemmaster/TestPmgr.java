package cn.com.bbut.iy.itemmaster;

import cn.shiy.common.pmgr.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import cn.com.bbut.iy.itemmaster.service.StoreService;

/**
 * @author shiy
 */
@SpringBootTest(classes = Application.class)
public class TestPmgr extends AbstractTestNGSpringContextTests {

	@Autowired
	private PermissionService permissionService;

	@Autowired
	private StoreService storeService;

	@Test
	public void testPermissionService() {
		Assert.assertNotNull(permissionService);
	}

	@Test
	public void testStoreService() {
		storeService.getAllStore();
	}
}
