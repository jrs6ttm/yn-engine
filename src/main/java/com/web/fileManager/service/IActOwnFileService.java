package com.web.fileManager.service;

import java.util.List;

import com.web.fileManager.entity.ActOwnFile;

public interface IActOwnFileService {

	public int deleteByPrimaryKey(String fileid);

	public int insertSelective(ActOwnFile record);

	public ActOwnFile selectByPrimaryKey(String fileid);
	
	public List<ActOwnFile> selectOwnFiles(ActOwnFile record);

	public int updateByPrimaryKeySelective(ActOwnFile record);
}
