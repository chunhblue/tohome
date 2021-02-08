package cn.com.bbut.iy.itemmaster.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import cn.com.bbut.iy.itemmaster.dao.gen.IymActorAssignmentInfoGenMapper;
import cn.shiy.common.pmgr.entity.ResourceGroup;

public interface IymActorAssignmentInfoMapper extends IymActorAssignmentInfoGenMapper {

    List<Integer> getActorAssIdsByInfo(int assType);

    List<Integer> getRoleIdsByResource(Collection<ResourceGroup> groups);

    List<Integer> getActorAssIdsByAssUserIdAndRoleId(Map<String, Object> map);
}
