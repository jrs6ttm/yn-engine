package com.yineng.dev_V_3_0.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class ActStudyComment {
    private String cId;

    private String fileId;

    private String commenterId;

    private String commenterName;

    private String comments;

    private String ownerId;

    private String ownerName;

	private String taskId;

    private String taskName;

    private String taskDefineKey;

    private String processInstanceId;

    private String createTime;

    private String lastUpdateTime;

    public ActStudyComment(){
    	
    }
    
    /**
     * update
     * @param fileId
     * @param commenterId
     * @param comments
     * @param taskId
     */
    public ActStudyComment(String fileId, String commenterId, String comments, String taskId){
    	this.fileId = fileId;
    	this.commenterId = commenterId;
    	this.comments = comments;
    	this.taskId = taskId;
    	
    	this.lastUpdateTime = this.getDateStr(null);
    }
    
    /**
     * select
     * @param fileId
     * @param commenterId
     * @param ownerId
     * @param taskId
     * @param taskDefinekey
     * @param processInstanceId
     */
    public ActStudyComment(String fileId, String commenterId, String ownerId, String taskId, String taskDefinekey, String processInstanceId){
    	if(fileId != null){
			this.fileId = fileId;
		}
    	if(commenterId != null){
			this.commenterId = commenterId;
		}
    	if(ownerId != null){
			this.ownerId = ownerId;
		}
		if(taskId != null){
			this.taskId = taskId;
		}
		if(taskDefinekey != null){
			this.taskDefineKey = taskDefinekey;
		}
		if(processInstanceId != null){
			this.processInstanceId = processInstanceId;
		}
    }
    
    /**
     * insert
     * @param cId
     * @param fileId
     * @param commenterId
     * @param commenterName
     * @param comments
     * @param ownerId
     * @param ownerName
     * @param taskId
     * @param taskName
     * @param taskDefinekey
     * @param processInstanceId
     */
    public ActStudyComment(String cId, String fileId, String commenterId, String commenterName, String comments,  String ownerId, String ownerName, 
    		String taskId, String taskName, String taskDefinekey, String processInstanceId){
    	if(cId != null){
			this.cId = cId;
		}else{
			this.cId = UUID.randomUUID().toString();
		}
    	
    	if(fileId != null){
			this.fileId = fileId;
		}
    	if(commenterId != null){
			this.commenterId = commenterId;
		}
    	if(commenterName != null){
			this.commenterName = commenterName;
		}
    	if(comments != null){
			this.comments = comments;
		}
    	if(ownerId != null){
			this.ownerId = ownerId;
		}
    	if(ownerName != null){
			this.ownerName = ownerName;
		}
    	if(taskId != null){
			this.taskId = taskId;
		}
    	if(taskName != null){
			this.taskName = taskName;
		}
		if(taskDefinekey != null){
			this.taskDefineKey = taskDefinekey;
		}
		if(processInstanceId != null){
			this.processInstanceId = processInstanceId;
		}
		
    	this.createTime = this.getDateStr(null);
		this.lastUpdateTime = this.createTime;
    }
    
    public String getcId() {
		return cId;
	}

	public void setcId(String cId) {
		this.cId = cId;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getCommenterId() {
		return commenterId;
	}

	public void setCommenterId(String commenterId) {
		this.commenterId = commenterId;
	}

	public String getCommenterName() {
		return commenterName;
	}

	public void setCommenterName(String commenterName) {
		this.commenterName = commenterName;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
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