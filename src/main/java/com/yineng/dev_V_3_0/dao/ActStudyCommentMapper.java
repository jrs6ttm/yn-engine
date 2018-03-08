package com.yineng.dev_V_3_0.dao;

import java.util.List;

import com.core.base.mybatis.annotation.YNRepository;
import com.yineng.dev_V_3_0.model.ActStudyComment;

@YNRepository
public interface ActStudyCommentMapper {
    int deleteByPrimaryKey(String cId);

    int insert(ActStudyComment record);

    int insertSelective(ActStudyComment record);

    ActStudyComment selectByPrimaryKey(String cId);
    
    List<ActStudyComment> selectActStudyComments(ActStudyComment record);

    int updateByPrimaryKeySelective(ActStudyComment record);

    int updateByPrimaryKey(ActStudyComment record);
}