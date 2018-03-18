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
	private static final long serialVersionUID = 2137525931869245613L;
	
	private String id;
    private String loginName;		//系统账号
    private String nickName;		//昵称
    private String userIcon;		//用户头像
    private String groupId;			//所属课程分组id
    private String roleCid;			//所属课程分组的角色id
    private String token;			// 登录用户访问Token
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getUserIcon() {
		return userIcon;
	}
	public void setUserIcon(String userIcon) {
		this.userIcon = userIcon;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getRoleCid() {
		return roleCid;
	}
	public void setRoleCid(String roleCid) {
		this.roleCid = roleCid;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
