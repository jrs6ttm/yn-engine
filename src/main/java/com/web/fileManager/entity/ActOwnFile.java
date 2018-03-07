package com.web.fileManager.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.activiti.engine.impl.util.json.JSONObject;

public class ActOwnFile {
    private String fileid;

    private String filepath;

    private String filename;

    private String userid;

    private String filesize;

    private String imagezooms;

    private String filetype;

    private Integer readtimes;

    private Integer downloadtimes;

    private Integer praisetimes;

    private String createtime;

    private String lastupdatetime;
    
    //---------------------------------extends---------------------------
    private String starttime;
    private String endtime;

    public ActOwnFile() {
		
	}
    
    /**
     * 做查询条件用
     * @param fileid
     * @param filename
     * @param userid
     * @param startTime
     * @param endTime
     */
    public ActOwnFile(String fileid, String filename, String fileType, String userid, String startTime, String endTime) {
    	if(fileid != null && !"".equals(fileid.trim())){
    		this.fileid = fileid.trim();
    	}
    	if(filename != null && !"".equals(filename.trim())){
    		this.filename = filename.trim();
    	}
    	if(fileType != null && !"".equals(fileType.trim())){
    		this.filetype = fileType.trim().toLowerCase();
    	}
    	if(userid != null && !"".equals(userid.trim())){
    		this.userid = userid.trim();
    	}
    	if(startTime != null && !"".equals(startTime.trim())){
    		this.starttime = startTime.trim();
    	}
    	if(endTime != null && !"".equals(endTime.trim())){
    		this.endtime = endTime.trim();
    	}
	}

    /**
     * 做记录保存用
     * @param fileid
     * @param filepath
     * @param filename
     * @param userid
     * @param filesize
     * @param imagezooms
     * @param filetype
     */
    public ActOwnFile(String fileid, String filepath, String filename, 
			    		String userid, String filesize, String imagezooms, 
			    		String filetype){
    	if(fileid != null){
    		this.fileid = fileid;
    	}
    	this.filepath = filepath;
    	this.filename = filename;
    	this.userid = userid;
    	this.filesize = filesize;
    	if(imagezooms != null){
    		this.imagezooms = imagezooms;
    	}
    	this.filetype = filetype;
    	
    	this.readtimes = 0;
    	this.downloadtimes = 0;
    	this.praisetimes = 0;
    	this.createtime = this.getDateStr(null);
    	this.lastupdatetime = this.createtime;
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

    public Integer getReadtimes() {
        return readtimes;
    }

    public void setReadtimes(Integer readtimes) {
        this.readtimes = readtimes;
    }

    public Integer getDownloadtimes() {
        return downloadtimes;
    }

    public void setDownloadtimes(Integer downloadtimes) {
        this.downloadtimes = downloadtimes;
    }

    public Integer getPraisetimes() {
        return praisetimes;
    }

    public void setPraisetimes(Integer praisetimes) {
        this.praisetimes = praisetimes;
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
    
    
	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}
	
	public JSONObject toJSON(){
		JSONObject json = new JSONObject();
		
		json.put("fileId", this.getFileid());
		json.put("filePath", this.getFilepath());
		json.put("fileName", this.getFilename());
		json.put("userId", this.getUserid());
		json.put("fileSize", this.getFilesize());
		json.put("fileType", this.getFiletype());
		json.put("imagezooms", this.getImagezooms());
		json.put("createTime", this.getCreatetime());
		/*
		json.put("readTimes", this.getReadtimes());
		json.put("downloadtimes", this.getDownloadtimes());
		json.put("praisetimes", this.getPraisetimes());
		json.put("lastUpdateTime", this.getLastupdatetime());
		*/
		
		return json;
	}
	public String getDateStr(Date date){
		  if(date == null){
			 date = new Date();
		  }
		  SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
		  return sf.format(date);
	}

	
}