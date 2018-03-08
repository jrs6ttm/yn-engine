package com.yineng.dev_V_3_0.dao;

import java.util.List;
import java.util.Map;

import com.core.base.mybatis.annotation.YNRepository;
import com.yineng.dev_V_3_0.model.CourseOrgStructure;

@YNRepository
public interface CourseOrgStructureMapper {
    int deleteByPrimaryKey(String orgStructureId);

    int insert(CourseOrgStructure record);

    int insertSelective(CourseOrgStructure record);

    CourseOrgStructure selectByPrimaryKey(String orgStructureId);

    int updateByPrimaryKeySelective(CourseOrgStructure record);

    int updateByPrimaryKey(CourseOrgStructure record);
    
    List<CourseOrgStructure> getUserGroupInfo(Map<String, String> map);
    
    int checkIsAllGroupFinishCourse(String lrnscnOrgId);
}