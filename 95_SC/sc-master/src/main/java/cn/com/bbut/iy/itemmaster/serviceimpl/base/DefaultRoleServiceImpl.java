package cn.com.bbut.iy.itemmaster.serviceimpl.base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import cn.com.bbut.iy.itemmaster.dao.MA4160Mapper;
import cn.com.bbut.iy.itemmaster.entity.MA4160;
import cn.com.bbut.iy.itemmaster.entity.MA4160Example;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.bbut.iy.itemmaster.dao.IymDefAssignmentMapper;
import cn.com.bbut.iy.itemmaster.dto.AjaxResultDto;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.base.DefaultRole.DefaultRoleEditDTO;
import cn.com.bbut.iy.itemmaster.dto.base.DefaultRole.DefaultRoleGridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.base.DefaultRole.DefaultRoleParamDTO;
import cn.com.bbut.iy.itemmaster.dto.base.role.IyRoleDTO;
import cn.com.bbut.iy.itemmaster.entity.IymDefAssignment;
import cn.com.bbut.iy.itemmaster.entity.IymDefAssignmentExample;
import cn.com.bbut.iy.itemmaster.entity.IymDefAssignmentExample.Criteria;
import cn.com.bbut.iy.itemmaster.service.StoreService;
import cn.com.bbut.iy.itemmaster.service.base.DefaultRoleService;
import cn.com.bbut.iy.itemmaster.service.base.role.IyRoleService;
import cn.com.bbut.iy.itemmaster.util.TimeUtil;
import cn.shiy.common.baseutil.Container;

/**
 * @author songxz
 */
@Slf4j
@Service
public class DefaultRoleServiceImpl implements DefaultRoleService {
    @Autowired
    IymDefAssignmentMapper mapper;
    @Autowired
    private MA4160Mapper ma4160Mapper;
    @Autowired
    private IyRoleService iyRoleService;

    @Override
    public GridDataDTO<DefaultRoleGridDataDTO> getDataByParam(DefaultRoleParamDTO param) {
        IymDefAssignmentExample example = new IymDefAssignmentExample();
        Criteria criteria = example.or();
        if (param.getRoleId() != null) {
            criteria.andRoleIdEqualTo(param.getRoleId());
        }
        if (StringUtils.isNotBlank(param.getPostCode())) {
            criteria.andPostCodeEqualTo(param.getPostCode());
        }

        example.setOrderByClause(param.getOrderByClause());
        example.setLimitStart(param.getLimitStart());
        example.setLimitEnd(param.getLimitEnd());
        List<IymDefAssignment> list = mapper.selectByExample(example);

        example.setLimitStart(-1);
        example.setLimitEnd(-1);
        long count = mapper.countByExample(example);

        List<DefaultRoleGridDataDTO> rest = getInfoData(list);
        GridDataDTO<DefaultRoleGridDataDTO> data = new GridDataDTO<DefaultRoleGridDataDTO>(rest,
                param.getPage(), count, param.getRows());
        return data;
    }

    /**
     * 解析list内容，向各个表中取得对应名称内容
     * 
     * @param list
     * @return
     */
    private List<DefaultRoleGridDataDTO> getInfoData(List<IymDefAssignment> list) {
        if (list == null || list.size() == 0) {
            return null;
        }
        List<DefaultRoleGridDataDTO> rest = new ArrayList<DefaultRoleGridDataDTO>();
        // 拆解集合 向各个表取得对应名称
        for (IymDefAssignment ls : list) {
            DefaultRoleGridDataDTO t = new DefaultRoleGridDataDTO();
            t.setId(ls.getId());

            IyRoleDTO role = iyRoleService.getRoleInfoByRoleId(ls.getRoleId());
            t.setRoleId(ls.getRoleId());
            if (role != null) {
                t.setRoleName(role.getName());
                t.setRemark(role.getRemark());
            }

            MA4160Example ma4160Example = new MA4160Example();
            ma4160Example.or().andJobTypeCdEqualTo(ls.getPostCode());
            // MA4160 ma4160 = ma4160Mapper.selectByExample(ma4160Example).get(0);
            List<MA4160> ma4160List = ma4160Mapper.selectByExample(ma4160Example);
            if (ma4160List!=null&&ma4160List.size()>0) {
                MA4160 ma4160 = ma4160List.get(0);
                t.setPostCode(ls.getPostCode());
                t.setPostName(ma4160.getJobCatagoryName());
                rest.add(t);
            }
        }
        return rest;
    }

    @Override
    public AjaxResultDto updateDataByParam(DefaultRoleEditDTO param) {
        AjaxResultDto rest = new AjaxResultDto();
        DefaultRoleService thisService = Container.getBean(DefaultRoleService.class);
        Integer roleId = param.getRoleId();
        String postCode = param.getPostCode();
        String userId = param.getUserid();
        if (param.getId() != null) {
            // 修改
            IymDefAssignment up = new IymDefAssignment();
            up.setId(param.getId());
            up.setRoleId(roleId);
            up.setPostCode(postCode);
            up.setUpdateUserid(userId);
            up.setUpdateTime(TimeUtil.getDate());
            List<IymDefAssignment> exist = new ArrayList<>();
            exist.add(up);
            int count = thisService.isDataExist(exist);
            if (count > 0) {
                rest.setSuccess(true);
                thisService.updateDataByListEntity(up);
                rest.setMessage("Operation Succeeded!");
            } else {
                rest.setSuccess(false);
                rest.setMessage("Sorry, operation failed! There is no available data to modify!");
            }
            return rest;
        } else {
            List<IymDefAssignment> inlist = new ArrayList<>();
            // Add
            IymDefAssignment in = new IymDefAssignment();
            in.setRoleId(roleId);
            in.setPostCode(postCode);
            in.setUpdateUserid(userId);
            in.setUpdateTime(TimeUtil.getDate());
            inlist.add(in);
            // 查询是否存在要插入的数据,不存在才可以插入新数据
            int count = thisService.isDataExist(inlist);
            if (count > 0) {
                rest.setSuccess(false);
                rest.setMessage("Privilege duplicated! Please select a new  privilege or select a new position!");
            } else {
                rest.setSuccess(true);
                thisService.insertDataByListEntity(inlist);
                rest.setMessage("Operation Succeeded!");
            }
            return rest;
        }
    }

    @Override
    public int insertDataByListEntity(List<IymDefAssignment> inlist) {
        int inCount = 0;
        for (IymDefAssignment in : inlist) {
            mapper.insert(in);
            inCount++;
        }
        return inCount;
    }

    @Override
    public int isDataExist(List<IymDefAssignment> inlist) {
        for (IymDefAssignment in : inlist) {
            IymDefAssignmentExample example = new IymDefAssignmentExample();
            Criteria criteria = example.or();
            if (in.getId() != null) {
                criteria.andIdEqualTo(in.getId());
            } else {
                criteria.andRoleIdEqualTo(in.getRoleId())
                        .andPostCodeEqualTo(in.getPostCode());
            }
            long count = mapper.countByExample(example);
            if (count > 0) {
                return 1;
            }
        }
        return 0;
    }

    @Override
    public int updateDataByListEntity(IymDefAssignment upEntity) {
        int upCount = mapper.updateByPrimaryKey(upEntity);
        return upCount;
    }

    @Override
    public AjaxResultDto updateCancelDataById(Integer id) {
        AjaxResultDto rest = new AjaxResultDto();
        mapper.deleteByPrimaryKey(id);
        rest.setSuccess(true);
        rest.setMessage("Operation Succeeded!");
        return rest;
    }

    @Override
    public Collection<Integer> getDefRoleId(String jobTypeCd) {
        IymDefAssignmentExample example = new IymDefAssignmentExample();
        example.or().andPostCodeEqualTo(jobTypeCd);
        List<IymDefAssignment> list = mapper.selectByExample(example);
        if (list != null && list.size() > 0) {
            Collection<Integer> roleIds = new ArrayList<>();
            for (IymDefAssignment ls : list) {
                roleIds.add(ls.getRoleId());
            }
            return roleIds;
        }
        return null;
    }

    @Override
    public boolean isRoleUse(Integer roleId) {
        IymDefAssignmentExample example = new IymDefAssignmentExample();
        example.or().andRoleIdEqualTo(roleId);
        List<IymDefAssignment> list = mapper.selectByExample(example);
        if (list != null && list.size() > 0) {
            return true;
        }
        return false;
    }

    @Override
    public List<AutoCompleteDTO> getRolesAutoByParam(String v) {
        List<AutoCompleteDTO> rest = new ArrayList<>();
        List<IyRoleDTO> list = iyRoleService.getRoleInfosByRoleName(v);
        if (list != null && list.size() > 0) {
            for (IyRoleDTO ls : list) {
                AutoCompleteDTO dto = new AutoCompleteDTO();
                dto.setK(ls.getId() + "");
                dto.setV(ls.getName());
                rest.add(dto);
            }
        }
        return rest;
    }

    @Override
    public int getMaxPosition(String userId) {
        return ma4160Mapper.getMaxPositionByuserId(userId);
    }

    @Override
    public int countFinancePosition(String userId){

        return ma4160Mapper.countFinancePosition(userId);
    }
}
