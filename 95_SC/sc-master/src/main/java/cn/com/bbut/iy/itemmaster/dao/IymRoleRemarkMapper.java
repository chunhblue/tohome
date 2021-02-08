package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dao.gen.IymRoleRemarkGenMapper;
import cn.com.bbut.iy.itemmaster.dto.base.role.RoleParamDTO;
import cn.shiy.common.pmgr.entity.Role;
import cn.shiy.common.pmgr.entity.RoleExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IymRoleRemarkMapper extends IymRoleRemarkGenMapper {

    List<Role> selectRolesByExample(@Param("param") RoleParamDTO example);

    long selectCountRolesByExample(@Param("param") RoleParamDTO param);

    List<Role> getRolesLikeName(@Param("param") String roleName);
}