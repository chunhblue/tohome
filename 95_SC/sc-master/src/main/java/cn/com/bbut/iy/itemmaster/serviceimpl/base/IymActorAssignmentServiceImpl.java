package cn.com.bbut.iy.itemmaster.serviceimpl.base;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.bbut.iy.itemmaster.constant.ConstantsDB;
import cn.com.bbut.iy.itemmaster.dao.IymActorAssignmentInfoMapper;
import cn.com.bbut.iy.itemmaster.dao.IymActorAssignmentRecordMapper;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.base.actorAssignment.ActorAssGridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.base.actorAssignment.ActorAssGridParamDTO;
import cn.com.bbut.iy.itemmaster.entity.base.IymActorAssignmentInfo;
import cn.com.bbut.iy.itemmaster.entity.base.IymActorAssignmentInfoExample;
import cn.com.bbut.iy.itemmaster.entity.base.IymActorAssignmentRecord;
import cn.com.bbut.iy.itemmaster.entity.base.IymRoleRemark;
import cn.com.bbut.iy.itemmaster.exception.SystemRuntimeException;
import cn.com.bbut.iy.itemmaster.service.UserService;
import cn.com.bbut.iy.itemmaster.service.base.IymActorAssignmentService;
import cn.com.bbut.iy.itemmaster.service.base.role.IyRoleService;
import cn.shiy.common.baseutil.Container;
import cn.shiy.common.pmgr.dao.ActorAssignmentMapper;
import cn.shiy.common.pmgr.entity.ActorAssignment;
import cn.shiy.common.pmgr.entity.ActorAssignmentExample;
import cn.shiy.common.pmgr.entity.Role;
import cn.shiy.common.pmgr.service.PermissionService;

@Service
public class IymActorAssignmentServiceImpl implements IymActorAssignmentService {

	@Autowired
	private ActorAssignmentMapper mapper;
	@Autowired
    private IymActorAssignmentInfoMapper infoMapper;
    @Autowired
    private IymActorAssignmentRecordMapper recordMapper;
	@Autowired
    private IyRoleService iyRoleService;
    @Autowired
    private PermissionService permService;
	@Autowired
	private UserService userService;

	/**
	 * 检索特殊授权列表
	 *
	 * @param param
	 * @return
	 */
	@Override
	public GridDataDTO<ActorAssGridDataDTO> getGridList(ActorAssGridParamDTO param) {

		ActorAssignmentExample e = new ActorAssignmentExample();
		ActorAssignmentExample.Criteria cri = e.or();
        if (param.getRoleId() != null) {
            // List<Integer> roleIds =
            // iyRoleService.getRoleIdsLikeName(param.getRoleName());
            cri.andRoleIdEqualTo(param.getRoleId());
		}
		if (StringUtils.isNotBlank(param.getUserId())) {
			cri.andActorIdLike("%" + param.getUserId() + "%");
		}
		if (param.getAssType() != null) {
			List<Integer> ids = infoMapper.getActorAssIdsByInfo(param.getAssType());
			if (ids != null && !ids.isEmpty()) {
				cri.andIdIn(ids);
			} else {
				cri.andIdEqualTo(-1);
			}
		}

		e.setOrderByClause(param.getOrderByClause());
		e.setLimitStart(param.getLimitStart());
		e.setLimitEnd(param.getLimitEnd());
		List<ActorAssignment> aas = mapper.selectByExample(e);
		e.setLimitStart(-1);
		e.setLimitEnd(-1);
		long count = mapper.countByExample(e);
		IymActorAssignmentService service = Container.getBean(IymActorAssignmentService.class);
		List<ActorAssGridDataDTO> dtos = new ArrayList<>();
		if (aas != null && aas.size() > 0) {
			for (ActorAssignment aa : aas) {
				ActorAssGridDataDTO dto = new ActorAssGridDataDTO();
				dto.setId(aa.getId());
                Role role = iyRoleService.getRoleById(aa.getRoleId());
                IymRoleRemark remark = iyRoleService.getRoleRemarkByRoleId(aa.getRoleId());
                dto.setRoleId(role.getId());
				dto.setRoleName(role.getName());
                if (remark != null && StringUtils.isNotBlank(remark.getRemark())) {
                    dto.setRemark(remark.getRemark());
                }
				dto.setUserId(aa.getActorId());
                // User user = userService.getFullUserById(aa.getActorId());
                dto.setUserName(userService.getIyedStaffNameByUserId(aa.getActorId()));
				IymActorAssignmentInfo info = service
						.getIymActorAssignmentInfoByActorAssignmentId(aa.getId());
				if (info != null) {
					dto.setStartDate(info.getStartDate());
					dto.setEndDate(info.getEndDate());
				}
				dtos.add(dto);
			}
		}

		return new GridDataDTO<ActorAssGridDataDTO>(dtos, param.getPage(), count, param.getRows());
	}

	/**
     * 添加新授权
     *
     * @param aa
     * @return
     */
    @Override
    public Integer addActorAssignment(ActorAssignment aa) {
        if (StringUtils.isBlank(aa.getActorId()) || aa.getRoleId() == null) {
            return null;
        }
        Integer count = mapper.insert(aa);
        ActorAssignmentExample e = new ActorAssignmentExample();
        e.or().andActorIdEqualTo(aa.getActorId()).andRoleIdEqualTo(aa.getRoleId());
        Integer id = mapper.selectByExample(e).get(0).getId();
        if (count > 0) {
            return id;
        }
        return null;
    }

    /**
     * 修改新授权
     *
     * @param aa
     * @return
     */
    @Override
    public boolean updateActorAssignment(ActorAssignment aa) {
        if (aa.getRoleId() == null
                || StringUtils.isBlank(aa.getActorId()) && aa.getRoleId() == null) {
            return false;
        }

        ActorAssignmentExample e = new ActorAssignmentExample();
        e.or().andIdEqualTo(aa.getId());
        Integer count = mapper.updateByExampleSelective(aa, e);
        if (count > 0) {
            return true;
        }
        return false;
    }

	/**
	 * 检索特殊授权代审信息
	 *
	 * @param aaId
	 * @return
	 */
	@Override
	public IymActorAssignmentInfo getIymActorAssignmentInfoByActorAssignmentId(Integer aaId) {
		if (aaId == null) {
			return null;
		}
		IymActorAssignmentInfoExample e = new IymActorAssignmentInfoExample();
		e.or().andRoleIdEqualTo(aaId);
		List<IymActorAssignmentInfo> infos = infoMapper.selectByExample(e);
		if (infos != null && !infos.isEmpty()) {
			return infos.get(ConstantsDB.COMMON_ZERO);
		}
		return null;
	}

    /**
     * 删除特殊授权的代审信息
     *
     * @param aaId
     * @return
     */
    @Override
    public boolean delActorAssignmentInfo(Integer aaId) {
        if (aaId == null) {
            return false;
        }
        IymActorAssignmentInfoExample e = new IymActorAssignmentInfoExample();
        e.or().andRoleIdEqualTo(aaId);
        long count = infoMapper.countByExample(e);
        int delCount = infoMapper.deleteByExample(e);
        if (delCount == count) {
            return true;
        }
        return false;
    }

    /**
     * 删除特殊授权信息
     *
     * @param aaId
     * @return
     */
    @Override
    public boolean delActorAssignment(Integer aaId) {
        if (aaId == null) {
            return false;
        }
        IymActorAssignmentService service = Container.getBean(IymActorAssignmentService.class);

        boolean b1 = service.delActorAssignmentInfo(aaId);
        ActorAssignmentExample e = new ActorAssignmentExample();
        e.or().andIdEqualTo(aaId);
        long count = mapper.countByExample(e);
        int delCount = mapper.deleteByExample(e);
        if (count == delCount && b1) {
            return true;
        }
        return false;
    }

    /**
     * 更新特殊授权信息
     *
     * @param aa
     * @param info
     * @return
     */
    @Override
    public boolean updateActorAssignment(ActorAssignment aa, IymActorAssignmentInfo info) {
        IymActorAssignmentService service = Container.getBean(IymActorAssignmentService.class);

        Integer id = null;
        Integer operationFlg = ConstantsDB.COMMON_ZERO;
        if (aa.getId() == null) {
            id = service.addActorAssignment(aa);
            if (id == null) {
                throw new SystemRuntimeException("添加特殊授权失败");
            }
            aa.setId(id);
        } else {
            boolean b1 = service.updateActorAssignment(aa);
            if (!b1) {
                throw new SystemRuntimeException("修改特殊授权失败");
            }
            boolean b2 = service.delActorAssignmentInfo(aa.getId());
            if (!b2) {
                throw new SystemRuntimeException("删除特殊授权信息失败");
            }
            id = aa.getId();
            operationFlg = ConstantsDB.COMMON_ONE;
        }
        if (info != null && info.getStartDate() != null) {
            boolean b = service.addActorAssignmentInfo(id, info);
            if (!b) {
                throw new SystemRuntimeException("添加特殊授权信息失败");
            }
            IymActorAssignmentRecord record = new IymActorAssignmentRecord();
            record.setActorAssId(id);
            record.setOldUserId(info.getAssUserId());
            record.setNewUserId(aa.getActorId());
            record.setRoleId(aa.getRoleId());
            record.setOperationFlg(operationFlg);
            record.setUpdateTime(info.getUpdateTime());
            record.setUpdateUserid(info.getUpdateUserid());
            record.setStartTime(info.getStartDate());
            record.setEndTime(info.getEndDate());
            boolean recordBoo = service.addRecord(record);
            if (!recordBoo) {
                throw new SystemRuntimeException("添加特殊授权记录信息失败");
            }
        }

        return true;
    }

    /**
     * 插入特殊授权信息
     *
     * @param aaId
     * @param info
     * @return
     */
    @Override
    public boolean addActorAssignmentInfo(Integer aaId, IymActorAssignmentInfo info) {
        if (aaId == null) {
            return false;
        }
        info.setRoleId(aaId);
        int count = infoMapper.insert(info);
        return count > 0 ? true : false;
    }

    /**
     * 删除特殊授权及授权代审信息
     *
     * @param aaId
     * @return
     */
    @Override
    public boolean delActorAssAndInfo(Integer aaId) {
        if (aaId == null) {
            return false;
        }
        IymActorAssignmentService service = Container.getBean(IymActorAssignmentService.class);
        ActorAssignment aa = service.getActorAssignmentById(aaId);
        IymActorAssignmentInfo info = service.getIymActorAssignmentInfoByActorAssignmentId(aaId);
        if (info != null) {
            IymActorAssignmentRecord record = new IymActorAssignmentRecord();
            record.setActorAssId(aaId);
            record.setOldUserId(info.getAssUserId());
            record.setNewUserId(aa.getActorId());
            record.setRoleId(aa.getRoleId());
            record.setOperationFlg(ConstantsDB.COMMON_TWO);
            record.setUpdateTime(info.getUpdateTime());
            record.setUpdateUserid(info.getUpdateUserid());
            record.setStartTime(info.getStartDate());
            record.setEndTime(info.getEndDate());
            boolean recordBoo = service.addRecord(record);
            if (!recordBoo) {
                throw new SystemRuntimeException("添加特殊授权记录信息失败");
            }
        }
        boolean b = service.delActorAssignment(aaId);
        if (!b) {
            throw new SystemRuntimeException("取消特殊授权失败");
        }
        b = service.delActorAssignmentInfo(aaId);
        if (!b) {
            throw new SystemRuntimeException("取消特殊授权的代审信息失败");
        }
        return true;
    }

    /**
     * 提供根据用户id得到特殊角色id集合的方法（不包含代审权限）
     *
     * @param actorId
     * @return
     */
    @Override
    public Collection<Integer> getSpecialRoleIdsByActorId(String actorId) {
        return this.getRoleIdsByActorId(actorId, ConstantsDB.COMMON_ONE, null);
    }

    /**
     * 提供根据用户id得到特殊角色id集合的方法（只包含代审权限）
     *
     * @param actorId
     * @return
     */
    @Override
    public Collection<Integer> getSpecialAlternateRoleIdsByActorId(String actorId) {
        return this.getRoleIdsByActorId(actorId, ConstantsDB.COMMON_TWO, null);
    }

    /**
     * 提供根据用户id得到特殊角色id集合的方法（包含正常和代审）
     *
     * @param actorId
     * @return
     */
    @Override
    public Collection<Integer> getAllRoleIdsByActorId(String actorId) {
        return this.getRoleIdsByActorId(actorId, ConstantsDB.COMMON_ZERO, null);
    }

    /**
     * 提供根据用户id得到特殊角色id集合的方法（只包含代审权限且当前日期在代审时间段内）
     *
     * @param actorId
     * @param date
     * @return
     */
    @Override
    public Collection<Integer> getSpecialAlternateRoleIdsByActorId(String actorId, Date date) {
        return this.getRoleIdsByActorId(actorId, ConstantsDB.COMMON_TWO, date);
    }

    /**
     * 判断是否不存在重复的授权
     *
     * @param userId
     * @param roleId
     * @return
     */
    @Override
    public boolean isNotExistActorAssignment(String userId, Integer roleId) {
        if (StringUtils.isBlank(userId) || roleId == null) {
            return false;
        }
        ActorAssignmentExample e = new ActorAssignmentExample();
        e.or().andActorIdEqualTo(userId).andRoleIdEqualTo(roleId);
        List<ActorAssignment> aas = mapper.selectByExample(e);
        if (aas != null && !aas.isEmpty()) {
            int count = ConstantsDB.COMMON_ZERO;
            for (ActorAssignment aa : aas) {
                IymActorAssignmentInfoExample example = new IymActorAssignmentInfoExample();
                example.or().andRoleIdEqualTo(aa.getId());
                long infocount = infoMapper.countByExample(example);
                if (infocount == ConstantsDB.COMMON_ZERO) {
                    count++;
                    break;
                }
            }
            return count > ConstantsDB.COMMON_ZERO ? false : true;
        }
        return true;
    }

    /**
     * 判断是否不存在重复的代审授权
     *
     * @param assUserId
     * @param roleId
     * @return
     */
    @Override
    public boolean isNotExistActorAssignmentInfo(Integer id, String assUserId, Integer roleId) {
        if (StringUtils.isBlank(assUserId) || roleId == null) {
            return false;
        }
        Map<String, Object> map = new ConcurrentHashMap<>();
        map.put("assUserId", assUserId);
        map.put("roleId", roleId);
        if (id != null) {
            map.put("id", id);
        }
        List<Integer> ids = infoMapper.getActorAssIdsByAssUserIdAndRoleId(map);
        return ids != null && ids.size() > 0 ? false : true;
    }

    /**
     * 更新代审授权的状态为超期
     *
     * @param date
     * @return
     */
    @Override
    public boolean updateReviewRoleStatus(Date date) {
        if (date == null) {
            return false;
        }
        IymActorAssignmentInfoExample example = new IymActorAssignmentInfoExample();
        example.or().andEndDateLessThan(date).andStatusEqualTo(ConstantsDB.COMMON_ZERO);
        IymActorAssignmentInfo info = new IymActorAssignmentInfo();
        info.setStatus(ConstantsDB.COMMON_ONE);
        Long count = infoMapper.countByExample(example);
        if (count != null && count > 0) {
            int updateCount = infoMapper.updateByExampleSelective(info, example);
            if (count.intValue() != updateCount) {
                return false;
            }
        }
        return true;
    }

    /**
     * 添加历史记录信息
     *
     * @param record
     * @return
     */
    @Override
    public boolean addRecord(IymActorAssignmentRecord record) {
        if (record == null) {
            return false;
        }
        int count = recordMapper.insert(record);
        return count > 0 ? true : false;
    }

    /**
     * 根据id检索用户授权信息
     *
     * @param id
     * @return
     */
    @Override
    public ActorAssignment getActorAssignmentById(Integer id) {
        if (id == null) {
            return null;
        }
        ActorAssignmentExample example = new ActorAssignmentExample();
        example.or().andIdEqualTo(id);
        List<ActorAssignment> aas = mapper.selectByExample(example);
        if (aas != null && aas.size() == ConstantsDB.COMMON_ONE) {
            return aas.get(ConstantsDB.COMMON_ZERO);
        }
        return null;
    }

    /**
     * 提供根据用户id得到特殊角色id集合的方法
     * 
     * @param actorId
     *            用户id
     * @param roleType
     *            包含角色类型，0：全部，1：正常，2：代审
     * @return
     */
    private Collection<Integer> getRoleIdsByActorId(String actorId, Integer roleType, Date date) {
        if (StringUtils.isBlank(actorId)) {
            return null;
        }
        IymActorAssignmentService service = Container.getBean(IymActorAssignmentService.class);
        ActorAssignmentExample e = new ActorAssignmentExample();
        e.or().andActorIdEqualTo(actorId);
        List<ActorAssignment> aas = mapper.selectByExample(e);
        if (aas != null && !aas.isEmpty()) {
            Set<Integer> set = new HashSet<>();
            for (ActorAssignment x : aas) {
                IymActorAssignmentInfo aai = null;
                if (roleType != ConstantsDB.COMMON_ZERO) {
                    aai = service.getIymActorAssignmentInfoByActorAssignmentId(x.getId());
                    if (date != null) {
                        if (aai != null && (date.before(aai.getStartDate())
                                || date.after(aai.getEndDate()))) {
                            aai = null;
                        }
                    }
                }
                switch (roleType) {
                case ConstantsDB.COMMON_ZERO:
                    set.add(x.getRoleId());
                    break;
                case ConstantsDB.COMMON_ONE:
                    if (aai == null)
                        set.add(x.getRoleId());
                    break;
                case ConstantsDB.COMMON_TWO:
                    if (aai != null)
                        set.add(x.getRoleId());
                    break;
                }
            }
            return set;
        }
        return null;
    }
}
