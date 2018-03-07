package com.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.activiti.engine.impl.util.json.JSONObject;

public class LogEntity {
	
	public String logId;
	
	public String logLevel;
	
	public String className;
	
	public String methodName;
	
	public int lineNumber;
	
	public String params;
	
	public String optUserId;
	
	public String message;
	
	public String createTime;
	
	public LogEntity(){
		this.logId = UUID.randomUUID().toString();
		this.createTime =  this.getDateStr(null);
	}
	
	/**
	 * 
	 * @param logLevel 日志级别
	 * @param className 类名
	 * @param methodName 方法名
	 * @param lineNumber 行数
	 * @param params 参数
	 * @param optUserId 操作人
	 * @param message 信息
	 */
	public LogEntity(String logLevel, String className, String methodName, int lineNumber, String params, String optUserId, String message){
		this.logId = UUID.randomUUID().toString();
		
		if(logLevel != null && !"".equals(logLevel)){
			this.logLevel = logLevel;
		}
		if(className != null && !"".equals(className)){
			this.className = className;
		}
		if(methodName != null && !"".equals(methodName)){
			this.methodName = methodName;
		}
		
		this.lineNumber = lineNumber;
		
		if(params != null && !"".equals(params)){
			this.params = params;
		}
		
		if(optUserId != null && !"".equals(optUserId)){
			this.optUserId = optUserId;
		}
		
		if(message != null && !"".equals(message)){
			this.message = message;
		}
		
		this.createTime =  this.getDateStr(null);
	}
	
	public String getLogId() {
		return logId;
	}

	public void setLogId(String logId) {
		this.logId = logId;
	}

	public String getLogLevel() {
		return logLevel;
	}

	public void setLogLevel(String logLevel) {
		this.logLevel = logLevel;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}
	
	public int getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}
	
	public String getOptUserId() {
		return optUserId;
	}

	public void setOptUserId(String optUserId) {
		this.optUserId = optUserId;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	public  String getDateStr(Date date){
		  if(date == null){
			 date = new Date();
		  }
		  SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
		  return sf.format(date);
	}
	
	public JSONObject toJSON(){
		JSONObject logEntity = new JSONObject();
		
		logEntity.put("logId", this.getLogId());
		logEntity.put("logLevel", this.getLogLevel());
		logEntity.put("className", this.getClassName());
		logEntity.put("methodName", this.getMethodName());
		logEntity.put("lineNumber", this.getLineNumber());
		logEntity.put("params", this.getParams());
		logEntity.put("optUserId", this.getOptUserId());
		logEntity.put("message", this.getMessage());
		logEntity.put("createTime", this.getCreateTime());
		
		return logEntity;
	}
}
