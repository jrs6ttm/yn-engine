package com.yineng.dev_V_3_0.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yineng.dev_V_3_0.dao.CourseOrgStructureMapper;
import com.yineng.dev_V_3_0.model.CourseOrgStructure;
import com.yineng.dev_V_3_0.service.ICourseOrgStructureService;

@Service("courseOrgStructureService")
public class CourseOrgStructureServiceImpl implements
		ICourseOrgStructureService {

	@Resource
	private CourseOrgStructureMapper courseOrgStructureMapper;
	
	@Override
	public List<CourseOrgStructure> getUserGroupOnfo(String userId, String lrnscnOrgId) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("userId", userId);
		map.put("lrnscnOrgId", lrnscnOrgId);
		
		return this.courseOrgStructureMapper.getUserGroupInfo(map);
		
	}

	@Override
	public int updateByPrimaryKeySelective(CourseOrgStructure record) {
		// TODO Auto-generated method stub
		return this.courseOrgStructureMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int checkIsAllGroupFinishCourse(String lrnscnOrgId) {
		// TODO Auto-generated method stub
		return this.courseOrgStructureMapper.checkIsAllGroupFinishCourse(lrnscnOrgId);
	}

}
