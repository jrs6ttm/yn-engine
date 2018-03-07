package com.yineng.dev_V_3_0.service;

import java.util.List;

import com.yineng.dev_V_3_0.model.ActStudyVar;

public interface IActStudyVarService {
	int deleteByPrimaryKey(String varid);

    int insert(ActStudyVar record);

    int insertSelective(ActStudyVar record);

    ActStudyVar selectByPrimaryKey(String varid);
    
    List<ActStudyVar> selectActStudyVars(ActStudyVar record);

    int updateByPrimaryKeySelective(ActStudyVar record);

    int updateByPrimaryKey(ActStudyVar record);
    
    int updateActStudyVar(ActStudyVar record);
}
