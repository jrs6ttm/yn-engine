package com.web.fileManager.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.core.base.mybatis.dao.YNBaseDao;
import com.core.base.mybatis.service.YNBaseServiceImpl;
import com.web.fileManager.entity.ActOwnFile;
import com.web.fileManager.repository.ActOwnFileDao;
import com.web.fileManager.service.IActOwnFileService;

@Service("actOwnFileService")
public class ActOwnFileServiceImpl extends YNBaseServiceImpl<ActOwnFile, String> implements IActOwnFileService {

	@Resource
	private ActOwnFileDao actOwnFileDao;
	
	/**  
	 * @Title: getDao 
	 * @Description: TODO  
	 * @return
	 */
	@Override
	public YNBaseDao<ActOwnFile, String> getDao() {
		return actOwnFileDao;
	}
	
	@Override
	public List<ActOwnFile> selectOwnFiles(ActOwnFile record) {
		return actOwnFileDao.selectOwnFiles(record);
	}

}
