package cn.com.bbut.iy.itemmaster.serviceimpl.base;

import java.util.Collection;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import cn.com.bbut.iy.itemmaster.Application;
import cn.com.bbut.iy.itemmaster.service.base.IymActorAssignmentService;
import cn.com.bbut.iy.itemmaster.util.TimeUtil;
import cn.shiy.common.baseutil.Container;

@SpringBootTest(classes = Application.class)
public class IymActorAssignmentServiceImplTest extends AbstractTestNGSpringContextTests {

    @Test
    public void testGetReivewRoleIdsByActorId() throws Exception {
        IymActorAssignmentService service = Container.getBean(IymActorAssignmentService.class);
        Collection<Integer> roleIds = service.getSpecialAlternateRoleIdsByActorId("0019323",
                TimeUtil.getDate());
        roleIds.stream().forEach(System.out::println);

    }
}