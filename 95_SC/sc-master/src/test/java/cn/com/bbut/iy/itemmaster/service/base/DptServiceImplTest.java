package cn.com.bbut.iy.itemmaster.service.base;

import java.util.List;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import cn.com.bbut.iy.itemmaster.Application;
import cn.com.bbut.iy.itemmaster.constant.ConstantsDB;
import cn.com.bbut.iy.itemmaster.dto.base.DptResourceDTO;
import cn.shiy.common.baseutil.Container;

@SpringBootTest(classes = Application.class)
public class DptServiceImplTest extends AbstractTestNGSpringContextTests {

	@Test
	public void testGetDpts() throws Exception {
		DptService dptService = Container.getBean(DptService.class);
		List<DptResourceDTO> dtos = dptService.getDpts(null, ConstantsDB.COMMON_TWO, null, null,
				ConstantsDB.COMMON_TWO);
		Assert.assertEquals(dtos.size(), 4);

	}
}