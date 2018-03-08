package com.yineng.dev_V_3_0.dao;

import java.util.List;
import java.util.Map;

import com.core.base.mybatis.annotation.YNRepository;
import com.yineng.dev_V_3_0.model.CourseOrgUser;

@YNRepository
public interface CourseOrgUserMapper {
    int deleteByPrimaryKey(String lrnscnOrgUserCid);

    int insert(CourseOrgUser record);

    int insertSelective(CourseOrgUser record);

    CourseOrgUser selectByPrimaryKey(String lrnscnOrgUserCid);

    int updateByPrimaryKeySelective(CourseOrgUser record);

    int updateByPrimaryKey(CourseOrgUser record);
    
    List<String> getUsersOfRole(Map<String, String> map);
    
    int checkIsAllUserFinishCourse(String lrnscnOrgId);
}