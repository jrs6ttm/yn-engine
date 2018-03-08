package com.web.formManager.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.core.base.mybatis.dao.YNBaseDao;
import com.core.base.mybatis.service.YNBaseServiceImpl;
import com.web.formManager.entity.ActFormAttr;
import com.web.formManager.repository.ActFormAttrDao;
import com.web.formManager.service.IActFormAttrService;

@Service("actFormAttrService")
public class ActFormAttrServiceImpl extends YNBaseServiceImpl<ActFormAttr, String> implements IActFormAttrService {

	@Resource
	private ActFormAttrDao actFormAttrDao;
	
	/**  
	 * @Title: getDao 
	 * @Description: TODO  
	 * @return
	 */
	@Override
	public YNBaseDao<ActFormAttr, String> getDao() {
		return actFormAttrDao;
	}
	
	@Override
	public int deleteFormAttr(ActFormAttr record) {
		return actFormAttrDao.deleteFormAttr(record);
	}

	@Override
	public List<ActFormAttr> getFormAttrList(ActFormAttr record) {
		return actFormAttrDao.getFormAttrList(record);
	}

}
