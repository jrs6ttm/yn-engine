package com.web.formManager.service;

import java.util.List;

import com.web.formManager.entity.ActForm;
import com.web.formManager.entity.ActFormAttr;
import com.web.formManager.entity.ActFormBind;
import com.web.formManager.entity.ActFormRun;

public interface IActFormManagerService {
	
	//-----------------------------------formAttr opts start --------------------
	int deleteFormAttr(ActFormAttr record);
	
	int insertFormAttrSelective(ActFormAttr record);
	
	int insertFormAttrBatch(List<ActFormAttr> recordList);
	
	int updateFormAttrSelective(ActFormAttr record);
	
	ActFormAttr selectFormAttr(String attrid);
	
	List<ActFormAttr> getFormAttrList(ActFormAttr record);
	
	//-----------------------------------formAttr opts end --------------------
	
	//-----------------------------------form opts start --------------------
	int deleteForm(String formid);

    int insertFormSelective(ActForm record);
    
    int updateFormSelective(ActForm record);

    ActForm selectForm(String formid);
    
    String multiplexForm(String formid);
    
    ActForm checkFormName(String formName);

    List<ActForm> getFormList(ActForm record);
	
	//-----------------------------------form opts end --------------------
    
    //-----------------------------------formBind opts end --------------------
    int deleteFormBind(String cid);

    int insertFormBindSelective(ActFormBind record);
    
    int updateFormBindSelective(ActFormBind record);

    ActFormBind selectFormBind(String cid);

    List<ActFormBind> getFormBindList(ActFormBind record);
    
  	//-----------------------------------formBind opts start --------------------
    
    
    //-----------------------------------formRun opts end --------------------
    int deleteFormRun(String cid);

    int insertFormRunSelective(ActFormRun record);

    ActFormRun selectFormRun(String cid);

    int updateFormRunSelective(ActFormRun record);
    
    List<ActFormRun> getFormRunList(ActFormRun record);
    
    //-----------------------------------formRun opts end --------------------
	
}
