package cn.com.bbut.iy.itemmaster.serviceimpl.base.role;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import cn.com.bbut.iy.itemmaster.Application;
import cn.com.bbut.iy.itemmaster.constant.ConstantsDB;
import cn.com.bbut.iy.itemmaster.constant.PermissionCode;
import cn.com.bbut.iy.itemmaster.dto.base.ConditionDTO;
import cn.com.bbut.iy.itemmaster.dto.base.role.IyResourceDTO;
import cn.com.bbut.iy.itemmaster.service.base.role.IyRoleService;
import cn.shiy.common.baseutil.Container;
import cn.shiy.common.pmgr.entity.Resource;
import cn.shiy.common.pmgr.entity.ResourceGroup;

@SpringBootTest(classes = Application.class)
public class IyRoleServiceImplTest extends AbstractTestNGSpringContextTests {

    // @Test
    public void testGetResource() throws Exception {
//        IyRoleService service = Container.getBean(IyRoleService.class);
//        List<IyResourceDTO> dtos = service.getResource("0019323", "113", "10", "01", "00911",
//                PermissionCode.P_CODE_DEFASS_VIEW);
//        dtos.stream().forEach(System.out::println);
    }

    // @Test
    public void testGetRoleIdsByResource1() throws Exception {
        IyRoleService service = Container.getBean(IyRoleService.class);
        Collection<ResourceGroup> groups = new ArrayList<ResourceGroup>() {
            {
                add(new ResourceGroup(new Resource("161", ConstantsDB.COMMON_THREE),
                        new Resource("00001", 4)));
            }
        };
        Collection<Integer> roleIds = service.getRoleIdsByResource(groups, "P-DEFASS-001");
        System.out.println("---------测试1--------");
        roleIds.forEach(System.out::println);
    }

    // @Test
    public void testGetRoleIdsByResource2() throws Exception {
        IyRoleService service = Container.getBean(IyRoleService.class);
        Collection<ResourceGroup> groups = new ArrayList<ResourceGroup>() {
            {
                add(new ResourceGroup(new Resource("16", ConstantsDB.COMMON_TWO),
                        new Resource("00001", 4)));
            }
        };
        Collection<Integer> roleIds = service.getRoleIdsByResource(groups, "P-DEFASS-001");
        System.out.println("---------测试2--------");
        roleIds.forEach(System.out::println);
    }

    // @Test
    public void testGetRoleIdsByResource() throws Exception {
        IyRoleService service = Container.getBean(IyRoleService.class);
        Collection<ResourceGroup> groups = new ArrayList<ResourceGroup>() {
            {
                add(new ResourceGroup(new Resource("001", ConstantsDB.COMMON_ONE),
                        new Resource("00001", 4)));
            }
        };
        Collection<Integer> roleIds = service.getRoleIdsByResource(groups, "P-DEFASS-001");
        System.out.println("---------测试3--------");
        roleIds.forEach(System.out::println);
    }

    // @Test
    public void testCreateConditionsFromResourceGroup() throws Exception {
        List<ResourceGroup> groups = new ArrayList<ResourceGroup>() {
            {
                add(new ResourceGroup(new Resource("999", ConstantsDB.COMMON_ONE),
                        new Resource("00001", ConstantsDB.COMMON_FOUR)));
            }
        };
        IyRoleService service = Container.getBean(IyRoleService.class);
        ConditionDTO dto = service.createConditionsFromResourceGroup(groups);
        System.out.println(dto.getCondition());
        String str = "(store='00001')";
        Assert.assertEquals(str, dto.getCondition());
    }

    // @Test
    public void test1CreateConditionsFromResourceGroup() throws Exception {
        List<ResourceGroup> groups = new ArrayList<ResourceGroup>() {
            {
                add(new ResourceGroup(new Resource("999", ConstantsDB.COMMON_ONE),
                        new Resource("99999", ConstantsDB.COMMON_FOUR)));
            }
        };
        IyRoleService service = Container.getBean(IyRoleService.class);
        ConditionDTO dto = service.createConditionsFromResourceGroup(groups);
        System.out.println(dto.getCondition());
        String str = "";
        Assert.assertEquals(str, dto.getCondition());
    }

    // @Test
    public void test2CreateConditionsFromResourceGroup() throws Exception {
        List<ResourceGroup> groups = new ArrayList<ResourceGroup>() {
            {
                add(new ResourceGroup(new Resource("001", ConstantsDB.COMMON_ONE),
                        new Resource("99999", ConstantsDB.COMMON_FOUR)));
            }
        };
        IyRoleService service = Container.getBean(IyRoleService.class);
        ConditionDTO dto = service.createConditionsFromResourceGroup(groups);
        System.out.println(dto.getCondition());
        String str = "(substr(dpt,1,1)='1')";
        Assert.assertEquals(str, dto.getCondition());
    }

    // @Test
    public void test3CreateConditionsFromResourceGroup() throws Exception {
        List<ResourceGroup> groups = new ArrayList<ResourceGroup>() {
            {
                add(new ResourceGroup(new Resource("001", ConstantsDB.COMMON_ONE),
                        new Resource("16", ConstantsDB.COMMON_TWO),
                        new Resource("99999", ConstantsDB.COMMON_FOUR)));
            }
        };
        IyRoleService service = Container.getBean(IyRoleService.class);
        ConditionDTO dto = service.createConditionsFromResourceGroup(groups);
        System.out.println(dto.getCondition());
        String str = "(substr(dpt,1,2)='16')";
        Assert.assertEquals(str, dto.getCondition());
    }

    @Test
    public void test4CreateConditionsFromResourceGroup() throws Exception {
        List<ResourceGroup> groups = new ArrayList<ResourceGroup>() {
            {
                add(new ResourceGroup(new Resource("001", ConstantsDB.COMMON_ONE),
                        new Resource("16", ConstantsDB.COMMON_TWO),
                        new Resource("00001", ConstantsDB.COMMON_FOUR)));
            }
        };
        IyRoleService service = Container.getBean(IyRoleService.class);
        ConditionDTO dto = service.createConditionsFromResourceGroup(groups);
        System.out.println(dto.getCondition());
        String str = "(substr(dpt,1,2)='16' and store='00001')";
        Assert.assertEquals(str, dto.getCondition());
    }

    @Test
    public void test5CreateConditionsFromResourceGroup() throws Exception {
        List<ResourceGroup> groups = new ArrayList<ResourceGroup>() {
            {
                add(new ResourceGroup(new Resource("001", ConstantsDB.COMMON_ONE),
                        new Resource("16", ConstantsDB.COMMON_TWO),
                        new Resource("161", ConstantsDB.COMMON_THREE),
                        new Resource("99999", ConstantsDB.COMMON_FOUR)));
            }
        };
        IyRoleService service = Container.getBean(IyRoleService.class);
        ConditionDTO dto = service.createConditionsFromResourceGroup(groups);
        System.out.println(dto.getCondition());
        String str = "(dpt='161')";
        Assert.assertEquals(str, dto.getCondition());
    }

    @Test
    public void test6CreateConditionsFromResourceGroup() throws Exception {
        List<ResourceGroup> groups = new ArrayList<ResourceGroup>() {
            {
                add(new ResourceGroup(new Resource("001", ConstantsDB.COMMON_ONE),
                        new Resource("16", ConstantsDB.COMMON_TWO),
                        new Resource("161", ConstantsDB.COMMON_THREE),
                        new Resource("00001", ConstantsDB.COMMON_FOUR)));
            }
        };
        IyRoleService service = Container.getBean(IyRoleService.class);
        ConditionDTO dto = service.createConditionsFromResourceGroup(groups);
        System.out.println(dto.getCondition());
        String str = "(dpt='161' and store='00001')";
        Assert.assertEquals(str, dto.getCondition());
    }

    @Test
    public void test7CreateConditionsFromResourceGroup() throws Exception {
        List<ResourceGroup> groups = new ArrayList<ResourceGroup>() {
            {
                add(new ResourceGroup(new Resource("001", ConstantsDB.COMMON_ONE),
                        new Resource("16", ConstantsDB.COMMON_TWO),
                        new Resource("161", ConstantsDB.COMMON_THREE),
                        new Resource("00001", ConstantsDB.COMMON_FOUR)));
                add(new ResourceGroup(new Resource("001", ConstantsDB.COMMON_ONE),
                        new Resource("16", ConstantsDB.COMMON_TWO),
                        new Resource("163", ConstantsDB.COMMON_THREE),
                        new Resource("00002", ConstantsDB.COMMON_FOUR)));
            }
        };
        IyRoleService service = Container.getBean(IyRoleService.class);
        ConditionDTO dto = service.createConditionsFromResourceGroup(groups);
        System.out.println(dto.getCondition());
        String str = "(dpt='161' and store='00001') or (dpt='163' and store='00002')";
        Assert.assertEquals(str, dto.getCondition());
    }

    @Test
    public void test8CreateConditionsFromResourceGroup() throws Exception {
        List<ResourceGroup> groups = new ArrayList<ResourceGroup>() {
            {
                add(new ResourceGroup(new Resource("999", ConstantsDB.COMMON_ONE),
                        new Resource("00001", ConstantsDB.COMMON_FOUR)));
                add(new ResourceGroup(new Resource("001", ConstantsDB.COMMON_ONE),
                        new Resource("16", ConstantsDB.COMMON_TWO),
                        new Resource("163", ConstantsDB.COMMON_THREE),
                        new Resource("00002", ConstantsDB.COMMON_FOUR)));
            }
        };
        IyRoleService service = Container.getBean(IyRoleService.class);
        ConditionDTO dto = service.createConditionsFromResourceGroup(groups);
        System.out.println(dto.getCondition());
        String str = "(store='00001') or (store='00002')";
        Assert.assertEquals(str, dto.getCondition());
    }

    @Test
    public void test9CreateConditionsFromResourceGroup() throws Exception {
        List<ResourceGroup> groups = new ArrayList<ResourceGroup>() {
            {
                add(new ResourceGroup(new Resource("999", ConstantsDB.COMMON_ONE),
                        new Resource("99999", ConstantsDB.COMMON_FOUR)));
                add(new ResourceGroup(new Resource("001", ConstantsDB.COMMON_ONE),
                        new Resource("16", ConstantsDB.COMMON_TWO),
                        new Resource("163", ConstantsDB.COMMON_THREE),
                        new Resource("00002", ConstantsDB.COMMON_FOUR)));
            }
        };
        IyRoleService service = Container.getBean(IyRoleService.class);
        ConditionDTO dto = service.createConditionsFromResourceGroup(groups);
        System.out.println(dto.getCondition());
        String str = "";
        Assert.assertEquals(str, dto.getCondition());
    }

    @Test
    public void test10CreateConditionsFromResourceGroup() throws Exception {
        List<ResourceGroup> groups = new ArrayList<ResourceGroup>() {
            {
                add(new ResourceGroup(new Resource("001", ConstantsDB.COMMON_ONE),
                        new Resource("99999", ConstantsDB.COMMON_FOUR)));
                add(new ResourceGroup(new Resource("001", ConstantsDB.COMMON_ONE),
                        new Resource("16", ConstantsDB.COMMON_TWO),
                        new Resource("163", ConstantsDB.COMMON_THREE),
                        new Resource("00002", ConstantsDB.COMMON_FOUR)));
            }
        };
        IyRoleService service = Container.getBean(IyRoleService.class);
        ConditionDTO dto = service.createConditionsFromResourceGroup(groups);
        System.out.println(dto.getCondition());
        String str = "(substr(dpt,1,1)='1') or (dpt='163')";
        Assert.assertEquals(str, dto.getCondition());
    }
}