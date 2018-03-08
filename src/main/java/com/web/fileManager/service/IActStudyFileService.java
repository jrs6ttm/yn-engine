package com.web.fileManager.service;

import java.util.List;

import com.core.base.mybatis.service.YNBaseService;
import com.web.fileManager.entity.ActStudyFile;

public interface IActStudyFileService extends YNBaseService<ActStudyFile, String>{
  
	List<ActStudyFile> selectActStudyFile(ActStudyFile record);
}
