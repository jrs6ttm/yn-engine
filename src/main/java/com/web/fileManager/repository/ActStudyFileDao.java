package com.web.fileManager.repository;

import java.util.List;

import com.web.fileManager.entity.ActStudyFile;

public interface ActStudyFileDao {
    int deleteByPrimaryKey(String fileid);

    int insert(ActStudyFile record);

    int insertSelective(ActStudyFile record);

    ActStudyFile selectByPrimaryKey(String fileid);
    
    List<ActStudyFile> selectActStudyFile(ActStudyFile record);

    int updateByPrimaryKeySelective(ActStudyFile record);

    int updateByPrimaryKey(ActStudyFile record);
}