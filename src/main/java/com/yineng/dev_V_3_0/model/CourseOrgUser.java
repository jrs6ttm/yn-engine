package com.yineng.dev_V_3_0.model;

public class CourseOrgUser {
    private String lrnscnOrgUserCid;
    
    private String synid;

    private String userId;

    private String name;

    private String sex;

    private String skill;

    private String deptId;

    private String deptFullDes;

    private String orgId;

    private String lrnscnOrgId;

    private String orgStructureId;
    
    private String status;
    
    private String procInstId;

    private String remark;

    private String isvalid;

    private String creatorId;

    private String createDate;

    private String lstupdid;

    private String lstupddate;

    public String getLrnscnOrgUserCid() {
        return lrnscnOrgUserCid;
    }

    public void setLrnscnOrgUserCid(String lrnscnOrgUserCid) {
        this.lrnscnOrgUserCid = lrnscnOrgUserCid == null ? null : lrnscnOrgUserCid.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex == null ? null : sex.trim();
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill == null ? null : skill.trim();
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId == null ? null : deptId.trim();
    }

    public String getDeptFullDes() {
        return deptFullDes;
    }

    public void setDeptFullDes(String deptFullDes) {
        this.deptFullDes = deptFullDes == null ? null : deptFullDes.trim();
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId == null ? null : orgId.trim();
    }

    public String getLrnscnOrgId() {
        return lrnscnOrgId;
    }

    public void setLrnscnOrgId(String lrnscnOrgId) {
        this.lrnscnOrgId = lrnscnOrgId == null ? null : lrnscnOrgId.trim();
    }

    public String getOrgStructureId() {
        return orgStructureId;
    }

    public void setOrgStructureId(String orgStructureId) {
        this.orgStructureId = orgStructureId == null ? null : orgStructureId.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getIsvalid() {
        return isvalid;
    }

    public void setIsvalid(String isvalid) {
        this.isvalid = isvalid == null ? null : isvalid.trim();
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId == null ? null : creatorId.trim();
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate == null ? null : createDate.trim();
    }

    public String getLstupdid() {
        return lstupdid;
    }

    public void setLstupdid(String lstupdid) {
        this.lstupdid = lstupdid == null ? null : lstupdid.trim();
    }

    public String getLstupddate() {
        return lstupddate;
    }

    public void setLstupddate(String lstupddate) {
        this.lstupddate = lstupddate == null ? null : lstupddate.trim();
    }

	public String getSynid() {
		return synid;
	}

	public void setSynid(String synid) {
		this.synid = synid;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getProcInstId() {
		return procInstId;
	}

	public void setProcInstId(String procInstId) {
		this.procInstId = procInstId;
	}
}