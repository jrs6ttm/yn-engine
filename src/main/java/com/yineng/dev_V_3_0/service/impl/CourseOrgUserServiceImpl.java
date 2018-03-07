package com.yineng.dev_V_3_0.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yineng.dev_V_3_0.dao.CourseOrgUserMapper;
import com.yineng.dev_V_3_0.model.CourseOrgUser;
import com.yineng.dev_V_3_0.service.ICourseOrgUserService;

@Service("courseOrgUserService")
public class CourseOrgUserServiceImpl implements ICourseOrgUserService {

	@Resource
	private CourseOrgUserMapper courseOrgUserMapper;
	
	@Override
	public List<String> getUsersOfRole(String groupId, String roleId) {
		// TODO Auto-generated method stub
		Map<String, String> map = new HashMap<String, String>();
		map.put("groupId", groupId);
		map.put("roleId", roleId);
		
		return this.courseOrgUserMapper.getUsersOfRole(map);
	}

	@Override
	public int updateByPrimaryKeySelective(CourseOrgUser record) {
		// TODO Auto-generated method stub
		return this.courseOrgUserMapper.updateByPrimaryKeySelective(record);
	}
	
	@Override
	public int checkIsAllUserFinishCourse(String lrnscnOrgId) {
		// TODO Auto-generated method stub
		return this.courseOrgUserMapper.checkIsAllUserFinishCourse(lrnscnOrgId);
	}

}
