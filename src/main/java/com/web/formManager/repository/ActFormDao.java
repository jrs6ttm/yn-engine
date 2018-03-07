package com.web.formManager.repository;

import java.util.List;

import com.web.formManager.entity.ActForm;

public interface ActFormDao {
    int deleteByPrimaryKey(String formId);

    int insert(ActForm record);

    int insertSelective(ActForm record);

    ActForm selectByPrimaryKey(String formId);
    
    ActForm checkFormName(String formName);
    
    List<ActForm> getFormList(ActForm record);

    int updateByPrimaryKeySelective(ActForm record);

    int updateByPrimaryKey(ActForm record);
}