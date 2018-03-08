package com.web.formManager.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.core.base.mybatis.dao.YNBaseDao;
import com.core.base.mybatis.service.YNBaseServiceImpl;
import com.web.formManager.entity.ActForm;
import com.web.formManager.entity.ActFormAttr;
import com.web.formManager.repository.ActFormAttrDao;
import com.web.formManager.repository.ActFormDao;
import com.web.formManager.service.IActFormService;

@Service("actFormService")
public class ActFormServiceImpl extends YNBaseServiceImpl<ActForm, String> implements IActFormService {

	@Resource
	private ActFormDao actFormDao;
	
	@Resource
	private ActFormAttrDao actFormAttrDao;
	
	/**  
	 * @Title: getDao 
	 * @Description: TODO  
	 * @return
	 */
	@Override
	public YNBaseDao<ActForm, String> getDao() {
		return actFormDao;
	}
	
	@Override
	public String multiplexForm(String formId) {
		ActFormAttr record = new ActFormAttr();
		record.setFormId(formId);
		List<ActFormAttr> formAttrList = this.actFormAttrDao.getFormAttrList(record);
		
		int batchInsertR = 1;
		String newFormId = UUID.randomUUID().toString();
		List<ActFormAttr> recordList = new ArrayList<ActFormAttr>();
		if(formAttrList.size() > 0){
			for(int i=0;i<formAttrList.size();i++){
				ActFormAttr tempFormAttr = formAttrList.get(i);
				tempFormAttr.setAttrId(UUID.randomUUID().toString());
				tempFormAttr.setFormId(newFormId);
				tempFormAttr.setCreateTime(this.getDateStr(null, i*1000));
				tempFormAttr.setLastUpdateTime(tempFormAttr.getCreateTime());
				//System.out.println(tempFormAttr.getLabel());
				recordList.add(tempFormAttr);
				/*
				int tempInsert = this.formAttrDao.insertSelective(tempFormAttr);
				if(tempInsert <= 0){
					batchInsertR = 0;
					break;
				}
				*/
			}
			
			//batch insert
			try{
		    batchInsertR = this.actFormAttrDao.insertBatch(recordList);
			}catch(Exception e){
				System.out.println(e);
			}
		}
		
		if(batchInsertR > 0){
			ActForm actForm = actFormDao.selectByPrimaryKey(formId);
			actForm.setFormId(newFormId);
			actForm.setFormStatus("0");
			actForm.setCreateTime(this.getDateStr(null, 0));
			actForm.setLastUpdateTime(actForm.getCreateTime());
			
			int insertFormR = actFormDao.insertSelective(actForm);
			if(insertFormR <= 0)
				newFormId = "error";
		}
		
		return newFormId;
	}

	@Override
	public ActForm checkFormName(String formName) {
		return actFormDao.checkFormName(formName);
	}

	@Override
	public List<ActForm> getFormList(ActForm record) {
		return actFormDao.getFormList(record);
	}

	public String getDateStr(Date date, int addSecond){
	    if(date == null){
		   date = new Date();
	    }
	
	    Date destDate = new Date(date.getTime() + addSecond);
	    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
	    return sf.format(destDate);
	}
	
}
