package cn.com.bbut.iy.itemmaster.serviceimpl.base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.ConstantsDB;
import cn.com.bbut.iy.itemmaster.dao.IyDptMapper;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.ConditionDTO;
import cn.com.bbut.iy.itemmaster.dto.base.DptResourceDTO;
import cn.com.bbut.iy.itemmaster.dto.base.LabelDTO;
import cn.com.bbut.iy.itemmaster.entity.base.IyDpt;
import cn.com.bbut.iy.itemmaster.entity.base.IyDptExample;
import cn.com.bbut.iy.itemmaster.service.CommonService;
import cn.com.bbut.iy.itemmaster.service.base.DptService;
import cn.com.bbut.iy.itemmaster.service.base.role.IyRoleService;
import cn.shiy.common.pmgr.entity.Resource;
import cn.shiy.common.pmgr.entity.ResourceGroup;

/**
 * @author songxz
 */
@Slf4j
@Service
public class DptServiceImpl implements DptService {

    @Autowired
    IyDptMapper dptMapper;
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private IyRoleService iyRoleService;
    @Autowired
    private CommonService commonService;

    @Override
    public List<AutoCompleteDTO> getDptAutoCompleteByParam(String param) {
        IyDptExample example = new IyDptExample();
        example.or().andDptLike("%" + param + "%").andDptNotLike("%9%");
        example.or().andDptNameLike("%" + param + "%").andDptNotLike("%9%");
        example.setOrderByClause("dpt");
        List<IyDpt> list = dptMapper.selectByExample(example);
        List<AutoCompleteDTO> rest = new ArrayList<>();
        if (list != null && list.size() > 0) {
            for (IyDpt ls : list) {
                AutoCompleteDTO t = new AutoCompleteDTO();
                t.setK(ls.getDpt());
                t.setV(ls.getDpt() + " " + ls.getDptName().trim());
                rest.add(t);
            }
        }
        List<AutoCompleteDTO> storeDpts = dptMapper.selectDptByStore("%" + param + "%");
        if (storeDpts != null && storeDpts.size() > 0) {
            rest.addAll(storeDpts);
        }
        List<AutoCompleteDTO> staffDpts = dptMapper.selectDptByStaff("%" + param + "%");
        if (staffDpts != null && staffDpts.size() > 0) {
            rest.addAll(staffDpts);
        }
        List<AutoCompleteDTO> tenDpts = dptMapper.selectDptByTen("%" + param + "%");
        if (tenDpts != null && tenDpts.size() > 0) {
            rest.addAll(tenDpts);
        }
        // 排个序
        Collections.sort(rest, new Comparator<AutoCompleteDTO>() {
            public int compare(AutoCompleteDTO o1, AutoCompleteDTO o2) {
                return Integer.parseInt(o1.getK()) - Integer.parseInt(o2.getK());
            }
        });
        // 去个重后返回
        return new ArrayList<AutoCompleteDTO>(new HashSet<AutoCompleteDTO>(rest));
    }

    @Override
    public LabelDTO getDptName(String dpt) {
        IyDptExample example = new IyDptExample();
        example.or().andDptEqualTo(dpt);
        List<IyDpt> dptList = dptMapper.selectByExample(example);
        LabelDTO rest = new LabelDTO();
        if (dptList != null && dptList.size() > 0) {
            IyDpt d = dptList.get(0);
            rest.setCode(d.getDpt());
            rest.setName(d.getDptName().trim());
            rest.setCodeName(d.getDpt() + " " + d.getDptName().trim());
            return rest;
        }
        rest = dptMapper.getDptNameByStore(dpt);
        if (rest != null) {
            return rest;
        }
        rest = dptMapper.getDptNameByStaff(dpt);
        if (rest != null) {
            return rest;
        }
        rest = dptMapper.getDptNameByTen(dpt);
        if (rest != null) {
            return rest;
        }
        return null;
    }

    /**
     * 检索DPT信息
     *
     * @param parentId
     *            上级id
     * @param flg
     *            检索层级标识 0：DPT，1：部，2：事业部
     * @param id
     *            ID
     * @param name
     *            名称
     * @param allFlg
     *            是否包含全事业部 或 全部 或 全dpt，1：包含，2：不包含
     * @return
     */
    @Override
    public List<DptResourceDTO> getDpts(String parentId, Integer flg, String id, String name,
            Integer allFlg) {
        if (flg == null) {
            return null;
        }
        String dptCodeType = null;
        if (flg == ConstantsDB.COMMON_ZERO) {
            dptCodeType = String.valueOf(ConstantsDB.COMMON_ZERO);
        } else if (flg == ConstantsDB.COMMON_ONE) {
            dptCodeType = String.valueOf(ConstantsDB.COMMON_ONE);
        } else if (flg == ConstantsDB.COMMON_TWO) {
            dptCodeType = String.valueOf(ConstantsDB.COMMON_TWO);
        }

        IyDptExample example = new IyDptExample();
        IyDptExample.Criteria cri = example.or();
        cri.andSalesExlFlgEqualTo(String.valueOf(ConstantsDB.COMMON_ZERO)).andDptCodeTypeEqualTo(
                dptCodeType);
        if (StringUtils.isNotBlank(parentId)) {
            if (flg == ConstantsDB.COMMON_ONE) {
                cri.andGrandDivEqualTo(parentId);
            } else if (flg == ConstantsDB.COMMON_ZERO) {
                cri.andDepartmentEqualTo(parentId);
            }
        }
        if (StringUtils.isNotBlank(name)) {
            cri.andDptNameLike("%" + name + "%");
        }
        if (StringUtils.isNotBlank(id)) {
            IyDptExample.Criteria cri1 = example.or();
            cri1.andSalesExlFlgEqualTo(String.valueOf(ConstantsDB.COMMON_ZERO))
                    .andDptCodeTypeEqualTo(dptCodeType);
            if (StringUtils.isNotBlank(parentId)) {
                if (flg == ConstantsDB.COMMON_ONE) {
                    cri1.andGrandDivEqualTo(parentId);
                } else if (flg == ConstantsDB.COMMON_ZERO) {
                    cri1.andDepartmentEqualTo(parentId);
                }
            }
            if (StringUtils.isNotBlank(id)) {
                if (flg == ConstantsDB.COMMON_ZERO) {
                    cri1.andDptLike("%" + id + "%");
                } else if (flg == ConstantsDB.COMMON_ONE) {
                    cri1.andDepartmentLike("%" + id + "%");
                } else if (flg == ConstantsDB.COMMON_TWO) {
                    cri1.andGrandDivLike("%" + id + "%");
                }
            }
        }
        List<IyDpt> dpts = dptMapper.selectByExample(example);
        List<DptResourceDTO> result = new ArrayList<>();
        if (dpts != null && !dpts.isEmpty()) {

            for (IyDpt dpt : dpts) {
                String vid = null;
                if (flg == ConstantsDB.COMMON_ZERO) {
                    vid = dpt.getDpt();
                } else if (flg == ConstantsDB.COMMON_ONE) {
                    vid = dpt.getDepartment();
                } else if (flg == ConstantsDB.COMMON_TWO) {
                    vid = dpt.getGrandDiv();
                }
                DptResourceDTO dto = new DptResourceDTO(vid, vid.concat(" ").concat(
                        dpt.getDptName().trim()));
                result.add(dto);
            }
            if (allFlg != null && allFlg == ConstantsDB.COMMON_ONE) {
                DptResourceDTO dto = null;
                if (flg == ConstantsDB.COMMON_ZERO) {
                    dto = new DptResourceDTO("", ALL_DPT_NAME);
                } else if (flg == ConstantsDB.COMMON_ONE) {
                    dto = new DptResourceDTO("", ALL_DEPARTMENT_NAME);
                } else if (flg == ConstantsDB.COMMON_TWO) {
                    dto = new DptResourceDTO(ALL_GRAND_DIV, ALL_GRAND_DIV_NAME);
                }
                result.add(ConstantsDB.COMMON_ZERO, dto);

            }
        }
        return result;
    }

    @Override
    public IyDpt getIyDPTById(String divId, String departmentId, String dptId) {
        if (StringUtils.isBlank(divId) && StringUtils.isBlank(departmentId)
                && StringUtils.isBlank(dptId)) {
            return null;
        }
        IyDptExample e = new IyDptExample();
        IyDptExample.Criteria c = e.or();
        c.andSalesExlFlgEqualTo(String.valueOf(ConstantsDB.COMMON_ZERO));
        if (StringUtils.isNotBlank(divId)) {
            c.andGrandDivEqualTo(divId);
            c.andDptCodeTypeEqualTo(String.valueOf(ConstantsDB.COMMON_TWO));
        }
        if (StringUtils.isNotBlank(departmentId)) {
            c.andDepartmentEqualTo(departmentId);
            c.andDptCodeTypeEqualTo(String.valueOf(ConstantsDB.COMMON_ONE));
        }
        if (StringUtils.isNotBlank(dptId)) {
            c.andDptEqualTo(dptId);
            c.andDptCodeTypeEqualTo(String.valueOf(ConstantsDB.COMMON_ZERO));
        }
        e.setOrderByClause("dpt desc, department desc, grand_div desc");
        List<IyDpt> dpts = dptMapper.selectByExample(e);
        return dpts != null && dpts.size() > 0 ? dpts.get(ConstantsDB.COMMON_ZERO) : null;
    }

    /**
     * 检索DPT信息
     *
     * @param id
     *            ID
     * @param name
     *            名称
     * @param pCode
     *            权限
     * @return
     */
    @Override
    public List<AutoCompleteDTO> getDpts(String id, String name, String pCode) {
        if (StringUtils.isBlank(name) && StringUtils.isBlank(id)) {
            return new ArrayList<>();
        }
        HttpSession session = request.getSession();// 从session中拿到当前登录人的角色id集合
        List<Integer> roleIds = (List<Integer>) session.getAttribute(Constants.SESSION_ROLES);
        Collection<ResourceGroup> resouces = iyRoleService.getResourcesByRoleAndPCode(roleIds,
                pCode);
        if (resouces == null || resouces.isEmpty()) {
            return new ArrayList<>();
        }
        ConditionDTO conditon = iyRoleService.createConditionsFromResourceGroup(resouces);
        Map<String, Object> map = new ConcurrentHashMap<>();

        map.put("name", "%" + name + "%");
        map.put("dpt", "%" + id + "%");
        map.put("condition", conditon.getConditionDpt());
        List<AutoCompleteDTO> dtos = dptMapper.getDptsInResource(map);

        return dtos;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<AutoCompleteDTO> getDivisionByPrmission(String pCode) {
        List<Integer> roleIds = (List<Integer>) commonService.getSessionUserRoleIds();
        Collection<ResourceGroup> resouces = iyRoleService.getResourcesByRoleAndPCode(roleIds,
                pCode);
        List<String> dpt = null;
        if (resouces != null && resouces.size() > 0) {
            // 拿到dpt资源 资源类型为事业部
            dpt = new ArrayList<>();
            for (ResourceGroup rGroup : resouces) {
                for (Resource rs : rGroup.getGroup()) {
                    switch (rs.getType()) {
                    case ConstantsDB.COMMON_ONE:
                        if (!ConstantsDB.ALL_DPT.equals(rs.getId())) {
                            dpt.add(rs.getId());
                        }
                        break;
                    }
                }
            }
        }
        List<AutoCompleteDTO> list = dptMapper.getDivisionByPrmission(dpt);
        return list;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<AutoCompleteDTO> getDepartmentByPrmission(String division, String pCode) {
        List<Integer> roleIds = (List<Integer>) commonService.getSessionUserRoleIds();
        Collection<ResourceGroup> resouces = iyRoleService.getResourcesByRoleAndPCode(roleIds,
                pCode);
        List<String> dpt = null;
        if (resouces != null && resouces.size() > 0) {
            // 拿到dpt资源 资源类型为部
            dpt = new ArrayList<>();
            for (ResourceGroup rGroup : resouces) {
                for (Resource rs : rGroup.getGroup()) {
                    switch (rs.getType()) {
                    case ConstantsDB.COMMON_TWO:
                        if (rs.getId() != null) {
                            dpt.add(rs.getId());
                        }
                        break;
                    }
                }
            }
        }

        Map<String, Object> selectMap = new HashMap<String, Object>();
        selectMap.put("division", division);
        selectMap.put("resource", dpt);
        List<AutoCompleteDTO> list = dptMapper.getDepartmentByPrmission(selectMap);
        return list;
    }

    @Override
    public List<AutoCompleteDTO> getDptByPrmission(String division, String department, String pCode) {
        List<Integer> roleIds = (List<Integer>) commonService.getSessionUserRoleIds();
        Collection<ResourceGroup> resouces = iyRoleService.getResourcesByRoleAndPCode(roleIds,
                pCode);
        List<String> dpt = null;
        if (resouces != null && resouces.size() > 0) {
            // 拿到dpt资源 资源类型为DPT
            dpt = new ArrayList<>();
            for (ResourceGroup rGroup : resouces) {
                for (Resource rs : rGroup.getGroup()) {
                    switch (rs.getType()) {
                    case ConstantsDB.COMMON_THREE:
                        if (rs.getId() != null) {
                            dpt.add(rs.getId());
                        }
                        break;
                    }
                }
            }
        }

        Map<String, Object> selectMap = new HashMap<String, Object>();
        selectMap.put("division", division);
        selectMap.put("department", department);
        selectMap.put("resource", dpt);
        List<AutoCompleteDTO> list = dptMapper.getDptByPrmission(selectMap);
        return list;
    }
}
