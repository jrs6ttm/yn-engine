package com.yineng.dev_V_3_0.dao;

import com.core.base.mybatis.annotation.YNRepository;
import com.yineng.dev_V_3_0.model.CourseOrg;

@YNRepository
public interface CourseOrgMapper {
    int deleteByPrimaryKey(String lrnscnOrgId);

    int insert(CourseOrg record);

    int insertSelective(CourseOrg record);

    CourseOrg selectByPrimaryKey(String lrnscnOrgId);

    int updateByPrimaryKeySelective(CourseOrg record);

    int updateByPrimaryKey(CourseOrg record);
}