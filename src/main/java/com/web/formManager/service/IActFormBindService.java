package com.web.formManager.service;

import java.util.List;

import com.core.base.mybatis.dao.YNBaseDao;
import com.web.formManager.entity.ActFormBind;

public interface IActFormBindService extends YNBaseDao<ActFormBind, String> {
	
    List<ActFormBind> getFormBindList(ActFormBind record);
}
