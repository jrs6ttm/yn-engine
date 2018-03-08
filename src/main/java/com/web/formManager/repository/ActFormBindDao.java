package com.web.formManager.repository;

import java.util.List;

import com.core.base.mybatis.annotation.YNRepository;
import com.core.base.mybatis.dao.YNBaseDao;
import com.web.formManager.entity.ActFormBind;

@YNRepository
public interface ActFormBindDao extends YNBaseDao<ActFormBind, String>{

    List<ActFormBind> getFormBindList(ActFormBind record);
}