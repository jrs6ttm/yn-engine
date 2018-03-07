package com.yineng.dev_V_3_0.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class ActStudyVar {
    private String varId;

    private String varType;

    private String varValue;

    private String userId;

    private String userName;

    private String taskId;

    private String taskName;

    private String taskDefineKey;

    private String processInstanceId;

    private String createTime;

    private String lastUpdateTime;

    public ActStudyVar(){
    	
    }
    
    /**
     * update用
     * @param varId
     * @param varValue
     */
    public ActStudyVar(String varId, String varValue){
    	this.varId = varId;
    	this.varValue = varValue;
    	
    	this.lastUpdateTime = this.getDateStr(null);
    }
    
    /**
     * select
     * @param varType
     * @param userId
     * @param userName
     * @param taskId
     * @param taskName
     * @param taskDefineKey
     * @param processInstanceId
     */
    public ActStudyVar(String varType, String userId, String taskId, String taskDefineKey, String processInstanceId){
    	if(varType != null){
			this.varType = varType;
		}
    	if(userId != null){
			this.userId = userId;
		}
		if(taskId != null){
			this.taskId = taskId;
		}
		if(taskDefineKey != null){
			this.taskDefineKey = taskDefineKey;
		}
		if(processInstanceId != null){
			this.processInstanceId = processInstanceId;
		}
    }
    
    /**
     * insert
     * @param varId
     * @param varType
     * @param varValue
     * @param userId
     * @param userName
     * @param taskId
     * @param taskName
     * @param taskDefineKey
     * @param processInstanceId
     */
    public ActStudyVar(String varId, String varType, String varValue, String userId, String userName, 
    		String taskId, String taskName, String taskDefineKey, String processInstanceId){
    	if(varId != null){
			this.varId = varId;
		}else{
			this.varId = UUID.randomUUID().toString();
		}
    	
    	if(varType != null){
			this.varType = varType;
		}
    	if(varValue != null){
			this.varValue = varValue;
		}
    	if(userId != null){
			this.userId = userId;
		}
    	if(userName != null){
			this.userName = userName;
		}
		if(taskId != null){
			this.taskId = taskId;
		}
		if(taskName != null){
			this.taskName = taskName;
		}
		if(taskDefineKey != null){
			this.taskDefineKey = taskDefineKey;
		}
		if(processInstanceId != null){
			this.processInstanceId = processInstanceId;
		}
		
    	this.createTime = this.getDateStr(null);
		this.lastUpdateTime = this.createTime;
    }
    
    public String getVarId() {
		return varId;
	}

	public void setVarId(String varId) {
		this.varId = varId;
	}

	public String getVarType() {
		return varType;
	}

	public void setVarType(String varType) {
		this.varType = varType;
	}

	public String getVarValue() {
		return varValue;
	}

	public void setVarValue(String varValue) {
		this.varValue = varValue;
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

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getTaskDefineKey() {
		return taskDefineKey;
	}

	public void setTaskDefineKey(String taskDefineKey) {
		this.taskDefineKey = taskDefineKey;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
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