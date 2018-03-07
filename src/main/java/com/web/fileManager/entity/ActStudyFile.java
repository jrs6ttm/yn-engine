package com.web.fileManager.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ActStudyFile {
    private String fileid;

    private String filepath;

    private String filename;

    private String userid;

    private String filesize;

    private String imagezooms;

    private String filetype;

    private String opttype;

    private String taskid;

    private String taskdefinekey;

    private String processinstanceid;

    private String lastflow;

    private String flowsource;

    private Integer isendflow;

    private String createtime;

    private String lastupdatetime;

    public ActStudyFile() {
		
	}

    /**
     * save
     * @param fileid
     * @param filepath
     * @param filename
     * @param userid
     * @param filesize
     * @param imagezooms
     * @param filetype
     * @param opttype
     * @param taskid
     * @param taskdefinekey
     * @param processinstanceid
     * @param lastflow
     * @param flowsource
     * @param isendflow
     */
    public ActStudyFile(String fileid, String filepath, String filename, 
			    		String userid, String filesize, String imagezooms, 
			    		String filetype, String opttype, String taskid, String taskdefinekey, 
			    		String processinstanceid, String lastflow, String flowsource, int isendflow){
			
    		this.fileid = fileid;
			this.filepath = filepath;
			this.filename = filename;
			this.userid = userid;
			if(filesize != null){
				this.filesize = filesize;
			}
			if(imagezooms != null){
				this.imagezooms = imagezooms;
			}
			this.filetype = filetype;
			if(opttype != null){
				this.opttype = opttype;
			}
			if(taskid != null){
				this.taskid = taskid;
			}
			if(taskdefinekey != null){
				this.taskdefinekey = taskdefinekey;
			}
			if(processinstanceid != null){
				this.processinstanceid = processinstanceid;
			}
			if(lastflow != null){
				this.lastflow = lastflow;
			}
			if(flowsource != null){
				this.flowsource = flowsource;
			}
			this.isendflow = isendflow;
			
			this.createtime = this.getDateStr(null);
			this.lastupdatetime = this.createtime;
    }
    
    /**
     * select
     * @param fileid
     * @param filename
     * @param userid
     * @param filetype
     * @param taskid
     * @param taskdefinekey
     * @param processinstanceid
     */
    public ActStudyFile(String fileid, String filename, String filetype, String userid, String taskid, String taskdefinekey, String processinstanceid){
    	if(fileid != null){
			this.fileid = fileid;
		}
		if(filename != null){
			this.filename = filename;
		}
		if(filetype != null){
			this.filetype = filetype;
		}
		if(userid != null){
			this.userid = userid;
		}
		if(taskid != null){
			this.taskid = taskid;
		}
		if(taskdefinekey != null){
			this.taskdefinekey = taskdefinekey;
		}
		if(processinstanceid != null){
			this.processinstanceid = processinstanceid;
		}
	}
    
	public String getFileid() {
        return fileid;
    }

    public void setFileid(String fileid) {
        this.fileid = fileid == null ? null : fileid.trim();
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath == null ? null : filepath.trim();
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename == null ? null : filename.trim();
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid == null ? null : userid.trim();
    }

    public String getFilesize() {
        return filesize;
    }

    public void setFilesize(String filesize) {
        this.filesize = filesize == null ? null : filesize.trim();
    }

    public String getImagezooms() {
        return imagezooms;
    }

    public void setImagezooms(String imagezooms) {
        this.imagezooms = imagezooms == null ? null : imagezooms.trim();
    }

    public String getFiletype() {
        return filetype;
    }

    public void setFiletype(String filetype) {
        this.filetype = filetype == null ? null : filetype.trim();
    }

    public String getOpttype() {
        return opttype;
    }

    public void setOpttype(String opttype) {
        this.opttype = opttype == null ? null : opttype.trim();
    }

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid == null ? null : taskid.trim();
    }

    public String getTaskdefinekey() {
        return taskdefinekey;
    }

    public void setTaskdefinekey(String taskdefinekey) {
        this.taskdefinekey = taskdefinekey == null ? null : taskdefinekey.trim();
    }

    public String getProcessinstanceid() {
        return processinstanceid;
    }

    public void setProcessinstanceid(String processinstanceid) {
        this.processinstanceid = processinstanceid == null ? null : processinstanceid.trim();
    }

    public String getLastflow() {
        return lastflow;
    }

    public void setLastflow(String lastflow) {
        this.lastflow = lastflow == null ? null : lastflow.trim();
    }

    public String getFlowsource() {
        return flowsource;
    }

    public void setFlowsource(String flowsource) {
        this.flowsource = flowsource == null ? null : flowsource.trim();
    }

    public Integer getIsendflow() {
        return isendflow;
    }

    public void setIsendflow(Integer isendflow) {
        this.isendflow = isendflow;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime == null ? null : createtime.trim();
    }

    public String getLastupdatetime() {
        return lastupdatetime;
    }

    public void setLastupdatetime(String lastupdatetime) {
        this.lastupdatetime = lastupdatetime == null ? null : lastupdatetime.trim();
    }
    
    public String getDateStr(Date date){
		  if(date == null){
			 date = new Date();
		  }
		  SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
		  return sf.format(date);
	}
}