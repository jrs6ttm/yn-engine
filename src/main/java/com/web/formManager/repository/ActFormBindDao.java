package com.web.formManager.repository;

import java.util.List;

import com.web.formManager.entity.ActFormBind;

public interface ActFormBindDao {
    
    int deleteByPrimaryKey(String formId);

    int insert(ActFormBind record);

    int insertSelective(ActFormBind record);

    ActFormBind selectByPrimaryKey(String cId);

    List<ActFormBind> getFormBindList(ActFormBind record);
    
    int updateByPrimaryKeySelective(ActFormBind record);

    int updateByPrimaryKey(ActFormBind record);
}