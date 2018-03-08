package com.web.formManager.service;

import java.util.List;

import com.core.base.mybatis.dao.YNBaseDao;
import com.web.formManager.entity.ActFormAttr;

public interface IActFormAttrService extends YNBaseDao<ActFormAttr, String> {
	
	int deleteFormAttr(ActFormAttr record);
	
	List<ActFormAttr> getFormAttrList(ActFormAttr record);
}
