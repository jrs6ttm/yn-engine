package com.web.fileManager.service;

import java.util.List;

import com.web.fileManager.entity.ActStudyFile;

public interface IActStudyFileService {

	public int deleteByPrimaryKey(String fileid);

	public int insertSelective(ActStudyFile record);

	public ActStudyFile selectByPrimaryKey(String fileid);
	
	List<ActStudyFile> selectActStudyFile(ActStudyFile record);

	public int updateByPrimaryKeySelective(ActStudyFile record);
}
