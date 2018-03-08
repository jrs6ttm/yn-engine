package com.web.formManager.service;

import java.util.List;

import com.core.base.mybatis.dao.YNBaseDao;
import com.web.formManager.entity.ActFormRun;

public interface IActFormRunService extends YNBaseDao<ActFormRun, String> {
    
    List<ActFormRun> getFormRunList(ActFormRun record);
}
