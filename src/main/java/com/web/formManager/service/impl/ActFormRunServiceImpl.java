package com.web.formManager.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.core.base.mybatis.dao.YNBaseDao;
import com.core.base.mybatis.service.YNBaseServiceImpl;
import com.web.formManager.entity.ActFormRun;
import com.web.formManager.repository.ActFormRunDao;
import com.web.formManager.service.IActFormRunService;

@Service("actFormRunService")
public class ActFormRunServiceImpl extends YNBaseServiceImpl<ActFormRun, String> implements IActFormRunService {

	@Resource
	private ActFormRunDao actFormRunDao;
	
	/**  
	 * @Title: getDao 
	 * @Description: TODO  
	 * @return
	 */
	@Override
	public YNBaseDao<ActFormRun, String> getDao() {
		return actFormRunDao;
	}
	
	@Override
	public List<ActFormRun> getFormRunList(ActFormRun record) {
		return actFormRunDao.getFormRunList(record);
	}

}
