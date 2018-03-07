package com.yineng.dev_V_3_0.service;

import com.yineng.dev_V_3_0.model.CourseOrg;

public interface ICourseOrgService {

	public CourseOrg getCourseOrgById(String lrnscnOrgId);
	
	int updateByPrimaryKeySelective(CourseOrg record);
}
