package com.web.fileManager.service;

import java.util.List;

import com.core.base.mybatis.service.YNBaseService;
import com.web.fileManager.entity.ActOwnFile;

public interface IActOwnFileService extends YNBaseService<ActOwnFile, String>{

	public List<ActOwnFile> selectOwnFiles(ActOwnFile record);
}
