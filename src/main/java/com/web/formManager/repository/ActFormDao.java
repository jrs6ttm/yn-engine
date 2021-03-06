package com.web.formManager.repository;

import java.util.List;

import com.core.base.mybatis.annotation.YNRepository;
import com.core.base.mybatis.dao.YNBaseDao;
import com.web.formManager.entity.ActForm;

@YNRepository
public interface ActFormDao extends YNBaseDao<ActForm, String>{
    
    ActForm checkFormName(String formName);
    
    List<ActForm> getFormList(ActForm record);
}