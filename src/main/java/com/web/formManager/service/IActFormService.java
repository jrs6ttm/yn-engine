package com.web.formManager.service;

import java.util.List;

import com.core.base.mybatis.dao.YNBaseDao;
import com.web.formManager.entity.ActForm;

public interface IActFormService extends YNBaseDao<ActForm, String> {
	
    String multiplexForm(String formid);
    
    ActForm checkFormName(String formName);

    List<ActForm> getFormList(ActForm record);
}
