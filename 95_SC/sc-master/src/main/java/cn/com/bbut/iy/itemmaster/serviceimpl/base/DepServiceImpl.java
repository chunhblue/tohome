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

import cn.com.bbut.iy.itemmaster.dao.*;
import cn.com.bbut.iy.itemmaster.entity.ma0070.MA0070;
import cn.com.bbut.iy.itemmaster.entity.ma0070.MA0070Example;
import cn.com.bbut.iy.itemmaster.entity.ma0080.MA0080;
import cn.com.bbut.iy.itemmaster.entity.ma0080.MA0080Example;
import cn.com.bbut.iy.itemmaster.entity.ma0090.MA0090;
import cn.com.bbut.iy.itemmaster.entity.ma0090.MA0090Example;
import cn.com.bbut.iy.itemmaster.entity.ma0100.MA0100;
import cn.com.bbut.iy.itemmaster.entity.ma0100.MA0100Example;
import cn.com.bbut.iy.itemmaster.service.base.DepService;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.ConstantsDB;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.ConditionDTO;
import cn.com.bbut.iy.itemmaster.dto.base.DptResourceDTO;
import cn.com.bbut.iy.itemmaster.dto.base.LabelDTO;
import cn.com.bbut.iy.itemmaster.entity.base.IyDpt;
import cn.com.bbut.iy.itemmaster.entity.base.IyDptExample;
import cn.com.bbut.iy.itemmaster.service.CommonService;
import cn.com.bbut.iy.itemmaster.service.base.role.IyRoleService;
import cn.shiy.common.pmgr.entity.Resource;
import cn.shiy.common.pmgr.entity.ResourceGroup;

/**
 * @author baixg
 */
@Slf4j
@Service
public class DepServiceImpl implements DepService {

    @Autowired
    IyDptMapper dptMapper;
    @Autowired
    MA0070Mapper MA0070Mapper;
    @Autowired
    MA0080Mapper MA0080Mapper;
    @Autowired
    MA0090Mapper MA0090Mapper;
    @Autowired
    MA0100Mapper MA0100Mapper;
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
     * @param depId
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
    public List<DptResourceDTO> getDpts(String depId,String pmaId,String categoryId, Integer flg, String id, String name,
            Integer allFlg) {
        if (flg == null) {
            return null;
        }
        List<DptResourceDTO> result = new ArrayList<>();
        if (flg == ConstantsDB.COMMON_THREE) {
            MA0070Example example = new MA0070Example();
            MA0070Example.Criteria cri = example.or();
            if (StringUtils.isNotBlank(name)) {
                cri.andDepNameLike("%" + name.toLowerCase() + "%");
            }
            MA0070Example.Criteria cri1 = example.or();
            if (StringUtils.isNotBlank(id)) {
                cri1.andDepCdLike("%" + id + "%");
            }
            List<MA0070> deps = MA0070Mapper.selectByExample(example);
            for (MA0070 dep : deps) {
                DptResourceDTO dto = new DptResourceDTO(dep.getDepCd(), dep.getDepCd().concat(" ").concat(
                        dep.getDepName().trim()));
                result.add(dto);
            }
        } else if (flg == ConstantsDB.COMMON_TWO) {
            MA0080Example example = new MA0080Example();
            MA0080Example.Criteria cri = example.or();
            if (StringUtils.isNotBlank(depId)) {
                cri.andDepCdEqualTo(depId);
            }
            if (StringUtils.isNotBlank(name)) {
                cri.andPmNameLike("%" + name.toLowerCase() + "%");
            }
            MA0080Example.Criteria cri1 = example.or();
            if (StringUtils.isNotBlank(depId)) {
                cri1.andDepCdEqualTo(depId);
            }
            if (StringUtils.isNotBlank(id)) {
                cri1.andPmaCdLike("%" + id + "%");
            }
            List<MA0080> pmas = MA0080Mapper.selectByExample(example);
            for (MA0080 pma : pmas) {
                DptResourceDTO dto = new DptResourceDTO(pma.getPmaCd(), pma.getPmaCd().concat(" ").concat(
                        pma.getPmName().trim()));
                result.add(dto);
            }
        } else if (flg == ConstantsDB.COMMON_ONE) {
            MA0090Example example = new MA0090Example();
            MA0090Example.Criteria cri = example.or();
            if (StringUtils.isNotBlank(depId)) {
                cri.andDepCdEqualTo(depId);
            }
            if (StringUtils.isNotBlank(pmaId)) {
                cri.andPmaCdEqualTo(pmaId);
            }
            if (StringUtils.isNotBlank(name)) {
                cri.andCategoryNameLike("%" + name.toLowerCase() + "%");
            }
            MA0090Example.Criteria cri1 = example.or();
            if (StringUtils.isNotBlank(depId)) {
                cri1.andDepCdEqualTo(depId);
            }
            if (StringUtils.isNotBlank(pmaId)) {
                cri1.andPmaCdEqualTo(pmaId);
            }
            if (StringUtils.isNotBlank(id)) {
                cri1.andCategoryCdLike("%" + id + "%");
            }
            List<MA0090> categorys = MA0090Mapper.selectByExample(example);
            for (MA0090 category : categorys) {
                DptResourceDTO dto = new DptResourceDTO(category.getCategoryCd(), category.getCategoryCd().concat(" ").concat(
                        category.getCategoryName().trim()));
                result.add(dto);
            }
        } else if (flg == ConstantsDB.COMMON_ZERO) {
            MA0100Example example = new MA0100Example();
            MA0100Example.Criteria cri = example.or();
            if (StringUtils.isNotBlank(depId)) {
                cri.andDepCdEqualTo(depId);
            }
            if (StringUtils.isNotBlank(pmaId)) {
                cri.andPmaCdEqualTo(pmaId);
            }
            if (StringUtils.isNotBlank(categoryId)) {
                cri.andCategoryCdEqualTo(categoryId);
            }
            if (StringUtils.isNotBlank(name)) {
                cri.andSubCategoryNameLike("%" + name.toLowerCase() + "%");
            }
            MA0100Example.Criteria cri1 = example.or();
            if (StringUtils.isNotBlank(depId)) {
                cri1.andDepCdEqualTo(depId);
            }
            if (StringUtils.isNotBlank(pmaId)) {
                cri1.andPmaCdEqualTo(pmaId);
            }
            if (StringUtils.isNotBlank(categoryId)) {
                cri1.andCategoryCdEqualTo(categoryId);
            }
            if (StringUtils.isNotBlank(id)) {
                cri1.andSubCategoryCdLike("%" + id + "%");
            }
            List<MA0100> subCategorys = MA0100Mapper.selectByExample(example);
            for (MA0100 subCategory : subCategorys) {
                DptResourceDTO dto = new DptResourceDTO(subCategory.getSubCategoryCd(), subCategory.getSubCategoryCd().concat(" ").concat(
                        subCategory.getSubCategoryName().trim()));
                result.add(dto);
            }
        }
        if (allFlg != null && allFlg == ConstantsDB.COMMON_ONE) {
            DptResourceDTO dto = null;
            if (flg == ConstantsDB.COMMON_ZERO) {
                dto = new DptResourceDTO("", ALL_SUB_CATEGORY_NAME);
            } else if (flg == ConstantsDB.COMMON_ONE) {
                dto = new DptResourceDTO("", ALL_CATEGORY_NAME);
            } else if (flg == ConstantsDB.COMMON_TWO) {
                dto = new DptResourceDTO("", ALL_PMA_NAME);
            } else if (flg == ConstantsDB.COMMON_THREE) {
                dto = new DptResourceDTO(ALL_DEP, ALL_DEP_NAME);
            }
            result.add(ConstantsDB.COMMON_ZERO, dto);
        }
        return result;
    }

    @Override
    public MA0070 getDepById(String depId) {
        if (StringUtils.isBlank(depId)) {
            return null;
        }
        MA0070Example e = new MA0070Example();
        MA0070Example.Criteria c = e.or();
        if (StringUtils.isNotBlank(depId)) {
            c.andDepCdEqualTo(depId);
        }
        e.setOrderByClause("dep_cd");
        List<MA0070> deps = MA0070Mapper.selectByExample(e);
        return deps != null && deps.size() > 0 ? deps.get(ConstantsDB.COMMON_ZERO) : null;
    }

    @Override
    public MA0080 getPmaById(String depId,String pmaId) {
        if (StringUtils.isBlank(pmaId)) {
            return null;
        }
        MA0080Example e = new MA0080Example();
        MA0080Example.Criteria c = e.or();
        if (StringUtils.isNotBlank(depId)) {
            c.andDepCdEqualTo(depId);
        }
        if (StringUtils.isNotBlank(pmaId)) {
            c.andPmaCdEqualTo(pmaId);
        }
        e.setOrderByClause("pma_cd");
        List<MA0080> deps = MA0080Mapper.selectByExample(e);
        return deps != null && deps.size() > 0 ? deps.get(ConstantsDB.COMMON_ZERO) : null;
    }

    @Override
    public MA0090 getCategoryById(String depId,String pmaId,String categoryId) {
        if (StringUtils.isBlank(categoryId)) {
            return null;
        }
        MA0090Example e = new MA0090Example();
        MA0090Example.Criteria c = e.or();
        if (StringUtils.isNotBlank(depId)) {
            c.andDepCdEqualTo(depId);
        }
        if (StringUtils.isNotBlank(pmaId)) {
            c.andPmaCdEqualTo(pmaId);
        }
        if (StringUtils.isNotBlank(categoryId)) {
            c.andCategoryCdEqualTo(categoryId);
        }
        e.setOrderByClause("category_cd");
        List<MA0090> deps = MA0090Mapper.selectByExample(e);
        return deps != null && deps.size() > 0 ? deps.get(ConstantsDB.COMMON_ZERO) : null;
    }

    @Override
    public MA0100 getSubCategoryById(String depId,String pmaId,String categoryId,String subCategoryId) {
        if (StringUtils.isBlank(subCategoryId)) {
            return null;
        }
        MA0100Example e = new MA0100Example();
        MA0100Example.Criteria c = e.or();
        if (StringUtils.isNotBlank(depId)) {
            c.andDepCdEqualTo(depId);
        }
        if (StringUtils.isNotBlank(pmaId)) {
            c.andPmaCdEqualTo(pmaId);
        }
        if (StringUtils.isNotBlank(categoryId)) {
            c.andCategoryCdEqualTo(categoryId);
        }
        if (StringUtils.isNotBlank(subCategoryId)) {
            c.andSubCategoryCdEqualTo(subCategoryId);
        }
        e.setOrderByClause("sub_category_cd");
        List<MA0100> deps = MA0100Mapper.selectByExample(e);
        return deps != null && deps.size() > 0 ? deps.get(ConstantsDB.COMMON_ZERO) : null;
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
