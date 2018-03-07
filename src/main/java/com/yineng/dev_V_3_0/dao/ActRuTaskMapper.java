package com.yineng.dev_V_3_0.dao;

import com.yineng.dev_V_3_0.model.ActRuTask;

public interface ActRuTaskMapper {
    int deleteByPrimaryKey(String id);

    int insert(ActRuTask record);

    int insertSelective(ActRuTask record);

    ActRuTask selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ActRuTask record);

    int updateByPrimaryKey(ActRuTask record);
}