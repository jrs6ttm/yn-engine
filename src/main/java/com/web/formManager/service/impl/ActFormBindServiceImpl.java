package com.web.formManager.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.core.base.mybatis.dao.YNBaseDao;
import com.core.base.mybatis.service.YNBaseServiceImpl;
import com.web.formManager.entity.ActFormBind;
import com.web.formManager.repository.ActFormBindDao;
import com.web.formManager.service.IActFormBindService;

@Service("actFormBindService")
public class ActFormBindServiceImpl extends YNBaseServiceImpl<ActFormBind, String> implements IActFormBindService {

	@Resource
	private ActFormBindDao actFormBindDao;
	
	/**  
	 * @Title: getDao 
	 * @Description: TODO  
	 * @return
	 */
	@Override
	public YNBaseDao<ActFormBind, String> getDao() {
		return actFormBindDao;
	}
	
	@Override
	public List<ActFormBind> getFormBindList(ActFormBind record) {
		return actFormBindDao.getFormBindList(record);
	}

}
