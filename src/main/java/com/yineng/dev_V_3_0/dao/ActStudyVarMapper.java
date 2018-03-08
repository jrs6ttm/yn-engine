package com.yineng.dev_V_3_0.dao;

import java.util.List;

import com.core.base.mybatis.annotation.YNRepository;
import com.yineng.dev_V_3_0.model.ActStudyVar;

@YNRepository
public interface ActStudyVarMapper {
    int deleteByPrimaryKey(String varId);

    int insert(ActStudyVar record);

    int insertSelective(ActStudyVar record);

    ActStudyVar selectByPrimaryKey(String varId);
    				  
    List<ActStudyVar> selectStudyVarList(ActStudyVar record);

    int updateByPrimaryKeySelective(ActStudyVar record);

    int updateByPrimaryKey(ActStudyVar record);
    
    int updateActStudyVar(ActStudyVar record);
}