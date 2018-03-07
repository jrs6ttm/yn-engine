package com.web.fileManager.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.web.fileManager.entity.ActOwnFile;
import com.web.fileManager.repository.ActOwnFileDao;
import com.web.fileManager.service.IActOwnFileService;

@Service("actOwnFileService")
public class ActOwnFileServiceImpl implements IActOwnFileService {

	@Resource
	private ActOwnFileDao actOwnFileDao;
	
	@Override
	public int deleteByPrimaryKey(String fileid) {
		return actOwnFileDao.deleteByPrimaryKey(fileid);
	}

	@Override
	public int insertSelective(ActOwnFile record) {
		return actOwnFileDao.insertSelective(record);
		//return actOwnFileDao.insert(record);
	}

	@Override
	public ActOwnFile selectByPrimaryKey(String fileid) {
		return actOwnFileDao.selectByPrimaryKey(fileid);
	}

	@Override
	public int updateByPrimaryKeySelective(ActOwnFile record) {
		return actOwnFileDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public List<ActOwnFile> selectOwnFiles(ActOwnFile record) {
		return actOwnFileDao.selectOwnFiles(record);
	}

}
