package cn.com.bbut.iy.itemmaster.service.base;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.base.actorAssignment.ActorAssGridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.base.actorAssignment.ActorAssGridParamDTO;
import cn.com.bbut.iy.itemmaster.entity.base.IymActorAssignmentInfo;
import cn.com.bbut.iy.itemmaster.entity.base.IymActorAssignmentRecord;
import cn.shiy.common.pmgr.entity.ActorAssignment;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;

@Service
public interface IymActorAssignmentService {

	/**
	 * 检索特殊授权列表
	 * 
	 * @param param
	 * @return
	 */
	GridDataDTO<ActorAssGridDataDTO> getGridList(ActorAssGridParamDTO param);

	/**
     * 添加新授权
     * 
     * @param aa
     *
     * @return
     */
    Integer addActorAssignment(ActorAssignment aa);

    /**
     * 修改新授权
     *
     * @param aa
     * @return
     */
    boolean updateActorAssignment(ActorAssignment aa);

	/**
	 * 检索特殊授权代审信息
	 * 
	 * @param aaId
	 * @return
	 */
	IymActorAssignmentInfo getIymActorAssignmentInfoByActorAssignmentId(Integer aaId);

    /**
     * 删除特殊授权的代审信息
     * 
     * @param aaId
     * @return
     */
    boolean delActorAssignmentInfo(Integer aaId);

    /**
     * 删除特殊授权信息
     *
     * @param aaId
     * @return
     */
    boolean delActorAssignment(Integer aaId);

    /**
     * 更新特殊授权信息
     * 
     * @param aa
     * @param info
     * @return
     */
    boolean updateActorAssignment(ActorAssignment aa, IymActorAssignmentInfo info);

    /**
     * 插入特殊授权信息
     *
     * @param aaId
     * @param info
     * @return
     */
    boolean addActorAssignmentInfo(Integer aaId, IymActorAssignmentInfo info);

    /**
     * 删除特殊授权及授权代审信息
     * 
     * @param aaId
     * @return
     */
    boolean delActorAssAndInfo(Integer aaId);

    /**
     * 提供根据用户id得到特殊角色id集合的方法（不包含代审权限）
     * 
     * @param actorId
     * @return
     */
    Collection<Integer> getSpecialRoleIdsByActorId(String actorId);

    /**
     * 提供根据用户id得到特殊角色id集合的方法（只包含代审权限）
     *
     * @param actorId
     * @return
     */
    Collection<Integer> getSpecialAlternateRoleIdsByActorId(String actorId);

    /**
     * 提供根据用户id得到特殊角色id集合的方法（包含正常和代审）
     *
     * @param actorId
     * @return
     */
    Collection<Integer> getAllRoleIdsByActorId(String actorId);

    /**
     * 提供根据用户id得到特殊角色id集合的方法（只包含代审权限且当前日期在代审时间段内）
     *
     * @param actorId
     * @param date
     * @return
     */
    Collection<Integer> getSpecialAlternateRoleIdsByActorId(String actorId, Date date);

    /**
     * 判断是否不存在重复的授权
     * 
     * @param userId
     * @param roleId
     * @return
     */
    boolean isNotExistActorAssignment(String userId, Integer roleId);

    /**
     * 判断是否不存在重复的代审授权
     *
     * @param id
     * @param assUserId
     * @param roleId
     * @return
     */
    boolean isNotExistActorAssignmentInfo(Integer id, String assUserId, Integer roleId);

    /**
     * 更新代审授权的状态为超期
     * 
     * @param date
     * @return
     */
    boolean updateReviewRoleStatus(Date date);

    /**
     * 添加历史记录信息
     * 
     * @param record
     * @return
     */
    boolean addRecord(IymActorAssignmentRecord record);

    /**
     * 根据id检索用户授权信息
     * 
     * @param id
     * @return
     */
    ActorAssignment getActorAssignmentById(Integer id);

}
