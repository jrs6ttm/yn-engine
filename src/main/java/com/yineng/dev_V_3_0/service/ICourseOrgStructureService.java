package com.yineng.dev_V_3_0.service;

import java.util.List;

import com.yineng.dev_V_3_0.model.CourseOrgStructure;

public interface ICourseOrgStructureService {
	
	public List<CourseOrgStructure> getUserGroupOnfo(String userId, String lrnscnOrgId);
	
	public int updateByPrimaryKeySelective(CourseOrgStructure record); 
	
	public int checkIsAllGroupFinishCourse(String lrnscnOrgId);
	
}
