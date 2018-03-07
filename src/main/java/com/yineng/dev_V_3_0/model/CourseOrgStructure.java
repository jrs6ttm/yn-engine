package com.yineng.dev_V_3_0.model;

public class CourseOrgStructure {
    private String orgStructureId;

    private String parentId;

    private String treeNodeCode;

    private String sort;

    private String contextId;

    private String contextDes;

    private Byte level;

    private String isleaf;

    private Byte count;

    private String lrnscnOrgId;
    
    private String status;

    private String remark;

    private String isvalid;

    private String creatorId;

    private String createDate;

    private String lstupdid;

    private String lstupddate;
    
    //------------------------ extends ------------------------
    private int counts;
    
    private String processDefinitionId;
    
    private String teacherId;

    private String teacherName;

    private String orgUserId;

    private String orgUserName;

    public String getOrgStructureId() {
        return orgStructureId;
    }

    public void setOrgStructureId(String orgStructureId) {
        this.orgStructureId = orgStructureId == null ? null : orgStructureId.trim();
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId == null ? null : parentId.trim();
    }

    public String getTreeNodeCode() {
        return treeNodeCode;
    }

    public void setTreeNodeCode(String treeNodeCode) {
        this.treeNodeCode = treeNodeCode == null ? null : treeNodeCode.trim();
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort == null ? null : sort.trim();
    }

    public String getContextId() {
        return contextId;
    }

    public void setContextId(String contextId) {
        this.contextId = contextId == null ? null : contextId.trim();
    }

    public String getContextDes() {
        return contextDes;
    }

    public void setContextDes(String contextDes) {
        this.contextDes = contextDes == null ? null : contextDes.trim();
    }

    public Byte getLevel() {
        return level;
    }

    public void setLevel(Byte level) {
        this.level = level;
    }

    public String getIsleaf() {
        return isleaf;
    }

    public void setIsleaf(String isleaf) {
        this.isleaf = isleaf == null ? null : isleaf.trim();
    }

    public Byte getCount() {
        return count;
    }

    public void setCount(Byte count) {
        this.count = count;
    }

    public String getLrnscnOrgId() {
        return lrnscnOrgId;
    }

    public void setLrnscnOrgId(String lrnscnOrgId) {
        this.lrnscnOrgId = lrnscnOrgId == null ? null : lrnscnOrgId.trim();
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

	public int getCounts() {
		return counts;
	}

	public void setCounts(int counts) {
		this.counts = counts;
	}

	public String getProcessDefinitionId() {
		return processDefinitionId;
	}

	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public String getOrgUserId() {
		return orgUserId;
	}

	public void setOrgUserId(String orgUserId) {
		this.orgUserId = orgUserId;
	}

	public String getOrgUserName() {
		return orgUserName;
	}

	public void setOrgUserName(String orgUserName) {
		this.orgUserName = orgUserName;
	}
}