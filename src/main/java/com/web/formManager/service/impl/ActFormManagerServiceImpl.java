package com.web.formManager.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.web.formManager.entity.ActForm;
import com.web.formManager.entity.ActFormAttr;
import com.web.formManager.entity.ActFormBind;
import com.web.formManager.entity.ActFormRun;
import com.web.formManager.repository.ActFormAttrDao;
import com.web.formManager.repository.ActFormBindDao;
import com.web.formManager.repository.ActFormDao;
import com.web.formManager.repository.ActFormRunDao;
import com.web.formManager.service.IActFormManagerService;

@Service("actFormManagerService")
public class ActFormManagerServiceImpl implements IActFormManagerService {

	@Resource
	private ActFormAttrDao formAttrDao;
	@Resource
	private ActFormDao formDao;
	@Resource
	private ActFormBindDao formBindDao;
	@Resource
	private ActFormRunDao formRunDao;
	
	@Override
	public int deleteFormAttr(ActFormAttr record) {
		return formAttrDao.deleteByPrimaryKey(record);
	}

	@Override
	public int insertFormAttrSelective(ActFormAttr record) {
		return formAttrDao.insertSelective(record);
	}
	
	@Override
	public int insertFormAttrBatch(List<ActFormAttr> recordList) {
		return formAttrDao.insertBatch(recordList);
	}

	@Override
	public int updateFormAttrSelective(ActFormAttr record) {
		return formAttrDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public ActFormAttr selectFormAttr(String attrid) {
		return formAttrDao.selectByPrimaryKey(attrid);
	}

	@Override
	public List<ActFormAttr> getFormAttrList(ActFormAttr record) {
		return formAttrDao.getFormAttrList(record);
	}

	@Override
	public int deleteForm(String formid) {
		return formDao.deleteByPrimaryKey(formid);
	}

	@Override
	public int insertFormSelective(ActForm record) {
		return formDao.insertSelective(record);
	}

	@Override
	public int updateFormSelective(ActForm record) {
		return formDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public ActForm selectForm(String formid) {
		return formDao.selectByPrimaryKey(formid);
	}
	
	@Override
	public String multiplexForm(String formId) {
		
		ActFormAttr record = new ActFormAttr();
		record.setFormId(formId);
		List<ActFormAttr> formAttrList = this.formAttrDao.getFormAttrList(record);
		
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
		    batchInsertR = this.formAttrDao.insertBatch(recordList);
			}catch(Exception e){
				System.out.println(e);
			}
		}
		
		if(batchInsertR > 0){
			ActForm actForm = this.formDao.selectByPrimaryKey(formId);
			actForm.setFormId(newFormId);
			actForm.setFormStatus("0");
			actForm.setCreateTime(this.getDateStr(null, 0));
			actForm.setLastUpdateTime(actForm.getCreateTime());
			
			int insertFormR = this.formDao.insertSelective(actForm);
			if(insertFormR <= 0)
				newFormId = "error";
		}
		
		return newFormId;
	}
	
	@Override
	public ActForm checkFormName(String formName) {
		return formDao.checkFormName(formName);
	}

	@Override
	public List<ActForm> getFormList(ActForm record) {
		return formDao.getFormList(record);
	}

	@Override
	public int deleteFormBind(String cid) {
		return formBindDao.deleteByPrimaryKey(cid);
	}

	@Override
	public int insertFormBindSelective(ActFormBind record) {
		return formBindDao.insertSelective(record);
	}

	@Override
	public int updateFormBindSelective(ActFormBind record) {
		return formBindDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public ActFormBind selectFormBind(String cid) {
		return formBindDao.selectByPrimaryKey(cid);
	}

	@Override
	public List<ActFormBind> getFormBindList(ActFormBind record) {
		return formBindDao.getFormBindList(record);
	}

	@Override
	public int deleteFormRun(String cid) {
		return formRunDao.deleteByPrimaryKey(cid);
	}

	@Override
	public int insertFormRunSelective(ActFormRun record) {
		return formRunDao.insertSelective(record);
	}

	@Override
	public ActFormRun selectFormRun(String cId) {
		return formRunDao.selectByPrimaryKey(cId);
	}

	@Override
	public int updateFormRunSelective(ActFormRun record) {
		return formRunDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public List<ActFormRun> getFormRunList(ActFormRun record) {
		return formRunDao.getFormRunList(record);
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
