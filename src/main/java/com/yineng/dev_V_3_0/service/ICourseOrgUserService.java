package com.yineng.dev_V_3_0.service;

import java.util.List;

import com.yineng.dev_V_3_0.model.CourseOrgUser;

public interface ICourseOrgUserService {
	
	public int updateByPrimaryKeySelective(CourseOrgUser record);
	
	public List<String> getUsersOfRole(String groupId, String roleId);
	
	public int checkIsAllUserFinishCourse(String lrnscnOrgId);
}
