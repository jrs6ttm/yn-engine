package com.sso.rpc;

import java.io.Serializable;

/**
 * 
 * @ClassName: RpcUser 
 * @Description: 远程用户信息
 * @author 张龙龙 
 * @date 2018年3月3日 下午2:43:38 
 */
public class RpcUser implements Serializable {
    /**
	 * @Fields serialVersionUID : TODO
	 */
	private static final long serialVersionUID = 1L;
	
	private String ID;
    private String DOMAIN_ID;		//所属域id
    private String LOGIN_NAME;		//系统账号
    private String NICKNAME;		//昵称
    private String USER_ICON;		//用户头像
    private String role_ids; //新版权限, 关联sys_role
    private String admin_type;//管理员类型, super_admin:超级管理员, domain_admin:域管理员
    private String PERSON_ID;
    // 登录用户访问Token
    private String token;
    private String UMENG_TOKEN;//友盟推送Token
    public RpcUser() {
    }

    public RpcUser(String ID, String DOMAIN_ID, String LOGIN_NAME, String NICKNAME, String USER_ICON, String role_ids, String admin_type, String PERSON_ID,String UMENG_TOKEN) {
        this.ID = ID;
        this.DOMAIN_ID = DOMAIN_ID;
        this.LOGIN_NAME = LOGIN_NAME;
        this.NICKNAME = NICKNAME;
        this.USER_ICON = USER_ICON;
        this.role_ids = role_ids;
        this.admin_type = admin_type;
        this.PERSON_ID = PERSON_ID;
        this.UMENG_TOKEN = UMENG_TOKEN;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getDOMAIN_ID() {
        return DOMAIN_ID;
    }

    public void setDOMAIN_ID(String DOMAIN_ID) {
        this.DOMAIN_ID = DOMAIN_ID;
    }

    public String getLOGIN_NAME() {
        return LOGIN_NAME;
    }

    public void setLOGIN_NAME(String LOGIN_NAME) {
        this.LOGIN_NAME = LOGIN_NAME;
    }

    public String getNICKNAME() {
        return NICKNAME;
    }

    public void setNICKNAME(String NICKNAME) {
        this.NICKNAME = NICKNAME;
    }

    public String getUSER_ICON() {
        return USER_ICON;
    }

    public void setUSER_ICON(String USER_ICON) {
        this.USER_ICON = USER_ICON;
    }

    public String getRole_ids() {
        return role_ids;
    }

    public void setRole_ids(String role_ids) {
        this.role_ids = role_ids;
    }

    public String getAdmin_type() {
        return admin_type;
    }

    public void setAdmin_type(String admin_type) {
        this.admin_type = admin_type;
    }

    public String getPERSON_ID() {
        return PERSON_ID;
    }

    public void setPERSON_ID(String PERSON_ID) {
        this.PERSON_ID = PERSON_ID;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUMENG_TOKEN() {
        return UMENG_TOKEN;
    }

    public void setUMENG_TOKEN(String UMENG_TOKEN) {
        this.UMENG_TOKEN = UMENG_TOKEN;
    }
}
