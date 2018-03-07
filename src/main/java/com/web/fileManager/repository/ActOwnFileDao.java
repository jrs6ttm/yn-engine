package com.web.fileManager.repository;

import java.util.List;

import com.web.fileManager.entity.ActOwnFile;


public interface ActOwnFileDao {
    int deleteByPrimaryKey(String fileid);

    int insert(ActOwnFile record);

    int insertSelective(ActOwnFile record);

    ActOwnFile selectByPrimaryKey(String fileid);
    
    List<ActOwnFile> selectOwnFiles(ActOwnFile record);

    int updateByPrimaryKeySelective(ActOwnFile record);

    int updateByPrimaryKey(ActOwnFile record);
}