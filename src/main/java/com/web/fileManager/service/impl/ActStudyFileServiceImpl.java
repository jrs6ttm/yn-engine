package com.web.fileManager.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.web.fileManager.entity.ActStudyFile;
import com.web.fileManager.repository.ActStudyFileDao;
import com.web.fileManager.service.IActStudyFileService;

@Service("actStudyFileService")
public class ActStudyFileServiceImpl implements IActStudyFileService {

	@Resource
	private ActStudyFileDao actStudyFileDao;
	
	@Override
	public int deleteByPrimaryKey(String fileid) {
		return actStudyFileDao.deleteByPrimaryKey(fileid);
	}

	@Override
	public int insertSelective(ActStudyFile record) {
		return actStudyFileDao.insertSelective(record);
	}

	@Override
	public ActStudyFile selectByPrimaryKey(String fileid) {
		return actStudyFileDao.selectByPrimaryKey(fileid);
	}

	@Override
	public int updateByPrimaryKeySelective(ActStudyFile record) {
		return actStudyFileDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public List<ActStudyFile> selectActStudyFile(ActStudyFile record) {
		return actStudyFileDao.selectActStudyFile(record);
	}


}
