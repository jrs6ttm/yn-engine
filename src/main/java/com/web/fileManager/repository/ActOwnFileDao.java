package com.web.fileManager.repository;

import java.util.List;

import com.core.base.mybatis.annotation.YNRepository;
import com.core.base.mybatis.dao.YNBaseDao;
import com.web.fileManager.entity.ActOwnFile;

@YNRepository
public interface ActOwnFileDao extends YNBaseDao<ActOwnFile, String>{
    
    
    List<ActOwnFile> selectOwnFiles(ActOwnFile record);
    int updateByPrimaryKey(ActOwnFile record);
    
}