package com.web.fileManager.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.core.base.mybatis.dao.YNBaseDao;
import com.core.base.mybatis.service.YNBaseServiceImpl;
import com.web.fileManager.entity.ActStudyFile;
import com.web.fileManager.repository.ActStudyFileDao;
import com.web.fileManager.service.IActStudyFileService;

@Service("actStudyFileService")
public class ActStudyFileServiceImpl extends YNBaseServiceImpl<ActStudyFile, String> implements IActStudyFileService {

	@Resource
	private ActStudyFileDao actStudyFileDao;
	
	/**  
	 * @Title: getDao 
	 * @Description: TODO  
	 * @return
	 */
	@Override
	public YNBaseDao<ActStudyFile, String> getDao() {
		return actStudyFileDao;
	}
	
	@Override
	public List<ActStudyFile> selectActStudyFile(ActStudyFile record) {
		return actStudyFileDao.selectActStudyFile(record);
	}

}
