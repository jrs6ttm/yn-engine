package com.web.fileManager.repository;

import java.util.List;

import com.core.base.mybatis.annotation.YNRepository;
import com.core.base.mybatis.dao.YNBaseDao;
import com.web.fileManager.entity.ActStudyFile;

@YNRepository
public interface ActStudyFileDao extends YNBaseDao<ActStudyFile, String>{
    
    List<ActStudyFile> selectActStudyFile(ActStudyFile record);
}