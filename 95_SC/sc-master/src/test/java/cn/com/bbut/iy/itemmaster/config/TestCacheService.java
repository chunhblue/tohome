package cn.com.bbut.iy.itemmaster.config;

import cn.com.bbut.iy.itemmaster.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import cn.shiy.common.pmgr.dao.RoleMapper;
import cn.shiy.common.pmgr.entity.RoleExample;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shiy
 */
@Service
@Slf4j
public class TestCacheService {

	@Autowired
	private RoleMapper roleMapper;

	@Cacheable("cTest1")
	public String getNameById(String id) {
		log.debug("********************* getNameById: {}", id);
		RoleExample ex = new RoleExample();
		ex.or().andNameLike("SY测试%");
		ex.setOrderByClause("id desc");
		roleMapper.selectByExample(ex);
		return id + "name";
	}

	@Cacheable("cTest2")
	public User getUser(User user) {
		return user;
	}
}
