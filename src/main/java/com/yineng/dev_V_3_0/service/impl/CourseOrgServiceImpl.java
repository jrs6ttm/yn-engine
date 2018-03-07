package com.yineng.dev_V_3_0.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yineng.dev_V_3_0.dao.CourseOrgMapper;
import com.yineng.dev_V_3_0.model.CourseOrg;
import com.yineng.dev_V_3_0.service.ICourseOrgService;

@Service("courseOrgService")
public class CourseOrgServiceImpl implements ICourseOrgService{
	@Resource
	private CourseOrgMapper courseOrgMapper;

	@Override
	public CourseOrg getCourseOrgById(String lrnscnOrgId) {
		return this.courseOrgMapper.selectByPrimaryKey(lrnscnOrgId);
	}

	@Override
	public int updateByPrimaryKeySelective(CourseOrg record) {
		// TODO Auto-generated method stub
		return this.courseOrgMapper.updateByPrimaryKeySelective(record);
	}
}
