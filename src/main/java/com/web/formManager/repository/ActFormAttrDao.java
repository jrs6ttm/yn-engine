package com.web.formManager.repository;

import java.util.List;

import com.core.base.mybatis.annotation.YNRepository;
import com.core.base.mybatis.dao.YNBaseDao;
import com.web.formManager.entity.ActFormAttr;

@YNRepository
public interface ActFormAttrDao extends YNBaseDao<ActFormAttr, String>{
    
	int deleteFormAttr(ActFormAttr record);
	
    int insertBatch(List<ActFormAttr> recordList);
    
    List<ActFormAttr> getFormAttrList(ActFormAttr record);
}