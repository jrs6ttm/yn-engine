package com.web.formManager.repository;

import java.util.List;

import com.web.formManager.entity.ActFormRun;

public interface ActFormRunDao {
    int deleteByPrimaryKey(String cId);

    int insert(ActFormRun record);

    int insertSelective(ActFormRun record);

    ActFormRun selectByPrimaryKey(String cId);
    
    List<ActFormRun> getFormRunList(ActFormRun record);

    int updateByPrimaryKeySelective(ActFormRun record);

    int updateByPrimaryKey(ActFormRun record);
}