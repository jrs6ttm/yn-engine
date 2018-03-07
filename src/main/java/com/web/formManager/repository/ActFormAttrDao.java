package com.web.formManager.repository;

import java.util.List;

import com.web.formManager.entity.ActFormAttr;

public interface ActFormAttrDao {
    int deleteByPrimaryKey(ActFormAttr record);

    int insert(ActFormAttr record);

    int insertSelective(ActFormAttr record);
    
    int insertBatch(List<ActFormAttr> recordList);

    ActFormAttr selectByPrimaryKey(String attrId);
    
    List<ActFormAttr> getFormAttrList(ActFormAttr record);

    int updateByPrimaryKeySelective(ActFormAttr record);

    int updateByPrimaryKey(ActFormAttr record);
}