package com.web.formManager.repository;

import java.util.List;

import com.core.base.mybatis.annotation.YNRepository;
import com.core.base.mybatis.dao.YNBaseDao;
import com.web.formManager.entity.ActFormRun;

@YNRepository
public interface ActFormRunDao extends YNBaseDao<ActFormRun, String>{
    
    List<ActFormRun> getFormRunList(ActFormRun record);
}