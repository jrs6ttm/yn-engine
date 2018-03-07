package com.yineng.dev_V_3_0.service;

public interface IUserAndGroupService {

	public void createUser(String userId);
	
	public void createGroup(String groupId);
	
	public void createMembership(String userId, String groupId);
	
	public void getUser(String userId);
	
	public void getGroup(String groupId);
	
	public void getMembership(String userId, String groupId);
	
	public void deleteGroup(String groupId);
	
	public void deleteUser(String userId);
	
	public void deleteMembership(String userId, String groupId);
}
