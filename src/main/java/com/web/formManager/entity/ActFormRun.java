package com.web.formManager.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ActFormRun {
    private String cId;

    private String formId;

    private String formName;

    private String formHtml;
    
    private String flowAttrs;
    
    private String processVars;

    private String taskId;

    private String taskDefineKey;

    private String taskName;

    private String processInstanceId;
    
    private String userId;

    private String userName;

    private String createTime;

    private String lastUpdateTime;
    
    public ActFormRun(){
    	
    }
    
    /**
     * 
     * @param cId
     * @param formHtml
     */
    public ActFormRun(String cId, String formHtml){
    	this.cId = cId;
    	this.formHtml = formHtml;
    	
    	this.lastUpdateTime = this.getDateStr(null);
    }
    
    /**
     * 
     * @param cId
     * @param formId
     * @param formName
     * @param formHtml
     * @param taskId
     * @param taskDefineKey
     * @param taskName
     * @param processInstanceId
     * @param userId
     * @param userName
     */
    public ActFormRun(String cId, String formId, String formName, String formHtml,
    		String taskId, String taskDefineKey, String taskName, String processInstanceId,
    		String userId, String userName){
    	
    	this.cId = cId;
    	this.formId = formId;
    	this.formName = formName;
    	this.formHtml = formHtml;
    	this.taskId = taskId;
    	this.taskDefineKey = taskDefineKey;
    	this.taskName = taskName;
    	this.processInstanceId = processInstanceId;
    	this.userId = userId;
    	this.userName = userName;
    	
    	this.createTime = this.getDateStr(null);
		this.lastUpdateTime = this.createTime;
    }

    public String getcId() {
		return cId;
	}

	public void setcId(String cId) {
		this.cId = cId;
	}

	public String getFormId() {
		return formId;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}

	public String getFormName() {
		return formName;
	}

	public void setFormName(String formName) {
		this.formName = formName;
	}

	public String getFormHtml() {
		return formHtml;
	}

	public void setFormHtml(String formHtml) {
		this.formHtml = formHtml;
	}

	public String getFlowAttrs() {
		return flowAttrs;
	}

	public void setFlowAttrs(String flowAttrs) {
		this.flowAttrs = flowAttrs;
	}

	public String getProcessVars() {
		return processVars;
	}

	public void setProcessVars(String processVars) {
		this.processVars = processVars;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getTaskDefineKey() {
		return taskDefineKey;
	}

	public void setTaskDefineKey(String taskDefineKey) {
		this.taskDefineKey = taskDefineKey;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	
	public String getDateStr(Date date){
		  if(date == null){
			 date = new Date();
		  }
		  SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
		  return sf.format(date);
	}
    
}